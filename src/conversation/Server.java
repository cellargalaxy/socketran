package conversation;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import dataByte.Data;

public abstract class Server extends Conversation{

	public Server(Socket socket, LinkedList<Data> datas) throws IOException {
		super(socket, datas);
		// TODO Auto-generated constructor stub
	}
	
}
