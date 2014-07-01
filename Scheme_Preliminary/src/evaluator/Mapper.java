package evaluator;

/* This method takes in an expression, goes through the expression recursively. Produce a map which memorizes names of bindings which are defined 
 * in LetExpression, and their upper expression (the LetExpression that bindings are defined.)
 * 
 */
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Map.Entry;

import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import scheme_ast.OperatorExpression;

public class Mapper {
	private static HashMap<String, Expression> memo;

	public Mapper() {
		memo = new HashMap<String, Expression>();
	}

	public HashMap<String, Expression> getMap() {
		return memo;
	}

	public void evaluate(Expression e) {
		if (e instanceof IfExpression) {
			ifEval((IfExpression) e);
		} else if (e instanceof CallExpression) {
			callEval((CallExpression) e);
		} else if (e instanceof LetExpression) {
			letEval((LetExpression) e);
		} else {

		}
	}

	private void ifEval(IfExpression e) {
		if (e.getThen() instanceof LetExpression) {
			letEval(((LetExpression) e.getThen()));
		}
		if (e.getElse() instanceof LetExpression) {
			letEval(((LetExpression) e.getThen()));
		}
	}

	private void letEval(LetExpression e) {
		HashMap<String, Expression> temp = e.getBindings();
		for (Entry<String, Expression> i : temp.entrySet()) {
			System.out.println(i.getKey());
			memo.put(i.getKey(), e);
		}
	}

	private void callEval(CallExpression e) {
		List<Expression> items = e.getOperands();
		for (Expression item : items) {
			evaluate(item);
		}
	}
}
