package socketThread;

import java.io.IOException;
import java.io.OutputStream;

import conversation.Conversation;
import io.Data;
import io.DataPackage;
import io.DataPool;

public class OutputThread extends IOThread{
	private OutputStream outputStream;
	private DataPool dataPool;
	
//	public static void main(String[] args) throws IOException, InterruptedException {
//		FileData fileData=new FileData(new File("f:/test"), null);
//		LinkedList<Data> datas=new LinkedList<Data>();
//		datas.add(fileData);
//		DataPackage dataPackage=new DataPackage(datas);
//		Socket socket=new Socket("127.0.0.1", 1234);
//		DataPool dataPool=new DataPool();
//		OutputThread outputThread=new OutputThread(null, dataPackage, 1111, socket.getOutputStream(), dataPool);
//		
//		File root=new File("g:/图片");
//		LinkedList<File> files=new LinkedList<File>();
//		addFolder(files, root);
//		for(File file:files){
//			dataPool.addData(new FileData(file, file.getAbsolutePath().substring(2)));
//		}
//		
//		
//		new Thread(outputThread,"输出").start();
//		
//	}
//	private static void addFolder(LinkedList<File> files,File folder) {
//		File[] fs=folder.listFiles();
//		for(File file:fs){
//			if(file.isFile()) files.add(file);
//			else addFolder(files, file);
//		}
//	}
	
	
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
			DataPool dataPool) {
		super(conversation, dataPackage);
		this.byteLen=byteLen;
		this.outputStream = outputStream;
		this.dataPool = dataPool;
	}
	
	public void sendShakeHandInfo(byte[] bs,int off,int len) throws IOException {
		send(bs, off, len);
	}
	
	private synchronized void sendDataPool() throws Exception {
		while (run) {
			Data data=dataPool.pollData();
			if (data!=null) {
				sendData(data);
			} else {
				this.wait();
			}
		}
	}
	private void sendData(Data data) throws Exception {
		dataPackage.setData(data);
		int len=-1;
		byte[] bs=new byte[byteLen];
		while ((len=dataPackage.read(bs, byteOff, bs.length-byteOff))!=-1) {
			send(bs, byteOff, len);
		}
		dataPackage.closeRead();
//		System.out.println(bs.length+":"+byteOff+":"+len+"摧毁:"+new String(bs,byteOff,bs.length,"utf-8").replaceAll("\r", "").replaceAll("\n", ""));
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			conversation.stopAll();
			System.out.println("对方断开了连接");
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { outputStream.close(); } catch (Exception e) { e.printStackTrace(); }
	}
	
}
