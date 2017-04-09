package conversation;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import dataByte.Data;

public abstract class Client extends Conversation{

	public Client(int byteLen, Socket socket, LinkedList<Data> datas,int waitTime) throws IOException {
		super(byteLen, socket, datas, waitTime);
		// TODO Auto-generated constructor stub
	}
	
}
