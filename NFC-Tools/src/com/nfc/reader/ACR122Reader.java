package com.nfc.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardChannel;
import javax.smartcardio.Card;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import com.data.connection.Connection;
import com.data.connection.ConnectionType;
import com.data.connection.PassiveConnection;
import com.data.connection.ActiveConnection;
import com.nfc.reader.ErrorHandler;

public class ACR122Reader implements Reader{
	
	/**** Instructions of ACR122 NFC Reader for PN53x interface ***/
	public final static byte[] GET_FIRMWARE_VERSION = {(byte)0xFF,(byte)0x00,
													(byte)0x48,(byte)0x00,(byte)0x00};
	
	/** ACR122 Header is 5 byte and the last byte for the length of the payload data should be added*/
	public final static byte[] ACR122_HEADER = {(byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00 }; // one byte more needed for the length
	
	private Logger logger = null;
	
	/*********** Class fields ***********/
	private String __name;
	private boolean __debugMode = false;
	private CardTerminal terminal = null;
	private CardChannel channel = null;
	private Card card = null;
	private List<CardTerminal> terminalList = null;
	private ConnectionType connectionType = null;
	private Connection connection = null;
	
	
	/*********** Constructor **************/
	public ACR122Reader(boolean debugMode) {
		this.__name = "ACS ACR 122";
		this.__debugMode = debugMode;
		this.logger = Logger.getLogger("ACR122Logger");
	}
	
	@Override
	public String getName() {
		return this.__name;
	}
	
	private int getACR122TerminalIndex(List<CardTerminal> terminals)
	{
		if(terminals.size() <= 0)
			return -1;
		Iterator<CardTerminal> it = terminals.iterator();
		int index = 0;
		while(it.hasNext())
		{
			CardTerminal terminal = it.next();
			if(terminal.getName().contains("ACS ACR 122"))
			{
				return index;
			}
			index++;
		}
		return 0;
	}
	
	@Override
	public ConnectionType getConnectionType()
	{
		return this.connectionType;
	}
	
	private void setTerminal() throws ReaderException
	{
		TerminalFactory factory;
		try 
		{
			factory = TerminalFactory.getDefault();
			terminalList = factory.terminals().list();          
			 
			if (terminalList.size() == 0) {
				terminalList = null;		
				throw new ReaderException("No terminal found. Terminal count is 0.");
			}
			else {
				int index = getACR122TerminalIndex(this.terminalList);
				if(index < 0)
					throw new ReaderException("Error while setting the terminal. ACS ACR 122 terminal couldn't be found.");
				this.terminal=terminalList.get(index);
				if(this.__debugMode)
					logger.log(Level.INFO,"terminal name: "+terminal.getName());
			}	
		} 
		catch (CardException c) {
			if(terminalList != null)
				terminalList.clear();
			terminalList = null;
			throw new ReaderException(c.getMessage());
		}		 
	}
	
	
	@Override
	public void connect() throws ReaderException {
		try
		{
			setTerminal();
			if(__debugMode)
				logger.log(Level.INFO,"connected to the terminal.");
		}
		catch(ReaderException ex)
		{
			if(__debugMode)
				logger.log(Level.WARNING,"connect to the terminal failed !");
			throw ex;
		}
	}
	
	@Override
	public List<String> getDeviceList() {
		
		List<String> deviceList = new ArrayList<String>();
		Iterator<CardTerminal> it = this.terminalList.iterator();
		while(it.hasNext())
		{
			CardTerminal terminal = it.next();
			if(terminal != null)
				deviceList.add(terminal.getName());
		}
		return deviceList;
	}
	
	@Override
	public Connection getConnection() {
		return this.connection;
	}
	
	@Override
	public boolean waitForDeviceReady(long timeout) throws ReaderException, UnsupportedDeviceException {

		try{
			this.connect();
		}
		catch(ReaderException e)
		{
			throw e;
		}
		
		if (this.terminal == null)
			throw new ReaderException("terminal is null");

		try {
			// wait timeout milisecond for card being present
			if (this.terminal.waitForCardPresent(timeout)) 
			{
				this.card = terminal.connect("*");
				
				byte[] historicalBytes = card.getATR().getHistoricalBytes();

				if(historicalBytes.length >= 11)
				{
					if (historicalBytes[9] == (byte)0xff && historicalBytes[10] == (byte)0x40) {
						this.connectionType = ConnectionType.Active;
						this.connection = new ActiveConnection(this,this.__debugMode);
					}
					else {
						this.connectionType = ConnectionType.Passive;
						this.connection = new PassiveConnection(this,(byte)0x08,this.__debugMode);
					}
				}
				else
				{
					this.connectionType = ConnectionType.Unknown;
					this.connection = null;
					throw new UnsupportedDeviceException("The device is neither active nor passive");
				}
				this.channel = card.getBasicChannel();
				if(__debugMode)
					logger.log(Level.INFO,"channel is open to the device : " + this.connectionType.toString() + " connection" );
				return true;
			} 
			else 
			{
				if(__debugMode)
					logger.log(Level.WARNING,"device is not present. channel could not be opened.");
				
				this.connectionType = null;
				this.connection = null;
				return false;
			}
		} 
		catch (CardException e) {
			this.connectionType = null;
			this.connection = null;
			throw new ReaderException("no device found");
		}
	}
	
	@Override
	public void disconnect() throws ReaderException{
		if (this.card != null) {
			try {
				this.card.disconnect(true);
			} 
			catch (CardException e) {
				throw new ReaderException(e.getMessage());
			}
		}
		try {
			while (this.terminal != null && this.terminal.isCardPresent()) {
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException e) {
					break;
				}
			}
			if(this.terminal != null)
				this.terminal.waitForCardAbsent(1000);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	/**** Transmit data between ACR122U Reader and another NFC device *******/	
	public byte[] transmit(byte[] command) throws ReaderException{
		if (this.channel == null) {
			throw new ReaderException("Channel is null. The device could have been removed.");
		}
		try {
			CommandAPDU c = new CommandAPDU(command);			
			ResponseAPDU response = channel.transmit(c);
			
			String errorMessage = ErrorHandler.errorChecker(response);
			
			if(errorMessage != "")
			{
				throw new ReaderException(errorMessage);
			}
			
			return response.getData();
			
		}
		catch (CardException e) {
			throw new ReaderException(e.getMessage());
		}
	}
}
