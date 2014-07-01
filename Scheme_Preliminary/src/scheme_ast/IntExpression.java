package scheme_ast;

public class IntExpression extends Expression {
	private int mValue;
	
	public IntExpression(int value) {
		super();
		mValue = value;
	}
	
	public int getValue() {
		return mValue;
	}
}
