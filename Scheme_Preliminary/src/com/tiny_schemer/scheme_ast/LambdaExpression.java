package com.tiny_schemer.scheme_ast;

import java.util.ArrayList;

public class LambdaExpression extends Expression {
	private ArrayList<String> mParameters;
	private Expression mBody;
	
	public LambdaExpression(ArrayList<String> parameters, Expression body) {
		mParameters = parameters;
		mBody = body;
	}

	public ArrayList<String> getParameters() {
		return mParameters;
	}

	public Expression getBody() {
		return mBody;
	}
		
}
