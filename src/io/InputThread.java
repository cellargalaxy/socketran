package io;

import java.io.IOException;
import java.io.InputStream;

import conversation.Conversation;
import dataByte.DataPackage;

public class InputThread extends IOThread{
	private InputStream inputStream;
	
	
	/**
	 * 服务端创建InputThread对象
	 * @param conversation
	 * @param dataPackage
	 * @param inputStream
	 */
	public InputThread(Conversation conversation, DataPackage dataPackage, InputStream inputStream) {
		super(conversation, dataPackage);
		this.inputStream = inputStream;
	}
	
	/**
	 * 客户端创建InputThread对象
	 * @param conversation
	 * @param byteLen
	 * @param dataPackage
	 * @param inputStream
	 */
	public InputThread(Conversation conversation, int byteLen,DataPackage dataPackage, InputStream inputStream) {
		this(conversation, dataPackage, inputStream);
		this.byteLen=byteLen;
	}
	
	
	private void receiveData() throws IOException, InterruptedException {
		int len=-1;
		byte[] bs;
		if (conversation.isServer()) {
			bs=new byte[Conversation.INIT_BYTE_LEN];
			if(run && (len=inputStream.read(bs,0,bs.length))!=-1) conversation.dealMainShakeHandInfo(bs, 0, len);
			else {
				System.out.println("处理握手信息失败");
				return;
			}
		}
		bs=new byte[byteLen];
		while (run && (len=inputStream.read(bs,0,bs.length))!=-1) {
			if(!dataPackage.write(bs, 0, len)) dataPackage.destroyWrite();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			receiveData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			conversation.stopAll();
		}
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { inputStream.close(); } catch (Exception e) { e.printStackTrace(); }
	}
	
	
}
