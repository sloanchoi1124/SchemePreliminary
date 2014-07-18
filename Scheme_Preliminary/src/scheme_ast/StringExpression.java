package scheme_ast;

import java.math.BigInteger;

public class StringExpression extends Expression{
	private String content;
	
	public StringExpression(String s) {
		super();
		this.content = s;
	}
	
	public String toString() {
		return this.content;
	}
	
	public IntExpression toInt() {
		Integer temp = Integer.valueOf(content);
		BigInteger result = BigInteger.valueOf(temp);
		return new IntExpression(result);
	}
	
}
