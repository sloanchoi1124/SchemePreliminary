package scheme_ast;

import java.util.List;

import util.Pair;

public abstract class AbstractLetExpression extends Expression{
	
	private List<Pair<String, Expression>> mBindings; 
	private Expression mBody;
	
	public AbstractLetExpression(List<Pair<String, Expression>> bindings, Expression body) {
		super();
		mBindings = bindings;
		mBody = body;
	}
	
	public List<Pair<String, Expression>> getBindings() {
		return mBindings;
	}
	
	public Expression getBody() {
		return mBody;
	}

	public void setBody(Expression body) {
		this.mBody = body;
	}
}
