package com.nfc.reader;

import java.util.List;

import com.data.connection.Connection;
import com.data.connection.ConnectionType;

public interface Reader {
	
	public String getName();
	
	// Return a list of string representing the name of the readers
	public List<String> getDeviceList();
	
	// connects to the device reader 
	public void connect() throws ReaderException;
	
	// disconnect from device
	public void disconnect() throws ReaderException;
	
	public ConnectionType getConnectionType();
	
	public Connection getConnection();
	
	// waits for device whether ready or not. Returns true if the device is ready
	public boolean waitForDeviceReady(long timeout) throws ReaderException, UnsupportedDeviceException;
	
	// transmits the command and returns the response
	public byte[] transmit(byte[] command) throws ReaderException;
		
}
