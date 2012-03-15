package com.nfc.device;

import com.nfc.reader.Reader;
import com.nfc.reader.ReaderException;

public interface Tag {
	
	public int getBlockCount();
	public int getSectorCount();
	public void setUID(Reader reader) throws ReaderException;
	public byte[] getUID();
	public void setKeyA(byte[] key);
	public void setKeyB(byte[] key);
	public byte[] getKeyA();
	public byte[] getKeyB();
	public boolean authenticateSector(Reader reader,int block,Key keyType, byte[] key) throws ReaderException;
	public byte[] readData(Reader reader,int block, Key keyType) throws ReaderException;
	public byte[] readAllData(Reader reader,int initialSector,Key keyType) throws ReaderException;
	public void setData(Reader reader,int block, byte[] data, Key keyType) throws ReaderException;
	public void setAllData(Reader reader,int initialSector, byte[] data, Key keyType) throws ReaderException;
}
