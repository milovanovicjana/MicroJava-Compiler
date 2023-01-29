package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;


public class SemanticAnalyzer extends VisitorAdaptor {
	
	
	
	boolean errorDetected = false; 
	int nVars; 
	
	/*CONST AND VAR*/
	int currentConstVal = 0;
	Struct currentTypeOfDeclList = null;
	
	/*METHOD*/
	boolean currentMethodIsOK=true;
	Obj currentMethod = null;
	int numberOfFormalArg=0;
	boolean returnFound = false; //vrv visak
	
	/*DESIGNATOR*/
	Stack<Obj> designatorObj = new Stack<>();
	
	/*ACTUAL PARAM*/
	Stack<List<Struct>> actualParamsStack = new Stack<>();
	
	/*ZA BREAK I CONTINUE*/
	int loopLevel=0;
	
	Stack<Obj> identObjList = new Stack<>();/*za proveru ident-a u okviru foreach petlje*/
	Stack<Struct> elemTypes = new Stack<>(); //za [,,]=niz; /*za proveru tipova kod [,...]=Designator*/
	
	
	
	Logger log = Logger.getLogger(getClass());
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder();
		int line = (info == null) ? 0: info.getLine();
		if (line != 0) {
			msg.append ("Greska na liniji ").append(line);
			msg.append(": ");
		}
		
