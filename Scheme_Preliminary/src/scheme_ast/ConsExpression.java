package scheme_ast;

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
	
	public String toString() {
		return toString(this);
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
}
