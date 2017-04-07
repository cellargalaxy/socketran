package test;

public class M {
	private int m=0;
	
	public synchronized void add(int add,String name) {
		System.out.print(name+":"+m+"+"+add+"=");
		m+=add;
		System.out.println(m);
	}
}
