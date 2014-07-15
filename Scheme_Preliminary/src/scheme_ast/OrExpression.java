package scheme_ast;

import java.util.List;

public class OrExpression extends Expression {

	private List<Expression> mConditions;
	
	public OrExpression(List<Expression> conditions) {
		super();
		mConditions = conditions;
	}
	
	public List<Expression> getConditions() {
		return mConditions;
	}

}
