package com.message.ndef.tlv;

import com.nfc.utils.NfcUtils;

public class TlvGenerator {
	
	private byte tlv_type = TlvConstants.NULL_TLV;
	
	public TlvGenerator(byte type) {
		this.tlv_type = type;
	}
	
	public byte getTlv_type() {
		return tlv_type;
	}

	public void setTlv_type(byte tlv_type) {
		this.tlv_type = tlv_type;
	}
	
	public byte[] generateTLV(byte[] message) throws TlvException {
		
		if(message == null || message.length == 0)
		{
			// null tlv with length 0
			byte[] tlv = {(byte)TlvConstants.NULL_TLV,(byte)0x00};
			return tlv;
		}
		
		byte[] singleByteLength = {(byte)message.length};
		byte[] tlvLength = null;
		
		if(message.length < 0xFE)
			tlvLength = singleByteLength;
		else if(message.length < 0xFFFE)
		{
			byte[] array = NfcUtils.int2bytearray(message.length);
			byte[] threeByteLength = {(byte)0xFF,(byte)array[array.length-2],(byte)array[array.length-1]};
			tlvLength = threeByteLength;
		}
		else
			throw new TlvException("Too big data for TLV format, must be less than {0xFFFE} bytes");
			
			
		byte[] typeBytes = {(byte)this.tlv_type};
		
		byte[] tlvTermination = {(byte) TlvConstants.TERMINATOR_TLV};
		
		byte[] tlvBytes = null;
		
		tlvBytes = NfcUtils.appendToByteArray(tlvBytes, typeBytes , 0, typeBytes.length);
		tlvBytes = NfcUtils.appendToByteArray(tlvBytes,tlvLength , 0, tlvLength.length);
		tlvBytes = NfcUtils.appendToByteArray(tlvBytes, message, 0, message.length);
		tlvBytes = NfcUtils.appendToByteArray(tlvBytes, tlvTermination,0,tlvTermination.length);
		
		return tlvBytes;
	}
}
