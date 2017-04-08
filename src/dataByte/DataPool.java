package dataByte;

import java.util.LinkedList;

public class DataPool {
	LinkedList<Data> dataPool;

	public DataPool() {
		super();
		dataPool=new LinkedList<Data>();
	}
	
	public void addData(Data data) {
		dataPool.add(data);
	}
	public Data pollData() {
		return dataPool.poll();
	}
	
}
