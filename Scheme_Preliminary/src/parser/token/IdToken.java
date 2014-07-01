package parser.token;

public class IdToken extends Token {

	private String mId;
	
	public IdToken(String id) {
		super(TokenKind.ID);
		mId = id;
	}
	
	@Override
	public String toString() {
		return mId;
	}

}
