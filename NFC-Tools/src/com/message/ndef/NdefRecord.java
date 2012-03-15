package com.message.ndef;


import com.nfc.utils.NfcUtils;

public class NdefRecord {
	
	
	private byte[] record = null;
	private byte[] payload = null;
	private byte TNF = 0x07;  // 0x07 is invalid(reserved) TNF. Here used to check whether set or not
	private byte[] type = null;
	private byte[] id = null;
	
	/***Constructors, three overloads ***/
	public NdefRecord(byte[] __payload)
	{
		this.payload = __payload;
		this.TNF = NdefConstants.TNF_UNKNOWN;
	}
	public NdefRecord(byte tnf,byte[] __payload)
	{
		this.TNF = tnf;
		this.payload = __payload;
	}
	public NdefRecord(byte tnf,byte[] __type,byte[] __payload)
	{
		this.TNF = tnf;
		this.type = __type;
		this.payload = __payload;
	}
	
	public NdefRecord(byte tnf,byte[] __type,byte[] __id,byte[] __payload)
	{
		this.TNF = tnf;
		this.type = __type;
		this.id = __id;
		this.payload = __payload;
	}
	
	
	public byte[] getId() {
		return this.id;
	}
	public void setId(byte[] __id) {
		this.id = __id;
	}
	
	public byte[] getType() {
		return type;
	}
	public void setType(byte[] type) {
		this.type = type;
	}
	
	public byte getTNF() {
		return this.TNF;
	}
	
	public void setTNF(byte tnf) {
		this.TNF = tnf;
	}
	
	public byte[] getPayload() {
		return payload;
	}
	
	public void setPayload(byte[] __payload) {
		this.payload = __payload;
	}
	
	public byte[] toByteArray() {
		return record;
	}
	
	
	public void createRecord() throws NdefFormatException
	{
		byte flags = (byte) 0x00;
		
		int payload_length = payload.length;
		
		flags = (byte) (flags | NdefConstants.NDEF_MB);
		flags = (byte) (flags | NdefConstants.NDEF_ME);
		
		// is short payload
		if (payload_length < 256) {
			flags = (byte) (flags | NdefConstants.NDEF_SF);
		}
		
		// ID present or not
		if(this.id != null) {
			flags = (byte) (flags | NdefConstants.NDEF_IL);
		}
		
		if(this.TNF != (byte)0x07)
			flags = (byte) (flags | this.TNF); // No payload type and no type length field
		else
			flags = (byte) (flags | NdefConstants.TNF_UNKNOWN);
		
		
		byte[] flags_bytes = {(byte)flags};
		
		// Append flags to the record
		this.record = NfcUtils.appendToByteArray(this.record, flags_bytes, 0, flags_bytes.length);
		
		if(this.TNF != NdefConstants.TNF_UNKNOWN)
		{
			// In this case type should not be null or empty
			if(this.type == null)
				throw new NdefFormatException("Record type is missing, needs to be set !");
			
			// append type length
			byte[] type_length_octet = {(byte) this.type.length };
			this.record = NfcUtils.appendToByteArray(this.record, type_length_octet,0,type_length_octet.length);
			
			// append payload length
			if(payload_length < 256)
			{
				byte[] payload_length_octet = {(byte) payload_length }; // one byte is used
				this.record = NfcUtils.appendToByteArray(this.record, payload_length_octet,0,payload_length_octet.length);
			}
			else
			{
				byte[] payload_length_octet = NfcUtils.int2bytearray(payload_length); // four byte is used
				this.record = NfcUtils.appendToByteArray(this.record, payload_length_octet,0,payload_length_octet.length);
			}
			
			if(this.id != null)
			{
				// append ID length
				byte[] ID_length_octet = {(byte) payload_length };
				this.record = NfcUtils.appendToByteArray(this.record, ID_length_octet,0,ID_length_octet.length);
			}
			
			// append type
			this.record = NfcUtils.appendToByteArray(this.record, this.type,0,this.type.length);
			
			if(this.id != null)
			{
				// append ID
				this.record = NfcUtils.appendToByteArray(this.record, this.id,0,this.id.length);
			}
			
		}		
		else // TNF_UNKNOWN 0x05
		{
			// append payload length
			if(payload_length < 256)
			{
				byte[] payload_length_octet = {(byte) payload_length }; // one byte is used
				this.record = NfcUtils.appendToByteArray(this.record, payload_length_octet,0,payload_length_octet.length);
			}
			else
			{
				byte[] payload_length_octet = NfcUtils.int2bytearray(payload_length); // four byte is used
				this.record = NfcUtils.appendToByteArray(this.record, payload_length_octet,0,payload_length_octet.length);
			}
			
			if(this.id != null)
			{
				// append ID length
				byte[] ID_length_octet = {(byte) payload_length };
				this.record = NfcUtils.appendToByteArray(this.record, ID_length_octet,0,ID_length_octet.length);
			}
			
			if(this.id != null)
			{
				// append ID
				this.record = NfcUtils.appendToByteArray(this.record, this.id,0,this.id.length);
			}
			
		}
		
		// Append payload data
		this.record = NfcUtils.appendToByteArray(this.record, this.payload, 0, this.payload.length);
	}
	
	
}
