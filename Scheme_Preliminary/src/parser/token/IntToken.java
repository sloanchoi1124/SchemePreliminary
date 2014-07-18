package parser.token;

public class IntToken extends Token {

	private String mLiteral;
	
	public IntToken(String literal) {
		super(TokenKind.INT);
		mLiteral = literal;
	}
	
	@Override
	public String toString() {
		return mLiteral;
	}

}