package exp;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import dataByte.Data;

public class SerThread implements Runnable{
	private File saveFolder;
	private boolean run;
	private ServerSocket serverSocket;
	
	public static void main(String[] args) throws IOException {
		new Thread(new SerThread(1234, new File("f:/")),"服务端").start();
	}
	
	public SerThread(int port,File saveFolder) throws IOException {
		super();
		this.saveFolder=saveFolder;
		run=true;
		serverSocket=new ServerSocket(port);
	}

	private void server(Socket socket) throws IOException {
		LinkedList<Data> datas=new LinkedList<Data>();
		datas.add(new DataFile(saveFolder,null));
		datas.add(new DataString(null));
		new Ser(socket, datas);
	}
	private void stop() {
		run=false;
		try { serverSocket.close(); } catch (IOException e) { e.printStackTrace(); }
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (run) {
				server(serverSocket.accept());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			stop();
		}
	}

}
