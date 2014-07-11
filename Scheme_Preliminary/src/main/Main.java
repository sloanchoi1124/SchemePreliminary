package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.Expression;
import scheme_ast.IntExpression;
import scheme_ast.Program;
import unparser.Unparser;
import util.Uid;
import evaluator.Evaluator;
import evaluator.Mapper;

public class Main {
	
	public static void main(String[] args) {
		//String simpleSource = "(let ((modExp (lambda (base exponent modulus) (let ((modExpRec (lambda (a sq x) (if (= x 0) a (let ((newA (if (odd? x) (remainder (* a sq) modulus) a)) (newSq (remainder (* sq sq) modulus)) (newX (quotient x 2))) (modExpRec newA newSq newX)))))) (modExpRec 1 (remainder base modulus) exponent))))) (modExp 2 100 101))";
		//stringTest(simpleSource);
		
		String nonrec = "(let ((a 5)) (let* ((a (* 2 a)) (a (* 3 a))) a))";
		stringTest(nonrec);
		String let = "(let ((x 5) (y 10)) (+ x y))";
		stringTest(let);
		String fac = "(let* ((fact (lambda (n) (if (= n 0) 1 (* n (fact (- n 1))))))) (fact 5))";
		stringTest(fac);
		/*AstExamples examples = new AstExamples();
		for (Expression e : examples.examples) {
			astTest(e);
			
		}*/
		//String test = "(let ((f (lambda ( x ) (+ x 1 ))) (f 5))";
		//stringTest(test);
	}
		
	public static void stringTest(String s) {
		List<Token> tokens = Lexer.lex(s);
		System.out.println("Tokens: " + tokens);
		Program ast = Parser.parse(tokens);
		IntExpression v = Evaluator.evaluate(ast);
		System.out.println("Evaluates to: " + v.getValue());
		if (ast != null) {
//			String unparsed = Unparser.unparse(ast);
//			System.out.println(unparsed);
//			IntExpression v = Evaluator.evaluate(ast);
//			System.out.println("Evaluates to: " + v.getValue());
		}
	}
	
	public static void astTest(Expression e) {
		System.out.println("Testing...");
//		String unparsed = Unparser.unparse(e);
//		System.out.println(unparsed + "\n");
//		IntExpression v = Evaluator.evaluate(e);
		HashMap<Uid, HashSet<String>> map = Mapper.getMap(e);
		if (map != null) {
			System.out.printf("size of the map is : %d \n", map.size());
			for (Entry<Uid, HashSet<String>> item : map.entrySet()) {
				System.out.printf("this is the key: %s \n", item.getKey());
				System.out.printf("this is the string set: %s \n", item.getValue());
			}
		}
			
//		System.out.println("Evaluates to: " + v.getValue() + "\n");
	}
	
}
