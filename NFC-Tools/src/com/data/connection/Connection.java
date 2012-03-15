package com.data.connection;

public interface Connection {
	
	public ConnectionType getConnectionType();
	public void sendData(byte[] data)throws ConnectionException;
	public byte[] receiveData() throws ConnectionException;
}
