import java.util.*;

public class Parser {

    private Symtab symtab = new Symtab();

    // the first sets.
    // note: we cheat sometimes:
    // when there is only a single token in the set,
    // we generally just compare tkrep with the first token.
    TK f_declarations[] = {TK.VAR, TK.none};
    TK f_statement[] = {TK.ID, TK.PRINT, TK.IF, TK.DO, TK.FA, TK.SKIP, TK.STOP, TK.BREAK, TK.DUMP, TK.none};
    TK f_print[] = {TK.PRINT, TK.none};
    TK f_assignment[] = {TK.ID, TK.none};
    TK f_if[] = {TK.IF, TK.none};
    TK f_do[] = {TK.DO, TK.none};
    TK f_fa[] = {TK.FA, TK.none};
    TK f_expression[] = {TK.ID, TK.NUM, TK.LPAREN, TK.none};
    TK f_skip[] = {TK.SKIP, TK.none};
    TK f_stop[] = {TK.STOP, TK.none};
    TK f_break[] = {TK.BREAK, TK.none};
    TK f_dump[] = {TK.DUMP, TK.none};
    TK f_predef[] = {TK.MODULO, TK.MAX, TK.none};

    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private int MaxCounter = -1;
    private  int inLoop = 0; // default inloop=0 means not in loop, add 1 when enter loop: fa, do, and minus 1 when end loop
    private int[] gotoPool;
    private int currentPool = 0;
    private int currentOut = 0;
    private int setGoto = 0;
    private void scan() {
        tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner) {
        this.scanner = scanner;
	this.gotoPool = new int[100];
        scan();
        program();
        if( tok.kind != TK.EOF )
            parse_error("junk after logical end of program");
        symtab.reportVariables();
    }

    // print something in the generated code
    private void gcprint(String str) {
        System.out.println(str);
    }
    // print identifier in the generated code
    // it prefixes x_ in case id conflicts with C keyword.
    private void gcprintid(String str) {
        System.out.println("x_"+str);
    }

    private void program() {
        // generate the E math functions:
        gcprint("#include <stdio.h>");
        gcprint("#include <math.h>");
	gcprint("#include <stdlib.h>");
	gcprint("#define MAX(X,Y) (X>Y)?X:Y");
        gcprint("int esquare(int x){ return x*x;}");
        gcprint("int esqrt(int x){ double y; if (x < 0) return 0; y = sqrt((double)x); return (int)y;}");
        gcprint("void checkMod(int b) {if(b==0) {fprintf(stdout, \"%s\", \"\\nmod(a,b) with b=0\\n\"); exit(1);}}");
	gcprint("int mod(int a, int b){ checkMod(b); int ret=a%abs(b); if(ret<0 && b>0) ret+=abs(b); else if(ret>0 && b<0) ret-=abs(b); return ret;}");	

        gcprint("int main() {");
	block();
        gcprint("return 0; }");
    }

    private void block() {
        symtab.begin_st_block();
	gcprint("{");
        if( first(f_declarations) ) {
            declarations();
        }
        statement_list();
        symtab.end_st_block();
	gcprint("}");
    }

    private void declarations() {
        mustbe(TK.VAR);
        while( is(TK.ID) ) {
            if( symtab.add_var_entry(tok.string,tok.lineNumber) ) {
                gcprint("int");
                gcprintid(tok.string);
                gcprint("= -12345;");
            }
            scan();
        }
        mustbe(TK.RAV);
    }

    private void statement_list(){
        while( first(f_statement) ) {
            statement();
        }
    }

    private void statement(){
        if( first(f_assignment) )
            assignment();
        else if( first(f_print) )
            print();
        else if( first(f_if) )
            ifproc();
        else if( first(f_do) )
            doproc();
        else if( first(f_fa) )
            fa();
	else if( first(f_skip) )
	    skip();
	else if( first(f_stop) )
	    stop();
	else if( first(f_break) )
	    breakproc();
	else if( first(f_dump) )
	    dump();
        else
            parse_error("statement");
    }

    private void assignment(){
        String id = tok.string;
        int lno = tok.lineNumber; // save it too before mustbe!
        mustbe(TK.ID);
        referenced_id(id, true, lno);
        gcprintid(id);
        mustbe(TK.ASSIGN);
        gcprint("=");
        expression();
        gcprint(";");
    }

    private void print(){
        mustbe(TK.PRINT);
        gcprint("printf(\"%d\\n\", ");
        expression();
        gcprint(");");
    }

    private void ifproc(){
        mustbe(TK.IF);
        guarded_commands(TK.IF);
        mustbe(TK.FI);
    }

    private void skip(){
	mustbe(TK.SKIP);
    }

    private void stop(){
	mustbe(TK.STOP);
	gcprint("exit(0);");
	if( !is(TK.FI) && !is(TK.OD) && !is(TK.AF) && !is(TK.EOF)) 
		System.err.println("warning: on line "+tok.lineNumber+" statement(s) follows stop statement");
    }

