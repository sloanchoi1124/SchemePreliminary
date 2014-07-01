package scheme_ast;

public class IdExpression extends Expression {
	private String mId;
	
	public IdExpression(String id) {
		super();
		mId = id;
	}

	public String getId() {
		return mId;
	}
	
}
