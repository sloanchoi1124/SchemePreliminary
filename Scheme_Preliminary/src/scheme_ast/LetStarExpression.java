package scheme_ast;

import java.util.List;

import util.Pair;

public class LetStarExpression extends AbstractLetExpression {
	
	public LetStarExpression(List<Pair<String, Expression>> bindings, Expression body) {
		super(bindings, body);
	}
}

