package com.nfc.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NfcUtils {
	
	
	public static List<String> byteArrayToHexArray(byte[] bytes) {
		List<String> hexString = new ArrayList<String>();
		for(int i=0;i<bytes.length;i++)
		{
			String hex = Integer.toHexString(0xFF & bytes[i]);
			hex = hex.toUpperCase();
			if (hex.length() == 1) {
			    // could use a for loop, but we're only dealing with a single byte
			    hex = "0" + hex;
			}
			hexString.add(hex);
		}
		return hexString;
	}
	
	public static String hexArrayToString(List<String> hexArray)
	{
		Iterator<String> it = hexArray.iterator();
		String hexString = "";
		while(it.hasNext())
		{
			String hex = it.next();
			hexString += hex + " ";
		}
		return hexString;
	}
	
	public static String byteArrayToHexString(byte[] bytes)
	{
		List<String> hexArray = byteArrayToHexArray(bytes);
		String hexString = hexArrayToString(hexArray);
		return hexString;
	}
	
	
	/**
	 * Append a byte array to another byte array
	 * 
	 * @param first
	 *            the byte array to append to
	 * @param second
	 *            the byte array to append
	 * @return the appended array
	 */
	public static byte[] appendToByteArray(byte[] first, byte[] second) {
		int secondLength = (second != null) ? second.length : 0;
		return appendToByteArray(first, second, 0, secondLength);
	}
	
	
	/**
	 * Append a byte array to another byte array specifying which part of the
	 * second byte array should be appended to the first
	 * 
	 * @param first
	 *            the byte array to append to
	 * @param second
	 *            the byte array to append
	 * @param offset
	 *            offset in second array to start appending from
	 * @param length
	 *            number of bytes to append from second to first array
	 * @return the appended array
	 */
	public static byte[] appendToByteArray(byte[] first, byte[] second,
			int offset, int length) {
		if (second == null || second.length == 0) {
			// if (first == null)
			// return new byte[0];
			return first;
		}
		int firstLength = (first != null) ? first.length : 0;

		if (length < 0 || offset < 0 || second.length < length + offset)
			throw new ArrayIndexOutOfBoundsException();
		byte[] result = new byte[firstLength + length];
		if (firstLength > 0)
			System.arraycopy(first, 0, result, 0, firstLength);
		System.arraycopy(second, offset, result, firstLength, length);
		return result;
	}
	
	/**
	 * Converts a byte array to readable string
	 * 
	 * @param a
	 *            array to print
	 * @return readable byte array string
	 */
	public static String byteArrayToString(byte[] a) {
		if (a == null)
			return "[null]";
		if (a.length == 0)
			return "[empty]";
		String result = "";
		for (int i = 0; i < a.length; i++) {
			result += byteToString(a[i]) + " ";
		}
		return result;
	}
	
	/**
	 * Convert a byte to a human readable representation
	 * 
	 * @param b
	 *            the byte
	 * @return the human readable representation
	 */
	public static String byteToString(int b) {
		String s = Integer.toHexString(b);
		if (s.length() == 1)
			s = "0" + s;
		else
			s = s.substring(s.length() - 2);
		s = "0x" + s.toUpperCase();
		return s;
	}
	
	public static byte[] int2bytearray(int i) {  
		  byte b[] = new byte[4];  
		   
		  ByteBuffer buf = ByteBuffer.wrap(b);  
		  buf.putInt(i);  
		  return b;  
	}  
	public static int bytearray2int(byte[] b) {  
		  ByteBuffer buf = ByteBuffer.wrap(b);  
		  return buf.getInt();  
	}  
}
