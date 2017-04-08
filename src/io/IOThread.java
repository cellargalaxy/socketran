package io;

import conversation.Conversation;
import dataByte.DataPackage;

public abstract class IOThread implements Runnable{
	protected boolean run;
	protected Conversation conversation;
	protected int byteLen;
	protected DataPackage dataPackage;
	
	
	
	
	public IOThread(Conversation conversation, int byteLen,DataPackage dataPackage) {
		super();
		run = true;
		this.conversation = conversation;
		this.byteLen = byteLen;
		this.dataPackage=dataPackage;
	}




	public abstract void stop();
	
}