package conversation;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import dataByte.Data;
import dataByte.DataPackage;
import dataByte.DataPool;
import io.InputThread;
import io.OutputThread;

public class Conversation{
	private String converName;
	private String shakeHandInfo;
	private Socket socket;
	private InputThread inputThread;
	private OutputThread outputThread;
	private boolean isConversing;
	private final static int initByteLen=1024;
	private int byteLen;
	private DataPool dataPool;
	private LinkedList<Data> datas;
	
	
	
	
	public Conversation(String converNmae, Socket socket, int byteLen,LinkedList<Data> datas,String shakeHandInfo) throws IOException {
		super();
		this.converName = converNmae;
		this.socket = socket;
		this.byteLen = byteLen;
		this.datas=datas;
		this.shakeHandInfo=shakeHandInfo;
		inputThread=new InputThread(this, initByteLen, new DataPackage(datas), socket.getInputStream());
		outputThread=new OutputThread(this, initByteLen, new DataPackage(null), socket.getOutputStream(), dataPool);
		if(shakeHandInfo!=null) outputThread.sendShakeHandsInfo(createShakeHandInfo());
		new Thread(inputThread,converNmae+":inputThread").start();
		new Thread(outputThread,converNmae+":outputThread").start();
	}
	
	private String createShakeHandInfo() {
		return byteLen+","+shakeHandInfo;
	}
	public void dealShakeHandInfo(String shakeHandInfo) {
		String[] shakeHandInfos=shakeHandInfo.split(",");
		byteLen=new Integer(shakeHandInfos[0]);
		inputThread.setByteLen(byteLen);
		outputThread.setByteLen(byteLen);
	}
	
	public synchronized boolean isConversing() {
		if(isConversing==false) isConversing=true;
		return isConversing;
	}
	/**
	 * 接收方通知发送方接收完成，并且释放会话权
	 */
	public void converInputOver() {
		isConversing=false;
	}
	/**
	 * 发送方接收到接收方的完成确认，并且释放会话权
	 */
	public void converOutputOver() {
		isConversing=false;
	}
	
	
	
	public void stopAll() {
		inputThread.stop();
		outputThread.stop();
		try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
	}
	
	
}
