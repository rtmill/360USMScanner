
import java.util.*;

public class setScanner {

    private Scanner src;
    private Token currToken;
    // I would do this line by line, but you could do something
    // else
    private char[] currline = {};
    private int currPos; // 0 <= currPos <= currLine.length
    // to record where we are on the current line

    public setScanner() {
        src = new Scanner(System.in);
        // load currToken with the first token
        /**
         * NEW CODE
         */
        currPos = 0;

        //Just a temp value so currToken is not null
        currToken = new Token(-1);

        this.consume();
    }

    // returns the current token w/o advancing
    public Token lookahead() {
        return currToken;
    }

    public void consume() {
        /*

         if the current token is already UNRECOGNIZED this
         operation has no effect(do it this way; another time
         I might have it discard the current character and try
         again)

         skip over WS in src until either reaches end of file
         or a non WS char(I'd do it line by line, but you may
         do it some other way)
       
         if reaches eof w/o seeing nonWS loads 
         currToken with UNRECOGNIZED
         else
         scans the src from the current non-ws position and
         loads currToken with the longest prefix that
         matches a token definition; if no prefix matches,
         loads currToken with UNRECOGNIZED

         The thing to be mindful of is when one token's definition
         is in effect a prefix of another's.  Proper prefixes of 
         reserved words and "not" and "in" are identifiers, and
         "not", "in", and reserved words, if followed immediately
         by letters or digits are identifiers, but I don't think any
         of the other token definitions have common prefixes. I think
         if you start with a letter and keep scanning as long as you
         have a letter or digit, then the result will be easy to
         classify.  The other tokens are easily distinguished by
         the first character.

         */

        //Once an UNRECOGNIZED Token is encountered we do nothing on consume().
        if (currToken.tokenType == Token.UNRECOGNIZED) {
            return;
        }

        //Next line if at end
        if (currPos >= currline.length) {
            if (src.hasNextLine()) {
                currline = src.nextLine().toCharArray();
                currPos = 0;
            } else { //No more lines
                currToken = new Token(Token.UNRECOGNIZED);
                return;
            }
        }


        while (currline.length == 0 || Character.isWhitespace(currline[currPos])) {
            if (currline.length == 0) {
                if (src.hasNextLine()) {
                    currline = src.nextLine().toCharArray();
                    currPos = 0;
                } else {
                    currToken = new Token(Token.UNRECOGNIZED);
                    return;
                }
            } else if (Character.isWhitespace(currline[currPos])) {
                currPos++;
                if (currPos >= currline.length) {
                    currline = src.nextLine().toCharArray();
                    currPos = 0;
                }
            }
        }

        int tkCode;

        switch (currline[currPos]) {
            //Letters that begin keywords.
            case 'p':
            case 'v':
            case 'b':
            case 'e':
            case 'i':
            case 'n':
            case 's':
            case 't':
                processP();
                return;
            //All single character tokens.
            case '0':
                currToken = new Token(Token.NATCONST, "0");
                currPos++;
                return;
            case '{':
                tkCode = Token.LEFTBRACE;
                break;
            case '}':
                tkCode = Token.RIGHTBRACE;
                break;
            case '(':
                tkCode = Token.LEFTPAREN;
                break;
            case ')':
                tkCode = Token.RIGHTPAREN;
                break;
            case ';':
                tkCode = Token.SEMICOLON;
                break;
            case '.':
                tkCode = Token.PERIOD;
                break;
            case ',':
                tkCode = Token.COMMA;
                break;
            case ':':
                tkCode = currline[currPos + 1] == '=' ? Token.ASSIGN : Token.UNRECOGNIZED;
                currPos++;
                break;
            case '<':
                tkCode = currline[currPos + 1] == '=' ? Token.SUBSET : Token.UNRECOGNIZED;
                currPos++;
                break;
            case '=':
                tkCode = Token.EQUALS;
                break;
            case '*':
                tkCode = Token.INTERSECTION;
                break;
            case '+':
                tkCode = Token.UNION;
                break;
            case '\\':
                tkCode = Token.SETDIFFERENCE;
                break;
            case '-':
                tkCode = Token.COMPLEMENT;
                break;
            default:
                processDefault();
                return;
        }
        Token result = new Token(tkCode);
        currToken = result;
        currPos++;
    }

    /**
     * Testing Routine
     *
     * @param args
     */
    public static void main(String[] args) {
        setScanner s = new setScanner();
        while (s.lookahead().tokenType != Token.UNRECOGNIZED) {
            System.out.print(s.currToken + " ");
            s.consume();
        }
    }

    /**
     * Called to process tokens that span multiple characters beginning with a
     * character and are not discernable from their first or second char.
     *
     */
    private void processP() {
        StringBuilder sb = new StringBuilder();

        //Append chars to the StringBuilder until a non alphanumeric char or the end of the line is encountered
        for (; currPos < currline.length; currPos++) {
            if (Character.isWhitespace(currline[currPos]) || !Character.isLetterOrDigit(currline[currPos])) {
                break;
            }
            sb.append(currline[currPos]);

        }

        String tkString = sb.toString();

        //If the string is a keyword then return the appropriate token. 
        //Else it must be an identifier.
        switch (tkString) {
            case "program":
                this.currToken = new Token(Token.PROGRAM);
                break;
            case "var":
                this.currToken = new Token(Token.VAR);
                break;
            case "begin":
                this.currToken = new Token(Token.BEGIN);
                break;
            case "end":
                this.currToken = new Token(Token.END);
                break;
            case "if":
                this.currToken = new Token(Token.IF);
                break;
            case "else":
                this.currToken = new Token(Token.ELSE);
                break;
            case "endif":
                this.currToken = new Token(Token.ENDIF);
                break;
            case "nat":
                this.currToken = new Token(Token.NAT);
                break;
            case "not":
                this.currToken = new Token(Token.NOT);
                break;
            case "set":
                this.currToken = new Token(Token.SET);
                break;
            case "in":
                this.currToken = new Token(Token.IS_IN);
                break;
            case "then":
                this.currToken = new Token(Token.THEN);
                break;
            default:
                this.currToken = new Token(Token.ID, tkString);
                break;
        }
    }

    /**
     * Method processes all tokens that begin with a nonzero digit or with a
     * char that does not also begin a keyword.
     */
    private void processDefault() {
        StringBuilder sb = new StringBuilder();

        //We are dealing with a number
        if (Character.isDigit(currline[currPos])) {
            while (currPos < currline.length && Character.isDigit(currline[currPos])) {
                sb.append(currline[currPos]);
                currPos++;
            }
            currToken = new Token(Token.NATCONST, sb.toString());
        } //We are dealing with a identifier
        else if (Character.isLetter(currline[currPos])) {
            while (currPos < currline.length && Character.isLetterOrDigit(currline[currPos])) {
                sb.append(currline[currPos]);
                currPos++;
            }
            currToken = new Token(Token.ID, sb.toString());
        } else {
            currToken = new Token(Token.UNRECOGNIZED);

        }
    }
}
