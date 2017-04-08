package dataByte;

import java.util.LinkedList;

public class DataPackage {
	private final static byte space=32;
	
	private LinkedList<Data> datas;
	
	private String dataName;
	private Data data;
	private int randomNum;
	private int infoLen;
	
	
	
	public DataPackage(LinkedList<Data> datas) {
		super();
		this.datas = datas;
	}
	
	
	


	public void initRead() {
		data.initRead();
	}
	public int readMainHead(byte[] bs,int off ,int len) {
		String mainHead=data.getDataName()+","+createRandomNum()+","+data.getInfoLen();
		byte[] mainHeadByte=mainHead.getBytes();
		fillingByte(bs, off, len, mainHeadByte);
		return bs.length;
	}
	public int readDeputyHead(byte[] bs,int off ,int len) {
		byte[] deputyHeadByte=data.readHead().getBytes();
		fillingByte(bs, off, len, deputyHeadByte);
		return bs.length;
	}
	private void fillingByte(byte[] bs1,int off ,int len,byte[] bs2) {
		int point=off;
		for (; point<len && point < bs1.length && point<bs2.length; point++) {
			bs1[point]=bs2[point-off];
		}
		for (; point < bs1.length; point++) {
			bs1[point]=space;
		}
	}
	public int readInfo(byte[] bs,int off ,int len) {
		return data.readInfo(bs, off, len);
	}
	public void destroyRead() {
		data.destroyRead();
		data=null;
	}
	
	
	/**
	 * @param bs
	 * @param off
	 * @param len
	 * 
	 * 控制报错怎么办,没有对应的data怎么办
	 */
	public boolean writeMainHead(byte[] bs,int off,int len) {
		try {
			String mainHead=new String(bs, off, len).trim();
			String[] mainHeads=mainHead.split(",");
			dataName=mainHeads[0];
			randomNum=new Integer(mainHeads[1]);
			infoLen=new Integer(mainHeads[2]);
			for(Data d:datas){
				if (d.getDataName().equals(dataName)) {
					data=d;
					break;
				}
			}
			if(data!=null) return true;
			else return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			data=null;
			return false;
		}
	}
	public void initWrite() {
		data.initWrite();
	}
	public void writeDeputyHead(byte[] bs,int off,int len) {
		data.writeHead(new String(bs, off, len).trim());
	}
	public void writeInfo(byte[] bs,int off,int len) {
		data.writeInfo(bs, off, len);
	}
	public void destroyWrite() {
		data.destroyWrite();
		data=null;
	}
	
	private int createRandomNum() {
		return (int)(Math.random()*1000000000);
	}
	
	
	
	public int getInfoLen() {
		return infoLen;
	}





	public void setData(Data data) {
		this.data = data;
	}
	
	
}
