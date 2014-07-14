package parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map.Entry;

import parser.token.*;
import scheme_ast.*;
import util.Pair;

public class Parser {
    
    public static Expression parseExpression(List<Token> tokens) {
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
            case TRUE:
            	toReturn = new BoolExpression(true);
            	break;
            case FALSE:
            	toReturn = new BoolExpression(false);
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
            	List<Pair<String, Expression>> letbindings = parseBindings(iter);
                body = parseExpression(iter, iter.next());
                if (! (token = iter.next()).getKind().equals(TokenKind.RPAREN)) {
                    System.out.println("Expected end of let call, received " + token + " instead.");
                    return null;
                }
                return new LetExpression(letbindings, body);
            case LETREC:
            	List<Pair<String, Expression>> letrecBindings = parseBindings(iter);
                body = parseExpression(iter, iter.next());
                if (! (token = iter.next()).getKind().equals(TokenKind.RPAREN)) {
                    System.out.println("Expected end of letrec call, received " + token + " instead.");
                    return null;
                }
                return new LetrecExpression(letrecBindings, body);
            case LETSTAR:
            	List<Pair<String, Expression>> letStarBindings = parseBindings(iter);
                body = parseExpression(iter, iter.next());
                if (! (token = iter.next()).getKind().equals(TokenKind.RPAREN)) {
                    System.out.println("Expected end of let* call, received " + token + " instead.");
                    return null;
                }
                return new LetStarExpression(letStarBindings, body);
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
    
    private static List<Pair<String, Expression>> parseBindings(Iterator<Token> iter) {
        // Parse bindings of a let call
    	List<Pair<String, Expression>> map = new ArrayList<Pair<String,Expression>>();
        iter.next(); // = LPAREN starting list of bindings
        String key;
        Expression value;
        Token paren;
        while ((paren = iter.next()).getKind().equals(TokenKind.LPAREN)) { // while next token is a '(' (which starts a new binding)
            key = iter.next().toString();
            value = parseExpression(iter, iter.next());
            map.add(new Pair<String, Expression>(key, value));
            iter.next(); // = iterate over the RPAREN that ends the single binding
        }
        // paren should now be the RPAREN ending the list of bindings
        if (! paren.getKind().equals(TokenKind.RPAREN)) {
            System.out.println("Expected RPAREN at end of bindings.  Found " + paren + " instead.");
            return null;
        }
        return map;
    }
    
    // -------------------------------------------------------------------------------------------
    // Definition support
    // -------------------------------------------------------------------------------------------
    
    private static List<List<Token>> separateDefOrExps(List<Token> tokens) {
        List<List<Token>> listOfLists = new ArrayList<List<Token>>();
        Iterator<Token> tokenIter = tokens.iterator();
        int currentIndex = -1;
        int parenBalance = 0;
        Token t;
        TokenKind kind;
        while (tokenIter.hasNext()) {
            if (parenBalance == 0) {
                listOfLists.add(new ArrayList<Token>());
                currentIndex++;
            }
            
            t = tokenIter.next();
            listOfLists.get(currentIndex).add(t);
            
            kind = t.getKind();
            if (kind.equals(TokenKind.LPAREN))
                parenBalance++;
            else if (kind.equals(TokenKind.RPAREN))
                parenBalance--;
        }
        return listOfLists;
    }
    
    public static Program parse(List<Token> tokens) {
        List<List<Token>> defOrExps = separateDefOrExps(tokens);
        
        List<DefOrExp> programList = new ArrayList<DefOrExp>();
        for (List<Token> list : defOrExps) {
            if (isDefinition(list))
                programList.add(parseDefinition(list));
            else // is expression
                programList.add(parseExpression(list));
        }
        return new Program(programList);
    }

    private static Definition parseDefinition(List<Token> tokens) {
        tokens.remove(0); // (
        tokens.remove(0); // define
        String symbol = tokens.remove(0).toString();
        tokens.remove(tokens.size() - 1); // )
        Expression expression = parseExpression(tokens);
        return new Definition(symbol, expression);
    }
    
    private static boolean isDefinition(List<Token> tokens) {
        return tokens.get(1).getKind().equals(TokenKind.DEFINE);
    }
    
}
