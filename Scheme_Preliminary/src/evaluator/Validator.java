package evaluator;

import scheme_ast.*;
import java.util.List;
import util.Pair;

/*
 * Validate a scheme program or expression by checking that every subexpression is not null.
 */

public class Validator {

	public static boolean isComplete(Program program) {
		for (DefOrExp doe : program.getProgram()) {
			if (doe instanceof Definition) {
				Definition def = (Definition)doe;
				if (def.getSymbol() == null || !isComplete(def.getBody())) {
					return false;
				}
			} else if (doe instanceof Expression) {
				if (!isComplete((Expression) doe)) {
					return false;
				}
			} else {
				throw new RuntimeException("Unexpected DefOrExp subclass");
			}
		}
		return true;
	}

	public static boolean isComplete(Expression e) {
		if (e == null) {
			return false;
		} else if (e instanceof BoolExpression || e instanceof NullExpression) {
			return true;
		}
		if (e instanceof IntExpression) {
			return ((IntExpression) e).getValue() != null;
		} else if (e instanceof StringExpression) {
			return ((StringExpression) e).toString() != null;
		} else if (e instanceof IdExpression) {
			return ((IdExpression) e).getId() != null;
		} else if (e instanceof AndExpression) {
			return isCompleteList(((AndExpression) e).getConditions());
		} else if (e instanceof OrExpression) {
			return isCompleteList(((OrExpression) e).getConditions());
		} else if (e instanceof CallExpression) {
			CallExpression call = (CallExpression) e;
			return (isComplete(call.getOperator()) && isCompleteList(call
					.getOperands()));
		} else if (e instanceof IfExpression) {
			IfExpression ifExp = (IfExpression) e;
			return (isComplete(ifExp.getCondition())
					&& isComplete(ifExp.getThen()) && isComplete(ifExp
						.getElse()));
		} else if (e instanceof CondExpression) {
			for (Pair<Expression, Expression> pair : ((CondExpression) e)
					.getAllPairs()) {
				if (pair == null || !isComplete(pair.first)
						|| !isComplete(pair.second)) {
					return false;
				}
			}
			return true;
		} else if (e instanceof ConsExpression) {
			ConsExpression consExp = (ConsExpression) e;
			return (isComplete(consExp.car()) && isComplete(consExp.cdr()));
		} else if (e instanceof LambdaExpression) {
			LambdaExpression lambda = (LambdaExpression) e;
			for (String parameter : lambda.getParameters()) {
				if (parameter == null) {
					return false;
				}
			}
			return isComplete(lambda.getBody());
		} else if (e instanceof AbstractLetExpression) {
			AbstractLetExpression letExp = (AbstractLetExpression)e;
			for(Pair<String,Expression> binding : letExp.getBindings()) {
				if (binding.first == null || !isComplete(binding.second)) {
					return false;
				}
			}
			return isComplete(letExp.getBody());
		} else if (e instanceof OperatorExpression) {
			// In principle, this case could be ignored (or just returned true)
			// since the only way OperatorExpressions should be generated are
			// internally for the initial environment of the evaluator.
			// That said, we flesh it out if only to establish a fuller
			// template for recursively traversing the expression tree.
			return ((OperatorExpression)e).getName() != null;
		}
		
		else {
			throw new RuntimeException("Unexpected Expression subclass");
		}
	}

	private static boolean isCompleteList(List<Expression> es) {
		for (Expression e : es) {
			if (!isComplete((Expression) e)) {
				return false;
			}
		}
		return true;
	}

}
