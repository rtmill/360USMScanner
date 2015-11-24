
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Grant Wilson & Robert Miller
 */
public class setTranslator {
    
    static String programID;
    static TreeMap<String, String> symbols = new TreeMap<String, String>(); //ID , TYPE("NAT" or "SET")
    static ArrayList<Token> tokens = new ArrayList<Token>();
    static setScanner scanner = new setScanner();
    static int currPos = 0;
    static Token currToken;
    static StringBuilder sb = new StringBuilder();
    
    static boolean declared;
    static String varType = "NAT"; //used to determine what sort of variable is being declared, "NAT" and "SET"

    public static void main(String[] args) {
        
        fillArrayList();
        getNextToken();
        parseProgram();

        //TODO output into file ID.java
        //This seems to work
//        File f = new File(programID+".java");
//        try {
//            PrintWriter write = new PrintWriter(f);
//            write.print(sb.toString());
//            write.flush();
//            write.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(setTranslator.class.getName()).log(Level.SEVERE, null, ex);
//        }
        System.out.println(sb.toString());
        
    }
    
    public static void getNextToken() {
        //TODO: remove comment
        //There was a bug that getNextToken() was going out of bounds so i added this extra stuff.
        if (currPos < tokens.size()) {
            currToken = tokens.get(currPos);
            //TODO del 
            //          System.out.println(currToken);
            currPos++;
        } else {
//            System.out.println("end of tokens reached");
            currToken = new Token(Token.UNRECOGNIZED);
        }
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
            handleError("PROGRAM");
        }
        
        getNextToken();
        
        if (currToken.tokenType != Token.ID) {
            //TODO
            handleError("ID");
        }
        
        programID = currToken.getTokenString();

        //public class ID { ...
        sb.append("public class ").append(programID).append("{\n\n");
        
        getNextToken();
        
        if (currToken.tokenType != Token.VAR) {
            //TODO
            handleError("VAR");
        }
        
        getNextToken();
        
        parseDec();
        
        if (currToken.tokenType != Token.BEGIN) {
            //TODO 
            handleError("BEGIN");
        }
        
        getNextToken();

        //public static void main (String[] args){...
        sb.append("public static void main (String[] args){\n");
        
        parseStList();
        
        if (currToken.tokenType != Token.END) {
            //TODO
            handleError("END");
        }
        
        getNextToken();
        
        parseOut();

        // ... } end of main method
        sb.append("\n}");

