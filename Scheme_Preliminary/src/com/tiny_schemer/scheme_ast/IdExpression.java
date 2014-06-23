package com.tiny_schemer.scheme_ast;

public class IdExpression extends Expression {
	private String mId;
	
	public IdExpression(String id) {
		mId = id;
	}

	public String getId() {
		return mId;
	}
	
}
