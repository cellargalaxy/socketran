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
	
	
	
	public OutputThread(Conversation conversation, int byteLen, DataPackage dataPackage, OutputStream outputStream,
			DataPool dataPool) {
		super(conversation, byteLen, dataPackage);
		this.outputStream = outputStream;
		this.dataPool = dataPool;
	}

	private synchronized void sendDataPool() throws InterruptedException, IOException {
		while (run) {
			Data data=dataPool.pollData();
			if (data!=null) {
				while (run) {
					if (Math.random()<10) {
						System.out.println("添加一个Data");
						sendData(data);
						freeConver();
						break;
					} else {
						this.wait();
					}
				}
			} else {
				this.wait();
			}
			Thread.sleep(1000);/////////////////////////////////////////////////////////
		}
	}
	
	private void sendData(Data data) throws IOException {
		dataPackage.setData(data);
		int len=-1;
		byte[] bs=new byte[byteLen];
		while ((len=dataPackage.read(bs))!=-1) {
			send(bs, 0, len);
		}
		dataPackage.destroyRead();
	}
	
	private void send(byte[] bs,int off,int len) throws IOException {
		outputStream.write(bs, off, len);
		outputStream.flush();
	}
	
	@Override
	protected void freeConver() {
		//还有释放会话权
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			sendDataPool();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			stop();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { outputStream.close(); } catch (Exception e) { e.printStackTrace(); }
	}
}
