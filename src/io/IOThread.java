package io;

import conversation.Conversation;

public abstract class IOThread implements Runnable{
	protected boolean run;
	protected Conversation conversation;
	protected int byteLen;
	
	
	
	
	public IOThread(Conversation conversation, int byteLen) {
		super();
		run = true;
		this.conversation = conversation;
		this.byteLen = byteLen;
	}




	public abstract void stop();
	
}
