package scheme_ast;

public class StringExpression extends Expression{
	private String content;
	
	public StringExpression(String s) {
		super();
		this.content = s;
	}
	
	public String toString() {
		return this.content;
	}
	
	
}
