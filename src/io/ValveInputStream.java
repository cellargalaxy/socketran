package io;


public class ValveInputStream {
	private Data data;
	private long fileLen;
	private long fileLenCount;

	public ValveInputStream(Data data) throws Exception {
		super();
		this.data = data;
		fileLen=data.getInfoLen();
		fileLenCount=0;
		data.initRead();
	}
	
	public synchronized int readValve(byte[] b, int off, int len) throws Exception {
		return read(b, off, len);
	}
	
	private synchronized int read(byte[] b, int off, int len) throws Exception {
		// TODO Auto-generated method stub
		if (fileLenCount<fileLen) {
			int getLen=data.read(b, off, len);
			if (getLen!=-1) {
				fileLenCount+=getLen;
				if (fileLenCount>fileLen) getLen-=fileLenCount-fileLen;
			}
			return getLen;
		} else {
			return -1;
		}
		
	}
	
	public void close() {
		data.destroyRead();
	}
	
	
}
