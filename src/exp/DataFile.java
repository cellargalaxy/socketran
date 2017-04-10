package exp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import dataByte.Data;

public class DataFile implements Data{
//	private long yetInfoLen;
	private File file;
	private String headInfo;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	
	
	public DataFile(File file,String headInfo) {
		super();
		this.file = file;
		this.headInfo=headInfo;
//		yetInfoLen=0;
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
	public boolean initRead() {
		// TODO Auto-generated method stub
		//System.out.println("调用了initRead");
		try {
			System.out.println("发送文件:"+file.getAbsolutePath());
			inputStream=new FileInputStream(file);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int read(byte[] bs, int off, int len) {
		// TODO Auto-generated method stub
		//System.out.println("调用了read");
		try {
//			yetInfoLen+=len-off;
//			System.out.println("发送进度:"+headInfo+":"+(int)(100.0*yetInfoLen/file.length())+"%,"+yetInfoLen+":"+file.length());
			return inputStream.read(bs, off, len);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public void destroyRead() {
		// TODO Auto-generated method stub
		//System.out.println("调用了destroyRead");
		try {
			inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean initWrite(String headInfo) {
		//System.out.println("调用了initWrite");
		// TODO Auto-generated method stub
		try {
			this.headInfo=headInfo;
			File savefile=new File(file.getAbsolutePath()+"/"+headInfo);
			System.out.println("文件保存到:"+savefile.getAbsolutePath());
			if(!savefile.getParentFile().exists()&&!savefile.getParentFile().mkdirs()) return false;
			outputStream=new FileOutputStream(savefile);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void write(byte[] bs, int off, int len) {
		// TODO Auto-generated method stub
		//System.out.println("调用了write");
		try {
			outputStream.write(bs, off, len);
			outputStream.flush();
//			yetInfoLen+=len-off;
//			System.out.println("接收进度:"+headInfo+":"+yetInfoLen);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void destroyWrite() {
		//System.out.println("调用了destroyWrite");
		// TODO Auto-generated method stub
		try {
			outputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
