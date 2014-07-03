package scheme_ast;

import java.util.List;

import util.Pair;

public class LetrecExpression extends AbstractLetExpression {
	public LetrecExpression(List<Pair<String, Expression>> bindings, Expression body) {
		super(bindings, body);
	}
}
