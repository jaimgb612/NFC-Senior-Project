package com.message.ndef.tlv;

public class TlvException extends Exception {
	
	private static final long serialVersionUID = 6232784412797018290L;
	
	public TlvException(String message)
	{
		super(message);
	}
	public TlvException(String message,Exception innerException)
	{
		super(message,innerException);
	}

}
