package io;

import java.io.IOException;
import java.io.OutputStream;

import conversation.Conversation;
import dataByte.Data;
import dataByte.DataPackage;
import dataByte.DataPool;

public class OutputThread extends IOThread{
	private OutputStream outputStream;
	private DataPool dataPool;
	private int waitTime;
	
	
	
	/**
	 * 服务端和客户端都通过此创建OutputThread对象
	 * 客户端由于决定byteLen，所以可以一开始就创建
	 * 服务端则需要先创建InputThread，等待握手信息之后再创建OutputThread
	 * @param conversation
	 * @param dataPackage
	 * @param outputStream
	 * @param dataPool
	 * @param byteLen
	 */
	public OutputThread(Conversation conversation, DataPackage dataPackage,int byteLen, OutputStream outputStream,
			DataPool dataPool,int waitTime) {
		super(conversation, dataPackage);
		this.byteLen=byteLen;
		this.outputStream = outputStream;
		this.dataPool = dataPool;
		this.waitTime=waitTime;
	}
	
	public void sendShakeHandInfo(byte[] bs,int off,int len) throws IOException {
		send(bs, off, len);
	}
	
	private synchronized void sendDataPool() throws IOException, InterruptedException {
		while (run) {
			Data data=dataPool.pollData();
			if (data!=null) {
				sendData(data);
			} else {
				this.wait(waitTime);
			}
		}
	}
	
	private void sendData(Data data) throws IOException {
		dataPackage.setData(data);
		int len=-1;
		byte[] bs=new byte[byteLen];
		while ((len=dataPackage.read(bs,0,bs.length))!=-1) {
	//		System.out.println("发送:长度:"+len+";"+new String(bs,0,len));
			send(bs, 0, len);
		}
		dataPackage.destroyRead();
	}
	
	private void send(byte[] bs,int off,int len) throws IOException {
		outputStream.write(bs, off, len);
		outputStream.flush();
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			sendDataPool();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			conversation.stopAll();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { outputStream.close(); } catch (Exception e) { e.printStackTrace(); }
	}
}
