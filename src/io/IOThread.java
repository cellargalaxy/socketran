package io;

import conversation.Conversation;
import dataByte.DataPackage;

public abstract class IOThread implements Runnable{
	protected boolean run;
	protected Conversation conversation;
	protected int byteLen;
	protected DataPackage dataPackage;
	
	
	
	public IOThread(Conversation conversation, int byteLen, DataPackage dataPackage) {
		super();
		this.conversation = conversation;
		this.byteLen = byteLen;
		this.dataPackage = dataPackage;
		run=true;
	}
	
	protected abstract void freeConver();
	public abstract void stop();

	public void setByteLen(int byteLen) {
		this.byteLen = byteLen;
	}
	
}
