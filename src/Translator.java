
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Grant
 */
public class Translator {

    static String programID;
    static TreeMap<String, String> symbols;
    static ArrayList<Token> tokens = new ArrayList<>();
    static setScanner scanner = new setScanner();
    static int currPos = 0;
    static Token currToken;
    static StringBuilder sb;
    

    public static void main(String[] args) {
        fillArrayList();

    }
    
    public static void getNextToken(){
        currToken = tokens.get(currPos);
        currPos++;
    }

    public static void fillArrayList() {
        while (scanner.lookahead().tokenType != Token.UNRECOGNIZED) {
            Token toke = scanner.lookahead();
            scanner.consume();
            tokens.add(toke);
        }
    }

    public static void parseProgram() {

        if (currToken.tokenType != Token.PROGRAM) {
         //TODO   
        }
        
        getNextToken();
        
        if (currToken.tokenType != Token.ID) {
            //TODO
        }
        
        programID = currToken.getTokenString();
        
        getNextToken();
        
        if (currToken.tokenType != Token.VAR) {
            //TODO
        }
        
        getNextToken();
        
        parseDec();
        
        if (currToken.tokenType != Token.BEGIN) {
            //TODO 
        }
        
        getNextToken();
        
        parseStList();
        
        if (currToken.tokenType != Token.END) {
            //TODO
        }
        
        getNextToken();
        
        parseOut();

    }

    public static void parseDec() {

    }

    public static void parseNatDec() {

    }

    public static void parseSetDec() {

    }

    public static void parseNeVarList() {

    }

    public static void parseNeStList() {

    }
    
    public static void parseStList(){
        
    }

    public static void parseSt() {

    }

    public static void parseAsgn() {

    }

    public static void parseSetAsgn() {

    }
    
    //------  Start R Miller (TODO : Remove this temporary comment ) 

    public static void parseSetExp() {
    	Token lookAhead = tokens.get(currPos);
    	if (lookAhead.tokenType == Token.SETDIFFERENCE) {
    		//TODO:  Consume set difference token and add to output 
    		parseSetExp();
        }
    	else{
    		parseSLevel2();
    	}
    
    }
    
    public static void parseSLevel2() {
    	Token lookAhead = tokens.get(currPos);
    	if (lookAhead.tokenType == Token.UNION) {
            //TODO:  consume UNION and add to output AFTER
    		parseSLevel2();
        }
    	else{
    		parseSLevel1();
    	}

    }
    
    public static void parseSLevel1() {
    	Token lookAhead = tokens.get(currPos);
    	if (lookAhead.tokenType == Token.INTERSECTION) {
            //TODO: Consume INTERSECTION and add to output AFTER
    		parseSLevel1();
        }
    	else{
    		parseSLevel0();
    	}

    }

    public static void parseSLevel0() {
    	if (currToken.tokenType == Token.COMPLEMENT) {
            //TODO:  Consume COMPLEMENT and add to output BEFORE 
    		parseSAtomic();
        }

    }


    public static void parseSAtomic() {
    	
    	// if ( the <s atomic> is in the var list --> it is a set var
    	// TODO: Distinguish <set var> from <set const> .... ?
    		
    		
    	//else if.. 
    	if (currToken.tokenType == Token.LEFTPAREN) {
    		// it is a nested ( <set exp> ) 
    		// TODO: Consume ( and add to output
    		parseSetExp();
    		// TODO: Consume ) and add to output 
    	}
    	else{
    		// TODO : error
    	}
    }

    public static void parseSetConst() {
    	if (currToken.tokenType == Token.CMP) {
    		parseComplemented();
    	}
    	//TODO:  else if ( it is a set literal) ----  Not sure how to check this.. starts with parens? 
    		//parseSetLiteral()
    	else {
    		// TODO: error 
    	}

    }

    public static void parseComplemented() {
    	// TODO:  Consume CMP and add to output 
    	parseSetLiteral();
    }

    public static void parseSetLiteral() {
    	// TODO: Consume { and add to output 
    	parseNatList();
    	// TODO: Consume } and add to output 
    }

    public static void parseNatList() {
    	Token lookahead = tokens.get(currPos);
    	if (lookahead.tokenType == Token.RIGHTBRACE){
    		// TODO: Empty set.... Do nothing? The right brace included in parseSetLiteral
    	}
    	else{
    		parseNeNatList();
    	}
    }

    public static void parseNeNatList() {
    	Token lookahead = tokens.get(currPos);
    	if (lookahead.tokenType == Token.COMMA) {
    		// TODO: Consume comma and add to output AFTER
    		parseNeNatList();
    	}
    	else{
    		// TODO: Consume NATCONST and add to output ? 
    	}
    		
    	

    }

    public static void parseNatAsgn() {

    }

    public static void parseNatExp() {

    }

    public static void parseTest() {
    	if (currToken.tokenType == Token.NOT) {
    		// TODO: Consume NOT and add to output BEFORE 
    		parseTest();
    	}
    	else{
    		parseTestAtomic();
    	}

    }

    public static void parseTestAtomic() {
    	Token lookahead = tokens.get(currPos);
    	if (lookahead.tokenType == Token.EQUALS) {
    		// TODO: Consume EQUALS and add to output AFTER
    		parseSetExp();
    	}
    	else if (lookahead.tokenType == Token.SUBSET){
    		// TODO: Consume SUBSET and add to output AFTER
    		parseSetExp();
    	}
    	else if (lookahead.tokenType == Token.IS_IN){
    		// TODO: Consume IS_IN and add to output AFTER
    		parseNatExp();
    	}
    	else{
    		// TODO: error
    	}

    }

    public static void parseOut() {
    	// TODO: Consume the lookahead '.' ? 
    	parseSetExp();

    }

    public static void parseIf() {

    }

}
