package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import evaluator.Evaluator;
import evaluator.Mapper;
import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.*;
import unparser.Unparser;

public class Main {
	
	public static void main(String[] args) {
		String simpleSource = "(let ((modExp (lambda (base exponent modulus) (let ((modExpRec (lambda (a sq x) (if (= x 0) a (let ((newA (if (odd? x) (remainder (* a sq) modulus) a)) (newSq (remainder (* sq sq) modulus)) (newX (quotient x 2))) (modExpRec newA newSq newX)))))) (modExpRec 1 (remainder base modulus) exponent))))) (modExp 2 100 101))";
		stringTest(simpleSource);
		
		/*AstExamples examples = new AstExamples();
		for (Expression e : examples.examples) {
			astTest(e);
			
		}*/
		/*String test = "(let ((f (lambda ( x ) (+ x 1 ))) (f 5))";
		List<Token> tokens = Lexer.lex(test);
		Expression ast = Parser.parse(tokens);
		astTest(ast);*/
	}
		
	public static void stringTest(String s) {
		List<Token> tokens = Lexer.lex(s);
		System.out.println("Tokens: " + tokens);
		Expression ast = Parser.parse(tokens);
		if (ast != null) {
			String unparsed = Unparser.unparse(ast);
			System.out.println(unparsed);
			IntExpression v = Evaluator.evaluate(ast);
			System.out.println("Evaluates to: " + v.getValue());
		}
	}
	
	public static void astTest(Expression e) {
		System.out.println("Testing...");
		String unparsed = Unparser.unparse(e);
		System.out.println(unparsed + "\n");
		IntExpression v = Evaluator.evaluate(e);
		Mapper test = new Mapper();
		test.evaluate(e);
		HashMap<String, Expression> map = test.getMap();
		if (map != null) {
			System.out.printf("size of the map is : %d \n", map.size());
			for (Entry<String, Expression> item : map.entrySet()) {
				System.out.printf("this is the key: %s \n", item.getKey());
				System.out.printf("this is the mother function: %s \n", Unparser.unparse(item.getValue()));
			}
		}
			
		System.out.println("Evaluates to: " + v.getValue() + "\n");
	}
	
}
