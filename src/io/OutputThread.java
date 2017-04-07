package io;

import java.io.IOException;
import java.io.OutputStream;

import conversation.Conversation;
import dataByte.Data;
import dataByte.DataPool;

public class OutputThread extends IOThread{
	private OutputStream outputStream;
	private DataPool dataPool;
	
	

	public OutputThread(Conversation conversation, int byteLen, OutputStream outputStream, DataPool dataPool) {
		super(conversation, byteLen);
		this.outputStream = outputStream;
		this.dataPool = dataPool;
	}

	public void write(byte[] bs,int off,int len) throws IOException {
		outputStream.write(bs, off, len);
		outputStream.flush();
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { outputStream.close(); } catch (IOException e) { e.printStackTrace(); }
	}

	
	private void writeData(Data data) {
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (run) {
				Data data;
				while (run&&(data=dataPool.pollData())!=null) {
					writeData(data);
				}
				synchronized (this) {
					this.wait();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			stop();
		}
	}

}
