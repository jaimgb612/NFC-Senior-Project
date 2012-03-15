package com.message.ndef.tlv;

import java.util.Arrays;

public class TlvParser {
	
	private byte type = TlvConstants.NULL_TLV; // initialized to null tlv
	
	public TlvParser() {
		
	}
	
	public byte[] parse(byte[] tlvData) throws TlvException {
		
		try
		{
			int i= 0;
			while(i < tlvData.length)
			{
				if(tlvData[i] == TlvConstants.NULL_TLV)
				{
					i++;
					continue;
				}
				else if(tlvData[i] == TlvConstants.TERMINATOR_TLV)
					throw new TlvException("Unexpected TLV termination");
				else if(tlvData[i] == TlvConstants.NDEF_TLV)
					return readValueByLength(tlvData,i);
				else if(tlvData[i] == TlvConstants.TERMINATOR_TLV)
					return readValueByLength(tlvData,i);
				else
					throw new TlvException("Unknown/reserved TLV constant at index : " + i);
			}
			throw new TlvException("Unexcepted TLV with only zero paddings and no value field and no termination");
		}
		catch(Exception ex)
		{
			throw new TlvException("Error while parsing TLV",ex);
		}
	}
	
	private byte[] readValueByLength(byte[] tlvData,int index) throws TlvException
	{
		index += 1; // jump for length bytes
		int length = tlvData[index];
		if(length == 0xFF)
		{
			length = (tlvData[++index] << 8) | tlvData[++index];
		}
		
		if(++index + length > tlvData.length-1) // the last byte is TLV_TERMINATOR
			throw new TlvException("TLV format is invalid, not enough bytes in value field as specified in length");
		
		if(tlvData[index + length] != TlvConstants.TERMINATOR_TLV)
			throw new TlvException("TLV format is invalid, expected TLV terminator at the index : " + index+length);

		byte[] value = Arrays.copyOfRange(tlvData, index, index + length);
		
		return value;
	}
		
	
	
}
