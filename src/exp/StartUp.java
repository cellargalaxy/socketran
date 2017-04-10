package exp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StartUp {
	private static Cli cli;
	private static File confFile; 
	
	private static boolean isServer;
	private static String host;
	private static int port;
	private static int byteLen=1024;
	private static File saveFolder=new File(".");
	private static int waitTime;
	
	public static void main(String[] args) throws IOException {
		startup(new File("socketran.conf"));
		if (!isServer&&args!=null) {
			for(String filePath:args){
				cli.sendFolderOrFile(new File(filePath));
			}
		}
	}
	
	public static void startup(File conFile) throws IOException {
		StartUp.confFile=conFile;
		if(!conFile.exists()||conFile.isDirectory()){
			System.out.println("配置文件不存在:"+conFile.getAbsolutePath());
			return;
		}else if (conf()) {
			if(isServer) new Thread(new SerThread(port, saveFolder,waitTime),"服务端").start();
			else cli=Cli.createCli(byteLen, host, port, saveFolder,waitTime);
		}
	}
	
	private static boolean conf() {
		try {
			Properties properties=new Properties();
			properties.load(new FileInputStream(confFile));
			String string=null;
			
			if((string=properties.getProperty("isServer"))!=null) {
				int i;
				try { i=new Integer(string); } catch (Exception e) { System.out.println("isServer属性只能为0或1，0:Client 1:Server,请检查:"+string); return false;  }
				if(i==0) isServer=false;
				else if (i==1) isServer=true;
				else {
					System.out.println("isServer属性只能为0或1，0:Client 1:Server");
					return false;
				}
			}else {
				System.out.println("缺少isServer属性，必填");
				return false;
			}
			
			if(isServer||(string=properties.getProperty("host"))!=null){
				host=string;
			}else {
				System.out.println("缺少host属性，必填");
				return false;
			}
			
			if((string=properties.getProperty("port"))!=null){
				try { port=new Integer(string); } catch (Exception e) { System.out.println("请检查port属性是否正确:"+string); return false;  }
			}else {
				System.out.println("缺少port属性，必填");
				return false;
			}
			
			if((string=properties.getProperty("waitTime"))!=null){
				try { waitTime=new Integer(string); } catch (Exception e) { System.out.println("请检查waitTime属性是否正确:"+string); return false;  }
			}else {
				System.out.println("缺少waitTime属性，必填");
				return false;
			}
			
			if(isServer||(string=properties.getProperty("byteLen"))!=null){
				try { byteLen=new Integer(string); } catch (Exception e) { System.out.println("请检查byteLen属性是否正确:"+string);  return false; }
			}
			
			if((string=properties.getProperty("saveFolder"))!=null){
				saveFolder=new File(string);
				if(saveFolder.isFile()||(!saveFolder.exists()&&!saveFolder.mkdirs())) {
					System.out.println("请检查saveFolder属性是否正确:"+string);
					return false;
				}
			}
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
