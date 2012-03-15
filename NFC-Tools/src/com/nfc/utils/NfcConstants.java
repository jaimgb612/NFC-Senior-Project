package com.nfc.utils;

public class NfcConstants {

	public final static byte[] FACTORY_DEFAULT_KEY = {(byte) 0xFF,(byte) 0xFF,
		(byte) 0xFF,(byte) 0xFF,(byte) 0xFF,(byte) 0xFF };
	
	public final static byte[] PUBLIC_NDEF_KEY = {(byte) 0xD3, (byte) 0xF7,
		(byte) 0xD3, (byte) 0xF7,(byte) 0xD3, (byte) 0xF7 };
	
	public final static byte[] MAD_ACCESS_KEY = {(byte) 0xA0, (byte) 0xA1,
		(byte) 0xA2, (byte) 0xA3, (byte) 0xA4,(byte) 0xA5 };
	
	public final static byte[] GET_UID = {(byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00,(byte)0x04};
	
	/** ACR122 Header is 5 byte and the last byte for the length of the payload data should be added*/
	public final static byte[] ACR122_HEADER = {(byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00 }; // one byte more needed for the length
	
	public final static byte[] MIFARE_POLL = {(byte) 0xD4, (byte) 0x4A, (byte) 0x01, (byte) 0x00 };
	
	// One byte is needed for KEY_A_AUTH for the block to unlock. And the 6 byte key and 4 byte UID is appended at the end
	public final static byte[] KEY_A_AUTH = {(byte) 0xD4,(byte) 0x40,(byte) 0x01,(byte) 0x60};	
	
	// One byte is needed for KEY_B_AUTH for the block to unlock. And the 6 byte key and 4 byte UID is appended at the end
	public final static byte[] KEY_B_AUTH = {(byte) 0xD4,(byte) 0x40,(byte) 0x01,(byte) 0x61};
	
	// One byte is needed for READ_BLOCK command. The last byte will be the block number to read
	public final static byte[] READ_BLOCK = {(byte) 0xD4,(byte) 0x40,(byte) 0x01,(byte)0x30};

	// One byte is needed for UPDATE_BLOCK command. The last byte will be the block number to update. 
	// The new 16 byte data will be appended at the end.
	public final static byte[] UPDATE_BLOCK = {(byte) 0xD4,(byte) 0x40,(byte) 0x01,(byte)0xA0};
	
	// Halts the tag. In order to renew the communication, the tag should be removed and placed closer again.
	public final static byte[] HALT_TAG = {(byte)0xD4, (byte)0x44, (byte)0x01};
	
}
