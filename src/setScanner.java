
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

        //Next line if at end
        if (currPos >= currline.length && src.hasNextLine()) {
            currline = src.nextLine().toCharArray();
            currPos = 0;
        } else { //No more lines
            currToken = new Token(Token.UNRECOGNIZED);
        }

        //Advance through whitespace
        while (currPos >= currline.length || Character.isWhitespace(currline[currPos])) {
            currPos++;
            if (currPos >= currline.length) {
                currline = src.nextLine().toCharArray();
                currPos = 0;
            }
        }

        //UNRECOGNIZED by default
        int tkCode = 27;

        switch (currline[currPos]) {
            case 'p':
            case 'v':
            case 'b':
            case 'e':
            case 'i':
            case 'n':
            case 's':
                processP();
                return;
            case '0':
                currToken = new Token(Token.NATCONST);
                currPos++;
                return;
            case '{':
                tkCode = 11;
                break;
            case '}':
                tkCode = 12;
                break;
            case '(':
                tkCode = 13;
                break;
            case ')':
                tkCode = 14;
                break;
            case ';':
                tkCode = 15;
                break;
            case '.':
                tkCode = 16;
                break;
            case ',':
                tkCode = 17;
                break;
            case ':':
                tkCode = currline[currPos + 1] == '=' ? 18 : 27;
                break;
            case '<':
                tkCode = currline[currPos + 1] == '=' ? 19 : 27;
                break;
            case '=':
                tkCode = 20;
                break;
            case '*':
                tkCode = 22;
                break;
            case '+':
                tkCode = 23;
                break;
            case '\\':
                tkCode = 24;
                break;
            case '-':
                tkCode = 25;
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

    private void processP() {
        StringBuilder sb = new StringBuilder();

        for (; currPos < currline.length; currPos++) {
            if (Character.isWhitespace(currline[currPos]) || !Character.isLetterOrDigit(currline[currPos])) {
                break;
            }
            sb.append(currline[currPos]);

        }

        String tkString = sb.toString();

        switch (tkString) {
            case "program":
                this.currToken = new Token(0);
                break;
            case "var":
                this.currToken = new Token(2);
                break;
            case "begin":
                this.currToken = new Token(3);
                break;
            case "end":
                this.currToken = new Token(4);
                break;
            case "if":
                this.currToken = new Token(5);
                break;
            case "else":
                this.currToken = new Token(6);
                break;
            case "endif":
                this.currToken = new Token(7);
                break;
            case "nat":
                this.currToken = new Token(8);
                break;
            case "set":
                this.currToken = new Token(9);
                break;
            default:
                this.currToken = new Token(1, tkString);
                break;
        }
    }

    private void processDefault() {
        StringBuilder sb = new StringBuilder();
        if (Character.isDigit(currline[currPos])) {
            while (currPos < currline.length && Character.isDigit(currline[currPos])) {
                sb.append(currline[currPos]);
                currPos++;
            }
            currToken = new Token(10, sb.toString());
        } else if (Character.isLetter(currline[currPos])) {
            while (currPos < currline.length && Character.isLetterOrDigit(currline[currPos])) {
                sb.append(currline[currPos]);
                currPos++;
            }
            currToken = new Token(1, sb.toString());
        }
    }
}
