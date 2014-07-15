package scheme_ast;

import java.util.List;

public class AndExpression extends Expression {

	private List<Expression> mConditions;
	
	public AndExpression(List<Expression> conditions) {
		super();
		mConditions = conditions;
	}
	
	public List<Expression> getConditions() {
		return mConditions;
	}

}
