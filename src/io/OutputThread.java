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

	private void write(byte[] bs,int off,int len) throws IOException {
		outputStream.write(bs, off, len);
		outputStream.flush();
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { outputStream.close(); } catch (IOException e) { e.printStackTrace(); }
	}

	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			int len=-1;
			byte[] bs=new byte[byteLen];
			while (run) {
				Data data=dataPool.pollData();
				if (data!=null) {
					while (run) {
						if (!conversation.isConversing()) {
							dataPackage.setData(data);
							dataPackage.initRead();
							if((len=dataPackage.readMainHead(bs, 0, len))!=-1) write(bs, 0, len);
							if((len=dataPackage.readDeputyHead(bs, 0, len))!=-1) write(bs, 0, len);
							while ((len=dataPackage.readInfo(bs, 0, len))!=-1) {
								write(bs, 0, len);
							}
							dataPackage.destroyRead();
						} else {
							synchronized (this) { this.wait(); }
						}
					}
				}else {
					synchronized (this) { this.wait(); }
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			conversation.stopAll();
		}
	}

}
