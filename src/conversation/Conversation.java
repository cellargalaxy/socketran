package conversation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.LinkedList;

import io.Data;
import io.DataPackage;
import io.DataPool;
import socketThread.InputThread;
import socketThread.OutputThread;

public abstract class Conversation {
	public final static int INIT_BYTE_LEN=1024;
	private final static char SPACE=' ';
	private final static String SEPARATOR=",";
	private final static String CODING="utf-8";
	
	private boolean isServer;
	private int byteLen;
	
	private Socket socket;
	private InputThread inputThread;
	private LinkedList<Data> datas;
	private OutputThread outputThread;
	private DataPool dataPool;
	
	/**
	 * 客户端创建Conversation对象
	 * @param byteLen
	 * @param socket
	 * @param datas
	 * @throws IOException 
	 */
	Conversation(int byteLen, Socket socket, LinkedList<Data> datas) throws IOException {
		super();
		this.byteLen = byteLen;
		this.socket = socket;
		this.datas = datas;
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
	Conversation(Socket socket, LinkedList<Data> datas) throws IOException {
		super();
		this.socket = socket;
		this.datas = datas;
		isServer = true;
		dataPool=new DataPool();
		ServerStart();
	}
	
	private void ServerStart() throws IOException {
		inputThread=new InputThread(this , new DataPackage(datas), socket.getInputStream());
		new Thread(inputThread,"服务端inputThread").start();
	}
	
	private void ClientStart() throws IOException {
		inputThread=new InputThread(this, new DataPackage(datas), byteLen,  socket.getInputStream());
		outputThread=new OutputThread(this, new DataPackage(null), byteLen, socket.getOutputStream(), dataPool);
		new Thread(inputThread,"客户端inputThread").start();
		new Thread(outputThread,"客户端outputThread").start();
		
		byte[] bs=new byte[INIT_BYTE_LEN];
		createMainShakeHandInfo(bs, 0, bs.length);
		outputThread.sendShakeHandInfo(bs, 0, bs.length);
	}
	
	/**
	 * @return 使用者返回用于会话握手/检验的信息
	 */
	public abstract String createShakeHandInfo();
	public int createMainShakeHandInfo(byte[] bs,int off ,int len) throws UnsupportedEncodingException {
		String mainShakeHandInfo=byteLen+SEPARATOR+createShakeHandInfo();
		fillingByte(bs, off, len, mainShakeHandInfo.getBytes(CODING));
		return len;
	}
	
	
	/**
	 * @param shakeHandInfo 处理对方发送过来用于会话握手/检验的信息
	 */
	public abstract void dealShakeHandInfo(String shakeHandInfo);
	public void dealMainShakeHandInfo(byte[] bs,int off,int len) throws IOException {
		String mainShakeHandInfo=new String(bs, off, len,CODING);
		String[] mainShakeHandInfos=mainShakeHandInfo.split(SEPARATOR);
		byteLen=new Integer(mainShakeHandInfos[0].trim());
		dealShakeHandInfo(mainShakeHandInfos[1].trim());
		
		inputThread.setByteLen(byteLen);
		outputThread=new OutputThread(this, new DataPackage(null), byteLen, socket.getOutputStream(), dataPool);
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
		for (; point < bs1.length && point-off<bs2.length && point<off+len; point++) bs1[point]=bs2[point-off];
		for (; point < bs1.length; point++) bs1[point]=SPACE;
	}

	public boolean isServer() {
		return isServer;
	}
	
}