		msg.append(message);
		log.error(msg.toString());
	}
	public void report_info(Obj objNode, int line,String name) {
		StringBuilder msg = new StringBuilder(); 
		msg.append ("Pretraga na liniji ").append(line).append("("+name+")");
		msg.append(", nadjeno ");
		
		SymbolTableVisitor s=new MyDumpSymbolTableVisitor();
		s.visitObjNode(objNode);
		msg.append(s.getOutput());
		log.info(msg.toString());
		
	}
	public boolean passed(){
	    return !errorDetected;
	}
	
	/*PROGRAM*/
	public void visit(ProgName progName) {
		progName.obj=Tab.insert(Obj.Prog, progName.getProgName(),Tab.noType);
    	MySimbolTable.openScope();
	}
	public void visit(Program program) {
		/*nVars=Tab.currentScope().getnVars();*/
		
		Obj obj=MySimbolTable.currentScope.findSymbol("main");
		if(obj==null || obj.getLevel()!=0 || !obj.getType().equals(MySimbolTable.noType) || obj.getKind()!=Obj.Meth ) {
			report_error("U programu mora postojati metoda main, koja je void tipa i nema argumente",null);
		}
		
		MySimbolTable.chainLocalSymbols(program.getProgName().obj);
		MySimbolTable.closeScope();
	}

	/*TYPE*/
	public void visit(Type type) {
		Obj typeNode=MySimbolTable.find(type.getTypeName());
		if(typeNode==MySimbolTable.noObj) {
			report_error("Nije pronadjen tip "+ type.getTypeName()+" u tabeli simbola!",type);
			type.struct=MySimbolTable.noType;
		}
		else {
			if(Obj.Type==typeNode.getKind()) {//ako je u pitanju tip
				type.struct=typeNode.getType();
			}
			else {
				report_error("Ime " + type.getTypeName() + " ne predstavlja tip!", type); //tipa ako smo prosledili imt umesto int!
    			type.struct = MySimbolTable.noType;
			}
			
		}
		
	}
	
	/*CONST*/ 
	public void visit(ConstDeclListType constDeclListType) {
		this.currentTypeOfDeclList=constDeclListType.getType().struct;	
	}
	public void visit(Constant_n constant_number) {
		if((currentTypeOfDeclList!=null && currentTypeOfDeclList.equals(MySimbolTable.intType)) || currentTypeOfDeclList==null) {
				this.currentConstVal=constant_number.getVal();
				constant_number.struct=MySimbolTable.intType;
				return;
		}
		report_error("Nekompabilan tip u definiciji konstante", constant_number);
		constant_number.struct=MySimbolTable.noType;
	}
	public void visit(Constant_c constant_char) {
		if((currentTypeOfDeclList!=null && currentTypeOfDeclList.equals(MySimbolTable.charType)) || currentTypeOfDeclList==null) {
				this.currentConstVal=(int)constant_char.getVal().charValue();
				constant_char.struct=MySimbolTable.charType;
				return;
		}
		report_error("Nekompabilan tip u definiciji konstante", constant_char);
		constant_char.struct=MySimbolTable.noType;
		
	}
	public void visit(Constant_b constant_bool) {
		if((currentTypeOfDeclList!=null && currentTypeOfDeclList.equals(MySimbolTable.boolType)) || currentTypeOfDeclList==null) {		
				this.currentConstVal=constant_bool.getVal();
				constant_bool.struct=MySimbolTable.boolType;
				return;
		}
		report_error("Nekompabilan tip u definiciji konstante", constant_bool);
		constant_bool.struct=MySimbolTable.noType;
		
	}
	public void visit(ConstDeclList constDeclList) {
		this.currentTypeOfDeclList=null;
	}
	public void visit(ConDecl conDecl) {
		
		if(MySimbolTable.currentScope().findSymbol(conDecl.getConstName())!=null) {
			report_error("Ime konstante "+conDecl.getConstName()+" je vec deklarisano u okviru istog opsega!", conDecl);
		}
		else {
			if(conDecl.getConstant().struct!=MySimbolTable.noType) {
				Obj obj=MySimbolTable.insert(Obj.Con, conDecl.getConstName(), conDecl.getConstant().struct);
				obj.setAdr(currentConstVal); //postavljamo vrednost konstante
			}	
		}
	}
	

	/*VAR*/ 
	public void visit(VarDeclList varDeclList) {
		this.currentTypeOfDeclList=null;
	}
	public void visit(TypeVar_type typeVar_type) {
		this.currentTypeOfDeclList=typeVar_type.getType().struct;
	}
	public void visit(VarDecl_ident vardecl_ident) {
		
		if(MySimbolTable.currentScope().findSymbol(vardecl_ident.getVarName())!=null) {
			report_error("Ime varijable "+vardecl_ident.getVarName()+" je vec deklarisano u okviru istog opsega!", vardecl_ident);
		}
		else {
			if(this.currentTypeOfDeclList!=MySimbolTable.noType) {
				MySimbolTable.insert(Obj.Var, vardecl_ident.getVarName(), currentTypeOfDeclList);
			}	
			/*fali mi ono za brojanje glob varijabli*/
			
			
			//dodala
			if(currentMethod==null)nVars+=1;
		}
	}
	public void visit(VarDecl_bracket vardecl_bracket) {
		if(MySimbolTable.currentScope().findSymbol(vardecl_bracket.getVarName())!=null) {
			report_error("Ime varijable "+vardecl_bracket.getVarName()+" je vec deklarisano u okviru istog opsega!", vardecl_bracket);
		}
		else {
			if(this.currentTypeOfDeclList!=MySimbolTable.noType) {
				MySimbolTable.insert(Obj.Var, vardecl_bracket.getVarName(),new Struct(Struct.Array,currentTypeOfDeclList));
			}	
			/*fali mi ono za brojanje glob varijabli*/
			//dodala
			if(currentMethod==null)nVars+=1;
		}
	}
	
	/*METHOD*/
	public void visit(MethodRetAndName_type meth_type_name) {
		//meth_type_name.struct=meth_type_name.getType().struct; -> mislim da je visak
		if(MySimbolTable.currentScope().findSymbol(meth_type_name.getMethodName())!=null) {
			this.currentMethodIsOK=false;
			report_error("Ime funkcije "+meth_type_name.getMethodName()+" je vec deklarisano u okviru istog opsega!", meth_type_name);
		}
		else {
			
			meth_type_name.obj=MySimbolTable.insert(Obj.Meth, meth_type_name.getMethodName(), meth_type_name.getType().struct);
			this.currentMethod=meth_type_name.obj;
			MySimbolTable.openScope();	
		}
	}
	public void visit(MethodRetAndName_void meth_void_name) {
		//meth_type_name.struct=meth_type_name.getType().struct; -> mislim da je visak
		if(MySimbolTable.currentScope().findSymbol(meth_void_name.getMethodName())!=null) {
			this.currentMethodIsOK=false;
			report_error("Ime funkcije "+meth_void_name.getMethodName()+" je vec deklarisano u okviru istog opsega!", meth_void_name);
		}
		else {
			
			meth_void_name.obj=MySimbolTable.insert(Obj.Meth, meth_void_name.getMethodName(),MySimbolTable.noType);
			this.currentMethod=meth_void_name.obj;
			MySimbolTable.openScope();	
		}
	}	
	public void visit(FormParam_ident formParam_ident) {
		if(currentMethodIsOK) {
			MySimbolTable.insert(Obj.Var, formParam_ident.getName(), formParam_ident.getType().struct);
			this.numberOfFormalArg+=1;
		}
	}
	public void visit(FormParam_bracket formParam_bracket) {
		if(currentMethodIsOK) {
			MySimbolTable.insert(Obj.Var, formParam_bracket.getName(), new Struct(Struct.Array,formParam_bracket.getType().struct));
			this.numberOfFormalArg+=1;
		}
	}
	
	
	//IZMENITI ZA RETURN ISKAZ U 4. FAZU
	public void visit(MethodDecl methodDecl) {
		if(currentMethodIsOK) {
			
			//mislim da ovo treba p u 4.fazu
//			if(currentMethod.getType()!=MySimbolTable.noType && !returnFound) {
//				report_error("Funkcija "+currentMethod.getName()+ " nema return iskaz", methodDecl);
//			}
			methodDecl.getMethodRetAndName().obj.setLevel(this.numberOfFormalArg);  //broj formalnih argumenata metode!
			MySimbolTable.chainLocalSymbols(methodDecl.getMethodRetAndName().obj);
			MySimbolTable.closeScope();
		}
		this.currentMethodIsOK=true;
		this.currentMethod=null;
		this.numberOfFormalArg=0;
		//this.returnFound=false; //dodala
	}

	//PROVERA TIPOVA 
	/*FACTOR SUB*/
	public void visit(FactorSub_constant factorSub_constant) {
		factorSub_constant.struct=factorSub_constant.getConstant().struct;
	}
	public void visit(FactorSub_expr factorSub_expr) {
		factorSub_expr.struct=factorSub_expr.getExpr().struct; 
	}
	public void visit(FactorSub_new factorSub_new) {
		if(!factorSub_new.getExpr().struct.equals(MySimbolTable.intType)) {
			report_error("Izraz za indeksiranje niza mora biti tipa int", factorSub_new);
		}
		factorSub_new.struct=new Struct(Struct.Array,factorSub_new.getType().struct); //factorSub dobija tip array of ...
	}	
	//ako je u pitanju neka varijabla
	public void visit(FactorSub_designator1 factorSub_designator1) {
		factorSub_designator1.struct=factorSub_designator1.getDesignator().obj.getType();
		factorSub_designator1.getDesignator().obj=factorSub_designator1.getDesignator().getDesignatorName().obj;
	}
