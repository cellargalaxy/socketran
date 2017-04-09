package exp;

import java.io.File;
import java.io.UnsupportedEncodingException;

import dataByte.Data;

public class DataString implements Data{
	private final static String CONDING="utf-8";
	private byte[] stringByte;

	
	
	public DataString(String string) throws UnsupportedEncodingException {
		super();
		if(string!=null) stringByte=string.getBytes(CONDING);
	}

	@Override
	public String getDataName() {
		// TODO Auto-generated method stub
		System.out.println("调用了getDataName");
		return "string";
	}

	@Override
	public String getHeadInfo() {
		// TODO Auto-generated method stub
		System.out.println("调用了getHeadInfo");
		return "string的头信息";
	}

	@Override
	public long getInfoLen() {
		// TODO Auto-generated method stub
		System.out.println("调用了getInfoLen");
		return stringByte.length;
	}

	@Override
	public boolean initRead() {
		// TODO Auto-generated method stub
		System.out.println("调用了initRead");
		return true;
	}

	@Override
	public int read(byte[] bs, int off, int len) {
		// TODO Auto-generated method stub
		System.out.println("调用了read");
		fillingByte(bs, off, len, stringByte);
		return bs.length;
	}

	@Override
	public void destroyRead() {
		// TODO Auto-generated method stub
		System.out.println("调用了destroyRead");
	}

	@Override
	public boolean initWrite(String headInfo) {
		// TODO Auto-generated method stub
		System.out.println("调用了:initWrite;头信息:"+headInfo);
		return true;
	}

	@Override
	public void write(byte[] bs, int off, int len) {
		// TODO Auto-generated method stub
		System.out.println("调用了write");
		try {
			System.out.println("对方发来了:"+new String(bs,off,len,CONDING));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void destroyWrite() {
		// TODO Auto-generated method stub
		System.out.println("调用了destroyWrite");
		Ser.ser.addData(new DataFile(new File("e:/psiphon3.exe")));
	}
	private static void fillingByte(byte[] bs1,int off,int len,byte[] bs2) {
		int point=off;
		for (; point < bs1.length && point-off<bs2.length && point<len; point++) bs1[point]=bs2[point-off];
	}
}
