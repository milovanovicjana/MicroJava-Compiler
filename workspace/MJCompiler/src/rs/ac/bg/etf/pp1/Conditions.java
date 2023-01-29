package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;

public class Conditions {
	
	private boolean isLoop;
	
	//WHILE
	private int whileConditionAddress; //adresa condition-a while petlje
	private int whileStartAddress;//adresa prve naredbe while petlje
	private int whileEndAddress;//adresa naredbe nakon while petlje
	
	//IF
	private int ifStartAddress;//adresa prve naredbe u then grani
	private int elseStartAddress;//adresa prve naredbe u else grani
	private int stmtEndAdr;//adresa prve naredbe posle if/else ili samo if dela ako nema else

	//CONDITION TERM AND FACTOR
	private List<Cond_Factor>condFactors;
	private List<Cond_Term>condTerms;
	private One_Condition condition;
	
	//BREAK AND CONTINUE
	private List<Integer> breakAddress; //lista u kojoj se cuvaju adrese break  instrukcija kojima treba popuniti skokove
	private List<Integer> continueAddress;
	
	//FOREACH
	private int foreachStart;
	private int fixAddress;
	Obj indexObj;
	
	Logger log = Logger.getLogger(getClass());

	/*CONSTRUCTORS*/
	public Conditions() {//konstruktor za uslov while petlje i If elsa
		this.condFactors=new ArrayList<>();
		this.condTerms=new ArrayList<>();
		this.breakAddress=new ArrayList<>();
		this.continueAddress=new ArrayList<>();
		elseStartAddress=-1;
	}
	public Conditions(int id) {
		//konstruktor za foreach
		indexObj=new Obj(Obj.Var,"index"+id,MySimbolTable.intType);
		indexObj.setAdr(55-id); //max 55 promenljivih moguce

		this.breakAddress=new ArrayList<>();
		this.continueAddress=new ArrayList<>();
		this.isLoop=true;
		
	}
	
	
	public void isLoop(boolean flag) {
		this.isLoop=flag;
	}
	public boolean isLoop() {
		return this.isLoop;
	}
	
	/*CONDITION TERM AND FACTOR*/
	public void addCondFactorToCondition(Cond_Factor condFactor) {
		this.condFactors.add(condFactor);
	}
	public void addCondTermToCondition() {//znaci da smo pokupili sve faktore prethodne
		condTerms.add(new Cond_Term(condFactors));
		condFactors=new ArrayList<>();
	}
	
	/*IF ELSE*/
	public void endCondition() {
		condition=new One_Condition(condTerms);
		condTerms=new ArrayList<>();
	}
	public void setIfStartAddress() {
		this.ifStartAddress = Code.pc;//adresa prve naredbe u then grani
	}
	public void setElseStartAddress() {
		Code.putJump(0);//na kraju if-a bezuslovan skok na deo posle else ili na deo nakon if-a ako nema else-a, posle cemo popuniti ovu adresa
		this.elseStartAddress = Code.pc;
	}
	public void setStmtEndAdr() {
		this.stmtEndAdr = Code.pc;
	}
	
	/*WHILE*/
	public void setWhileStartAddress() {
		this.whileStartAddress = Code.pc;//adresa prve naredbe while petlje
	}
	public void setWhileConditionAddress() {
		this.whileConditionAddress=Code.pc;
	}
	public int getWhileConditionAddress() {
		return this.whileConditionAddress;
	}
	
	/*BREAK*/
	public void addBreakAddress() {
		breakAddress.add(Code.pc-2);//cuvamo adresu gde treba upisati  adresu skoka
	}
	
	/*CONTINUE*/
	public void addContinueAddress() {
		continueAddress.add(Code.pc-2);
	}
	
	/*IF AND WHILE OFFSETS*/
	public void fixConditionOffset() {
		int pc;
		
		if(isLoop) { //samo za petlje
			for(int i=0;i<breakAddress.size();i++) {//popunjavanje adresa za skok u break instrukciji
				pc=Code.pc;
				Code.pc=stmtEndAdr;
				Code.fixup(breakAddress.get(i));
				Code.pc=pc;
			}
			for(int i=0;i<continueAddress.size();i++) {//popunjavanje adresa za skok u break instrukciji
				pc=Code.pc;
				Code.pc=whileConditionAddress;
				Code.fixup(continueAddress.get(i));
				Code.pc=pc;
			}	
		}
		
		else {
			if(elseStartAddress!=-1) { //postoji i else statement u ifu
				pc=Code.pc;
				Code.pc=stmtEndAdr;
				Code.fixup(elseStartAddress-2);//adresa dugacka 2B, jmp adresa
				Code.pc=pc;
			}
		}
	

        List<Cond_Term>condTerms=condition.getCondTerms(); //svi term-ovi
		
		int nextAddress;
		Cond_Term condTerm;
		Cond_Factor condFactor;
		
		for(int i=0;i<condTerms.size();i++) { //popunjavamo sve termove sem poslednjeg
			
			condTerm=condTerms.get(i);
			nextAddress=condTerm.getEndAddr();
			
			if(i==condTerms.size()-1) {//ako je u pitanu poslednji term i vidimo da ni on nije tacan skacemo na kraj ifa
					nextAddress=stmtEndAdr;
					if(!isLoop) {//ako je u pitanju IF
						if(elseStartAddress==-1) {//nema elsa
							nextAddress=stmtEndAdr;
						}
						else nextAddress=elseStartAddress;
					}
			}
			List<Cond_Factor> condFactors=condTerm.getCondFactors();
			
			for(int j=0;j<condFactors.size();j++) { //popunjavamo sve factore na isti nacin sem poslednjeg
				
				condFactor=condFactors.get(j);
				pc=Code.pc;
				if(i!=condTerms.size()-1 && j==condFactors.size()-1) {//nije poslednji term jeste poslednji faktor
					//znaci da su svi prethodni faktori bili tacni i ako je tacan i ovaj prekidamo ispitivajne i skacemo na then
					Code.pc=condFactor.getAddress(); //u bafer zameni instrukciju sa instrukcijom koja ima inverznu operaciju
					Code.put(Code.jcc+Code.inverse[condFactor.getOperator()]);
					if(isLoop)Code.pc=whileStartAddress;
					else Code.pc=ifStartAddress;//prva instrukcija u then grani
				}
				else Code.pc=nextAddress;
				Code.fixup(condFactor.getAddress()+1);
				Code.pc=pc;
			}
		}
		
	}

	/*FOREACH*/
	public void setForeachStart(int addr){
		this.foreachStart=addr;
	}
	public void setfixAddress(int addr){
		this.fixAddress=addr;
	}
	public int getForeachStart() {
		return this.foreachStart;
	}
	public int getfixAddress() {
		return this.fixAddress;
	}
	public void fixForeachOffsets() {
		Code.fixup(fixAddress); //postavljanje adrese za izlazak iz petlje
		int pc;
		for(int i=0;i<breakAddress.size();i++) {//popunjavanje adresa za skok u break instrukciji
			Code.fixup(breakAddress.get(i));
		}
		for(int i=0;i<continueAddress.size();i++) {//popunjavanje adresa za skok u continue instrukciji
			pc=Code.pc;
			Code.pc=foreachStart;
			Code.fixup(continueAddress.get(i));
			Code.pc=pc;
		}	
	}
}
