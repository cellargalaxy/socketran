package exp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.Data;

public class FileData implements Data{
	private File file;
	private String headInfo;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	
	
	public FileData(File file,String headInfo) {
		super();
		this.file = file;
		this.headInfo=headInfo;
	}

	@Override
	public String getDataName() {
		// TODO Auto-generated method stub
		//System.out.println("调用了getDataName");
		return "DATAFILE";
	}

	@Override
	public String getHeadInfo() {
		// TODO Auto-generated method stub
		//System.out.println("调用了getHeadInfo");
		return headInfo;
	}

	@Override
	public long getInfoLen() {
		// TODO Auto-generated method stub
		//System.out.println("调用了getInfoLen");
		return file.length();
	}

	@Override
	public void initRead() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("发送文件:"+file.getAbsolutePath());
		inputStream=new FileInputStream(file);
	}

	@Override
	public int read(byte[] bs, int off, int len) throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("发送进度:"+headInfo+":"+(int)(100.0*yetInfoLen/file.length())+"%,"+yetInfoLen+":"+file.length());
		return inputStream.read(bs, off, len);
	}

	@Override
	public void destroyRead() {
		// TODO Auto-generated method stub
		System.out.println("发送完成:"+file.getAbsolutePath());
		try {
			inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initWrite(String headInfo) throws Exception {
		// TODO Auto-generated method stub
		this.headInfo=headInfo;
		File savefile=new File(file.getAbsolutePath()+"/"+headInfo);
		System.out.println("文件保存到:"+savefile.getAbsolutePath());
		if(!savefile.getParentFile().exists()) savefile.getParentFile().mkdirs();
		outputStream=new FileOutputStream(savefile);
	}

	@Override
	public void write(byte[] bs, int off, int len) throws Exception {
		// TODO Auto-generated method stub
		outputStream.write(bs, off, len);
		outputStream.flush();
//		System.out.println("接收进度:"+headInfo+":"+yetInfoLen);
	}

	@Override
	public void destroyWrite() {
		System.out.println("接收完成:"+headInfo);
		// TODO Auto-generated method stub
		try {
			outputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) throws Exception {
//		FileData fileData1=new FileData(new File("g:/BEncode Editor.exe"), "head");
//		FileData fileData2=new FileData(new File("g:/"), "head");
//		ValveInputStream inputStream=new ValveInputStream(fileData1);
//		ValveOutputStream outputStream=new ValveOutputStream(fileData2, fileData1.getInfoLen(),"BEncode Editor0.exe");
//		
//		System.out.println(new File("g:/BEncode Editor.exe").length());
//		System.out.println(fileData1.getInfoLen());
//		
//		long l=0;
//		int len=-1;
//		byte[] bs=new byte[1203];
//		while ((len=inputStream.readValve(bs, 0, bs.length))!=-1) {
//			if(len!=-1) l+=len;
//			else System.out.println("?");
//			outputStream.writeValve(bs, 0, len);
//		}
//		inputStream.close();
//		outputStream.close();
//		
//		System.out.println(l);
//		System.out.println(new File("g:/BEncode Editor0.exe").length());
//	}
	
}
