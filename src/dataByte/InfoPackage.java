package dataByte;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class InfoPackage {
	private long infoLen;
	private long infoLenCount;
	private Data data;
	
	private int read(byte[] bs,int off,int len) {
		infoLenCount+=len;
		
		
		return data.read(bs, off, len);
	}
	
	public static void main(String[] args) throws IOException {
		InputStream inputStream=new FileInputStream("g:/WiKi.java");
		byte[] bs=new byte[10];
		for(byte b:bs){
			System.out.print(b+" ");
		}System.out.println();
		int len=inputStream.read(bs, 2, 8);
		System.out.println("len:"+len);
		for(byte b:bs){
			System.out.print(b+" ");
		}System.out.println();
	}
}
