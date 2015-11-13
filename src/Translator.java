
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

    public static void parseSetExp() {

    }

    public static void parseSLevel0() {

    }

    public static void parseSLevel1() {

    }

    public static void parseSLevel2() {

    }

    public static void parseSAtomic() {

    }

    public static void parseSetConst() {

    }

    public static void parseComplemented() {

    }

    public static void parseSetLiteral() {

    }

    public static void parseNatList() {

    }

    public static void parseNeNatList() {

    }

    public static void parseNatAsgn() {

    }

    public static void parseNatExp() {

    }

    public static void parseTest() {

    }

    public static void parseTestAtomic() {

    }

    public static void parseOut() {

    }

    public static void parseIf() {

    }

}
