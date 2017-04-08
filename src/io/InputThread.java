package io;

import java.io.IOException;
import java.io.InputStream;

import conversation.Conversation;
import dataByte.DataPackage;

public class InputThread extends IOThread{
	private InputStream inputStream;
	
	


	

	

	public InputThread(Conversation conversation, int byteLen, DataPackage dataPackage, InputStream inputStream) {
		super(conversation, byteLen, dataPackage);
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			int len;
			byte[] bs=new byte[byteLen];
			while (run) {
				dataPackage.initWrite();
				if((len=inputStream.read(bs))!=-1) dataPackage.writeMainHead(bs, 0, len);
				if((len=inputStream.read(bs))!=-1) dataPackage.writeDeputyHead(bs, 0, len);
				int lineCount=dataPackage.getInfoLen()/byteLen;
				if(dataPackage.getInfoLen()%byteLen!=0) lineCount++;
				for (int i = 0; i < lineCount && ((len=inputStream.read(bs))!=-1); i++) {
					dataPackage.writeInfo(bs, 0, len);
				}
				dataPackage.destroyWrite();
				conversation.converInputOver();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			conversation.stopAll();
		}
	}

	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { inputStream.close(); } catch (IOException e) { e.printStackTrace(); }
	}

}
