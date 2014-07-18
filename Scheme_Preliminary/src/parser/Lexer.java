package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import parser.token.*;

public class Lexer {
    
    public static List<Token> lex(String source) {
    List<Token> tokenList = new ArrayList<Token>();
    
    List<String> regEx = new ArrayList<String>();
    
    String peculiarIdentifier = "[+-\\.{3}]"; // + - ...
    String initial =  "[\\p{Alpha}!$%&*/:<=>?^_~]"; // Letters and certain symbols
    String subsequent = "[\\p{Alpha}!$%&*/:<=>?^_~\\d+-\\.@]"; // Letters, digits, and more symbols
    String identifiers = peculiarIdentifier + "|" + initial + subsequent + "*";
 
 regEx.add("'\\(\\)");  // group 1 (null)
 regEx.add("\\(");       // group 2 (left parenthesis)
 regEx.add("\\)");       // group 3 (right parenthesis)
 regEx.add("\\d+");      // group 4 (numbers)
 regEx.add(identifiers);  // group 5 (identifiers)
 regEx.add("#[tf]");     // group 6 (booleans)
 regEx.add(";.*\\n");     // group 7 (comments)
 regEx.add("\"[^\"]*\"");    // group 8 (strings)
 
 String fullRegEx = "";
 while (true) {
  fullRegEx += "(" + regEx.remove(0) + ")";
  if (regEx.isEmpty()) break;
  else fullRegEx += "|";
 }
 Pattern pattern = Pattern.compile(fullRegEx);
 
 Scanner in = new Scanner(source);
 String token = in.findWithinHorizon(pattern, 0);
 while (token != null) {
     MatchResult result = in.match();
     if (result.group(1) != null)
      tokenList.add(new Token(TokenKind.NULL));
     else if (result.group(2) != null)
         tokenList.add(new Token(TokenKind.LPAREN));
     else if (result.group(3) != null)
         tokenList.add(new Token(TokenKind.RPAREN));
     else if (result.group(4) != null)
         tokenList.add(new IntToken(token));
     else if (result.group(5) != null) {
         if (token.equals("if"))
             tokenList.add(new Token(TokenKind.IF));
         else if (token.equals("and"))
          tokenList.add(new Token(TokenKind.AND));
         else if (token.equals("or"))
          tokenList.add(new Token(TokenKind.OR));
         else if (token.equals("lambda"))
             tokenList.add(new Token(TokenKind.LAMBDA));
         else if (token.equals("let"))
             tokenList.add(new Token(TokenKind.LET));
         else if (token.equals("letrec"))
          tokenList.add(new Token(TokenKind.LETREC));
         else if (token.equals("let*"))
          tokenList.add(new Token(TokenKind.LETSTAR));
         else if (token.equals("define"))
          tokenList.add(new Token(TokenKind.DEFINE));
         else
             tokenList.add(new IdToken(token));
     }
     else if (result.group(6) != null) {
      if (token.equals("#t"))
       tokenList.add(new Token(TokenKind.TRUE));
      else // token equals "#f"
       tokenList.add(new Token(TokenKind.FALSE));
     }
     else if (result.group(7) != null); // comments, do nothing
     else if (result.group(8) != null)
            tokenList.add(new StrToken(token.substring(1, token.length()-1)));
     else
         System.out.println("No match for " + token);
             
        token = in.findWithinHorizon(pattern, 0);
    }
    in.close();
    return tokenList;
    }
    
}
