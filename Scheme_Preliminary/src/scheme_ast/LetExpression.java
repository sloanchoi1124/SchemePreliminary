package scheme_ast;

import java.util.List;

import util.Pair;

public class LetExpression extends AbstractLetExpression {
	
	public LetExpression(List<Pair<String, Expression>> bindings, Expression body) {
		super(bindings, body);		
	}	
}
