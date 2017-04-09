package io;

import conversation.Conversation;
import dataByte.DataPackage;

public abstract class IOThread implements Runnable{
	protected boolean run;
	protected Conversation conversation;
	protected int byteLen;
	protected DataPackage dataPackage;
	
	
	
	public IOThread(Conversation conversation, DataPackage dataPackage) {
		super();
		this.conversation = conversation;
		this.dataPackage = dataPackage;
		run=true;
	}
	
	public abstract void stop();

	public void setByteLen(int byteLen) {
		this.byteLen = byteLen;
	}
	
}
