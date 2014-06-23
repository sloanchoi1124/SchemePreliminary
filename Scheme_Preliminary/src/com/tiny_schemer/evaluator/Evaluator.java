package com.tiny_schemer.evaluator;

import java.util.List;

import com.tiny_schemer.scheme_ast.*;

public class Evaluator {

	public static int evaluate(Expression e) {
		if (e instanceof IntExpression) {
			return ((IntExpression) e).getValue();
		} else {
			if (e instanceof IfExpression) {
				// System.out.println("this is an ifexpression");
				return ifEval((IfExpression) e);
			} else {
				return callExpressionEval((CallExpression) e);
			}
		}
	}

	public static int ifEval(IfExpression e) {
		if (callExpressionEval((CallExpression) e.getCondition()) == 1) {
			Expression then = e.getThen();
			System.out.println("evaluating then");
			return evaluate(then);
		} else {
			Expression not = e.getElse();
			System.out.println("evaluating else");
			return evaluate(not);
		}
	}

	// evaluate callExpression, handling operators: +, -, * /, <, >, =
	public static int callExpressionEval(CallExpression e) {
		IdExpression op = (IdExpression) ((CallExpression) e).getOperator();
		String id = op.getId();
		List<Expression> items = e.getOperands();

		// operators: <, >, =
		int item1 = (getEval(items.get(0)));
		int item2 = (getEval(items.get(1)));
		if (id.equals("=")) {
			if (item1 == item2) {
				return 1;
			} else {
				return 0;
			}
		} else if (id.equals("<")) {
			if (item1 < item2) {
				return 1;
			} else {
				return 0;
			}
		} else if (id.equals(">")) {
			if (item1 > item2) {
				return 1;
			} else {
				return 0;
			}
		}

		// operators: +, -, * /
		int result = 0;
		if (id.equals("+")) {
			result = 0;
			for (Expression item : items) {
				result = result + (getEval(item));
			}
		}
		if (id.equals("-")) {
			result = getEval(items.get(0));
			for (int i = 1; i < items.size(); i++) {
				result = result - (getEval(items.get(i)));
			}
		}
		if (id.equals("*")) {
			result = 1;
			for (Expression item : items) {
				result = result * (getEval(item));
			}
		}
		if (id.equals("/")) {
			result = getEval(items.get(0));
			for (int i = 1; i < items.size(); i++) {
				result = result / (getEval(items.get(i)));
			}
		}
		return result;
	}

	public static int getEval(Expression e) {
		if (e instanceof IntExpression) {
			return ((IntExpression) e).getValue();
		} else {
			return callExpressionEval((CallExpression) e);
		}
	}

	// keeping the code for potential usage, such as checking the validity of
	// the input
	/*
	 * public static boolean isCal(CallExpression e) { if
	 * (((IdExpression)e.getOperator()).getId().equals("+")) { return true; }
	 * else if (((IdExpression)e.getOperator()).getId().equals("-")) { return
	 * true; } else if (((IdExpression)e.getOperator()).getId().equals("*")) {
	 * return true; } else if
	 * (((IdExpression)e.getOperator()).getId().equals("/")) { return true; }
	 * else if (((IdExpression)e.getOperator()).getId().equals("=")) { return
	 * false; } else if (((IdExpression)e.getOperator()).getId().equals(">")) {
	 * return false; } else if
	 * (((IdExpression)e.getOperator()).getId().equals("<")) { return false; }
	 * else { System.out.println(e.getOperator());
	 * System.out.println("Invalid operator input"); return false; } }
	 */
}
