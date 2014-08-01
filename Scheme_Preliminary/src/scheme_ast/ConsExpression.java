package scheme_ast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import evaluator.Evaluator;
import unparser.Unparser;
import util.Pair;

public class ConsExpression extends Expression{
	
	private Pair<Expression, Expression> cell;

	public ConsExpression(Expression e1, Expression e2) {
		super();
		this.cell = new Pair(e1, e2);
	}
	
	public Expression car() {
		return this.cell.first;
	}
	
	public Expression cdr() {
		return this.cell.second;
	}
	
	public BigInteger length() {
		BigInteger result = BigInteger.valueOf(length(this, 1));
		return result;
	}
	
	private static int length(ConsExpression c, int helper) {
		while (! (c.cdr() instanceof NullExpression)) {
			helper ++;
			c = (ConsExpression) c.cdr();
		}
		return helper;
	}
	
	public ConsExpression reverse() {
		return reverse(this);
	}
	
	private ConsExpression reverse(ConsExpression c) {
		ConsExpression temp = c;
		ConsExpression result = new ConsExpression(temp.car(), new NullExpression());		
		while (! (temp.cdr() instanceof NullExpression)) {
			temp = (ConsExpression) temp.cdr();
			result = new ConsExpression(temp.car(), result);			
		}
		return result;
	}
	
	public static ConsExpression append(ConsExpression items1, ConsExpression items2) {
		ConsExpression temp = items1;		
		while (! (temp.cdr() instanceof NullExpression)) {
			items2 = new ConsExpression(temp.car(), items2);
			temp = (ConsExpression) temp.cdr();
		}
		items2 = new ConsExpression(temp.car(), items2);
		return items2;
	}
	
	public String toString() {
		return toString(this);
	}
	
	public ConsExpression list() {
		return null;
	}
	
	public String toString(ConsExpression c) {
		String result = "";
		if (c.car() instanceof IntExpression) {
			result = result + ((IntExpression)c.car()).getValue();
		} else if (c.car() instanceof StringExpression) {
			result = result + ((StringExpression)c.car()).toString();
		} else {
			result = result + c.car().toString();
		}
		
		if (c.cdr() instanceof ConsExpression) {
			return result + " " + toString((ConsExpression)c.cdr());
		} else {
			if (c.cdr() instanceof IntExpression) {
				result = result + " " + ((IntExpression)c.cdr()).getValue();
			} else if (c.cdr() instanceof StringExpression) {
				result = result + " " + ((StringExpression)c.cdr()).toString();
			} else {
				result = result + " " +  c.cdr().toString();
			}
			return result;
		}		
	}
	
	public static void main(String[] args) {
		String s = "(let ( ( l (cons 1 (cons 2 (cons 3 (cons 4 '())))))) (reverse l))";
		//String s = "(append (cons 1 (cons 2 (cons 3 (cons 4 '() )))) (cons 1 '()))";
		List<Token> tokens = Lexer.lex(s);
		System.out.println("Tokens: " + tokens);
		Program ast = Parser.parse(tokens);
		String unparsed = Unparser.unparse(ast);
		System.out.println(unparsed);
		Expression v = Evaluator.evaluate(ast);
		System.out.println("final result, evaluates to :  " + Unparser.unparse(v));
	}
}
