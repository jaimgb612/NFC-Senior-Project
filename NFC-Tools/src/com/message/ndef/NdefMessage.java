package com.message.ndef;

import com.nfc.utils.NfcUtils;

public class NdefMessage {
	
	private NdefRecord[] records = null;
	private byte[] data = null;
	
	public NdefMessage(byte[] __data) {
		this.data = __data;
	}
	public NdefMessage(NdefRecord[] __records) {
		this.records = __records;
	}
	public byte[] toByteArray() {
		if(this.records == null)
			return null;
		
		byte[] recordBytes = null;

		for(int i=0;i<this.records.length;i++)
		{
			recordBytes = NfcUtils.appendToByteArray(recordBytes, this.records[i].toByteArray(), 0, this.records[i].toByteArray().length);
		}
		
		return recordBytes;
	}
}
