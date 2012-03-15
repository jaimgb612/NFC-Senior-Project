package com.nfc.device;

import java.util.Arrays;
import java.util.logging.Logger;

import com.nfc.reader.ErrorHandler;
import com.nfc.reader.Reader;
import com.nfc.reader.ReaderException;
import com.nfc.utils.*;

public class Mifare1K implements Tag {
		
	private static final int sectorCount = 16;
	private static final int blockCount = 64;
	private byte[] UID = null;
	private byte[] keyA = null;
	private byte[] keyB = null;
	private Logger logger = null;
	private boolean __debugMode = false;
	
	public Mifare1K(boolean debugMode) {
		this.__debugMode = debugMode;
		logger = Logger.getLogger("Mifare1KLogger");
	}
	
	@Override
	public int getBlockCount() {
		return Mifare1K.blockCount;
	}

	@Override
	public int getSectorCount() {
		return Mifare1K.sectorCount;
	}
	
	@Override
	public void setUID(Reader reader) throws ReaderException {
		try {
			byte[] res = reader.transmit(NfcConstants.GET_UID);
			this.UID = res;
		}
		catch(ReaderException e) {
			throw e;
		}
	}
	
	@Override
	public byte[] getUID()
	{
		return this.UID;
	}
	
	@Override
	public boolean authenticateSector(Reader reader,int block, Key keyType, byte[] key) throws ReaderException {
		
		byte[] blockByte = {(byte)block};
		byte[] command = null;
		
		if(keyType == Key.A)
			command = NfcUtils.appendToByteArray(NfcConstants.KEY_A_AUTH, blockByte, 0, blockByte.length);
		else
			command = NfcUtils.appendToByteArray(NfcConstants.KEY_B_AUTH, blockByte, 0, blockByte.length);

		command = NfcUtils.appendToByteArray(command, key, 0, key.length);
		
		command = NfcUtils.appendToByteArray(command, this.UID, 0, this.UID.length);
		
		int length = command.length;
		byte[] LEN = {(byte) length};
		
		byte[] auth_command = NfcUtils.appendToByteArray(NfcConstants.ACR122_HEADER, LEN, 0, LEN.length);
		
		auth_command = NfcUtils.appendToByteArray(auth_command, command,0,command.length);
		
		byte[] result = null; 
		
		try {
			result = reader.transmit(auth_command);
		}
		catch(ReaderException e) {
			throw e;
		}
		
		if(result.length < 3)
			return false;
		
		// Authentication successful
		if(result[2] == 0x00)
			return true;
		else
			return false;
	}
	

	@Override
	public void setKeyA(byte[] key) {

		this.keyA = key;
	}

	@Override
	public void setKeyB(byte[] key) {
		this.keyB = key;
	}

	@Override
	public byte[] getKeyA() {
		return this.keyA;
	}

	@Override
	public byte[] getKeyB() {
		return this.keyB;
	}
	
	/*** Gets the response data by excluding the reader bytes ***/
	public byte[] getDataBytes(byte[] response) throws ReaderException
	{
		try
		{
			// First three bytes are reader specific
			if(response.length > 3)
			{
				String errorMessage = ErrorHandler.errorChecker(response[2]);
				if(errorMessage != "")
					throw new ReaderException(errorMessage);

				byte[] data = null;
				data = NfcUtils.appendToByteArray(data,response, 3, response.length-3);
				return data;
			}
			else 
				throw new ReaderException("No data has received or an error has occured");
		}
		catch(Exception e)
		{
			throw new ReaderException("Exception while getting the data : " + e.getMessage());
		}
			
	}
	@Override
	public byte[] readData(Reader reader,int block, Key keyType) throws ReaderException {
		
		boolean auth_result = false;
		try {
		if(keyType == Key.A)
			auth_result = authenticateSector(reader, block, keyType, getKeyA());
		else
			auth_result = authenticateSector(reader, block, keyType, getKeyB());
		}
		catch(ReaderException e)
		{
			throw e;
		}
		if( !auth_result ) {
			throw new ReaderException("Authentication to block " + block + " failed");
		}
		
		if(block < 0 || block > this.getBlockCount())
			throw new ReaderException("Request for an unachieved block to read");
		
		byte[] blockByte = {(byte)block};
		
		byte[] command = NfcUtils.appendToByteArray(NfcConstants.READ_BLOCK, blockByte, 0, blockByte.length);
		
		byte[] LEN = {(byte)command.length};
		
		byte[] read_command = NfcUtils.appendToByteArray(NfcConstants.ACR122_HEADER, LEN, 0, LEN.length);
		
		read_command = NfcUtils.appendToByteArray(read_command, command, 0, command.length);
		
		byte[] result = null;
		
		try {
			result = reader.transmit(read_command);
		}
		catch(ReaderException e) {
			throw e;
		}
		return result;
	}
	
	
	
