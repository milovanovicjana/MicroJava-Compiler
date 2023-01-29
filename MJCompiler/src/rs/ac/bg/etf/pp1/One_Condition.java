package rs.ac.bg.etf.pp1;

import java.util.List;

public class One_Condition {

	private List<Cond_Term>condTerms;

	public One_Condition(List<Cond_Term> condTerms) {
		super();
		this.condTerms = condTerms;
	}

	public List<Cond_Term> getCondTerms() {
		return condTerms;
	}

	public void setCondTerms(List<Cond_Term> condTerms) {
		this.condTerms = condTerms;
	}

}
