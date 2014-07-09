package testing;

import java.util.List;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.Expression;
import unparser.ShallowUnparser;
//FOR TESTING CLASS
//this is a class where you can testing.java(or other java files if needed) as java application

//In order to run testing.java itself as an java application
//1) right click -> properties ->run/debug settings-> 
//2) if there's testing already, click edit
     //if there's not, click add->choose java application
//3) go to class path, remove the Android 4.4 library
//4) click run
//5) In order to run the android application after running the java application, run configuration may need to be edited
public class testing {
	public static void main(String args[])
	{
		//String simpleSource = "(let ((modExp (lambda (base exponent modulus) (let ((modExpRec (lambda (a sq x) (if (= x 0) a (let ((newA (if (odd? x) (remainder (* a sq) modulus) a)) (newSq (remainder (* sq sq) modulus)) (newX (quotient x 2))) (modExpRec newA newSq newX)))))) (modExpRec 1 (remainder base modulus) exponent))))) (modExp 2 100 101))";
		String simpleSource="(if (< (+ 1 2) 4) (let ((a 1) (b 2) (c 3)) (+ a b c)) (let ((x (lambda (a b c) (* a b c)))) (x 1 2 3)))";
		//main.Main.stringTest(simpleSource);
		List<Token> tokens = Lexer.lex(simpleSource);
		Expression ast = Parser.parse(tokens);
		System.out.println(ShallowUnparser.shallowUnparse(ast, 0));
		System.out.println(ShallowUnparser.shallowUnparse(ast, 1));
		System.out.println(ShallowUnparser.shallowUnparse(ast, 2));
		System.out.println(ShallowUnparser.shallowUnparse(ast, 3));
		System.out.println(ShallowUnparser.shallowUnparse(ast, 4));
		System.out.println(ShallowUnparser.shallowUnparse(ast, 5));
		System.out.println(ShallowUnparser.shallowUnparse(ast, 6));
	}
}
