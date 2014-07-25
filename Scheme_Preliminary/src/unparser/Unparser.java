package unparser;

import scheme_ast.*;
import util.Pair;

import java.util.*;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;

public class Unparser {
	
	public static String unparse(Expression e) {
		List<DefOrExp> l = new ArrayList<DefOrExp>();
		l.add(e);
		Program temp = new Program(l);
		return unparse(temp);
	}

	public static String unparse(Program ast) {
		Iterator<DefOrExp> itr = ast.getProgram().iterator();
		DefOrExp temp;
		String s;
		String result = "";
		while (itr.hasNext()) {
			temp = itr.next();
			s = unparse(temp, new Stack<Integer>());
			result = result + "\n" + s;
		}
		return result;
	}
	

	public static String unparse(DefOrExp ast, Stack<Integer> indent_length) {
		String result = "";

		if (ast instanceof IntExpression) {
			result += ((IntExpression) ast).getValue().toString()
					+ " ";
		}
		
		else if (ast instanceof StringExpression) {
			result += ((StringExpression) ast).toString();
			result="\""+result+"\""+" ";
		}

		else if (ast instanceof IdExpression) {
			result += ((IdExpression) ast).getId() + " ";
		}
		else if(ast instanceof BoolExpression)
		{
			if(((BoolExpression) ast).getValue()==true)
				result+="#t"+" ";
			else
				result+="#f"+" ";
		}
		
		else if (ast instanceof OrExpression) {
			result += "(or ";
			for(Expression e:((OrExpression) ast).getConditions())
				result+=Unparser.unparse(e,indent_length)+" ";
			result+=")";
		}
		
		else if (ast instanceof AndExpression) {
			result+="(and ";
			for(Expression e:((AndExpression) ast).getConditions())
				result+=Unparser.unparse(e,indent_length)+" ";
			result+=")";
		}

		else if (ast instanceof CallExpression) {
			result+="("+ unparse(((CallExpression) ast).getOperator(),indent_length);
			for (Expression expression : ((CallExpression) ast).getOperands()) {
				result+=(unparse(expression, indent_length));
			}
			result+=")";
		}

		else if (ast instanceof IfExpression) {
			String indent_space="";
			if (indent_length.isEmpty() == true)
				indent_length.push(("(if ").length());
			else {
				indent_length.push(indent_length.peek() + ("(if ").length());
			}
			for (int i = 0; i < indent_length.peek(); i++)
				indent_space += " ";
			result += "(if "
					+ unparse(((IfExpression) ast).getCondition(),indent_length) + "\n" 
					+ indent_space+ unparse(((IfExpression) ast).getThen(), indent_length)+ "\n" 
					+ indent_space+ unparse(((IfExpression) ast).getElse(), indent_length)+ " )";
			indent_length.pop();
		}

		else if (ast instanceof LambdaExpression) {
			String indent_space = "";
			if (indent_length.isEmpty() == true)
				indent_length.push(("(lambda ").length());
			else
				indent_length.push(indent_length.peek() + ("(lambda ").length());
			for (int i = 0; i < indent_length.peek(); i++)
				indent_space += " ";

			result += "(lambda ( ";
			for (String s : ((LambdaExpression) ast).getParameters()) {
				result += s + " ";
			}
			result+= ")\n";
			result+= indent_space+ unparse(((LambdaExpression) ast).getBody(), indent_length)+" )\n";
			indent_length.pop();
		}

		else if (ast instanceof LetExpression) {
			String indent_space = "";
			if (indent_length.isEmpty() == true)
				indent_length.push(("(let ").length());
			else
				indent_length.push(indent_length.peek() + ("(let ").length());
			for (int i = 0; i < indent_length.peek(); i++)
				indent_space += " ";

			result += "(let (\n";

			for (Pair<String, Expression> i : ((LetExpression) ast).getBindings()) 
				result += indent_space + " ( " + i.first + " "+unparse(i.second, indent_length) + " )\n";

			result += indent_space + ")\n" 
			         + indent_space+ unparse(((LetExpression) ast).getBody(), indent_length)+ " )";
			indent_length.pop();
		}
		
		else if (ast instanceof LetrecExpression) {
			String indent_space = "";
			if (indent_length.isEmpty() == true)
				indent_length.push(("(letrec ").length());
			else
				indent_length.push(indent_length.peek() + ("(letrec ").length());

			for (int i = 0; i < indent_length.peek(); i++)
				indent_space += " ";

			result += "(letrec (\n";

			for (Pair<String, Expression> i : ((LetrecExpression) ast).getBindings()) 
				result += indent_space + " ( " + i.first + " "+unparse(i.second, indent_length) + " )\n";
			result += indent_space + ")\n" 
			         + indent_space+ unparse(((LetrecExpression) ast).getBody(), indent_length)+ " )";
			indent_length.pop();
		}
		
		else if (ast instanceof LetStarExpression) {
			String indent_space = "";
			if (indent_length.isEmpty() == true)
				indent_length.push(("(let* ").length());
			else
				indent_length.push(indent_length.peek() + ("(let* ").length());

			for (int i = 0; i < indent_length.peek(); i++)
				indent_space += " ";

			result += "(let* (\n";

			for (Pair<String, Expression> i : ((LetStarExpression) ast).getBindings()) 
				result += indent_space + " ( " + i.first + " "+unparse(i.second, indent_length) + " )\n";
			result += indent_space + ")\n" 
			         + indent_space+ unparse(((LetStarExpression) ast).getBody(), indent_length)+ " )";
			indent_length.pop();
		}
		
		else if (ast instanceof Definition) {
			String indent_space="";
			if(indent_length.isEmpty()==true)
				indent_length.push("(define ".length());
			else
				indent_length.push(indent_length.peek()+"(define ".length());
			for (int i = 0; i < indent_length.peek(); i++)
				indent_space += " ";
			result="(define "+"\n"
				+indent_space+((Definition)ast).getSymbol()+" "+"\n"
				+indent_space+Unparser.unparse(((Definition) ast).getBody(),indent_length)+")";
			indent_length.pop();
		}
		
		else if(ast instanceof CondExpression)
		{
			String indent_space="";
			if(indent_length.isEmpty()==true)
				indent_length.push("(cond ".length());
			else
				indent_length.push(indent_length.peek()+"(cond ".length());
			for (int i = 0; i < indent_length.peek(); i++)
				indent_space += " ";
			result="(cond \n";
			for(int i=0;i<((CondExpression)ast).getSize()-1;i++)
			{
				result+=indent_space+"("+unparse(((CondExpression)ast).getCond(i),indent_length)+"\n"
			            +" "+indent_space+unparse(((CondExpression)ast).getBody(i),indent_length)+")"+"\n";
			}
			result+=indent_space+"(else "+"\n"
			      +" "+indent_space+unparse(((CondExpression)ast).getBody(((CondExpression) ast).getSize()-1),indent_length)+")";
			result+=")";
			indent_length.pop();
		}
		else if(ast instanceof ConsExpression)
		{
			result+="(cons "+Unparser.unparse(((ConsExpression)ast).car())
					+" "+Unparser.unparse(((ConsExpression)ast).cdr())+" )";
		}
		else if(ast instanceof NullExpression)
		{
			result+="'()";
		}

		return result;
	}
/*
	public static void main(String args[])
	{
		String prime_extended = "(define find-smallest-factor (lambda (n) (letrec ((odd-factor (lambda (i) (if (= 0 (remainder n i)) i (if (> (* i i) n) n (odd-factor (+ i 2))))))) (if (= 0 (remainder n 2)) 2 (odd-factor 3)))))" +

		"(define definitely-prime? (lambda (n) (= n (find-smallest-factor n))))" +

		"(define mod-exp (lambda (base exponent modulus) (letrec ((mod-exp-rec (lambda (a sq x) (if (= x 0) a (let ((newA (if (odd? x) (modulo (* a sq) modulus) a)) (newSq (modulo (* sq sq) modulus)) (newX (quotient x 2))) (mod-exp-rec newA newSq newX)))))) (mod-exp-rec 1 (modulo base modulus) exponent))))" +

		"(define likely-prime? (lambda (n) (= (mod-exp 2 (- n 1) n) 1)))" +

		"(define primes-less-n (lambda (n) (letrec ((iterate (lambda (i primes) (if (< i 2) primes (if (definitely-prime? i) (iterate (- i 1) (cons i primes)) (iterate (- i 1) primes)))))) (iterate (- n 1) '()))))"
		+ "(definitely-prime? 20) (primes-less-n 50)";
		//String toParse1="(define square (lambda (x) (* x x)))";
		String toParse1="(let ((x (lambda (n) (letrec ((odd-factor (lambda (i) (if (= 0 (remainder n i)) i (if (> (* i i) n) n (odd-factor (+ i 2))))))) (if (= 0 (remainder n 2)) 2 (odd-factor 3))))) (y (lambda (t) (= t (+ 1 t)))) (THREE 3) (z (lambda (m) (* m m)))) (z THREE)) (define THREE 3) (define square (lambda (x) (* x x))) (square THREE) (define sumSquares (lambda (a b) (+ (square a) (square b)))) (sumSquares THREE 4) (define factorial (lambda (n) (if (= n 0) 1 (* n (factorial (- n 1)))))) (factorial THREE)";
		//String toParse1="(cond ((> 3 3) (+ 1 1)) ((< 3 3) (+ 1 1)) (else (+ 1 1)))";
		String cons="(cons 0 (cons 1 2))";
		String bools="(boolean? #f)";
		String temp="(define (f x) (+ x 1))";
		String andOr = "(define temp (lambda (n) (cond ( (or (> n 3) (< n 3) ) \"not 3\") (else \"equal to 3\") ) ) )";
		//String andOr="(or (> x 3) (< x 3))";

		String tempp=
				"(define smallest" 
				 +" (lambda (l c) "
						+"(cond ((null? l) c)"    
						 +"((> c (car l)) (smallest l (car l)))"         
						  +"(else (smallest (cdr l) c)))))"     

						+"(define remove"
						 +"(lambda (l r)" 
						    +"(cond ((null? l) l)"
						          +"((= (car l) r) (cdr l))"
						    +"(else (cons (car l)(remove (cdr l) r))))))"
						          +"\n1\n"
						          +"(letrec"
						    +"((selection (lambda (items)"
						               +"(if (null? items) '()"
						                  +"(cons (smallest items (car items)) (selection (remove items (smallest items (car items)))))))))" 
						 +"(selection '(20 1000000 4 5 10 19 2 )))"; 
		String t3="(define sublist (lambda (items begin stop steps)(cond ((null? items) items)"
                  +"((< steps begin) (sublist (cdr items) begin stop (+ steps 1)))"
			+" ((> steps stop) '()) (else (cons (car items) (sublist (cdr items) begin stop (+ steps 1)))))))	";		 
		List<Token> tokens1 = Lexer.lex(toParse1);
		System.out.println(tokens1);
		Program program1=Parser.parse(tokens1);
		String test1=Unparser.unparse(program1);
		System.out.println(test1);
		tokens1=Lexer.lex(test1);
		System.out.println(tokens1);
		program1=Parser.parse(tokens1);
		String test2=Unparser.unparse(program1);
		List<Token> tokens2=Lexer.lex(test2);
		Program program2=Parser.parse(tokens2);
		String test3=Unparser.unparse(program1);
		
		
		
		System.out.println(test3.equals(test2));
		System.out.println(test1);
		System.out.println(test2);
		System.out.println(diffLocation(test1,test2));
		System.out.printf("%d  %d",test3.length(),test2.length());
	}

	private static int diffLocation(String s1, String s2) {
		int len = s2.length() > s1.length() ? s1.length() : s2.length();
		for (int i = 0; i < len; i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				System.out.println(s1.charAt(i) + " != " + s2.charAt(i));
				return i;
			}
		}
		if (s1.length() > s2.length() || s1.length() < s2.length()) {
			System.out.println("Diff lengths");
			return len+1;
		}
		return -1;
	}
*/
}