    private void dump(){
	int dump_linenumber = tok.lineNumber;
	mustbe(TK.DUMP);
	int level_now = symtab.get_level();
	if( is(TK.NUM) ) {
		int slevel = Integer.parseInt(tok.string);
		if(slevel > level_now) {
			System.err.println("warning: on line "+dump_linenumber+" dump statement level ("+slevel+") exceeds block nesting level ("+level_now+"). using "+level_now);
			slevel = level_now;
		}
	        gcprint("printf(\"+++ dump on line "+dump_linenumber+" of level "+slevel+" begin +++\\n\");");
		ArrayList<Entry> p = symtab.printVariables_level(slevel);
		if(!p.isEmpty()) {
		    for(Entry e:p) {
			String str = "x_";
			str+=e.getName();
			gcprint("printf(\"%12d %3d %3d %s\\n\","+str+","+e.getLinenumber()+","+slevel+",\""+e.getName()+"\");");

		    }
		}
	        gcprint("printf(\"--- dump on line "+dump_linenumber+" of level "+slevel+" end ---\\n\");");
		mustbe(TK.NUM);
	}
	else {
	   gcprint("printf(\"+++ dump on line "+dump_linenumber+" of all levels begin +++\\n\");");
	   for(int i=0; i<=level_now; i++) {
	        ArrayList<Entry> p = symtab.printVariables_level(i);
	        if(!p.isEmpty()) {
		     for(Entry e: p) {
			   String str = "x_";
			   str+=e.getName();
			   gcprint("printf(\"%12d %3d %3d %s\\n\","+str+","+e.getLinenumber()+","+i+",\""+e.getName()+"\");");
		     }
	        }
	   }   
	   gcprint("printf(\"--- dump on line "+dump_linenumber+" of all levels end ---\\n\");");
	}
    }

    private void breakproc(){
	int breakLineNumber = tok.lineNumber;
	mustbe(TK.BREAK);
	if(inLoop > 0) {
		if( is(TK.NUM)) {
			int num = Integer.parseInt(tok.string);
			if(num == 0) System.err.println("warning: on line "+tok.lineNumber+" break 0 statement ignored");
			else if(num >inLoop) System.err.println("warning: on line "+tok.lineNumber+" break statement exceeding loop nesting ignored");
			mustbe(TK.NUM);
			if(num > 0 && num <= inLoop && currentPool==currentOut ){
				gotoPool[currentPool]=inLoop-num;
				String str = "LOOP";
				str += Integer.toString(currentPool);
				currentPool = currentPool+1;
				gcprint("goto "+str+";");
				if( !is(TK.FI) && !is(TK.OD) && !is(TK.AF))  System.err.println("warning: on line "+tok.lineNumber+" statement(s) follows break statement");
			}
		}
		else {
			gcprint("break;");
			if( !is(TK.FI) && !is(TK.OD) && !is(TK.AF))
				System.err.println("warning: on line "+tok.lineNumber+" statement(s) follows break statement");
		}
	}
	else if(inLoop == 0) System.err.println("warning: on line "+breakLineNumber+" break statement outside of loop ignored");
	else {
		System.err.println("inLoop is not 1 neither 0!");
		System.exit(1);
	}
    }

    private void predef(){
	if( is(TK.MODULO) ){
		mustbe(TK.MODULO);
		gcprint("mod");
		if( is(TK.LPAREN) ){
	    	gcprint("(");
		if(MaxCounter!=-1) MaxCounter=MaxCounter+1;
		else if(MaxCounter>=5) {
			System.err.println("can't parse: line 2 max expressions nested too deeply (> 5 deep)");
			System.exit(1);
			}
	    	scan();
	    	expression();
	    	mustbe(TK.COMMA);
	    	gcprint(",");
	    	expression();
            	mustbe(TK.RPAREN);
            	gcprint(")");
		if(MaxCounter!=-1) MaxCounter=MaxCounter-1;
		}
	}
	else if( is(TK.MAX) ){
		mustbe(TK.MAX);
		gcprint("(MAX");
		if( is(TK.LPAREN) ){
	    	gcprint("(");
		MaxCounter = MaxCounter+1;
		if(MaxCounter>=5) {
//			gcprint("exit(1);");
			System.err.println("can't parse: line 2 max expressions nested too deeply (> 5 deep)");
			System.exit(1);
		}
		scan();
		expression();
		mustbe(TK.COMMA);
		gcprint(",");
		expression();
		mustbe(TK.RPAREN);
		gcprint("))");
		MaxCounter = MaxCounter-1;
		}
	}
	else parse_error("mod");
    }
		