//	//ako je u pitanju fja
	public void visit(FactorSub_designator2 factorSub_designator2) {
		Obj designatorObjNode=factorSub_designator2.getFunCallName().getDesignator().obj;
	
	
		//postoji u tabeli simbola ali nije metoda
		if ( designatorObjNode.getKind()!=Obj.Meth) {
			report_error("Simbol "+factorSub_designator2.getFunCallName().getDesignator().getDesignatorName().getDesignatorName()+" nije funkcija", factorSub_designator2);	
			return;
		}
		
		//dodati proveru za stvarne i formalne argumente!!!
		int numberOfFormalArgInCall=this.actualParamsStack.peek().size();
		int numberOfActualParam=designatorObjNode.getLevel();
		
		if (numberOfFormalArgInCall!=numberOfActualParam) {
			report_error("Broj stvarnih i formalnih parametara funkcije se ne poklapa!", factorSub_designator2);
		} else {
			
			if(factorSub_designator2.getFunCallName().getDesignator().getDesignatorName().getDesignatorName().equals("len")) {
				
				//provera da li je prosledjen niz 
				if(this.actualParamsStack.peek().get(0).getKind()!=Struct.Array) {
					log.info(designatorObjNode);
					report_error("Argument metode len mora biti tipa niza", factorSub_designator2);
				}
				
			}
			else {
				boolean formParamNonCompatible = false;
				Collection<Obj> formalPars = designatorObjNode.getLocalSymbols();	
				
				int i=0;
				for(Obj formalPar:formalPars) {
					//log.info("petlja");
					if(i<this.actualParamsStack.peek().size() && !formalPar.getType().assignableTo(this.actualParamsStack.peek().get(i))) {
						formParamNonCompatible = true;
						break;
					}
					i+=1;
				}
				if (formParamNonCompatible) {
					report_error("Tipovi stvarnih i formalnih parametara nisu kompatibilni!", factorSub_designator2);
				}
			}
			
		
		}
		
		this.actualParamsStack.pop();
		
		factorSub_designator2.struct=designatorObjNode.getType();
		factorSub_designator2.getFunCallName().getDesignator().obj=factorSub_designator2.getFunCallName().getDesignator().getDesignatorName().obj;
		
	}
	
	/*FACTOR*/
	public void visit(Factor_minus factor_minus) {
		if(factor_minus.getFactorSub().struct.equals(MySimbolTable.intType)) {
			factor_minus.struct=MySimbolTable.intType;			
		}
		else {
			report_error("Operand mora biti tipa int", factor_minus);
			factor_minus.struct=MySimbolTable.noType;
		}
	}
	public void visit(Factor_e factor_e) {
		factor_e.struct=factor_e.getFactorSub().struct;
	}
	
	/*MULOPFactorList*/
	public void visit(MulopFactorList_list mulopFactorList_list) {
		//ako su oba cinioca tipa int
		if(mulopFactorList_list.getMulopFactorList().struct.equals(MySimbolTable.intType)&& mulopFactorList_list.getFactor().struct.equals(MySimbolTable.intType)) {
			mulopFactorList_list.struct=MySimbolTable.intType;
		}
		else {
			mulopFactorList_list.struct=MySimbolTable.noType;
			report_error("Oba cinioca moraju biti tipa int", mulopFactorList_list);
		}
	}
	public void visit(MulopFactorList_factor mulopFactorList_factor) {
		mulopFactorList_factor.struct=mulopFactorList_factor.getFactor().struct;
	}
	
	/*TERM*/
	public void visit(Term term) {
		term.struct=term.getMulopFactorList().struct;
	}
	
	/*ADDTermList*/
	public void visit(AddopTermList_terml addopTermList_terml) {
		addopTermList_terml.struct=addopTermList_terml.getTerm().struct;
	}
	public void visit(AddopTermList_add addopTermList_add) {
		
		if(addopTermList_add.getAddopTermList().struct.equals(MySimbolTable.intType)&& addopTermList_add.getTerm().struct.equals(MySimbolTable.intType)) {
			addopTermList_add.struct=MySimbolTable.intType;
		}
		else {
			addopTermList_add.struct=MySimbolTable.noType;
			report_error("Oba sabirka moraju biti tipa int", addopTermList_add);
		}	
	}
	
	/*EXPR*/
	public void visit(Expr expr) {
		expr.struct=expr.getAddopTermList().struct;
	}
	
	/*DESIGNATOR*/
	public void visit(DesignatorName designatorName) {
		designatorName.obj=MySimbolTable.find(designatorName.getDesignatorName());
		this.designatorObj.push(designatorName.obj);
		
		if(designatorName.obj==MySimbolTable.noObj) {
			report_error("Simbol "+designatorName.getDesignatorName()+" ne postoji u tabeli simbola", designatorName);
			
		}
		report_info(designatorName.obj, designatorName.getLine(),designatorName.getDesignatorName());//proveriti da li ovo treba ispisivati ako simbol nije nadjen u tabeli?
	}
	public void visit(DesignatorIndex_index designatorIndex_index) {
		
	
		if(this.designatorObj.peek().getType().getKind()!=Struct.Array) {
			report_error("Simbol "+this.designatorObj.peek().getName()+" nije niz", designatorIndex_index);
			return;
			
		}
		if(designatorIndex_index.getExpr().struct!=MySimbolTable.intType) {
			report_error("Indeks niza mora biti int", designatorIndex_index);
			return;
		
		}
		Obj elem=this.designatorObj.pop();
		elem=new Obj(Obj.Elem, elem.getName(), elem.getType().getElemType());
		this.designatorObj.push(elem);

	}
	public void visit(Designator designator) {
		designator.obj=this.designatorObj.pop();
	}

	
