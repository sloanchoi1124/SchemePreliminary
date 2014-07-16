package scheme_ast;

import java.util.ArrayList;

public class LambdaExpression extends Expression {
	private ArrayList<String> mParameters;
	private Expression mBody;
	
	public LambdaExpression(ArrayList<String> parameters, Expression body) {
		super();
		mParameters = parameters;
		mBody = body;
	}

	public ArrayList<String> getParameters() {
		return mParameters;
	}

	public Expression getBody() {
		return mBody;
	}
	
	public void setBody(Expression body) {
		this.mBody = body;
	}
		
}