        // ... } end of class
        sb.append("\n}");
        
    }
    
    public static void parseDec() {
        parseNatDec();
        parseSetDec();
    }
    
    public static void parseNatDec() {
        
        if (currToken.tokenType == Token.NAT) {
            getNextToken();
            varType = "NAT";
            parseNeVarList();
            if (!(currToken.tokenType == Token.SEMICOLON)) {
                //TODO   
                handleError("SEMICOLON");
            }
            getNextToken();
        }
        
    }
    
    public static void parseSetDec() {
        if (currToken.tokenType == Token.SET) {
            getNextToken();
            varType = "SET";
            parseNeVarList();
            if (!(currToken.tokenType == Token.SEMICOLON)) {
                //TODO
                handleError("SEMICOLON");
            }
            getNextToken();
            
        }
    }
    
    public static void parseNeVarList() {
        if (!(currToken.tokenType == Token.ID)) {
            //TODO    
            handleError("ID");
        }
        symbols.put(currToken.tokenString, varType);

        //Add static var declaration to output
        sb.append("private static ");
        switch (varType) {
            case "SET":
                sb.append(String.format("CofinFin %s = new CofinFin();\n", currToken.tokenString));
                break;
            case "NAT":
                sb.append(String.format("int %s;\n", currToken.tokenString));
                break;
            default:
                throw new AssertionError();
        }
        
        getNextToken();
        
        if (currToken.tokenType == Token.COMMA) {
            getNextToken();
            parseNeVarList();
        }
        
    }
    
    public static void parseStList() {
        //Empty Statement Listb n,m, ,  mnb.
        if (currToken.tokenType == Token.END) {
            
            return;
        } //First of <st list> : IF, ID 
        else if (currToken.tokenType == Token.IF || currToken.tokenType == Token.ID) {
            //TODO expected either if or id
            parseNeStList();
            
        } else {
            //Empty st list
        }
        
    }
    
    public static void parseNeStList() {
        parseSt();
        if (currToken.tokenType == Token.SEMICOLON) {
            getNextToken();
            parseNeStList();
        }
        
    }
    
    public static void parseSt() {
        if (currToken.tokenType == Token.IF) {
            parseIf();
        } else if (currToken.tokenType == Token.ID) {
            parseAsgn();
        } else {
            //TODO problem
            handleError("ID or IF");
        }
        
    }
    
    public static void parseAsgn() {
        declared = symbols.containsKey(currToken.tokenString);
        
        if (declared) {
            //TODO variable declared; must check type on assignment
            switch (symbols.get(currToken.tokenString)) {
                case "SET":
                    parseSetAsgn();
                    break;
                case "NAT":
                    parseNatAsgn();
                    break;
                default:
                    throw new AssertionError();
            }
        } else { //new variable
            //TODO determine type of variable
            handleError(String.format("VAR %s not declared", currToken.tokenString));
        }
        
    }
    
    public static void parseSetAsgn() {
        if (currToken.tokenType != Token.ID) {
            //TODO expected id
            handleError("ID");
        }
        getNextToken();
        if (currToken.tokenType != Token.ASSIGN) {
            //TODO expected assign
            handleError("ASSIGN");
        }
        getNextToken();
        parseSetExp();
    }

    //------  Start R Miller (TODO : Remove this temporary comment ) 
    public static void parseSetExp() {
        parseSLevel2();
        if (currToken.tokenType == Token.SETDIFFERENCE) {
            //TODO:  Consume set difference token and add to output
            sb.append(" \\ ");
            getNextToken();
            parseSetExp();
        }
        
    }
    
    public static void parseSLevel2() {
        parseSLevel1();
        if (currToken.tokenType == Token.UNION) {
            //TODO:  consume UNION and add to output AFTER
            sb.append(" + ");
            getNextToken();
            parseSLevel2();
        }
    }
    
    public static void parseSLevel1() {
        parseSLevel0();
        if (currToken.tokenType == Token.INTERSECTION) {
            sb.append("* ");
            getNextToken();
            parseSLevel1();
        }
    }
    
    public static void parseSLevel0() {
        if (currToken.tokenType == Token.COMPLEMENT) {
            //TODO:  Consume COMPLEMENT and add to output BEFORE
            sb.append("-");
            getNextToken();
            parseSLevel0();
        } else {
            parseSAtomic();
        }
    }
    
    public static void parseSAtomic() {
        if (currToken.tokenType == Token.ID) {
            if (symbols.get(currToken.tokenString) == "SET") {
                // TODO: What does he mean here? Add tokenString to output?
                // satomicResultVar = ID;

            } else if (symbols.get(currToken.tokenString) == "NAT") {
                throw new Error("Identifier declared as NAT, must be SET");
            } else {
                throw new Error("Identifier not declared");
            }
        } else if (currToken.tokenType == Token.LEFTPAREN) {
            sb.append("(");
            getNextToken();
            parseSetExp();

            if (currToken.tokenType == Token.RIGHTPAREN) {
                sb.append(") ");
                getNextToken();
            } else {
                throw new Error("RIGHTPAREN expected");
            }
        } else if (currToken.tokenType == Token.CMP || currToken.tokenType == Token.LEFTBRACE) {
            parseSetConst();
            // TODO: What does he mean here? Add tokenString to output?
            // satomicResultVar = setConstResultVar;
        }
    }



    
    public static void parseSetConst() {
        if (currToken.tokenType == Token.CMP) {
            parseComplemented();
        }
        else {
            parseSetLiteral();
        }
        
    }
    
    public static void parseComplemented() {
        if (currToken.tokenType != Token.CMP) {
            throw new Error("CMP expected");
        }
        sb.append("CMP");
        getNextToken();
        parseSetLiteral();
    }
    
    public static void parseSetLiteral() {
        // {  <nat list>  }
        if (currToken.tokenType != Token.LEFTBRACE) {
            //TODO del
            handleError("LEFTBRACE");
        }
        else{
            sb.append("{");
            getNextToken();
            parseNatList();
            if (currToken.tokenType != Token.RIGHTBRACE) {
                //TODO del
                handleError("RIGHTBRACE");
            }
            else{
                sb.append("}");
                getNextToken();
            }
        }

    }
    
    public static void parseNatList() {
        if (currToken.tokenType == Token.NATCONST) {
            parseNeNatList();
        }
        // else it is empty, thus doing nothing will result correctly in {}
    }
    
    public static void parseNeNatList() {
        if (currToken.tokenType != Token.NATCONST) {
            handleError("NATCONST");
        }
        sb.append(currToken.tokenString);
        getNextToken();
        
        if (currToken.tokenType == Token.COMMA) {
            sb.append(", ");
            getNextToken();
            parseNeNatList();
        }
        
    }
    
    public static void parseNatAsgn() {
        // ID ASSIGN <nat exp>
           // ID = natExpResult
        if (currToken.tokenType != Token.ID) {
            handleError("ID");
        }
        else if( symbols.get(currToken.tokenString) != "NAT"){
            throw new Error("NAT type expected");
        }
        else{
            sb.append(currToken.tokenString + " ");
            getNextToken();
            if (currToken.tokenType != Token.ASSIGN) {
                handleError("ASSIGN");
            }
            else{
                sb.append(" = ");
                getNextToken();
                parseNatExp();
            }
        }
    }
    // TODO
    public static void parseNatExp() {
        if (currToken.tokenType == Token.ID) {
            // TODO : Output here?
            getNextToken();
        } else if (currToken.tokenType == Token.NATCONST) {
            //TODO output assign to nat const
            //probably just get the string from nat const
            getNextToken();
        }
        else{
            throw new Error("expecting ID or NATCONST");
        }
    }
    
    public static void parseTest() {
        if (currToken.tokenType == Token.NOT) {
            sb.append("!");
            getNextToken();
            parseTest();
        } else {
            parseTestAtomic();
        }
        
    }
    
    public static void parseTestAtomic() {
//        if (currToken.tokenType == Token.ID){
//            if (symbols.get(currToken.tokenString) == "SET"){
//                // TODO
//
//            }
//            else if (symbols.get(currToken.tokenString) == "NAT"){
//                // TODO
//
//            }
//            else{
//                throw new Error("Identifier not declared");
//            }
//
//        }
//
//

        if (currToken.tokenType == Token.NATCONST) {
            
            if (currToken.tokenType != Token.IS_IN) {
                throw new Error("Expected ISIN token");
            }
            
            parseSetExp();
        } else if (currToken.tokenType == Token.ID) {
            if (symbols.get(currToken.tokenString).equals("SET")) {
                parseSetExp();
                if (currToken.tokenType == Token.EQUALS) {
                    //TODO output equals'
                    getNextToken();
                    parseSetExp();
                } else if (currToken.tokenType == Token.SUBSET) {
                    //TODO output subset
                    getNextToken();
                    
                    parseSetExp();
                }
            } else {
                parseNatExp();
                
                if (currToken.tokenType != Token.IS_IN) {
                    handleError("IS_IN");
                }
                getNextToken();
                parseSetExp();
            }
        } else {
            parseSetExp();
            if (currToken.tokenType == Token.EQUALS) {
                //TODO output equals'
                getNextToken();
                parseSetExp();
            } else if (currToken.tokenType == Token.SUBSET) {
                //TODO output subset
                getNextToken();
                parseSetExp();
            } else {
                handleError("EQUALS OR SUBSET");
            }
        }
        
    }
    
    public static void parseOut() {
        parseSetExp();
        
        if (currToken.tokenType != Token.PERIOD) {
            //TODO problem
            handleError("PERIOD");
        }
        
        getNextToken();
        
    }
    
    public static void parseIf() {
        
        if (currToken.tokenType != Token.IF) {
            //TODO expected if
            handleError("IF");
        }
        
        getNextToken();
        
        parseTest();
        
        if (currToken.tokenType != Token.THEN) {
            throw new Error("Expected Then token");
        }
        
        getNextToken();
        
        parseStList();
        
        if (currToken.tokenType == Token.ENDIF) {
            getNextToken();
        } else if (currToken.tokenType == Token.ELSE) {
            getNextToken();
            parseStList();
            if (currToken.tokenType != Token.ENDIF) {
                //TODO problem
                handleError("ENDIF");
            }
            getNextToken();
            
        } else {
            //TODO problem
            handleError("IF Poblem");
        }
    }
    
    private static void handleError(String s) {
        throw new Error(s);
    }
    
}
