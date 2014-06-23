package com.tiny_schemer.scheme_ast;

public class IntExpression extends Expression {
	private int mValue;
	
	public IntExpression(int value) {
		mValue = value;
	}
	
	public int getValue() {
		return mValue;
	}
}