	@Override
	public void setData(Reader reader,int block, byte[] data, Key keyType) throws ReaderException {
		
		if(data.length < 0 || data.length > 16)
			throw new ReaderException("Unexpected length of data to write/update, must be in the range of 0-16");
		
		boolean auth_result = false;
		try {
		if(keyType == Key.A)
			auth_result = authenticateSector(reader, block, keyType, getKeyA());
		else
			auth_result = authenticateSector(reader, block, keyType, getKeyB());
		}
		catch(ReaderException e)
		{
			throw e;
		}
		if( !auth_result ) {
			throw new ReaderException("Authentication to block " + block + " failed");
		}
		
		if(block < 0 || block > this.getBlockCount())
			throw new ReaderException("Request for an unachieved block to write/update");
		
		byte[] blockByte = {(byte)block};
		
		byte[] command = NfcUtils.appendToByteArray(NfcConstants.UPDATE_BLOCK, blockByte, 0, blockByte.length);
		
		command = NfcUtils.appendToByteArray(command, data, 0, data.length);
		
		byte[] LEN = {(byte)command.length};
		
		byte[] update_command = NfcUtils.appendToByteArray(NfcConstants.ACR122_HEADER, LEN, 0, LEN.length);
		
		update_command = NfcUtils.appendToByteArray(update_command, command, 0, command.length);
		
		byte[] result = null;
		
		try {
			result = reader.transmit(update_command);
		}
		catch(ReaderException e) {
			throw e;
		}
		
		if(result.length < 3)
			throw new ReaderException("Unexpected result after update command");
		
		String errorMessage = ErrorHandler.errorChecker(result[2]);
		if(errorMessage != "")
			throw new ReaderException(errorMessage);
		
		
	}
	@Override
	public byte[] readAllData(Reader reader, int initialSector, Key keyType) throws ReaderException {

		if(initialSector < 0 || initialSector >= this.getSectorCount())
			throw new ReaderException("Initial sector must be in the range of 0-" + (this.getSectorCount()-1));
		
		byte[] allData = null;
		byte[] blockData = null;
		int currentBlock = 0;
		
		for(int i=initialSector;i<this.getSectorCount();i++)
		{
			try {
				currentBlock = i*4;
				blockData = this.readData(reader, currentBlock, keyType);
				blockData = this.getDataBytes(blockData);
				allData = NfcUtils.appendToByteArray(allData, blockData,0,blockData.length);
				currentBlock++;
				blockData = this.readData(reader, currentBlock, keyType);
				blockData = this.getDataBytes(blockData);
				allData = NfcUtils.appendToByteArray(allData, blockData,0,blockData.length);
				currentBlock++;
				blockData = this.readData(reader, currentBlock, keyType);
				blockData = this.getDataBytes(blockData);
				allData = NfcUtils.appendToByteArray(allData, blockData,0,blockData.length);
			}
			catch(ReaderException e)
			{
				throw new ReaderException("Read failed at block " + currentBlock + ". Reason : " + e.getMessage());
			}
		}
		
		return allData;
	}

	@Override
	public void setAllData(Reader reader, int initialSector, byte[] data, Key keyType) throws ReaderException {
		
		if(initialSector < 0 || initialSector >= this.getSectorCount())
			throw new ReaderException("Initial sector must be in the range of 0-" + (this.getSectorCount()-1));
		
		int currentBlock = 0;
		int index = 0;
		byte[] blockData = null;
		for(int i=initialSector;i<this.getSectorCount();i++)
		{
			try {
				currentBlock = i*4;
				if(index > data.length)
					break;
				blockData = Arrays.copyOfRange(data, index, (index+16));
				this.setData(reader, currentBlock, blockData, keyType);
				if((index) < data.length)
					index += 16;
				
				
				currentBlock++;
				if(index > data.length)
					break;
				blockData = Arrays.copyOfRange(data, index, (index+16));
				this.setData(reader, currentBlock, blockData,keyType);
				if((index) < data.length)
					index += 16;
				
				
				currentBlock++;
				if(index > data.length)
					break;
				blockData = Arrays.copyOfRange(data, index, (index+16));
				this.setData(reader, currentBlock, blockData, keyType);
				if((index) < data.length)
					index += 16;
				
			}
			catch(ReaderException e)
			{
				throw new ReaderException("Read failed at block " + currentBlock , e);
			}
		}
		
		
	}

	

}
