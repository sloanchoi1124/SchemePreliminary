package com.tiny_schemer.parser;

import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;

import com.tiny_schemer.parser.token.*;

public class Lexer {
    
    public static List<Token> lex(String source) {
        List<Token> list = new LinkedList<Token>();
        
        String rVar = "\\p{Alpha}[\\p{Alpha}\\d]*";
        String rOp  = "[<>=*/+\\-]";
        String rLP  = "(\\()";  // group 1
        String rRP  = "(\\))";  // group 2
        String rNum = "(\\d+)"; // group 3
        String rID  = "(" + rVar + "|" + rOp + ")"; // group 4
        Pattern pattern = Pattern.compile(rLP + "|" + rRP + "|" + rNum + "|" + rID);
        
        
        Scanner in = new Scanner(source);
        String token = in.findWithinHorizon(pattern, 0);
        while (token != null) {
            MatchResult result = in.match();
            if (result.group(1) != null)
                list.add(new Token(TokenKind.LPAREN));
            else if (result.group(2) != null)
                list.add(new Token(TokenKind.RPAREN));
            else if (result.group(3) != null)
                list.add(new IntToken(Integer.parseInt(token)));
            else if (result.group(4) != null) {
                if (token.equals("if"))
                    list.add(new Token(TokenKind.IF));
                else if (token.equals("lambda"))
                    list.add(new Token(TokenKind.LAMBDA));
                else if (token.equals("let"))
                    list.add(new Token(TokenKind.LET));
                else
                    list.add(new IdToken(token));
            }
            else System.out.println("No match for " + token);
            token = in.findWithinHorizon(pattern, 0);
        }
        in.close();
        return list;
    }
    
}
