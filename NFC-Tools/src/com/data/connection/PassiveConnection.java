package com.data.connection;

import com.nfc.device.Key;
import com.nfc.device.Mifare1K;
import com.nfc.device.Tag;
import com.nfc.reader.Reader;
import com.nfc.reader.ReaderException;
import com.nfc.utils.NfcConstants;

public class PassiveConnection implements Connection {

	private Reader reader = null;
	private Tag tag = null;
	private boolean __debugMode = false;
	
	public PassiveConnection(Reader _reader,byte typeID)
	{
		this.reader = _reader;
		this.tag = new Mifare1K(this.__debugMode);
	}
	public PassiveConnection(Reader _reader,byte typeID,boolean debugMode)
	{
		this.reader = _reader;
		this.__debugMode = debugMode;
		this.tag = new Mifare1K(this.__debugMode);
	}

	@Override
	public void sendData(byte[] data) throws ConnectionException {
		try {
			tag.setUID(this.reader);
			tag.setKeyA(NfcConstants.PUBLIC_NDEF_KEY);
			tag.setAllData(this.reader, 1, data, Key.A);
		} 
		catch (ReaderException e) {
			throw new ConnectionException("Connection failed : " + e.getMessage());
		}
		
	}

	@Override
	public byte[] receiveData() throws ConnectionException {
		byte[] result = null;
		try {
			tag.setUID(this.reader);
			tag.setKeyA(NfcConstants.PUBLIC_NDEF_KEY);
			result = tag.readAllData(this.reader, 1, Key.A);
		} catch (ReaderException e) {
			throw new ConnectionException("Connection failed : " + e.getMessage());
		}
		return result;
	}
	
	@Override
	public ConnectionType getConnectionType() {
		return ConnectionType.Passive;
	}
}
