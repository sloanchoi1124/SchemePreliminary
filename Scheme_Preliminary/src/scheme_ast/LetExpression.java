package scheme_ast;

import java.util.HashMap;

public class LetExpression extends Expression {
	
	private HashMap<String,Expression> mBindings; //TODO: this should be a list of pairs
	private Expression mBody;
	
	public LetExpression(HashMap<String, Expression> bindings, Expression body) {
		super();
		mBindings = bindings;
		mBody = body;
	}
	
	public HashMap<String, Expression> getBindings() {
		return mBindings;
	}
	
	public Expression getBody() {
		return mBody;
	}
	
	
}
