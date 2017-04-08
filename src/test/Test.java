package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import dataByte.Data;
import dataByte.DataPackage;
import dataByte.DataPool;
import io.InputThread;
import io.OutputThread;

public class Test implements Runnable{
	public static void main(String[] args) throws UnknownHostException, IOException {
		new Thread(new Test()).start();
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket serverSocket=new ServerSocket(1234);
			Socket socket=serverSocket.accept();
			LinkedList<Data> datas=new LinkedList<Data>();
			datas.add(new DataTest(null));
			new Thread(new InputThread(null, 1024, new DataPackage(datas), socket.getInputStream()),"服务端InputThread").start();
			new Thread(new OutputThread(null, 1024, new DataPackage(datas), socket.getOutputStream(), new DataPool()),"服务端OutputThread").start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
