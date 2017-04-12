package conversation;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import io.Data;

public abstract class Client extends Conversation{

	public Client(int byteLen, Socket socket, LinkedList<Data> datas) throws IOException {
		super(byteLen, socket, datas);
		// TODO Auto-generated constructor stub
	}

}
