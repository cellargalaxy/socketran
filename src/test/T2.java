package test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import dataByte.DataPackage;
import dataByte.DataPool;
import io.InputThread;
import io.OutputThread;

public class T2 {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket=new Socket("127.0.0.1", 1234);
		DataTest dataTest=new DataTest("i am info");
		DataPool dataPool=new DataPool();
		dataPool.addData(dataTest);
		new Thread(new InputThread(null, 1024, new DataPackage(), socket.getInputStream()),"客户端InputThread").start();
		new Thread(new OutputThread(null, 1024, new DataPackage(), socket.getOutputStream(), dataPool),"客户端OutputThread").start();
	}
}
