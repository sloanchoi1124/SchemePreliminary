package scheme_ast;

import java.util.List;

public class CallExpression extends Expression {
	private Expression mOperator;
	private List<Expression> mOperands;
	
	public CallExpression(Expression operator, List<Expression> operands) {
		super();
		mOperator = operator;
		mOperands = operands;
	}
	
	public Expression getOperator() {
		return mOperator;
	}
	
	public List<Expression> getOperands() {
		return mOperands;
	}

	public void setOperator(Expression operator) {
		this.mOperator = operator;
	}
}
