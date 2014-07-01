package parser.token;

public class Token {
	private TokenKind mKind;
	
	public Token(TokenKind kind) {
		mKind = kind;
	}
	
	public String toString() {
		return mKind.toString();
	}
	
	public TokenKind getKind() {
		return mKind;
	}
}
