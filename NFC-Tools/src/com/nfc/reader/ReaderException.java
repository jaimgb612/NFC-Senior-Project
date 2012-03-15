package com.nfc.reader;

public class ReaderException extends Exception {

	private static final long serialVersionUID = -6721425559187187728L;

	public ReaderException(String exception)
	{
		super(exception);
	}
	public ReaderException(String exception,Exception innerException)
	{
		super(exception,innerException);
	}

	
	

}
