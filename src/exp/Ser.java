package exp;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import conversation.Server;
import io.Data;

public class Ser extends Server{
	
	public Ser(Socket socket, LinkedList<Data> datas) throws IOException {
		super(socket, datas);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String createShakeHandInfo() {
		// TODO Auto-generated method stub
		System.out.println("调用了服务端的createShakeHandInfo");
		return "服务端是不会发送握手信息的";
	}

	@Override
	public void dealShakeHandInfo(String shakeHandInfo) {
		// TODO Auto-generated method stub
		System.out.println("调用了服务端的dealShakeHandInfo:"+shakeHandInfo);
	}
}
