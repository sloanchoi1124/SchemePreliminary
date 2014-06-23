package com.tiny_schemer.main;

import java.util.List;

import com.tiny_schemer.evaluator.Evaluator;
import com.tiny_schemer.parser.Lexer;
import com.tiny_schemer.parser.Parser;
import com.tiny_schemer.parser.token.Token;
import com.tiny_schemer.scheme_ast.*;
import com.tiny_schemer.unparser.Unparser;



public class Main {
	
	public static void main(String[] args) {
		String simpleSource = "(if (< 3 4) (* 5 6) (- 18 7))";
		stringTest(simpleSource);
		
		AstExamples examples = new AstExamples();
		for (Expression e : examples.examples) {
			astTest(e);
		}
			
	}
		
	public static void stringTest(String s) {
		List<Token> tokens = Lexer.lex(s);
		System.out.println("Tokens: " + tokens);
		Expression ast = Parser.parse(tokens);
		if (ast != null) {
			String unparsed = Unparser.unparse(ast);
			System.out.println(unparsed);
			int v = Evaluator.evaluate(ast);
			System.out.println("Evaluates to: " + v);
		}
	}
	
	public static void astTest(Expression e) {
		System.out.println("Testing...");
		String unparsed = Unparser.unparse(e);
		System.out.println(unparsed + "\n");
//		int v = Evaluator.evaluate(e);
//		System.out.println("Evaluates to: " + v + "\n");
	}
	
}
