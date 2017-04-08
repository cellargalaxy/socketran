package dataByte;

public interface Data {
	public String getDataName();
	public int getHeadLen();
	public long getInfoLen();
	
	public void initRead();
	/**
	 * @return 头信息的String，一句过算了，长度超过byteLen的将丢失
	 */
	public String readHead();
	public int readInfo(byte[] bs,int off ,int len);
	public void destroyRead();
	
	public void initWrite();
	public void writeHead(String head);
	public void writeInfo(byte[] bs,int off ,int len);
	public void destroyWrite();
	
}
