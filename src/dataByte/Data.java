package dataByte;

/**
 * @author cellargalaxy
 * 跟其他数据类型的接口，其他数据类型需装换为byte数组，由使用者实现
 */
public interface Data {
	/**
	 * @return 返回数据类型的名字
	 */
	public String getDataName();
	/**
	 * @return 返回头信息
	 */
	public String getHeadInfo();
	/**
	 * @return 返回信息byte总长度
	 */
	public long getInfoLen();
	
	/**
	 * 初始化读，例如创建流对象
	 * @return 返回初始化是否成功，若失败则会跳过
	 */
	public boolean initRead();
	/**
	 * 跟io流一样含义
	 * @param bs
	 * @param off
	 * @param len
	 * @return
	 */
	public int read(byte[] bs,int off,int len);
	/**
	 * 对读做最后一些销毁处理，例如关闭流
	 */
	public void destroyRead();
	
	/**
	 * 读初始化，例如创建流对象
	 * @param headInfo ,对方发送过来的头信息
	 * @return 初始化是否成功，若失败则会跳过
	 */
	public boolean initWrite(String headInfo);
	/**
	 * 跟io流一样含义
	 * @param bs
	 * @param off
	 * @param len
	 */
	public void write(byte[] bs,int off ,int len);
	/**
	 * 对写做最后一些销毁处理，例如关闭流
	 */
	public void destroyWrite();
	
}
