package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;


public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			//C:\Users\xxx\Desktop\CETVRTA-GODINA\7.SEMESTAR\PP1\PROJEKAT_PP\workspace\MJCompiler>ant -f build.xml runObj
//			
			if(args.length<2) {
				log.info("Moraju se proslediti 2 argumenta mikrojava izvorni kod i izlazni obj fajl!");

			}
			//File sourceCode = new File("test/program.mj");
			File sourceCode = new File(args[0]);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        Program prog = (Program)(s.value); 
	        
	        //inicijalizacija tabele simbola
	        MySimbolTable.init();
	        
			// ispis sintaksnog stabla
			log.info(prog.toString(""));
			log.info("===================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v); 
			
			log.info("===================================");
			MySimbolTable.dump();
			
			if(!p.errorDetected && v.passed()){
				//ako je sve proslo kako treba pocinjemo 4.fazu
				//File objFile=new File("test/program.obj");
				File objFile=new File(args[1]);
				if(objFile.exists())objFile.delete();
				CodeGenerator codeGeneraor=new CodeGenerator();
				prog.traverseBottomUp(codeGeneraor);
				Code.dataSize=v.nVars; 
				Code.mainPc=codeGeneraor.getMainPc();
				Code.write(new FileOutputStream(objFile));
				log.info("Parsiranje uspesno zavrseno!");
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}
	      
//			log.info(" Print count calls = " + v.printCallCount);
//
//			log.info(" Deklarisanih promenljivih ima = " + v.varDeclCount);
			
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
}
