package io;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;


public class DataPackage {
	private static final String CODING="utf-8";
	private static final char SPACE=' ';
	private static final char MAIN_HEAD_START='#';
	private static final String SEPARATOR=",";
	
	private LinkedList<Data> datas;
	private Data data;
	private long fileLen;
	private ValveInputStream inputStream;
	private ValveOutputStream outputStream;
	private boolean yetSpaceHeadInfo1;
	private boolean yetSpaceHeadInfo2;
	private boolean yetMainHeadInfo;
	private boolean yetSeconHeadInfo;
	
	
	
	public DataPackage(LinkedList<Data> datas) {
		super();
		this.datas = datas;
		yetSpaceHeadInfo1=false;
		yetSpaceHeadInfo2=false;
		yetMainHeadInfo=false;
		yetSeconHeadInfo=false;
	}
	
	
	
	public void setData(Data data) {
		this.data = data;
	}
	public int read(byte[] bs,int off,int len) throws Exception {
		if (!yetSpaceHeadInfo1) {
			yetSpaceHeadInfo1=true;
			return readSpaceHeadInfo1(bs, off, len);
		}else if (!yetSpaceHeadInfo2) {
			yetSpaceHeadInfo2=true;
			return readSpaceHeadInfo2(bs, off, len);
		}else if (!yetMainHeadInfo) {
			initRead();
			yetMainHeadInfo=true;
			return readMainHeadInfo(bs, off, len);
		}else if (!yetSeconHeadInfo) {
			yetSeconHeadInfo=true;
			return readSeconHeadInfo(bs, off, len);
		}else {
			return readInfo(bs, off, len);
		}
	}
	public void closeRead() {
		yetSpaceHeadInfo1=false;
		yetSpaceHeadInfo2=false;
		yetMainHeadInfo=false;
		yetSeconHeadInfo=false;
		if(inputStream!=null) inputStream.close();
		inputStream=null;
		data=null;
	}
	private void initRead() throws Exception {
		inputStream=new ValveInputStream(data);
	}
	private int readSpaceHeadInfo1(byte[] bs,int off,int len) {
		fillingByte(bs, off, len);
		return len;
	}
	private int readSpaceHeadInfo2(byte[] bs,int off,int len) throws UnsupportedEncodingException {
		String spaceHeadInfo=MAIN_HEAD_START+"";
		byte[] spaceHeadInfoByte=spaceHeadInfo.getBytes(CODING);
		fillingByte(bs, off, len, spaceHeadInfoByte);
		return len;
	}
	private int readMainHeadInfo(byte[] bs,int off,int len) throws UnsupportedEncodingException {
		String mainHeadInfo=createRandomNum()+SEPARATOR+data.getDataName()+SEPARATOR+data.getInfoLen();
		byte[] mainHeadInfoByte=mainHeadInfo.getBytes(CODING);
		fillingByte(bs, off, len, mainHeadInfoByte);
		return len;
	}
	private int readSeconHeadInfo(byte[] bs,int off,int len) throws UnsupportedEncodingException {
		byte[] seconHeadInfoByte=data.getHeadInfo().getBytes(CODING);
		fillingByte(bs, off, len, seconHeadInfoByte);
		return len;
	}
	private int readInfo(byte[] bs,int off,int len) throws Exception {
		int getLen=inputStream.readValve(bs, off, len);
		if(getLen<0) return -1;
		else if (getLen<len) fillingByte(bs, off+getLen,len);
		return len;
	}
	
	
	
