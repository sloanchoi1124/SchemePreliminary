package com.tiny_schemer.parser.token;

public class IntToken extends Token {

	private int mLiteral;
	
	public IntToken(int literal) {
		super(TokenKind.INT);
		mLiteral = literal;
	}
	
	@Override
	public String toString() {
		return "" + mLiteral;
	}

}