    private void doproc(){
        mustbe(TK.DO);
        gcprint("while(1){");
	inLoop = inLoop+1;
        guarded_commands(TK.DO);
	gcprint("}");
	inLoop = inLoop-1;
	if(gotoPool[currentOut]==inLoop && currentPool==(currentOut+1)){
		String str = "LOOP";
		str += Integer.toString(currentOut);
        	gcprint(str+":");
		currentOut = currentOut+1;
	}
        mustbe(TK.OD);
    }

    private void fa(){
        mustbe(TK.FA);
        gcprint("for(");
	inLoop = inLoop+1;
        String id = tok.string;
        int lno = tok.lineNumber; // save it too before mustbe!
        mustbe(TK.ID);
        referenced_id(id, true, lno);
        gcprintid(id);
        mustbe(TK.ASSIGN);
        gcprint("=");
        expression();
        gcprint(";");
        mustbe(TK.TO);
        gcprintid(id);
        gcprint("<=");
        expression();
        gcprint(";");
        gcprintid(id);
        gcprint("++)");
        if( is(TK.ST) ) {
            gcprint("if( ");
            scan();
            expression();
            gcprint(")");
        }
        commands();
	inLoop = inLoop-1;
	if(gotoPool[currentOut] == inLoop && currentPool==(currentOut+1) ){
		String str = "LOOP";
		str += Integer.toString(currentOut);
		gcprint(str+":");
		currentOut = currentOut+1;
	}
        mustbe(TK.AF);
    }

    private void guarded_commands(TK which){
        guarded_command();
        while( is(TK.BOX) ) {
            scan();
            gcprint("else");
            guarded_command();
        }
        if( is(TK.ELSE) ) {
            gcprint("else");
            scan();
            commands();
        }
        else if( which == TK.DO )
            gcprint("else break;");
    }

    private void guarded_command(){
        gcprint("if(");
        expression();
        gcprint(")");
        commands();
    }

    private void commands(){
        mustbe(TK.ARROW);
        gcprint("{");/* note: generate {} to simplify, e.g., st in fa. */
        block();
        gcprint("}");
    }

    private void expression(){
        simple();
        while( is(TK.EQ) || is(TK.LT) || is(TK.GT) ||
               is(TK.NE) || is(TK.LE) || is(TK.GE)) {
            if( is(TK.EQ) ) gcprint("==");
            else if( is(TK.NE) ) gcprint("!=");
            else gcprint(tok.string);
            scan();
            simple();
        }
    }

    private void simple(){
        term();
        while( is(TK.PLUS) || is(TK.MINUS) ) {
            gcprint(tok.string);
            scan();
            term();
        }
    }

    private void term(){
        factor();
        while(  is(TK.TIMES) || is(TK.DIVIDE) || is(TK.REMAINDER) ) {
            gcprint(tok.string);
            scan();
            factor();
        }
    }

    private void factor(){
        if( is(TK.LPAREN) ) {
            gcprint("(");
	    if(MaxCounter!=-1) MaxCounter = MaxCounter+1;
	    else if(MaxCounter>=5) {
//		    gcprint("exit(1);");
		    System.err.println("can't parse: line 2 max expressions nested too deeply (> 5 deep)");
		    System.exit(1);
	    }
            scan();
            expression();
            mustbe(TK.RPAREN);
            gcprint(")");
	    if(MaxCounter!=-1) MaxCounter = MaxCounter-1;
        }
        else if( is(TK.SQUARE) ) {
            gcprint("esquare(");
            scan();
            expression();
            gcprint(")");
        }
        else if( is(TK.SQRT) ) {
            gcprint("esqrt(");
            scan();
            expression();
            gcprint(")");
        }
        else if( is(TK.ID) ) {
            referenced_id(tok.string, false, tok.lineNumber);
            gcprintid(tok.string);
            scan();
        }
        else if( is(TK.NUM) ) {
            gcprint(tok.string);
            scan();
        }
	else if( first(f_predef) ) {
		predef();
	}
        else
            parse_error("factor");
    }

    // be careful: use lno here, not tok.lineNumber
    // (which may have been advanced by now)
    private void referenced_id(String id, boolean assigned, int lno) {
        Entry e = symtab.search(id);
        if( e == null) {
            System.err.println("undeclared variable "+ id + " on line "
                               + lno);
            System.exit(1);
        }
        e.ref(assigned, lno);
    }

    // is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    private void mustbe(TK tk) {
        if( ! is(tk) ) {
            System.err.println( "mustbe: want " + tk + ", got " +
                                    tok);
            parse_error( "missing token (mustbe)" );
        }
        scan();
    }
    boolean first(TK [] set) {
        int k = 0;
	try{
        while(set[k] != TK.none && set[k] != tok.kind) {
            k++;
        }
	}
	catch(Exception oops){
	    System.err.println("linenumber="+tok.lineNumber+" set[0]="+set[0]+" tok.kind="+tok.kind);
	}
        return set[k] != TK.none;
    }

    private void parse_error(String msg) {
        System.err.println( "can't parse: line "
                            + tok.lineNumber + " " + msg );
        System.exit(1);
    }
}
