package dataByte;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

/**
 * @author cellargalaxy
 * 对Data类进行包装，封装Data类的初始化、读写以及销毁方法
 * 对外只暴露对Data封装好的读写和销毁方法
 */
public class DataPackage {
	private final static byte SPACE=32;
	private final static String SEPARATOR=",";
	private final static String CODING="utf-8";
	
	private LinkedList<Data> datas;
	private Data data;
	private int randomNum;
	private long infoLen;
	private long infoLenCount;
	
	private boolean yetMainHeadInfo;
	private boolean yetSeconHeadInfo;
	
	
	
	
	/**
	 * OutputThread创建DataPackage对象
	 */
	public DataPackage() {
		super();
		yetMainHeadInfo=false;
		yetSeconHeadInfo=false;
	}


	/**
	 * InputThread创DataPackage建对象
	 * @param datas
	 */
	public DataPackage(LinkedList<Data> datas) {
		super();
		this.datas = datas;
		yetMainHeadInfo=false;
		yetSeconHeadInfo=false;
	}

	
	public int read(byte[] bs,int off,int len) {
		try {
			if (!yetMainHeadInfo) {
				if(!initRead(len-off)) return -1;
				yetMainHeadInfo=true;
				return createMainHeadInfo(bs,off,len);
			}else if (!yetSeconHeadInfo) {
				yetSeconHeadInfo=true;
				return createSeconHeadInfo(bs,off,len);
			}else {
				infoLenCount+=len-off;
				if(infoLenCount>infoLen) {
					len-=infoLenCount-infoLen;
					return readInfo(bs,off,len);
				}else {
					return readInfo(bs,off,len);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
	}
	public void destroyRead() {
		if(data!=null) {
			data.destroyRead();
			data=null;
		}
		yetMainHeadInfo=false;
		yetSeconHeadInfo=false;
	}
	
	private boolean initRead(int byteLen) {
		randomNum=createRandomNum();
		infoLen=data.getInfoLen();
		infoLenCount=0;
		return data.initRead();
	}
	private int createMainHeadInfo(byte[] bs,int off ,int len) throws UnsupportedEncodingException {
		String mainHeadInfo=data.getDataName()+SEPARATOR+randomNum+SEPARATOR+infoLen;
		byte[] mainHeadInfoByte=mainHeadInfo.getBytes(CODING);
		fillingByte(bs, off,len,mainHeadInfoByte);
		return bs.length;
	}
	private int createSeconHeadInfo(byte[] bs,int off,int len) throws UnsupportedEncodingException {
		byte[] seconHeadInfoByte=data.getHeadInfo().getBytes(CODING);
		fillingByte(bs, off,len ,seconHeadInfoByte);
		return bs.length;
	}
	private int readInfo(byte[] bs,int off,int len) {
		if(len<off) return -1;
		else {
			data.read(bs,off,len);
			return bs.length;
		}
	}
	
	
	
	public boolean write(byte[] bs,int off ,int len) {
	//	System.out.println("收到:"+new String(bs,off,len));
		try {
			if (!yetMainHeadInfo) {
				yetMainHeadInfo=true;
				return dealMainHeadInfo(bs, off, len);
			}else if (!yetSeconHeadInfo) {
				yetSeconHeadInfo=true;
				return dealSeconHeadInfo(bs, off, len);
			}else {
				infoLenCount+=len-off;
				if (infoLenCount>=infoLen) {
					len-=infoLenCount-infoLen;
					writeInfo(bs, off, len);
					return false;
				} else {
					writeInfo(bs, off, len);
					return true;
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	public void destroyWrite() {
		if(data!=null) {
			data.destroyWrite();
			data=null;
		}
		yetMainHeadInfo=false;
		yetSeconHeadInfo=false;
	}
	
	private boolean initWrite(String seconHeadInfo) {
		infoLenCount=0;
		return data.initWrite(seconHeadInfo);
	}
	private boolean dealMainHeadInfo(byte[] bs,int off ,int len) throws UnsupportedEncodingException {
		String mainHeadInfo=new String(bs, off, len,CODING);
		String[] mainHeadInfos=mainHeadInfo.split(SEPARATOR);
		if(!choiceData(mainHeadInfos[0])) return false;
		randomNum=new Integer(mainHeadInfos[1]);
		infoLen=new Long(mainHeadInfos[2].trim());
		return true;
	}
	private boolean dealSeconHeadInfo(byte[] bs,int off ,int len) throws UnsupportedEncodingException {
		String seconHeadInfo=new String(bs, off, len,CODING);
		return initWrite(seconHeadInfo.trim());
	}
	private void writeInfo(byte[] bs,int off ,int len) {
		data.write(bs, off, len);
	}
	
	
	
	
	private boolean choiceData(String dataName) {
		for(Data d:datas){
			if (d.getDataName().equals(dataName)) {
				data=d;
				return true;
			}
		}
		return false;
	}
	private static void fillingByte(byte[] bs1,int off,int len,byte[] bs2) {
		int point=off;
		for (; point < bs1.length && point-off<bs2.length && point<len; point++) bs1[point]=bs2[point-off];
		for (; point < bs1.length; point++) bs1[point]=SPACE;
	}
	private static int createRandomNum() {
		return (int)(Math.random()*100000000);
	}


	public void setData(Data data) {
		this.data = data;
	}


	public long getInfoLen() {
		return infoLen;
	}
	
	
}
