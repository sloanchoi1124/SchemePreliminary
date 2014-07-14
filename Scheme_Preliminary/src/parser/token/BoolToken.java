package parser.token;

public class BoolToken extends Token {
	
	private boolean mValue;

	public BoolToken(boolean value) {
		super(TokenKind.BOOL);
		mValue = value;
	}
	
	@Override
	public String toString() {
		return "" + mValue;
	}

}
