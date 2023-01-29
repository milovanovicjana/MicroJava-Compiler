package rs.ac.bg.etf.pp1;

public class Cond_Factor {
	
	private int address;
	private int operator;
	
	public Cond_Factor(int address, int operator) {
		super();
		this.address = address;
		this.operator = operator;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public int getOperator() {
		return operator;
	}
	public void setOperator(int operator) {
		this.operator = operator;
	}

}
