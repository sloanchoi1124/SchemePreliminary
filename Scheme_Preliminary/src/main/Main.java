package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.ConsExpression;
import scheme_ast.Expression;
import scheme_ast.IntExpression;
import scheme_ast.Program;
import scheme_ast.BoolExpression;
import unparser.Unparser;
import util.Uid;
import evaluator.Evaluator;
import evaluator.Mapper;
import evaluator.TailRecOp;

public class Main {
	
	public static void main(String[] args) {
		//String simpleSource = "(let ((modExp (lambda (base exponent modulus) (let ((modExpRec (lambda (a sq x) (if (= x 0) a (let ((newA (if (odd? x) (remainder (* a sq) modulus) a)) (newSq (remainder (* sq sq) modulus)) (newX (quotient x 2))) (modExpRec newA newSq newX)))))) (modExpRec 1 (remainder base modulus) exponent))))) (modExp 2 100 101))";
		//stringTest(simpleSource);
		String cond = "(cond ((> 3 3) \"greater\") ((< 3 3) \"less\") (else \"equal\"))";
		stringTest(cond);
		String nonrec = "(let ((a 5)) (let* ((a (* 2 a)) (a (* 3 a))) a))";
		stringTest(nonrec);
		String fac = "(letrec ((fact (lambda (n) (if (= n 0) 1 (* n (fact (- n 1))))))) (fact 5))";
	    stringTest(fac);
		String let = "(let ((x 5) (y 10)) (+ x y))";
		stringTest(let);
		
		String odd = "(letrec " +
    "("
    + "(even " +
      "(lambda (n) " +
        "(if (= n 0) " +
            "1 " +
            "(if (= 1 (odd (- n 1))) " +
             "1 " +
             "0 )))) " +
     "(odd " +
      "(lambda (n) " +
        "(if (= n 1) " +
            "1 " +
            "(if (= 1 (even (- n 1))) " +
                 "1 " +
                  "0 )))) " +
     ")" +
  "(odd 51))";
		stringTest(odd);
		
		//String test = "(let ((f (lambda ( x ) (+ x 1 ))) (f 5))";
		//stringTest(test);
		
		//String andor = "(letrec ((even  (lambda (n) (or (= n 0) (odd (- n 1))))) (odd (lambda (n) (or (= n 1) (even (- n 1)))))) (odd 51))";
		//stringTest(andor);
		
		String prime_extended = "(define find-smallest-factor (lambda (n) (letrec ((odd-factor (lambda (i) (if (= 0 (remainder n i)) i (if (> (* i i) n) n (odd-factor (+ i 2))))))) (if (= 0 (remainder n 2)) 2 (odd-factor 3)))))" +

"(define definitely-prime? (lambda (n) (= n (find-smallest-factor n))))" +

"(define mod-exp (lambda (base exponent modulus) (letrec ((mod-exp-rec (lambda (a sq x) (if (= x 0) a (let ((newA (if (odd? x) (modulo (* a sq) modulus) a)) (newSq (modulo (* sq sq) modulus)) (newX (quotient x 2))) (mod-exp-rec newA newSq newX)))))) (mod-exp-rec 1 (modulo base modulus) exponent))))" +

"(define likely-prime? (lambda (n) (= (mod-exp 2 (- n 1) n) 1)))" +

"(define primes-less-n (lambda (n) (letrec ((iterate (lambda (i primes) (if (< i 2) primes (if (definitely-prime? i) (iterate (- i 1) (cons i primes)) (iterate (- i 1) primes)))))) (iterate (- n 1) '()))))"
+ "(define mylist (primes-less-n 25000)) (length mylist)";
		
		stringTest(prime_extended);
		//String testings = "(cons 4 (cons 5 (cons 5 (read))))";
		//stringTest(testings);
		//String program = fileToString();
		//stringTest(program);
	}
		
	public static void stringTest(String s) {
		List<Token> tokens = Lexer.lex(s);
		System.out.println("Tokens: " + tokens);
		Program ast = Parser.parse(tokens);
		String unparsed = Unparser.unparse(ast);
		System.out.println(unparsed);
		TailRecOp.checker(ast);
		Expression v = Evaluator.evaluate(ast);
		if (v instanceof IntExpression) {
			System.out.println("Evaluates to: " + ((IntExpression)v).getValue());
			
		} else if (v instanceof BoolExpression){
			System.out.println(((BoolExpression)v).getValue());
		} else if (v instanceof ConsExpression){
			System.out.println(((ConsExpression)v).toString());
		} else {
			System.out.println(v);
		}
		
		if (ast != null) {
//			String unparsed = Unparser.unparse(ast);
//			System.out.println(unparsed);
//			IntExpression v = Evaluator.evaluate(ast);
//			System.out.println("Evaluates to: " + v.getValue());
		}
	}
	
	public static String fileToString() {
		File file = new File("testing.txt");
		BufferedReader reader = null;
		String result = "";
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			while ((text = reader.readLine()) != null) {
				result = result + " " + text;
			}			
		}
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
		return result;
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
