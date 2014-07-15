package parser;

import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;
import parser.token.*;

public class Lexer {
    
    public static List<Token> lex(String source) {
        List<Token> list = new LinkedList<Token>();
        
        String rVar = "\\p{Alpha}[\\p{Alnum}\\?\\*]*"; //subset of legal identifiers
        String rOp  = "[<>=*/+\\-]";
        String rNull= "('\\(\\))"; // group 1
        String rLP  = "(\\()";  // group 2
        String rRP  = "(\\))";  // group 3
        String rNum = "(\\d+)"; // group 4
        String rID  = "(" + rVar + "|" + rOp + ")"; // group 5
        String rBoolean = "(#[tf])"; // group 6
        String rComment = "(;)"; // group 7
        String rEndLine = "(\\n)"; // group 8
        Pattern pattern = Pattern.compile(rNull + "|" + rLP + "|" + rRP + "|" + 
        								  rNum + "|" + rID + "|" + rBoolean + "|" + 
        								  rComment + "|" + rEndLine);
        
        
        Scanner in = new Scanner(source);
        String token = in.findWithinHorizon(pattern, 0);
        while (token != null) {
            MatchResult result = in.match();
            if (result.group(1) != null)
            	list.add(new Token(TokenKind.NULL));
            else if (result.group(2) != null)
                list.add(new Token(TokenKind.LPAREN));
            else if (result.group(3) != null)
                list.add(new Token(TokenKind.RPAREN));
            else if (result.group(4) != null)
                list.add(new IntToken(Integer.parseInt(token)));
            else if (result.group(5) != null) {
                if (token.equals("if"))
                    list.add(new Token(TokenKind.IF));
                else if (token.equals("and"))
                	list.add(new Token(TokenKind.AND));
                else if (token.equals("or"))
                	list.add(new Token(TokenKind.OR));
                else if (token.equals("lambda"))
                    list.add(new Token(TokenKind.LAMBDA));
                else if (token.equals("let"))
                    list.add(new Token(TokenKind.LET));
                else if (token.equals("letrec"))
                	list.add(new Token(TokenKind.LETREC));
                else if (token.equals("let*"))
                	list.add(new Token(TokenKind.LETSTAR));
                else if (token.equals("define"))
                 list.add(new Token(TokenKind.DEFINE));
                else
                    list.add(new IdToken(token));
            }
            else if (result.group(6) != null) {
            	if (token.equals("#t"))
            		list.add(new Token(TokenKind.TRUE));
            	else // token equals "#f"
            		list.add(new Token(TokenKind.FALSE));
            }
            else if (result.group(7) != null) {
                while ((token = in.findWithinHorizon(pattern, 0)) != null) { // while (token = next token) != null
                    result = in.match();
                    if (result.group(8) != null)
                        break;
                }
            }
            else if (result.group(8) != null); // do nothing
            else
                System.out.println("No match for " + token);
                
            token = in.findWithinHorizon(pattern, 0);
        }
        in.close();
        return list;
    }
    
}
