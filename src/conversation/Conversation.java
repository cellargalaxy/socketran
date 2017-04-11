package conversation;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import dataByte.Data;
import dataByte.DataPackage;
import dataByte.DataPool;
import socketIO.InputThread;
import socketIO.OutputThread;

public abstract class Conversation {
	private final static byte SPACE=32;
	private final static String SEPARATOR=",";
	public final static int INIT_BYTE_LEN=1024;
	private final static String CODING="utf-8";
	
	private boolean isServer;
	private int byteLen;
	
	private Socket socket;
	private InputThread inputThread;
	private LinkedList<Data> datas;
	private OutputThread outputThread;
	private DataPool dataPool;
	private int waitTime;
	
	
	/**
	 * 客户端创建Conversation对象
	 * @param byteLen
	 * @param socket
	 * @param datas
	 * @throws IOException 
	 */
	Conversation(int byteLen, Socket socket, LinkedList<Data> datas,int waitTime) throws IOException {
		super();
		this.byteLen = byteLen;
		this.socket = socket;
		this.datas = datas;
		this.waitTime=waitTime;
		isServer = false;
		dataPool=new DataPool();
		ClientStart();
	}

	/**
	 * 服务端创建Conversation对象
	 * @param socket
	 * @param datas
	 * @throws IOException 
	 */
	Conversation(Socket socket, LinkedList<Data> datas,int waitTime) throws IOException {
		super();
		this.socket = socket;
		this.datas = datas;
		this.waitTime=waitTime;
		isServer = true;
		dataPool=new DataPool();
		ServerStart();
	}

	private void ServerStart() throws IOException {
		inputThread=new InputThread(this , new DataPackage(datas), socket.getInputStream());
		new Thread(inputThread,"服务端inputThread").start();
	}
	
	private void ClientStart() throws IOException {
		inputThread=new InputThread(this, byteLen, new DataPackage(datas), socket.getInputStream());
		outputThread=new OutputThread(this, new DataPackage(), byteLen, socket.getOutputStream(), dataPool,waitTime);
		new Thread(inputThread,"客户端inputThread").start();
		new Thread(outputThread,"客户端outputThread").start();
		int len=-1;
		byte[] bs=new byte[INIT_BYTE_LEN];
		if((len=createMainShakeHandInfo(bs, 0, bs.length))!=-1) outputThread.sendShakeHandInfo(bs, 0, len);
		else System.out.println("头信息发送失败");
	}
	
	
	/**
	 * @return 使用者返回用于会话握手/检验的信息
	 */
	public abstract String createShakeHandInfo();
	public int createMainShakeHandInfo(byte[] bs,int off ,int len) {
		try {
			String mainShakeHandInfo=byteLen+SEPARATOR+createShakeHandInfo();
			fillingByte(bs, off, len, mainShakeHandInfo.getBytes(CODING));
			return bs.length;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
	}
	
	
	/**
	 * @param shakeHandInfo 处理对方发送过来用于会话握手/检验的信息
	 */
	public abstract void dealShakeHandInfo(String shakeHandInfo);
	public void dealMainShakeHandInfo(byte[] bs,int off,int len) throws IOException {
		String mainShakeHandInfo=new String(bs, off, len,CODING);
		String bl=mainShakeHandInfo.substring(0, mainShakeHandInfo.indexOf(SEPARATOR));
		byteLen=new Integer(bl);
		dealShakeHandInfo(mainShakeHandInfo.substring(mainShakeHandInfo.indexOf(SEPARATOR)+SEPARATOR.length()).trim());
		
		inputThread.setByteLen(byteLen);
		outputThread=new OutputThread(this, new DataPackage(), byteLen, socket.getOutputStream(), dataPool,waitTime);
		new Thread(outputThread,"服务端outputThread").start();
	}
	
	public void stopAll() {
		try { inputThread.stop(); } catch (Exception e) { e.printStackTrace(); }
		try { outputThread.stop(); } catch (Exception e) { e.printStackTrace(); }
		try { socket.close(); } catch (Exception e) { e.printStackTrace(); }
		try {
			synchronized (outputThread) { outputThread.notify(); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	public void addData(Data data) {
		synchronized (outputThread) { 
			dataPool.addData(data);
			outputThread.notify(); 
		}
	}
	public boolean removeData(Data data) {
		synchronized (outputThread) { 
			return dataPool.removeData(data);
		}
	}
	
	private static void fillingByte(byte[] bs1,int off,int len,byte[] bs2) {
		int point=off;
		for (; point < bs1.length && point-off<bs2.length && point<len; point++) bs1[point]=bs2[point-off];
		for (; point < bs1.length; point++) bs1[point]=SPACE;
	}

	public boolean isServer() {
		return isServer;
	}
	
	
}
