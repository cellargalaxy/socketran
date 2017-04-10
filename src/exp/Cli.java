package exp;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import conversation.Client;
import dataByte.Data;

public class Cli extends Client{

	

	public Cli(int byteLen, Socket socket, LinkedList<Data> datas, int waitTime) throws IOException {
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
	
	public void sendFolderOrFile(File file) {
		if (file.isFile()) {
			addData(new DataFile(file, file.getName()));
		}else {
			LinkedList<File> files=new LinkedList<File>();
			sendFolder(files, file, file);
		}
	}
	private void sendFolder(LinkedList<File> files,File root,File file) {
		if (file.isFile()) {
			String headInfo=root.getName()+file.getAbsolutePath().substring(root.getAbsolutePath().length());
			addData(new DataFile(file, headInfo));
		} else {
			File[] fs=file.listFiles();
			for(File f:fs) sendFolder(files, root, f);
		}
	}
	
	public static Cli createCli(int byteLen,String host,int port,File saveFolder,int waitTime) throws UnknownHostException, IOException {
		Socket socket=new Socket(host, port);
		LinkedList<Data> datas=new LinkedList<Data>();
		datas.add(new DataFile(saveFolder,null));
		datas.add(new DataString(null));
		return new Cli(byteLen, socket, datas,waitTime);
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		Cli cli=Cli.createCli(2048, "127.0.0.1", 1234, new File("g:/"),10);
		cli.sendFolderOrFile(new File("G:/图片"));
	}
}
