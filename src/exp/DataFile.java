package exp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dataByte.Data;

public class DataFile implements Data{
	private File file;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	
	
	public DataFile(File file) {
		super();
		this.file = file;
	}

	@Override
	public String getDataName() {
		// TODO Auto-generated method stub
		//System.out.println("调用了getDataName");
		return "file";
	}

	@Override
	public String getHeadInfo() {
		// TODO Auto-generated method stub
		//System.out.println("调用了getHeadInfo");
		return file.getName();
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
		System.out.println("调用了initRead");
		try {
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
			return inputStream.read(bs, off, len);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public void destroyRead() {
		// TODO Auto-generated method stub
		System.out.println("调用了destroyRead");
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean initWrite(String headInfo) {
		System.out.println("调用了initWrite");
		// TODO Auto-generated method stub
		try {
			if(!file.exists()) file.mkdirs();
			File savefile=new File(file.getAbsolutePath()+"/"+headInfo);
			outputStream=new FileOutputStream(savefile);
			return true;
		} catch (FileNotFoundException e) {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void destroyWrite() {
		System.out.println("调用了destroyWrite");
		// TODO Auto-generated method stub
		try {
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
