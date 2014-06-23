package com.tiny_schemer.scheme_ast;

public class IfExpression extends Expression {
	private Expression mCondition, mThen, mElse;
	
	public IfExpression(Expression condition, Expression thenBranch, Expression elseBranch) {
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
	
	
}