	/**
	 * 这个方法返回有点特殊
	 * @param bs
	 * @param off
	 * @param len
	 * @return -2：结束写入，-1：可以继续写入，(0,+00)：跳过这么多个比特
	 * @throws Exception
	 */
	public int wtire(byte[] bs,int off,int len) throws Exception {
//		System.out.println("收到:"+new String(bs,off,len,CODING));
		
		if (!yetSpaceHeadInfo2) {
			int point=writeSpaceHeadInfo(bs, off, len);
			if(point!=-1) yetSpaceHeadInfo2=true;
			return point;
		}else if (!yetMainHeadInfo) {
			writeMainHeadInfo(bs, off, len);
			yetMainHeadInfo=true;
			return -1;
		}else if (!yetSeconHeadInfo) {
			writeSeconHeadInfo(bs, off, len);
			yetSeconHeadInfo=true;
			return -1;
		}else {
			if(writeInfo(bs, off, len)) return -1;
			else return -2;
		}
	}
	public void closeWrite() {
		yetSpaceHeadInfo1=false;
		yetSpaceHeadInfo2=false;
		yetMainHeadInfo=false;
		yetSeconHeadInfo=false;
		if(outputStream!=null) outputStream.close();
		outputStream=null;
		data=null;
	}
	private void initWrite(long fileLen,String seconHeadInfo) throws Exception {
		outputStream=new ValveOutputStream(data, fileLen, seconHeadInfo);
	}
	private int writeSpaceHeadInfo(byte[] bs,int off,int len) throws UnsupportedEncodingException {
		String spaceHeadInfo=new String(bs, off, len,CODING);
		int startPoint=spaceHeadInfo.indexOf(MAIN_HEAD_START);
//		System.out.println(bs.length+":"+off+":"+len+":"+startPoint+":空行:"+spaceHeadInfo);
		return startPoint;
	}
	private void writeMainHeadInfo(byte[] bs,int off,int len) throws UnsupportedEncodingException {
		String mainHeadInfo=new String(bs, off, len,CODING);
//		System.out.println(bs.length+":"+off+":"+len+"主头:"+mainHeadInfo);
		String[] mainHeadInfos=mainHeadInfo.split(SEPARATOR);
		new Integer(mainHeadInfos[0].trim());
		if(!choice(mainHeadInfos[1])) System.out.println("找不到dataName");
		fileLen=new Long(mainHeadInfos[2].trim());	
	}
	private void writeSeconHeadInfo(byte[] bs,int off,int len) throws Exception {
		String seconHeadInfo=new String(bs, off, len,CODING);
//		System.out.println(bs.length+":"+off+":"+len+"副头:"+seconHeadInfo);
		initWrite(fileLen, seconHeadInfo.trim());
	}
	private boolean writeInfo(byte[] bs,int off,int len) throws Exception {
		return outputStream.writeValve(bs, off, len);
	}
	
	
	
	
	private static void fillingByte(byte[] bs,int off,int len){
		for (int i=off; i<off+len && i < bs.length ; i++) bs[i]=SPACE;
	}
	private static void fillingByte(byte[] bs1,int off,int len,byte[] bs2) {
		int point=off;
		for (; point < bs1.length && point-off<bs2.length && point<off+len; point++) bs1[point]=bs2[point-off];
		for (; point < bs1.length; point++) bs1[point]=SPACE;
	}
	
	private int createRandomNum() {
		return (int)(Math.random()*1000000000);
	}
	
	private boolean choice(String dataName) {
		for(Data d:datas){
			if(d.getDataName().equals(dataName)) {
				data=d;
				return true;
			}
		}
		return false;
	}
//	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
//		File file1=new File("g:/socketran.conf");
//		FileData fileData1=new FileData(file1, "head.txt");
//		DataPackage dataPackage1=new DataPackage(null);
//		dataPackage1.setData(fileData1);
//		
//		File file2=new File("g:/socketran0.conf");
//		FileData fileData2=new FileData(file2, "head.txt");
//		DataPackage dataPackage2=new DataPackage(null);
//		dataPackage2.setData(fileData2);
//		
//		int len=-1;
//		byte[] bs=new byte[30];
//		while ((len=dataPackage1.read(bs, 0, 30))!=-1) {
////			System.out.println("流2到:"+new String(bs,0,10));
//			
//			int point=dataPackage2.wtire(bs, 0, len);
//			if(point>0) {
//				System.out.println("条:"+point);
//				byte[] bss=new byte[point];
//				dataPackage1.read(bss, 0, bss.length);
//			}else if (point==-2) {
//				System.out.println("over");
//				break;
//			}
//		}
//		dataPackage1.closeRead();
//		dataPackage2.closeRead();
//		
//		
//	}
}
