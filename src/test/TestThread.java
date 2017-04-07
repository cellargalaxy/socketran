package test;

public class TestThread implements Runnable{
	private M m;
	private int add;
	private String name;
	
	
	
	public TestThread(M m, int add, String name) {
		super();
		this.m = m;
		this.add = add;
		this.name = name;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			m.add(add, name);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		M m=new M();
		new Thread(new TestThread(m, 10, "A")).start();
		new Thread(new TestThread(m, 50, "B")).start();
	}
}
