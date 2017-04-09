package exp;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import conversation.Client;
import dataByte.Data;

public class Cli extends Client{

	public Cli(int byteLen, Socket socket, LinkedList<Data> datas,int waitTime) throws IOException {
		super(byteLen, socket, datas, waitTime);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String createShakeHandInfo() {
		// TODO Auto-generated method stub
		System.out.println("调用了客户端的createShakeHandInfo");
		return "来自客户端的握手信息";
	}

	@Override
	public void dealShakeHandInfo(String shakeHandInfo) {
		// TODO Auto-generated method stub
		System.out.println("调用了客户端的dealShakeHandInfo;客户端是不会收到握手信息的:"+shakeHandInfo);
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		int byteLen=1024;
		Socket socket=new Socket("127.0.0.1", 1234);
		LinkedList<Data> datas=new LinkedList<Data>();
		datas.add(new DataFile(new File("g:/")));
		Cli cli=new Cli(byteLen, socket, datas,1000);
		cli.addData(new DataString("hello world！"));
		cli.addData(new DataFile(new File("h:/huaji.jpg")));
		cli.addData(new DataFile(new File("h:/templateback.jpg")));
	}
}
