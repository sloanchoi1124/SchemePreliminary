package scheme_ast;

import java.util.ArrayList;

import util.Pair;

public class CondExpression extends Expression{
	
	private ArrayList<Pair<Expression, Expression>> pairs;
	private Expression else_body;
	
	public CondExpression(ArrayList<Pair<Expression, Expression>> p, Expression e) {
		pairs = p;
		else_body = e;
	}

	public Pair<Expression, Expression> getPair(int n) {
		return pairs.get(n);
	}
	
	public Expression getCond(int n) {
		return pairs.get(n).first;
	}
	
	public Expression getBody(int n) {
		return pairs.get(n).second;
	}
	
	public Expression getElse(){
		return else_body;
	}
	
	
}
