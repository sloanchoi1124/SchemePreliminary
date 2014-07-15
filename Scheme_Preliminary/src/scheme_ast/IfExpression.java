package scheme_ast;

public class IfExpression extends Expression {
	private Expression mCondition, mThen, mElse;
	
	public IfExpression(Expression condition, Expression thenBranch, Expression elseBranch) {
		super();
		mCondition = condition;
		mThen = thenBranch;
		mElse = elseBranch;
	}

	public Expression getCondition() {
		return mCondition;
	}

	public Expression getThen() {
		return mThen;
	}

	public Expression getElse() {
		return mElse;
	}

	public void setCondition(Expression condition) {
		this.mCondition = condition;
	}

	public void setThen(Expression then) {
		this.mThen = then;
	}

	public void setElse(Expression elseExp) {
		this.mElse = elseExp;
	}
	
	
}
