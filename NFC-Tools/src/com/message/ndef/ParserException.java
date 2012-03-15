package com.message.ndef;

public class ParserException extends Exception {
	
	private static final long serialVersionUID = 5384970744421334718L;

	public ParserException(String message)
	{
		super(message);
	}
	
	public ParserException(String message,Exception innerException)
	{
		super(message,innerException);
	}

}
