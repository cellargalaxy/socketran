package conversation;

import java.io.IOException;
import java.net.Socket;

import dataByte.DataPool;
import io.InputThread;
import io.OutputThread;

public class Conversation{
	private Socket socket;
	private InputThread inputThread;
	private OutputThread outputThread;
	private boolean isConversing;
	private int byteLen;
	private DataPool dataPool;
	
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
	
	
	
	public void stopAll() {
		inputThread.stop();
		outputThread.stop();
		try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
	}
	
	
}
