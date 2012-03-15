package com.message.ndef;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.nfc.utils.NfcUtils;

public class NdefParser {
	
	private byte TNF = NdefConstants.TNF_EMPTY;
	private int payloadLength = 0;
	private int typeLength = 0;
	private byte[] Type = null;
	private byte[] ID = null;
	private byte[] Payload = null;
	
	private boolean __debugMode = false;
	private Logger logger = null;

	
	
	public NdefParser(boolean debugMode) {
		this.__debugMode = debugMode;
		this.logger = Logger.getLogger("NdefParser");
	}
	
	public byte[] getType() {
		return Type;
	}
	public byte[] getID() {
		return ID;
	}
	public byte[] getPayload() {
		return Payload;
	}
	
	public int getPayloadLength() {
		return payloadLength;
	}

	public byte getTNF() {
		return this.TNF;
	}
	
	private boolean isMessageBegin(byte flagByte) {
		if((flagByte & NdefConstants.NDEF_MB) == NdefConstants.NDEF_MB)
			return true;
		else
			return false;
	}

	private boolean isMessageEnd(byte flagByte) {
		if((flagByte & NdefConstants.NDEF_ME) == NdefConstants.NDEF_ME)
			return true;
		else
			return false;
	}

	private boolean isChunked(byte flagByte) {
		if((flagByte & NdefConstants.NDEF_CF) == NdefConstants.NDEF_CF)
			return true;
		else
			return false;
	}

	private boolean isShort(byte flagByte) {
		if((flagByte & NdefConstants.NDEF_SF) == NdefConstants.NDEF_SF)
			return true;
		else
			return false;
	}

	private boolean isIdPresent(byte flagByte) {
		if((flagByte & NdefConstants.NDEF_IL) == NdefConstants.NDEF_IL)
			return true;
		else
			return false;
	}

	private byte resolveTNF(byte flagByte) {
		return (byte) (flagByte & NdefConstants.TNF_RESERVED);
	}

	private void initializeVariables()
	{
		this.payloadLength = 0;
		this.typeLength = 0;
		this.Payload = null;
		this.ID = null;
		this.Type = null;
		this.TNF = NdefConstants.TNF_EMPTY;
	}
	
	public void parse(byte[] message) throws ParserException {		
				
		if(message == null || message.length == 0)
			throw new ParserException("Message is null or empty");
		
		// All the variables are initialized to an initial value
		initializeVariables();
		
		byte flag = message[0];
		int index = 0;
		
		try
		{
			if(!this.isMessageEnd(flag) && !this.isChunked(flag)) 
				throw new ParserException("Can not parse, the NDEF record is neither the end nor the chunked record");
			
			if(!this.isMessageBegin(flag) && !this.isMessageEnd(flag) && !this.isChunked(flag)) 
				throw new ParserException("Can not parse, the NDEF record is neither the begin/end nor the chunked record");
			
			if(this.resolveTNF(flag) == NdefConstants.TNF_EMPTY) {
				if(this.__debugMode)
					logger.log(Level.WARNING,"TNF_EMPTY : empty ndef record");
				return; 		// empty ndef record
			}
			
			else if(this.resolveTNF(flag) == NdefConstants.TNF_WELL_KNOWN) {
				if(this.__debugMode)
					logger.info("TNF_WELL_KNOWN");
				logger.log(Level.WARNING,"Not implemented yet !!!");
				return;
			}
			else if(this.resolveTNF(flag) == NdefConstants.TNF_ABSOLUTE_URI) {
				if(this.__debugMode)
					logger.info("TNF_ABSOLUTE_URI");
				logger.log(Level.WARNING,"Not implemented yet !!!");
				return;
			}
			else if(this.resolveTNF(flag) == NdefConstants.TNF_EXTERNAL) {
				if(this.__debugMode)
					logger.info("TNF_EXTERNAL");
				logger.log(Level.WARNING,"Not implemented yet !!!");
				return;
			}
			else if(this.resolveTNF(flag) == NdefConstants.TNF_MIME_MEDIA) {
				if(this.__debugMode)
					logger.info("TNF_MIME_MEDIA");
				logger.log(Level.WARNING,"Not implemented yet !!!");
				return;
			}
			else if(this.resolveTNF(flag) == NdefConstants.TNF_UNCHANGED) {
				if(this.__debugMode)
					logger.info("TNF_UNCHANGED");
				logger.log(Level.WARNING,"Not implemented yet !!!");
				return;
			}
			
			else if(this.resolveTNF(flag) == NdefConstants.TNF_UNKNOWN) {
				
				if(this.__debugMode)
					logger.info("TNF_UNKNOWN");
				
				this.TNF = NdefConstants.TNF_UNKNOWN;
				
				++index; // type length is omitted since the type is unknown
				
				this.typeLength = 0; // No type and thus no type length field
				
			}
			
			
			else if(this.resolveTNF(flag) == NdefConstants.TNF_RESERVED) {
				if(this.__debugMode)
					logger.info("TNF_RESERVED");
				logger.log(Level.WARNING,"This TYPE is not used. It is reserved for future use. Might be implemented for special purpose.");
				return;
			}
			
			else {
				throw new ParserException("TNF couldn't be retrieved correctly");
			}
			
			
			// index is at the start point of payload
			
			if(this.isShort(flag)) {
				this.payloadLength = message[index++];	// the next one byte shows the length of the payload
			}
			else {
				this.payloadLength = NfcUtils.bytearray2int(Arrays.copyOfRange(message,index,index + 4));
				index += 4;
			}
			
			
			// index is at the start of the ID length field
			int idLength = 0;			
			if(this.isIdPresent(flag)) {
				idLength = message[index++];
			}
			
			// index is at the beginning of the type field
			if(this.typeLength != 0) {
				this.Type = Arrays.copyOfRange(message, index, index + this.typeLength);
				index += this.typeLength;
			}
			
			// index is at the beginning of the ID field
			if(this.isIdPresent(flag) && idLength > 0) {
				this.ID = Arrays.copyOfRange(message, index, index + idLength);
				index += idLength;
			}
			
			// index is at the beginning of the payload data
			this.Payload = Arrays.copyOfRange(message, index, index + this.payloadLength);			
		
		}
		
		catch(Exception ex)  {
			throw new ParserException("NDEF parser failed",ex);
		}		
	}

}
