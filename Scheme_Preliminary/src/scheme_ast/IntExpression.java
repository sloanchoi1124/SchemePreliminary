package scheme_ast;

import java.math.BigInteger;

public class IntExpression extends Expression {
	private BigInteger mValue;
	
	public IntExpression(BigInteger value) {
		super();
		mValue = value;
	}
	
	public BigInteger getValue() {
		return mValue;
	}
}
