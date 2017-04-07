package io;

import java.io.IOException;
import java.io.InputStream;

import conversation.Conversation;
import dataByte.Data;

public class InputThread extends IOThread{
	private InputStream inputStream;
	private Data data;
	
	


	

	public InputThread( Conversation conversation, int byteLen, InputStream inputStream, Data data) {
		super(conversation, byteLen);
		this.inputStream = inputStream;
		this.data = data;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			int len;
			byte[] bs=new byte[byteLen];
			while (run&&((len=inputStream.read(bs))!=-1)) {
				data.write(bs,0,len);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			stop();
		}
	}

	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { inputStream.close(); } catch (IOException e) { e.printStackTrace(); }
	}

}
