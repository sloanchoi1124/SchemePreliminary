package parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import parser.token.*;
import scheme_ast.*;

public class Parser {
    
    public static Expression parse(List<Token> tokens) {
        if (tokens.isEmpty()) return null;
        Iterator<Token> iter = tokens.iterator();
        return parseExpression(iter, null);
    }
    
    private static Expression parseExpression(Iterator<Token> iter, Token nextToken) {
        // Returns parsed Expression if given valid list of tokens
        // Otherwise currently returns null
        
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
            System.out.println("Invalid token " + iter.next() + " after expression");
            return null;
        }
        return toReturn;
    }
    
    private static Expression parseSubExpression(Iterator<Token> iter) {
        // Takes an iterator pointing directly passed an expression's LPAREN
        // Returns the parsed expression that was wrapped in parentheses
        Token token = iter.next();
        TokenKind kind = token.getKind();
        Expression body;
        
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
            case LAMBDA: ;
                if (! iter.next().getKind().equals(TokenKind.LPAREN)) {
                    System.out.println("Expected '(' after 'lambda', received " + token + " instead.");
                    return null;
                }
                ArrayList<String> parameters = new ArrayList<String>();
                while (! (token = iter.next()).getKind().equals(TokenKind.RPAREN)) // while next token doesn't close list of params
                    parameters.add(token.toString());
                body = parseExpression(iter, iter.next());
                if (! (token = iter.next()).getKind().equals(TokenKind.RPAREN)) {
                    System.out.println("Expected end of lambda call, received " + token + " instead.");
                    return null;
                }
                return new LambdaExpression(parameters, body);
            case LET:
                HashMap<String,Expression> bindings = parseBindings(iter);
                body = parseExpression(iter, iter.next());
                if (! (token = iter.next()).getKind().equals(TokenKind.RPAREN)) {
                    System.out.println("Expected end of let call, received " + token + " instead.");
                    return null;
                }
                return new LetExpression(bindings, body);
            default:
                return null;
        }
    }
    
    private static List<Expression> parseOperands(Iterator<Token> iter) {
        // Parse operands of a function call
        List<Expression> expressions = new ArrayList<Expression>();
        Token token;
        while (! (token = iter.next()).getKind().equals(TokenKind.RPAREN)) // token = iter.next()... while token != ')'
            expressions.add(parseExpression(iter, token));
        return expressions;
    }
    
    private static HashMap<String,Expression> parseBindings(Iterator<Token> iter) {
        // Parse bindings of a let call
        HashMap<String,Expression> map = new HashMap<String,Expression>();
        iter.next(); // = LPAREN starting list of bindings
        String key;
        Expression value;
        Token paren;
        while ((paren = iter.next()).getKind().equals(TokenKind.LPAREN)) { // while next token is a '(' (which starts a new binding)
            key = iter.next().toString();
            value = parseExpression(iter, iter.next());
            map.put(key, value);
            iter.next(); // = iterate over the RPAREN that ends the single binding
        }
        // paren should now be the RPAREN ending the list of bindings
        if (! paren.getKind().equals(TokenKind.RPAREN)) {
            System.out.println("Expected RPAREN at end of bindings.  Found " + paren + " instead.");
            return null;
        }
        return map;
    }
    
}
