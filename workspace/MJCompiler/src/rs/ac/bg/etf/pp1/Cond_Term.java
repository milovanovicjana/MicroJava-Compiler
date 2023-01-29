package rs.ac.bg.etf.pp1;

import java.util.List;

public class Cond_Term {
	
	private List<Cond_Factor>condFactors;

	public Cond_Term(List<Cond_Factor> condFactors) {
		super();
		this.condFactors = condFactors;
	}

	public List<Cond_Factor> getCondFactors() {
		return condFactors;
	}

	public void setCondFactors(List<Cond_Factor> condFactors) {
		this.condFactors = condFactors;
	}
	
	public int getEndAddr() {
		return condFactors.get(condFactors.size() - 1).getAddress() + 3;//adresa narednog condTerma	 
	}


}
