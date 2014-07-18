package parser.token;

public class StrToken extends Token {
	
	private String mString;

	public StrToken(String string) {
		super(TokenKind.STR);
		mString = string;
	}
	
	@Override
	public String toString() {
		return mString;
	}

}