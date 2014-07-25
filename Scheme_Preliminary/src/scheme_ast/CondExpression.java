package scheme_ast;

import java.util.ArrayList;

import util.Pair;

public class CondExpression extends Expression{
	
	private ArrayList<Pair<Expression, Expression>> pairs;
	
	public CondExpression(ArrayList<Pair<Expression, Expression>> p, Expression e) {
		pairs = p;
		pairs.add(new Pair(new BoolExpression(true), e));
	}

	public Pair<Expression, Expression> getPair(int n) {
		return pairs.get(n);
	}
	
	public ArrayList<Pair<Expression,Expression>> getAllPairs()
	{
		return this.pairs;
	}
	
	public Expression getCond(int n) {
		return pairs.get(n).first;
	}
	
	public Expression getBody(int n) {
		return pairs.get(n).second;
	}

	public int getSize() {
		return this.pairs.size();
	}
	
	
}
