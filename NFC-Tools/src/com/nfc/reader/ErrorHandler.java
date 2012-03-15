package com.nfc.reader;

import javax.smartcardio.ResponseAPDU;

public class ErrorHandler {
	
	/** Checks whether transmission is failed or not. If successful, returns empty string **/
	public static String errorChecker(ResponseAPDU response)
	{
		String errorMessage = "";
		
		if(response.getSW1() == 0x63 && response.getSW2() == 0x00)
			errorMessage = "The operation is failed";
		
		else if(response.getSW1() == 0x63 && response.getSW2() == 0x01)
			errorMessage = "Time out. PN532 chip does not response.";
		
		else if(response.getSW1() == 0x63 && response.getSW2() == 0x27)
			errorMessage = "The checksum of the Contactless Response is wrong.";
		
		else if(response.getSW1() == 0x63 && response.getSW2() == 0x7F)
			errorMessage = "The PN532_Contactless Command is wrong.";

		else if(response.getSW1() == 0x90)
			errorMessage = ""; // empty means no error
		
		else
			errorMessage = "Unknown Error. Try running in DebugMode";
		
		return errorMessage;
	}
	
	public static String errorChecker(byte errorCode)
	{
		String errorMessage = "";
		if(errorCode == (byte) 0x00)
			errorMessage = ""; // No error
		else if(errorCode == (byte)0x01)
			errorMessage = "Time out while transmission";
		
		else if(errorCode == (byte)0x02)
			errorMessage = "CRC error detected by the contactless UART";
		
		else if(errorCode == (byte)0x03)
			errorMessage = "Parity error has been detected by the contactless UART";
		
		else if(errorCode == (byte)0x04)
			errorMessage = "During the Mifare anticollision/select opearation, an erroneous BIT COUNT has detected1";
		
		else if(errorCode == (byte)0x05)
			errorMessage = "Framing error during mifare operation";
		
		else if(errorCode == (byte)0x06)
			errorMessage = "An abnormal bit-collision has been detected during bitwise anticollision at 106 kbps";
		
		else if(errorCode == (byte)0x07)
			errorMessage = "Communication buffer size is insufficient";
		
		else if(errorCode == (byte)0x08)
			errorMessage = "RF buffer overflow has been detected by the contactless UART";
		
		else if(errorCode == (byte)0x0A)
			errorMessage = "In active communication mode, the RF field has not been switched on in time by counterpart";
		
		else if(errorCode == (byte)0x0B)
			errorMessage = "RF protocol error";
		
		else if(errorCode == (byte)0x0D)
			errorMessage = "Temperature error, overheating";
		
		else if(errorCode == (byte)0x0E)
			errorMessage = "Internal buffer overflow";
		
		else if(errorCode == (byte)0x10)
			errorMessage = "Invalid buffer overflow";
		
		else if(errorCode == (byte)0x12)
			errorMessage = "DEP Protocol : the chip configured in target mode does not support the command received from the initiator";

		else if(errorCode == (byte)0x13)
			errorMessage = "DEP protocol/Mifare/ISO/IEC 14443-4 : The data format does not match to the specification";
		
		else if(errorCode == (byte)0x14)
			errorMessage = "Mifare : Authentication error";
		
		else if(errorCode == (byte)0x23)
			errorMessage = "ISO/IEC 14443-3 : UID check byte is wrong";
		
		else if(errorCode == (byte)0x25)
			errorMessage = "DEP Protocol : invalid device state. Thus it does not allow operations";
		
		else if(errorCode == (byte)0x26)
			errorMessage = "Operation is not allowed in this configuration";
		
		else if(errorCode == (byte)0x27)
			errorMessage = "Command is not acceptable";
		
		else if(errorCode == (byte)0x29)
			errorMessage = "The chip configured as target has been released by its initiator";
		
		else if(errorCode == (byte)0x2A)
			errorMessage = "ISO/IEC 14443-3B only. The ID of the card does not match, meaning that the expected card has been exchanged with another one";
		
		else if(errorCode == (byte)0x2B)
			errorMessage = "ISO/IEC 14443-3B only. The card previously activated has been dissapeared";
		
		else if(errorCode == (byte)0x2C)
			errorMessage = "Mismatch between NFCID3 initiator and NFCID3 target in DEP 212/424 kbps passive";
		
		else if(errorCode == (byte)0x2D)
			errorMessage = "An over-current event has been detected";
		
		else if(errorCode == (byte)0x2E)
			errorMessage = "NAD missing in DEP frame";
		else
			errorMessage = "Unknown type of error with error code : " + errorCode;
		return errorMessage;
	}
	
}
