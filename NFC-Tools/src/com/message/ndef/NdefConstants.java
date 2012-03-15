package com.message.ndef;

public class NdefConstants {
	
	public static byte NDEF_IL = (byte) 0x08; // ID Length is present
	public static byte NDEF_SF = (byte) 0x10; // Short Record Flag
	public static byte NDEF_CF = (byte) 0x20; // Chunked message Flag
	public static byte NDEF_ME = (byte) 0x40; // Message begin
	public static byte NDEF_MB = (byte) 0x80; // Message end
	
	public static byte TNF_EMPTY = (byte) 0x00;
	public static byte TNF_WELL_KNOWN = (byte) 0x01;
	public static byte TNF_MIME_MEDIA = (byte) 0x02;
	public static byte TNF_ABSOLUTE_URI = (byte) 0x03;
	public static byte TNF_EXTERNAL = (byte) 0x04;
	public static byte TNF_UNKNOWN = (byte) 0x05;		// TYPE_ID is zero AND TYPE field is omitted
	public static byte TNF_UNCHANGED = (byte) 0x06;
	public static byte TNF_RESERVED = (byte) 0x07;		// 0x07 is reserved and MUST NOT be used
	
	
	
	
}
