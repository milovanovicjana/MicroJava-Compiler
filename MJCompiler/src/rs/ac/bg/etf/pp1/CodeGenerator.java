package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.AddopTermList_add;
import rs.ac.bg.etf.pp1.ast.Addop_minus;
import rs.ac.bg.etf.pp1.ast.Addop_plus;
import rs.ac.bg.etf.pp1.ast.ConditionFact_eq;
import rs.ac.bg.etf.pp1.ast.ConditionFact_expr;
import rs.ac.bg.etf.pp1.ast.ConditionFact_gte;
import rs.ac.bg.etf.pp1.ast.ConditionFact_lt;
import rs.ac.bg.etf.pp1.ast.ConditionFact_lte;
import rs.ac.bg.etf.pp1.ast.ConditionFact_neq;
import rs.ac.bg.etf.pp1.ast.ConditionFact_qt;
import rs.ac.bg.etf.pp1.ast.ConditionTermList_or;
import rs.ac.bg.etf.pp1.ast.ConditionTermList_term;
import rs.ac.bg.etf.pp1.ast.Constant_b;
import rs.ac.bg.etf.pp1.ast.Constant_c;
import rs.ac.bg.etf.pp1.ast.Constant_n;
import rs.ac.bg.etf.pp1.ast.Desig_d;
import rs.ac.bg.etf.pp1.ast.Desig_e;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DesignatorForeach;
import rs.ac.bg.etf.pp1.ast.DesignatorIndex_index;
import rs.ac.bg.etf.pp1.ast.DesignatorName;
import rs.ac.bg.etf.pp1.ast.DesignatorStatementAssignop_assignop;
import rs.ac.bg.etf.pp1.ast.DesignatorStatement_actPars;
import rs.ac.bg.etf.pp1.ast.DesignatorStatement_desig_array;
import rs.ac.bg.etf.pp1.ast.DesignatorStatement_minusminus;
import rs.ac.bg.etf.pp1.ast.DesignatorStatement_plusplus;
import rs.ac.bg.etf.pp1.ast.ElseStart;
import rs.ac.bg.etf.pp1.ast.EndForeach;
import rs.ac.bg.etf.pp1.ast.FactorSub_designator1;
import rs.ac.bg.etf.pp1.ast.FactorSub_designator2;
import rs.ac.bg.etf.pp1.ast.FactorSub_new;
import rs.ac.bg.etf.pp1.ast.Factor_minus;
import rs.ac.bg.etf.pp1.ast.ForeachIdentName;
import rs.ac.bg.etf.pp1.ast.IfStart;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodRetAndName_type;
import rs.ac.bg.etf.pp1.ast.MethodRetAndName_void;
import rs.ac.bg.etf.pp1.ast.MulopFactorList_list;
import rs.ac.bg.etf.pp1.ast.Mulop_div;
import rs.ac.bg.etf.pp1.ast.Mulop_mul;
import rs.ac.bg.etf.pp1.ast.Mulop_percent;
import rs.ac.bg.etf.pp1.ast.PrintParameters_withNumber;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.ac.bg.etf.pp1.ast.Statement_break;
import rs.ac.bg.etf.pp1.ast.Statement_continue;
import rs.ac.bg.etf.pp1.ast.Statement_foreach;
import rs.ac.bg.etf.pp1.ast.Statement_if;
import rs.ac.bg.etf.pp1.ast.Statement_print;
import rs.ac.bg.etf.pp1.ast.Statement_read;
import rs.ac.bg.etf.pp1.ast.Statement_return_expr;
import rs.ac.bg.etf.pp1.ast.Statement_return_noexpr;
import rs.ac.bg.etf.pp1.ast.Statement_while;
import rs.ac.bg.etf.pp1.ast.ThenStart;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.WhileCondition;
import rs.ac.bg.etf.pp1.ast.WhileEnd;
import rs.ac.bg.etf.pp1.ast.WhileLoopStart;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(getClass());

	private int mainPc;
	private boolean printWidthSpecified=false;
	
	private final Map<String, Integer> functionOffsets = new HashMap<>(); //za svaku metodu pamtimo ime i njen ofset
	private final Map<Class, Integer> operationCode = new HashMap<>(); 
	private final Map<Class, Integer> relOperationCode = new HashMap<>(); 

	private void insertOperationCodes() {
		//mulOp
		operationCode.put(Mulop_mul.class, Code.mul);
		operationCode.put(Mulop_div.class, Code.div);
		operationCode.put(Mulop_percent.class, Code.rem);
		//addOp
		operationCode.put(Addop_plus.class, Code.add);
		operationCode.put(Addop_minus.class, Code.sub);
		
	}
	private void insertRelOperationCodes() {
		
		relOperationCode.put(ConditionFact_expr.class, Code.gt);
		relOperationCode.put(ConditionFact_eq.class, Code.eq);
		relOperationCode.put(ConditionFact_neq.class, Code.ne);
		relOperationCode.put(ConditionFact_qt.class, Code.gt);
		relOperationCode.put(ConditionFact_gte.class, Code.ge);
		relOperationCode.put(ConditionFact_lt.class, Code.lt);
		relOperationCode.put(ConditionFact_lte.class, Code.le);
		
	}
	
	public int getMainPc(){
		return mainPc;
	}
	
	private void defineOrdFunction() {
	
		 Obj ordObj = MySimbolTable.find("ord");
	     ordObj.setAdr(Code.pc);
	     
	     functionOffsets.put("ord", Code.pc);

	     Code.put(Code.enter);
	     Code.put(1);
	     Code.put(1);

	     Code.put(Code.load_n + 0);		
	     
	     Code.put(Code.exit);
	     Code.put(Code.return_);
	}
	private void defineChrFunction() {
		 Obj chrObj = MySimbolTable.find("chr");
	     chrObj.setAdr(Code.pc);
	     
	     functionOffsets.put("chr", Code.pc);

	     Code.put(Code.enter);
	     Code.put(1);
	     Code.put(1);

	     Code.put(Code.load_n + 0);		
	     
	     Code.put(Code.exit);
	     Code.put(Code.return_);
	}
	private void defineLenFunction() {//poziva se sa len(arr); gde je arr niz
		 Obj chrObj = MySimbolTable.find("len");
	     chrObj.setAdr(Code.pc);
	     
	     functionOffsets.put("len", Code.pc);

	     /* Entry */
	     Code.put(Code.enter);
	     Code.put(1);
	     Code.put(1);
	     
	     /* Body */
	     Code.put(Code.load_n + 0);		// insert adrese arr u buffer!, to je parametar koji je prosledjen fji!
	     Code.put(Code.arraylength);    //zvanje fje arrayLenht koja zahteva adresu niza a ostavice na expr steku duzinu niza
	     
	     /* Exit */
	     Code.put(Code.exit);
	     Code.put(Code.return_);
	}
	
	private void defineLenOrdCharFunctions() {
		defineChrFunction();
		defineOrdFunction();
		defineLenFunction();
	}

	/*PROGRAM NAME*/
	public void visit(ProgName progName) {
		defineLenOrdCharFunctions(); //unapred predefinisane fje
		insertOperationCodes();
		insertRelOperationCodes();
	}
	
	/*CONST*/
	public void visit(Constant_n numConstant) {
		Code.loadConst(numConstant.getVal());//stavljamo broj na stek const broj
	}
	public void visit(Constant_c charConstant) {
		Code.loadConst((int)charConstant.getVal().charValue());//stavljamo broj na stek const broj
	}
	public void visit(Constant_b boolConstant) {
		Code.loadConst(boolConstant.getVal());//stavljamo broj na stek const broj
	}
	
	/*METHOD*/
	public void visit(MethodRetAndName_type methTypeAndName) {
		
		if(methTypeAndName.getMethodName().equals("main")) {
			this.mainPc=Code.pc;
		}
		
		methTypeAndName.obj.setAdr(Code.pc);
		functionOffsets.put(methTypeAndName.getMethodName(), Code.pc);
		
		int formArgCnt=methTypeAndName.obj.getLevel();
		int formAndLocalArgCnt=methTypeAndName.obj.getLocalSymbols().size();
		
//		log.info(formArgCnt);
//		log.info(formAndLocalArgCnt);
		
		Code.put(Code.enter);
		Code.put(formArgCnt);
		Code.put(formAndLocalArgCnt);
		
	}
	public void visit(MethodRetAndName_void methTypeAndName) {
		
		if(methTypeAndName.getMethodName().equals("main")) {
			this.mainPc=Code.pc;
		
		}
		functionOffsets.put(methTypeAndName.getMethodName(), Code.pc);
		methTypeAndName.obj.setAdr(Code.pc);
		
		int formArgCnt=methTypeAndName.obj.getLevel();
		int formAndLocalArgCnt=methTypeAndName.obj.getLocalSymbols().size();
		
//		log.info(formArgCnt);
//		log.info(formAndLocalArgCnt);
		
		Code.put(Code.enter);
		Code.put(formArgCnt);
		Code.put(formAndLocalArgCnt);
		
	}
	public void visit(MethodDecl methodDecl) {
	
		
		if(methodDecl.getMethodRetAndName().obj.getType()!=MySimbolTable.noType ) { //ako nije void metoda 
			Code.put(Code.trap);
			Code.put(0);
			
		}
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		
	}
	
	/*RETURN*/
	public void visit(Statement_return_noexpr returnNoExp) {
		Code.put(Code.exit);
		Code.put(Code.return_);
		
	}
	public void visit(Statement_return_expr returnWithExp) {
		Code.put(Code.exit);
		Code.put(Code.return_);
		
	}
	
	
	/*PRINT, PRINT WITH NUM*/
	public void visit(Statement_print print_statement) {
		if(printWidthSpecified) {
			printWidthSpecified=false;//vec stavljeno width
		}
		else {
			if(print_statement.getPrintParameters().struct == Tab.charType){
				Code.loadConst(1);
			}else{
				Code.loadConst(5);
			}
		}
		
		if(print_statement.getPrintParameters().struct == Tab.charType){
			Code.put(Code.bprint);
		}else{
			Code.put(Code.print);
		}	
	}
	public void visit(PrintParameters_withNumber printWithNum) {
		int width=printWithNum.getN2(); //sirina za print
		Code.loadConst(width);
		printWidthSpecified=true;
	}
	
	/*READ - cita sa standardnog ulaza*/
	public void visit(Statement_read readStmt) {
		if(readStmt.getDesignator().obj.getType()!=MySimbolTable.charType) {//citamo rec
			Code.put(Code.read);
			Code.store(readStmt.getDesignator().obj);
		}
		else {//cita bajt
			Code.put(Code.bread);
			Code.store(readStmt.getDesignator().obj);

		}
		
	}
	
	/*FACTOR - new, callFun, mul, add, minus*/
	public void visit(FactorSub_new FactorSub_new) {
		Code.put(Code.newarray);
		if(FactorSub_new.getType().struct==MySimbolTable.charType) {
			Code.put(0);
		}
		else Code.put(1);
	}
	public void visit(FactorSub_designator2 funCall) {
	
		int offset = functionOffsets.get(funCall.getFunCallName().getDesignator().getDesignatorName().getDesignatorName())-Code.pc;
		//int offset1=factor.getFunCallName().getDesignator().obj.getAdr()-Code.pc;
		// Generate instruction
		Code.put(Code.call);
		Code.put2(offset);
		//sigurno ce neko pokupiti vrednost sa steka
	}
	public void visit(MulopFactorList_list mulExpr) {
		int operation=operationCode.get(mulExpr.getMulop().getClass());
		Code.put(operation);
	}
	public void visit(AddopTermList_add addExpr) {
		int operation=operationCode.get(addExpr.getAddop().getClass());
		Code.put(operation);
	}
	public void visit(Factor_minus unaryMinus) {
		Code.put(Code.neg);
	}
	
	/*DESIGNATOR*/
	private Stack<Obj> designatorObjStack = new Stack<>();//mora stek zbog ugnjezdavanaja ako npr stoji niz[a]
	
	public void visit(DesignatorName designatorName) {
		designatorObjStack.push(designatorName.obj);
	}
	public void visit(DesignatorIndex_index desigIndex) { //ako je u pitanju element niza
		
		//postavljen vec exp pre nego sto udjemo ovde
		Code.load(designatorObjStack.peek());//mi postavimo niz
		
		//zamena mesta index-a i niza
		
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		
		//koristimo elem da bi se pozvale fje aload i astore iz Code fajla
		Obj elem = designatorObjStack.pop();
		elem = new Obj(Obj.Elem, elem.getName(), elem.getType().getElemType());
		
		designatorObjStack.push(elem);
		
	}
	public void visit(Designator designator) {
		if(designator.getParent().getClass()==FactorSub_designator1.class) {
			Code.load(designatorObjStack.peek());
		}
		designator.obj=designatorObjStack.pop();
	}
	
	/*DESIGNATOR STATEMENT -  funCall, assignOp, inc, dec*/
	public void visit(DesignatorStatement_actPars funCall) {
		int offset = functionOffsets.get(funCall.getFunCallName().getDesignator().getDesignatorName().getDesignatorName())-Code.pc;
		//int offset1=factor.getFunCallName().getDesignator().obj.getAdr()-Code.pc;
		// Generate instruction
		Code.put(Code.call);
		Code.put2(offset);
		//skidamo sa steka ako fja nije void
		if(funCall.getFunCallName().getDesignator().obj.getType()!=MySimbolTable.noType) {
			Code.put(Code.pop);
		}
		
	}
	public void visit(DesignatorStatementAssignop_assignop desigAsssing) {
		Code.store(desigAsssing.getDesignator().obj);
		
	}
	public void visit(DesignatorStatement_plusplus incStmt) {
		if(incStmt.getDesignator().obj.getKind()==Obj.Elem) {
			Code.put(Code.dup2); //ako je element niza na expr steku dodamo niz,index(niz,index,niz,index)
		}
		Code.load(incStmt.getDesignator().obj);//niz,index,value
		Code.loadConst(1);//niz,index,value,1
		Code.put(Code.add);////niz,index,value+1
		Code.store(incStmt.getDesignator().obj);//niz[index]=niz[index]+1
	}
	public void visit(DesignatorStatement_minusminus decStmt) {
		if(decStmt.getDesignator().obj.getKind()==Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(decStmt.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(decStmt.getDesignator().obj);
	}
	
	private List<Obj> desigObjList = new ArrayList<>();
	
	public void visit(DesignatorStatement_desig_array arrayStmt) {
		
		//dodati proveru duzine niza!!
	
		Obj elem = arrayStmt.getDesignator().obj;
		elem = new Obj(Obj.Elem, elem.getName(), elem.getType().getElemType());
		int index=desigObjList.size()-1;
	
		while(desigObjList.size()>0) {
			if(desigObjList.get(index)!=null) {
				Code.load(arrayStmt.getDesignator().obj);//adresa niza
				Code.loadConst(index); //index elementa koji se cita
				Code.load(elem); //ucitavanje vrednosti elementa niza na stek
				Code.store(desigObjList.get(index)); //storovanje dohvacene vrednosti
				
			}
			desigObjList.remove(index);
			index--;
		}
	}
	public void visit(Desig_d designator) {
		desigObjList.add(designator.getDesignator().obj);
	}
	public void visit(Desig_e epsilon) {
		desigObjList.add(null);
	}

	private Stack<Conditions>conditionsStack=new Stack<>(); //uslovi mogu biti ugnjezdeni ako se npr nadju while u while-u ili if u ifu itd

	/*WHILE*/
	public void visit(WhileLoopStart whileLoopStart) {
		Conditions newCondition=new Conditions();
		newCondition.isLoop(true);
		conditionsStack.push(newCondition);
		conditionsStack.peek().setWhileConditionAddress();
	}
	public void visit(WhileCondition whileCond) {
		conditionsStack.peek().setWhileStartAddress();
	}
	public void visit(WhileEnd WhileEnd) {
		Code.putJump(conditionsStack.peek().getWhileConditionAddress());
	}
	public void visit(Statement_while whileLoop) { 
		Conditions condition=conditionsStack.pop(); //skidamo sa steka uslov
		
		condition.setStmtEndAdr();
		condition.endCondition();
		condition.fixConditionOffset();
		
	}
	
	/*BREAK*/
	public void visit(Statement_break breakStmt) {	
		Code.putJump(0); //skacemo na prvu instrukciju nakon petlje i tu adresu cemo kasnije popuniti
		//posto se u while petlji mogu naci if condition ... onda je ppotrebno naci okruzujuci while i u njegovu listu break-ova dodati break
		
		@SuppressWarnings("unchecked")
		Stack<Conditions>findLoopStack=(Stack<Conditions>) conditionsStack.clone(); 
	
		while(!findLoopStack.isEmpty()) {
			Conditions condition=findLoopStack.pop();
			if(condition.isLoop()) {
				condition.addBreakAddress();
				break;
			}
		}
		findLoopStack.clear();
	}
	
	/*CONTINUE*/
	public void visit(Statement_continue continueStmt) {
		Code.putJump(0);
		@SuppressWarnings("unchecked")
		Stack<Conditions>findLoopStack=(Stack<Conditions>) conditionsStack.clone(); 
		
		while(!findLoopStack.isEmpty()) {
			Conditions condition=findLoopStack.pop();
			if(condition.isLoop()) {
				condition.addContinueAddress();
				break;
			}
		}
		findLoopStack.clear();
		
	}
	
	/*IF ELSE*/
	public void visit(IfStart ifStart) {
		Conditions newCondition=new Conditions();
		newCondition.isLoop(false);
		conditionsStack.push(newCondition); //novi condition za svaki novi if
	}
	public void visit(ThenStart thenStart) {
		conditionsStack.peek().setIfStartAddress();
	}
	public void visit(ElseStart elseStart) {
		conditionsStack.peek().setElseStartAddress();
	}
	public void visit(Statement_if statement_if) {
		Conditions condition=conditionsStack.pop();
		
		condition.setStmtEndAdr();
		condition.endCondition();
		condition.fixConditionOffset();
	}
	
	/*FOREACH*/
	int index=0;
	
	public void visit(DesignatorForeach arrayName) {
	
		conditionsStack.push(new Conditions(index));
		index++;
		
		Code.loadConst(0);//za svaku novu petlju index krece od 0
		Code.store(conditionsStack.peek().indexObj);
		
		conditionsStack.peek().setForeachStart(Code.pc);//pamtimo adresu gde treba skociti kad se dodje do kraja petlje
		
		//provera da li smo stigli do kraja, poredimo index i duzinu niza
		Code.load(arrayName.getDesignator().obj);
		Code.put(Code.arraylength);
		Code.load(conditionsStack.peek().indexObj);
		Code.putFalseJump(Code.ne, 0);
		
		//ovu adresu treba popuniti kada se dodje do kraja for petlje
		conditionsStack.peek().setfixAddress(Code.pc-2);
		
		//na stek stavljamo vrednost odgovarajuceg elementa niza
		Code.load(arrayName.getDesignator().obj);
		Code.load(conditionsStack.peek().indexObj);
		
		if(arrayName.getDesignator().obj.getType().getElemType()==MySimbolTable.charType) {
			Code.put(Code.baload);
		}
		else Code.put(Code.aload);	
	}
	public void visit(ForeachIdentName foreachIdent) {
		//element niza vec stavljen na stek pa ga cuvamo u ident promenljivoj
		Code.store(foreachIdent.obj);
		//inkrementiranje index-a
		Code.load(conditionsStack.peek().indexObj);
		Code.loadConst(1);
		Code.put(Code.add);
	    Code.store(conditionsStack.peek().indexObj);
	}
	public void visit(EndForeach endForeach) {
		Code.putJump(conditionsStack.peek().getForeachStart());//bezuslovni skok na pocetak petlje tj na sledecu iteraciju petlje
	}
	public void visit(Statement_foreach end_foreach_loop) {
		//kada obradimo celu petlju mozemo popuniti adresu koja sluzi za izlazak iz petlje
		//Code.fixup(conditionsStack.peek().getfixAddress());
		Conditions condition=conditionsStack.pop();
		condition.fixForeachOffsets();
	}
	
	/*CONDITION FACTOR*/
	public void visit(ConditionFact_eq cond_eq) {
		int code=relOperationCode.get(cond_eq.getClass());
		Code.putFalseJump(code, 0); //ubacice suprotan kod i mesto za offset koje cemo kasnije zakrpiti
		conditionsStack.peek().addCondFactorToCondition(new Cond_Factor(Code.pc-3, Code.inverse[code])); //za factor pamtitmo mesto koje je potrebno zakrpiti
	}
	public void visit(ConditionFact_neq cond_neq) {
		int code=relOperationCode.get(cond_neq.getClass());
		Code.putFalseJump(code, 0); //ubacice suprotan kod i mesto za offset koje cemo kasnije zakrpiti
		conditionsStack.peek().addCondFactorToCondition(new Cond_Factor(Code.pc-3, Code.inverse[code])); //za factor pamtitmo mesto koje je potrebno zakrpiti
	}
	public void visit(ConditionFact_qt cond_qt) {
		int code=relOperationCode.get(cond_qt.getClass());
		Code.putFalseJump(code, 0); //ubacice suprotan kod i mesto za offset koje cemo kasnije zakrpiti
		conditionsStack.peek().addCondFactorToCondition(new Cond_Factor(Code.pc-3, Code.inverse[code])); //za factor pamtitmo mesto koje je potrebno zakrpiti
	}
	public void visit(ConditionFact_gte cond_gte) {
		int code=relOperationCode.get(cond_gte.getClass());
		Code.putFalseJump(code, 0); //ubacice suprotan kod i mesto za offset koje cemo kasnije zakrpiti
		conditionsStack.peek().addCondFactorToCondition(new Cond_Factor(Code.pc-3, Code.inverse[code])); //za factor pamtitmo mesto koje je potrebno zakrpiti
	}
	public void visit(ConditionFact_lt cond_lt) {
		int code=relOperationCode.get(cond_lt.getClass());
		Code.putFalseJump(code, 0); //ubacice suprotan kod i mesto za offset koje cemo kasnije zakrpiti
		conditionsStack.peek().addCondFactorToCondition(new Cond_Factor(Code.pc-3, Code.inverse[code])); //za factor pamtitmo mesto koje je potrebno zakrpiti
	}
	public void visit(ConditionFact_lte cond_lte) {
		int code=relOperationCode.get(cond_lte.getClass());
		Code.putFalseJump(code, 0); //ubacice suprotan kod i mesto za offset koje cemo kasnije zakrpiti
		conditionsStack.peek().addCondFactorToCondition(new Cond_Factor(Code.pc-3, Code.inverse[code])); //za factor pamtitmo mesto koje je potrebno zakrpiti
	}
	public void visit(ConditionFact_expr conditionFact_expr) {
		Code.loadConst(0); //poredimo sa 0
		int code=relOperationCode.get(conditionFact_expr.getClass());
		Code.putFalseJump(code, 0); //ubacice suprotan kod i mesto za offset koje cemo kasnije zakrpiti
		conditionsStack.peek().addCondFactorToCondition(new Cond_Factor(Code.pc-3, Code.inverse[code])); //za factor pamtitmo mesto koje je potrebno zakrpiti
	}
	
	/*CONDITION TERM*/
	public void visit(ConditionTermList_or conditionTermList) {
		//log.info("usao u con term or");
		conditionsStack.peek().addCondTermToCondition();
	}
	public void visit(ConditionTermList_term conditionTerm) {
		//log.info("usao u con term list");
		conditionsStack.peek().addCondTermToCondition();
	}
	
}
