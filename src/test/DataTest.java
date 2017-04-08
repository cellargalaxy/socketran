package test;

import dataByte.Data;

public class DataTest implements Data{
	private byte[] string;
	
	
	
	public DataTest(String string) {
		super();
		if(string!=null) this.string=string.getBytes();
	}

	@Override
	public String getDataName() {
		// TODO Auto-generated method stub
		System.out.println("调用getDataName");
		return "print";
	}

	@Override
	public String getHeadInfo() {
		// TODO Auto-generated method stub
		System.out.println("调用getHeadInfo");
		return "seconHeadInfo啊";
	}

	@Override
	public long getInfoLen() {
		// TODO Auto-generated method stub
		System.out.println("调用getInfoLen");
		return string.length;
	}

	@Override
	public boolean initRead() {
		// TODO Auto-generated method stub
		System.out.println("调用initRead");
		return true;
	}

	@Override
	public int read(byte[] bs) {
		// TODO Auto-generated method stub
		System.out.println("调用read");
		if (string!=null) {
			fillingByte(bs, string);
			string=null;
			return bs.length;
		} else {
			return -1;
		}
	}

	@Override
	public void destroyRead() {
		// TODO Auto-generated method stub
		System.out.println("调用destroyRead");
	}

	@Override
	public boolean initWrite(String headInfo) {
		// TODO Auto-generated method stub
		System.out.println("调用initWrite，头信息:"+headInfo);
		return true;
	}

	@Override
	public void write(byte[] bs, int off, int len) {
		// TODO Auto-generated method stub
		System.out.println("调用write，对方说:"+new String(bs,off,len));
	}

	@Override
	public void destroyWrite() {
		// TODO Auto-generated method stub
		System.out.println("调用destroyWrite");
	}
	
	private static void fillingByte(byte[] bs1,byte[] bs2) {
		int point=0;
		for (; point < bs1.length && point<bs2.length ; point++) bs1[point]=bs2[point];
	}
	
}
