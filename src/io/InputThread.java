package io;

import java.io.IOException;
import java.io.InputStream;

import conversation.Conversation;
import dataByte.DataPackage;

public class InputThread extends IOThread{
	private InputStream inputStream;
	private boolean isReceiving;
	private long infoLineCount;
	private int infoLineRemainder;
	private long yetinfoLine;
	
	
	public InputThread(Conversation conversation, int byteLen, DataPackage dataPackage, InputStream inputStream) {
		super(conversation, byteLen, dataPackage);
		this.inputStream = inputStream;
		isReceiving=false;
	}
	
	
	private void receiveDatas() throws IOException, InterruptedException {
		int len=-1;
		byte[] bs=new byte[byteLen];
		while (run && (len=inputStream.read(bs))!=-1) {
			receiveData(bs, 0, len);
			Thread.sleep(1000);//////////////////////////////////////////////////////////////
		}
	}
	private void receiveData(byte[] bs,int off ,int len) {
		if (!isReceiving) {
			if(dataPackage.write(bs, off, len)) {
				infoLineCount=dataPackage.getInfoLen()/byteLen;
				infoLineRemainder=(int)(dataPackage.getInfoLen()%byteLen);
				if(infoLineRemainder!=0) infoLineCount++;
				yetinfoLine=-1;
				isReceiving=true;
			}else {
				freeConver();
			}
		}else {
			yetinfoLine++;
			if (yetinfoLine<infoLineCount) {
				if(!dataPackage.write(bs, off, len)) freeConver();
			}else if (yetinfoLine==infoLineCount) {
				dataPackage.write(bs, off, infoLineRemainder);
				freeConver();
			}
		}
	}
	
	@Override
	protected void freeConver() {
		//还有释放会话权
		isReceiving=false;
		dataPackage.destroyWrite();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			receiveDatas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			stop();
		}
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { inputStream.close(); } catch (Exception e) { e.printStackTrace(); }
	}
	
	
}
