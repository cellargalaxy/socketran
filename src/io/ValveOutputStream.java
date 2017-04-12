package io;


public class ValveOutputStream {
	private Data data;
	private long fileLen;
	private long fileLenCount;

	
	public ValveOutputStream(Data data, long fileLen,String headInfo) throws Exception {
		super();
		this.data = data;
		this.fileLen = fileLen;
		fileLenCount=0;
		data.initWrite(headInfo);
	}
	
	public  synchronized boolean writeValve(byte[] b, int off, int len) throws Exception {
		return write(b, off, len);
	}

	private synchronized boolean write(byte[] b, int off, int len) throws Exception {
		// TODO Auto-generated method stub
		if (fileLenCount<fileLen) {
			fileLenCount+=len;
			if (fileLenCount<fileLen) {
				data.write(b, off, len);
				return true;
			}else if (fileLenCount==fileLen) {
				data.write(b, off, len);
				return false;
			}else {
				len-=fileLenCount-fileLen;
				data.write(b, off, len);
				return false;
			}
		} else {
			return false;
		}
	}

	public void close() {
		data.destroyWrite();
	}

	
	
}
