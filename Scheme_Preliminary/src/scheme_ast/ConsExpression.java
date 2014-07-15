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
}
