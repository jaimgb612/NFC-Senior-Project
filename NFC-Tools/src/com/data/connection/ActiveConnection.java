package com.data.connection;

import com.nfc.reader.Reader;

public class ActiveConnection implements Connection{

	private Reader reader = null;
	private boolean __debugMode = false;
	
	public ActiveConnection(Reader _reader)
	{
		this.reader = _reader;
	}
	public ActiveConnection(Reader _reader,boolean debugMode)
	{
		this.reader = _reader;
		this.__debugMode = debugMode;
	}
	
	@Override
	public void sendData(byte[] data) throws ConnectionException {
	}

	@Override
	public byte[] receiveData() throws ConnectionException  {
		return null;
	}
	@Override
	public ConnectionType getConnectionType() {
		return ConnectionType.Active;
	}

}
