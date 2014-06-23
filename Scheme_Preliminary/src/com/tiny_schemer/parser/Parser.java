package com.tiny_schemer.parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import com.tiny_schemer.parser.token.Token;
import com.tiny_schemer.parser.token.TokenKind;
import com.tiny_schemer.scheme_ast.*;


public class Parser {
    
    public static Expression parse(List<Token> tokens) {
        if (tokens.isEmpty()) return null;
        Iterator<Token> iter = tokens.iterator();
        return parseExpression(iter, null);
    }
    
    private static Expression parseExpression(Iterator<Token> iter, Token nextToken) {
        // Outer expression parse called with nextToken = null
        // Other expressions called with nextToken = <next Token to be used>
        Expression toReturn;
        Token token;
        if (nextToken != null) token = nextToken;
        else token = iter.next();
        TokenKind kind = token.getKind();
        switch (kind) {
            case LPAREN:
                toReturn = parseSubExpression(iter);
                break;
            case INT:
                toReturn = new IntExpression(Integer.parseInt(token.toString()));
                break;
            case ID:
                toReturn = new IdExpression(token.toString());
                break;
            default:
                System.out.println("Invalid token");
                return null;
        }
        if (iter.hasNext() && nextToken == null) { // if more comes after RPAREN in outer expression...
            System.out.println("Invalid tokens after expression");
            return null;
        }
        return toReturn;
    }
    
    private static Expression parseSubExpression(Iterator<Token> iter) {
        // Takes an iterator pointing directly passed an expression's LPAREN
        // Returns the parsed expression that was wrapped in parentheses
        Token token = iter.next();
        TokenKind kind = token.getKind();
        switch (kind) {
            case ID:
                IdExpression operator = new IdExpression(token.toString());
                List<Expression> operands = parseOperands(iter);
                return new CallExpression(operator, operands);
            case IF:
                Expression condition = parseExpression(iter, iter.next());
                Expression thenBranch = parseExpression(iter, iter.next());
                Expression elseBranch = parseExpression(iter, iter.next());
                if (! iter.next().getKind().equals(TokenKind.RPAREN)) {
                    System.out.println("Too many args for if statement");
                    return null;
                }
                return new IfExpression(condition, thenBranch, elseBranch);
            case LAMBDA:
                return null; // temp
            case LET:
                HashMap<String,Expression> bindings = parseBindings(iter);
                Expression body = parseExpression(iter, iter.next());
                return new LetExpression(bindings, body);
            default:
                return null;
        }
    }
    
    private static List<Expression> parseOperands(Iterator<Token> iter) {
        List<Expression> expressions = new ArrayList<Expression>();
        Token token;
        while (! (token = iter.next()).getKind().equals(TokenKind.RPAREN)) // token = iter.next()... while token != ')'
            expressions.add(parseExpression(iter, token));
        return expressions;
    }
    
    private static HashMap<String,Expression> parseBindings(Iterator<Token> iter) {
        HashMap<String,Expression> map = new HashMap<String,Expression>();
        iter.next(); // = LPAREN starting list of bindings
        String key;
        Expression value;
        Token paren;
        while ((paren = iter.next()).getKind().equals(TokenKind.LPAREN)) { // while next token is a '(' that starts a new binding
            key = iter.next().toString();
            value = parseExpression(iter, iter.next());
            map.put(key, value);
            iter.next(); // = remove RPAREN ending the single binding
        }
        // paren should now be the RPAREN ending the list of bindings
        if (! paren.getKind().equals(TokenKind.RPAREN)) {
            System.out.println("Expected RPAREN at end of bindings.  Found " + paren + " instead.");
            return null;
        }
        return map;
    }
    
}
