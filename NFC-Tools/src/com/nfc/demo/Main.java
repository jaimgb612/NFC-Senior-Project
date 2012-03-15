package com.nfc.demo;




import com.data.connection.Connection;
import com.data.connection.ConnectionException;
import com.message.ndef.NdefParser;
import com.message.ndef.ParserException;
import com.message.ndef.tlv.TlvException;
import com.message.ndef.tlv.TlvParser;
import com.nfc.reader.ACR122Reader;
import com.nfc.reader.Reader;
import com.nfc.reader.ReaderException;
import com.nfc.reader.UnsupportedDeviceException;
import com.nfc.utils.NfcUtils;

public class Main {
	public static void main(String[] args)
	{
	
//		byte[] data = {(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05};
//		
//		NdefRecord record = new NdefRecord(data);
//		try {
//			record.createRecord();
//		} catch (NdefFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		TlvGenerator tlv = new TlvGenerator(TlvConstants.NDEF_TLV);
//		byte[] null_tlv = null;
//		try {
//			null_tlv = tlv.generateTLV(null);
//		} catch (TlvException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		byte[] tlv_record = null;
//		try {
//			tlv_record = tlv.generateTLV(record.toByteArray());
//		} catch (TlvException e1) {
//			e1.printStackTrace();
//		}
//		tlv_record = NfcUtils.appendToByteArray(null_tlv, tlv_record, 0, tlv_record.length);
//		System.out.println(NfcUtils.hexArrayToString(NfcUtils.byteArrayToHexArray(tlv_record)));
//		
//		Reader reader = new ACR122Reader(true);
//		try {
//			if (reader.waitForDeviceReady(500)) {					
//				Connection con = reader.getConnection();
//				
//				if(con != null)
//				{
//					con.sendData(tlv_record);
//				}
//			}
//		} catch (ReaderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedDeviceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch(ConnectionException c) {
//			c.printStackTrace();
//		}
		
		
		
		
		NdefParser parser = new NdefParser(true);
		
		Reader reader = new ACR122Reader(true);
		byte[] result  = null;
		byte[] message = null;
		
		while (!Thread.interrupted()) {
			try {
				if (reader.waitForDeviceReady(500)) {					
					Connection con = reader.getConnection();
					
					
					if(con != null)
					{
						result  = con.receiveData();
						TlvParser tlvParser = new TlvParser();
						message = tlvParser.parse(result);
						parser.parse(message);
						message = parser.getPayload();
						if(message != null)
							System.out.println("Payload data : " + NfcUtils.byteArrayToHexString(message));
						else
							System.out.println("Payload data : null");

					}
				}
			}
			catch(ParserException pe)
			{
				pe.printStackTrace();
			}
			catch(ReaderException e) {
				e.printStackTrace();
			}
			catch(ConnectionException ce) {
				ce.printStackTrace();
			}
			catch(UnsupportedDeviceException usde)
			{
				usde.printStackTrace();
			} catch (TlvException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				reader.disconnect();
			}
			catch(ReaderException e) {
				e.printStackTrace();
			}
			
		}
		
//		NFCDemo window = new NFCDemo();
//		
//		NFCDemo.setDefaultLookAndFeelDecorated(true);
//		
//		NFCListener listener = window.getListener();
//		listener.run();
		
	}
		


}