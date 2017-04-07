package dataByte;

import java.util.LinkedList;

public class DataPool {
	private LinkedList<Data> dataPool;
	
	public DataPool() {
		// TODO Auto-generated constructor stub
		dataPool=new LinkedList<Data>();
	}
	
	public void addData(Data data) {
		dataPool.add(data);
	}
	
	public Data pollData() {
		return dataPool.poll();
	}
	
}
