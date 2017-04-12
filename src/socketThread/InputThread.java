package socketThread;

import java.io.InputStream;

import conversation.Conversation;
import io.DataPackage;

public class InputThread extends IOThread{
	private InputStream inputStream;
	
//	public static void main(String[] args) throws IOException {
//		ServerSocket serverSocket=new ServerSocket(1234);
//		Socket socket=serverSocket.accept();
//		FileData fileData=new FileData(new File("f:/test"), null);
//		LinkedList<Data> datas=new LinkedList<Data>();
//		datas.add(fileData);
//		DataPackage dataPackage=new DataPackage(datas);
//		InputThread inputThread=new InputThread(null, 1111, dataPackage, socket.getInputStream());
//		new Thread(inputThread,"接收").start();
//	}
	
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
	public InputThread(Conversation conversation, DataPackage dataPackage,int byteLen, InputStream inputStream) {
		this(conversation, dataPackage, inputStream);
		this.byteLen=byteLen;
	}
	
	private void receiveData() throws Exception {
		byte[] bs;
		
		if (conversation.isServer()) {
			bs=new byte[Conversation.INIT_BYTE_LEN];
			int readCount=inputStream.read(bs,byteOff,bs.length-byteOff);
			while (readCount < bs.length-byteOff) {
				int getLen=inputStream.read(bs,readCount,bs.length-readCount);
				if(getLen==-1) break;
				else readCount+=getLen;
			}
			conversation.dealMainShakeHandInfo(bs, 0, readCount);
		}
		
		bs=new byte[byteLen];
		while (run) {
			int readCount=inputStream.read(bs,byteOff,bs.length-byteOff);
			while (readCount < bs.length-byteOff) {
				int getLen=inputStream.read(bs,readCount,bs.length-readCount);
				if(getLen==-1) break;
				else readCount+=getLen;
			}
			
			int point=dataPackage.wtire(bs, byteOff, readCount);
			if(point>0) {
				System.out.println("比特流偏正:"+point+":"+new String(bs,byteOff,inputStream.read(bs, byteOff, point),"utf-8"));
			}else if (point==-2) {
				dataPackage.closeWrite();
			}
		}
		dataPackage.closeWrite();
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
			System.out.println("对方断开了连接");
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		run=false;
		try { inputStream.close(); } catch (Exception e) { e.printStackTrace(); }
	}

}