//	//factor=f(f1(a),f2(b),c) //stvarni argumenti takodje mogu biti pozivi fja!
	
	/*ActParsList*/
	public void visit(FunCallName funCallName) { //detektujemo poziv neke fje
		this.actualParamsStack.push(new ArrayList<>()); //ubacimo listu koja je namenjena pozivu te funkcije
	}
	public void visit(ActParsList_ap actParsList_ap) {
		//log.info(actParsList_ap.getActPar().getExpr().struct.getKind());
		this.actualParamsStack.peek().add(0,actParsList_ap.getActPar().getExpr().struct);
	}
	public void visit(ActParMore_comma actParMore_comma) {
		this.actualParamsStack.peek().add( 0,actParMore_comma.getActPar().getExpr().struct); //dodajemo na pocetak liste
		//log.info(actParMore_comma.getActPar().getExpr().struct.getKind());
	}
	
	
	/*STATEMENT*/
	public void visit(Statement_return_expr statement_return_expr) {
		//returnFound=true;
		if(this.currentMethod!=null) {
			if(!currentMethod.getType().equals(statement_return_expr.getExpr().struct)) {
				report_error("Nekompatibilan izraz u return iskazu u metodi "+currentMethod.getName(), statement_return_expr);
			}
		}
		else {
			report_error("Return iskaz ne moze postojati izvan tele metoda", statement_return_expr);
		}
	}	
	public void visit(Statement_return_noexpr statement_return_noexpr) {
		if(this.currentMethod!=null) {
			if(!currentMethod.getType().equals(MySimbolTable.noType)) {
				report_error("Nekompatibilan izraz u return iskazuu metodi "+currentMethod.getName(), statement_return_noexpr.getParent());  //za ovo mi daje pogresnu liniju ???
			}
		}
		else {
			report_error("Return iskaz ne moze postojati izvan tele metoda", statement_return_noexpr);
		}
	}

	//STA AKO IMAMO VISE BREAK ILI CONTINUE ISKAZA U ISTOM LOOP-u???
	public void visit(WhileLoopStart whileLoopStart) { //ulazak u petju
		loopLevel++;
	}
	public void visit(ForeachLoopStart foreachLoopStart) { //ulazak u petju
		loopLevel++;
	}
	public void visit(Statement_while statement_while) { //izlazak iz petlje
		loopLevel--;
	}
	
	//DOVRSTITI ZA IDENT U FOREACHU ZA OVAJ CASE [,,,,]=DESIGNATOR;
	public void visit(Statement_foreach statement_foreach) { //izlazak iz petlje
		loopLevel--;
		//provera da li je Designator niz
		if(statement_foreach.getDesignatorForeach().getDesignator().obj.getType().getKind()!=Struct.Array) {
			report_error("Izraz nad kojim se zove foreach petlja mora biti niz proizvoljnog tipa", statement_foreach);
			
		}
		
		//provera da li je ident postoji u tabeli simbola
		Obj obj=MySimbolTable.find(statement_foreach.getForeachIdentName().getIdentName()); 
		
		statement_foreach.getForeachIdentName().obj=obj; //DODALA ZBOG 4.FAZE
		
		report_info(obj, statement_foreach.getLine(),statement_foreach.getForeachIdentName().getIdentName());
		
		if(obj==MySimbolTable.noObj  || obj.getKind()!=Obj.Var) { //dodala ovaj uslov za var!
			report_error("Promenljiva "+statement_foreach.getForeachIdentName().getIdentName()+" u foreach petlji mora biti lokalna ili globalna promenljiva", statement_foreach);	
		}

		//provera da li je tip identa jednak tipu elementa designatora
		if(statement_foreach.getDesignatorForeach().getDesignator().obj.getType().getKind()==Struct.Array && obj.getType().getKind()!=statement_foreach.getDesignatorForeach().getDesignator().obj.getType().getElemType().getKind()) {
			report_error("Promenljiva "+statement_foreach.getForeachIdentName().getIdentName()+" u foreach petlji mora biti istog tipa kao i elementi niza nad kojim se zove foreach petlja", statement_foreach);
		}
		
		//dodala
		identObjList.pop();
	}
	
	//dodala proveru identa u foreach petljI
	public void visit(ForeachIdentName foreachIdentName) {
		//dodala u fje  plusplus,minusminus,DesignatorStatementAssignop_assignop,foreachIdentName,statement_foreach pop samo
		Obj obj=MySimbolTable.find(foreachIdentName.getIdentName()); 
		identObjList.push(obj);
	}
	
	public void visit (Statement_continue statement_continue) {
		if(loopLevel==0) {
			report_error("Iskaz continue se moze nalaziti samo unutar while i foreach petlje", statement_continue);
		}
	}
	public void visit (Statement_break statement_break) {
		if(loopLevel==0) {
			report_error("Iskaz break se moze nalaziti samo unutar while i foreach petlje", statement_break);
		}
	}
	public void visit(Statement_print statement_print) {
		if(statement_print.getPrintParameters().struct.equals(MySimbolTable.intType) ||
				statement_print.getPrintParameters().struct.equals(MySimbolTable.charType) ||
				statement_print.getPrintParameters().struct.equals(MySimbolTable.boolType)) {
			return;
		}
		report_error("Izraz u print iskazu mora biti tipa int,char ili bool", statement_print);		
	}
	public void visit(Statement_read statement_read) {
		if(statement_read.getDesignator().obj.getKind()!=Obj.Var && statement_read.getDesignator().obj.getKind()!=Obj.Elem) { //nisam stavila za polje unutar objekta
			report_error("U read iskazu moze biti promenljiva ili element niza", statement_read);
			return;
		}
		if( 
				!statement_read.getDesignator().obj.getType().equals(MySimbolTable.intType) &&
				!statement_read.getDesignator().obj.getType().equals(MySimbolTable.charType) &&
				!statement_read.getDesignator().obj.getType().equals(MySimbolTable.boolType)) { //nisam stavila za polje unutar objekta
			report_error("Iskaz u  read iskazu mora biti tipa int, char ili bool", statement_read);
		
		}
		statement_read.getDesignator().obj=statement_read.getDesignator().getDesignatorName().obj;
	}


	/*PRINT*/
	public void visit(PrintParameters_noNumber printParameters_noNumber) {
		printParameters_noNumber.struct=printParameters_noNumber.getExpr().struct;
	}
	public void visit(PrintParameters_withNumber printParameters_withNumber) {
		printParameters_withNumber.struct=printParameters_withNumber.getExpr().struct;
	}

	/*DESIGNATOR STATEMENT*/
	public void visit(DesignatorStatementAssignop_assignop designatorAssign) {
		//dodala-provera da li je u pitanju ident u okviru foreach petlje
		boolean flag=false;
		for(Obj o:identObjList) {
			if(o.equals(designatorAssign.getDesignator().obj)) {
				flag=true;
				report_error("Promenljiva "+o.getName()+" koja predstavlja tekuci element niza se moze samo citati, ne moze se vrstit upis u nju", designatorAssign);
				break;
			}	
		}
	    if(flag) {
			return;
		}
		//dovde
	    
		if(designatorAssign.getDesignator().obj.getKind()!=Obj.Var && designatorAssign.getDesignator().obj.getKind()!=Obj.Elem) { 
			report_error("Sa leve strane znaka dodele mora biti promenljiva ili element niza", designatorAssign);
			return;
		}
		if( !designatorAssign.getDesignator().obj.getType().assignableTo(designatorAssign.getExpr().struct)) { //nisam stavila za polje unutar objekta
			report_error("Leva i desna strana u iskazu dedele nisu kompatibile", designatorAssign);
		
		}
		designatorAssign.getDesignator().obj=designatorAssign.getDesignator().getDesignatorName().obj;
	}
	public void visit(DesignatorStatement_plusplus plusplus) {
		//dodala-provera da li je u pitanju ident u okviru foreach petlje
		boolean flag=false;
		for(Obj o:identObjList) {
			if(o.equals(plusplus.getDesignator().obj)) {
				flag=true;
				report_error("Promenljiva "+o.getName()+" koja predstavlja tekuci element niza se moze samo citati, ne moze se vrstit upis u nju", plusplus);
				break;
			}	
		}
		if(flag) {
			return;
		}
		//dovde
		
		
		if(plusplus.getDesignator().obj.getKind()!=Obj.Var && plusplus.getDesignator().obj.getKind()!=Obj.Elem) { 
			report_error("Izraz koji se inkrementira mora biti promenljiva ili element niza", plusplus);
			return;
		}
		if(!plusplus.getDesignator().obj.getType().equals(MySimbolTable.intType)) { //nisam stavila za polje unutar objekta
			report_error("Simbol "+plusplus.getDesignator().getDesignatorName().getDesignatorName()+" mora biti tipa int", plusplus);
		}
		plusplus.getDesignator().obj=plusplus.getDesignator().getDesignatorName().obj;
	}
	public void visit(DesignatorStatement_minusminus minusminus) {
		//dodala-provera da li je u pitanju ident u okviru foreach petlje
		boolean flag=false;
		for(Obj o:identObjList) {
			if(o.equals(minusminus.getDesignator().obj)) {
				flag=true;
				report_error("Promenljiva "+o.getName()+" koja predstavlja tekuci element niza se moze samo citati, ne moze se vrstit upis u nju", minusminus);
				break;
			}	
		}
	    if(flag) {
			return;
		}
		//dovde
		
		if(minusminus.getDesignator().obj.getKind()!=Obj.Var && minusminus.getDesignator().obj.getKind()!=Obj.Elem) { 
			report_error("Izraz koji se dekrementira mora biti promenljiva ili element niza", minusminus);
			return;
		}
		if(!minusminus.getDesignator().obj.getType().equals(MySimbolTable.intType)) { //nisam stavila za polje unutar objekta
			report_error("Simbol "+minusminus.getDesignator().getDesignatorName().getDesignatorName()+" mora biti tipa int", minusminus);
		}
		minusminus.getDesignator().obj=minusminus.getDesignator().getDesignatorName().obj;
	}
	public void visit(DesignatorStatement_actPars designFun) {
		Obj designatorObjNode=designFun.getFunCallName().getDesignator().obj;
		//postoji u tabeli simbola ali nije metoda
		if ( designatorObjNode.getKind()!=Obj.Meth) {
			report_error("Simbol "+designFun.getFunCallName().getDesignator().getDesignatorName().getDesignatorName()+" nije funkcija", designFun);	
			return;
		}
		
		//dodati proveru za stvarne i formalne argumente!!!
		int numberOfFormalArgInCall=this.actualParamsStack.peek().size();
		int numberOfActualParam=designatorObjNode.getLevel();
		
		if (numberOfFormalArgInCall!=numberOfActualParam) {
			report_error("Broj stvarnih i formalnih parametara funkcije se ne poklapa!", designFun);
		} else {
			
			//dodala if else pre bilo samo ovo iz else
			if(designFun.getFunCallName().getDesignator().getDesignatorName().getDesignatorName().equals("len")) {
				
				//provera da li je prosledjen niz 
				if(this.actualParamsStack.peek().get(0).getKind()!=Struct.Array) {
					report_error("Argument metode len mora biti tipa niza", designFun);
				}
				
			}
			else {
				boolean formParamNonCompatible = false;
				Collection<Obj> formalPars = designatorObjNode.getLocalSymbols();	
				int i=0;
				for(Obj formalPar:formalPars) {
					if(i<this.actualParamsStack.peek().size() && !formalPar.getType().assignableTo(this.actualParamsStack.peek().get(i))) {
						formParamNonCompatible = true;
						break;
					}
					i+=1;
				}
				if (formParamNonCompatible) {
					report_error("Tipovi stvarnih i formalnih parametara nisu kompatibilni!", designFun);
				}
				
			}
			//kraj
			
		}
		
		this.actualParamsStack.pop();
		
		designFun.getFunCallName().getDesignator().obj=designFun.getFunCallName().getDesignator().getDesignatorName().obj;
	
	}
	//dodala
	public void visit(Desig_d desig) {
		//dodala-provera da li je u pitanju ident u okviru foreach petlje
		boolean flag=false;
		for(Obj o:identObjList) {
			if(o.equals(desig.getDesignator().obj)) {
				flag=true;
				report_error("Promenljiva "+o.getName()+" koja predstavlja tekuci element niza se moze samo citati, ne moze se vrstit upis u nju", desig);
				break;
			}	
		}
		if(flag) {
			return;
		}
		//dovde
		if((desig.getDesignator().obj.getKind()!=Obj.Var && desig.getDesignator().obj.getKind()!=Obj.Elem) || 
				desig.getDesignator().obj.getType().getKind()==Struct.Array){ 
			report_error("Simbol "+desig.getDesignator().getDesignatorName().getDesignatorName()+" nije promenljiva niti elementi niza", desig);
			
		}
		elemTypes.push(desig.getDesignator().obj.getType());
	}
	public void visit(DesignatorStatement_desig_array desig) {
		if(desig.getDesignator().obj.getType().getKind()!=Struct.Array) {
			report_error("Simbol "+desig.getDesignator().getDesignatorName().getDesignatorName()+" nije niz", desig);
			elemTypes.clear();
			return;
			
		}
		for(Struct elem:elemTypes) {
			if(!elem.equals(desig.getDesignator().obj.getType().getElemType())){
				report_error("Tip elemenata niza sa desne strane mora biti kompatibilan sa svim elementima sa leve strane znaka =", desig);	
			}
		}
		
	    elemTypes.clear();
		
	}

	/*CONDITIONS*/
	public void visit(ConditionFact_expr conditionFact_expr) {
		if(conditionFact_expr.getExpr().struct.equals(MySimbolTable.boolType)) {
			conditionFact_expr.struct=MySimbolTable.boolType;
		}
		else {
			report_error("Uslovni izraz mora biti tipa bool", conditionFact_expr);
			conditionFact_expr.struct=MySimbolTable.noType;
		}
	}
	public void visit(ConditionFact_eq cond_eq) {
		if(!cond_eq.getExpr().struct.compatibleWith(cond_eq.getExpr1().struct)) {
			report_error("Tipovi izraza u uslovu moraju biti komaptibilni", cond_eq);
			cond_eq.struct=MySimbolTable.noType;
		}
		else {
			cond_eq.struct=MySimbolTable.boolType;
		}
	}
	public void visit(ConditionFact_neq cond_neq) {
		if(!cond_neq.getExpr().struct.compatibleWith(cond_neq.getExpr1().struct)) {
			report_error("Tipovi izraza u uslovu moraju biti komaptibilni", cond_neq);
			cond_neq.struct=MySimbolTable.noType;
		}
		else {
			cond_neq.struct=MySimbolTable.boolType;
		}
	}
	public void visit(ConditionFact_qt cond_gt) {
		if(!cond_gt.getExpr().struct.compatibleWith(cond_gt.getExpr1().struct)) {
			report_error("Tipovi izraza u uslovu moraju biti komaptibilni", cond_gt);
			cond_gt.struct=MySimbolTable.noType;
		}
		else {
			if(cond_gt.getExpr().struct.getKind()!=Struct.Array && cond_gt.getExpr1().struct.getKind()!=Struct.Array) {
				cond_gt.struct=MySimbolTable.boolType;
			}
			else{
				report_error("Uz promenljive tipa niza se mogu koristiti samo relacioni operatori != i ==", cond_gt);
				cond_gt.struct=MySimbolTable.noType;
			}
		}
	}
	public void visit(ConditionFact_gte cond_gte) {
		if(!cond_gte.getExpr().struct.compatibleWith(cond_gte.getExpr1().struct)) {
			report_error("Tipovi izraza u uslovu moraju biti komaptibilni", cond_gte);
			cond_gte.struct=MySimbolTable.noType;
		}
		else {
			if(cond_gte.getExpr().struct.getKind()!=Struct.Array && cond_gte.getExpr1().struct.getKind()!=Struct.Array) {
				cond_gte.struct=MySimbolTable.boolType;
			}
			else{
				report_error("Uz promenljive tipa niza se mogu koristiti samo relacioni operatori != i ==", cond_gte);
				cond_gte.struct=MySimbolTable.noType;
			}
		}
	}
	public void visit(ConditionFact_lt cond_lt) {
		if(!cond_lt.getExpr().struct.compatibleWith(cond_lt.getExpr1().struct)) {
			report_error("Tipovi izraza u uslovu moraju biti komaptibilni", cond_lt);
			cond_lt.struct=MySimbolTable.noType;
		}
		else {
			if(cond_lt.getExpr().struct.getKind()!=Struct.Array && cond_lt.getExpr1().struct.getKind()!=Struct.Array) {
				cond_lt.struct=MySimbolTable.boolType;
			}
			else{
				report_error("Uz promenljive tipa niza se mogu koristiti samo relacioni operatori != i ==", cond_lt);
				cond_lt.struct=MySimbolTable.noType;
			}
		}
	}
	public void visit(ConditionFact_lte cond_lte) {
		if(!cond_lte.getExpr().struct.compatibleWith(cond_lte.getExpr1().struct)) {
			report_error("Tipovi izraza u uslovu moraju biti komaptibilni", cond_lte);
			cond_lte.struct=MySimbolTable.noType;
		}
		else {
			if(cond_lte.getExpr().struct.getKind()!=Struct.Array && cond_lte.getExpr1().struct.getKind()!=Struct.Array) {
				cond_lte.struct=MySimbolTable.boolType;
			}
			else{
				report_error("Uz promenljive tipa niza se mogu koristiti samo relacioni operatori != i ==", cond_lte);
				cond_lte.struct=MySimbolTable.noType;
			}
		}
	}
	public void visit(ConditionFactList_fact conditionFactList_fact) {
		conditionFactList_fact.struct=conditionFactList_fact.getConditionFact().struct;
	}
	public void visit(ConditionFactList_and conditionFactList_and) {
		if(conditionFactList_and.getConditionFact().struct.equals(MySimbolTable.boolType) 
				&& conditionFactList_and.getConditionFactList().struct.equals(MySimbolTable.boolType)) {
			conditionFactList_and.struct=MySimbolTable.boolType;
		}
		else {
			conditionFactList_and.struct=MySimbolTable.noType;
		}
	}	
	public void visit(ConditionTerm conditionTerm) {
		conditionTerm.struct=conditionTerm.getConditionFactList().struct;
	}	
	public void visit(ConditionTermList_term conditionTermList_term) {
		conditionTermList_term.struct=conditionTermList_term.getConditionTerm().struct;
	}
	public void visit(ConditionTermList_or conditionTermList_or) {
		if(conditionTermList_or.getConditionTerm().struct.equals(MySimbolTable.boolType) 
				&& conditionTermList_or.getConditionTermList().struct.equals(MySimbolTable.boolType)) {
			conditionTermList_or.struct=MySimbolTable.boolType;
		}
		else {
			conditionTermList_or.struct=MySimbolTable.noType;
		}
	}	
	public void visit(Condition_termlist condition_termlist) {
		condition_termlist.struct=condition_termlist.getConditionTermList().struct;
	}
	
}

