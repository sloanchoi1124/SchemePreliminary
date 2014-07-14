package scheme_ast;

public class BoolExpression extends Expression {
	private boolean mValue;
	
	public BoolExpression(boolean value) {
		super();
		mValue = value;
	}
	
	public boolean getValue() {
		return mValue;
	}
}
