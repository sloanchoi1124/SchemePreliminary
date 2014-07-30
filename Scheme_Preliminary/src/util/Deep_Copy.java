package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import scheme_ast.*;

public class Deep_Copy {

	public static Definition deepCopy(Definition def) {
		return new Definition(def.getSymbol(), (Expression) deepCopy(def.getBody()));
	}
	
	public static Expression deepCopy(Expression exp) {		
		if (exp instanceof BoolExpression)
			return new BoolExpression(((BoolExpression) exp).getValue());
		else if (exp instanceof ConsExpression) {
			ConsExpression cons = (ConsExpression) exp;
			return new ConsExpression(cons.car(), cons.cdr());
		}
		else if (exp instanceof IdExpression)
			return new IdExpression(((IdExpression) exp).getId());
		else if (exp instanceof IntExpression)
			return new IntExpression(((IntExpression) exp).getValue());
		else if (exp instanceof NullExpression)
			return new NullExpression();
		else if (exp instanceof OperatorExpression) return null; // ???
		
		else if (exp instanceof StringExpression)
			return new StringExpression(((StringExpression) exp).toString());
		else if (exp instanceof AndExpression) {
			AndExpression and = (AndExpression) exp;
			return new AndExpression(deepCopy(and.getConditions()));
		}
		else if (exp instanceof CallExpression) {
			CallExpression call = (CallExpression) exp;
			return new CallExpression(deepCopy(call.getOperator()), deepCopy(call.getOperands()));
		}
		else if (exp instanceof CondExpression) {
			CondExpression cond = (CondExpression) exp;
			ArrayList<Pair<Expression, Expression>> oldList = cond.getAllPairs();
			ArrayList<Pair<Expression, Expression>> copiedList = new ArrayList<Pair<Expression, Expression>>();
			Iterator<Pair<Expression, Expression>> iter = oldList.iterator();
			if (! iter.hasNext())
				return null;
			else {
				Pair<Expression, Expression> pair = iter.next();
				while (iter.hasNext()) {
					copiedList.add(new Pair<Expression, Expression>(deepCopy(pair.first), deepCopy(pair.second)));
					pair = iter.next();
				}
				return new CondExpression(copiedList, pair.second);
			}
		}
		else if (exp instanceof IfExpression) {
			IfExpression ifExp = (IfExpression) exp;
			return new IfExpression(deepCopy(ifExp.getCondition()),
									deepCopy(ifExp.getThen()),
									deepCopy(ifExp.getElse()));
		}
		else if (exp instanceof LambdaExpression) {
			LambdaExpression lambda = (LambdaExpression) exp;
			ArrayList<String> copiedBindings = new ArrayList<String>(lambda.getParameters());
			return new LambdaExpression(copiedBindings, deepCopy(lambda.getBody()));
		}
		else if (exp instanceof AbstractLetExpression) {
			AbstractLetExpression ale = (AbstractLetExpression) exp;
			ArrayList<Pair<String, Expression>> copiedBindings = new ArrayList<Pair<String, Expression>>();
			for (Pair<String, Expression> pair : ale.getBindings()) {
				copiedBindings.add(new Pair<String, Expression>(pair.first, deepCopy(pair.second)));
			}
			if (ale instanceof LetExpression)
				return new LetExpression(copiedBindings, ale.getBody());
			else if (ale instanceof LetrecExpression)
				return new LetrecExpression(copiedBindings, ale.getBody());
			else if (ale instanceof LetStarExpression)
				return new LetStarExpression(copiedBindings, ale.getBody());
			else return null; // unreachable
		}
		else if (exp instanceof OrExpression) {
			OrExpression or = (OrExpression) exp;
			return new AndExpression(deepCopy(or.getConditions()));
		}
		else
			return null; // shouldn't ever get here
	}

	private static List<Expression> deepCopy(List<Expression> oldList) {
		List<Expression> copiedList = new ArrayList<Expression>();
		for (Expression e : oldList) {
			copiedList.add((Expression) deepCopy(e));
		}
		return copiedList;
	}
	
}
