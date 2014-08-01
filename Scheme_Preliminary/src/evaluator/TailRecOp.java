package evaluator;

import java.util.Iterator;

import scheme_ast.CallExpression;
import scheme_ast.CondExpression;
import scheme_ast.DefOrExp;
import scheme_ast.Definition;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import scheme_ast.LetStarExpression;
import scheme_ast.LetrecExpression;
import scheme_ast.Program;
import unparser.Unparser;
import util.Pair;

public class TailRecOp {

	public static void checker(Program p) {
		Iterator<DefOrExp> itr = p.getProgram().iterator();
		DefOrExp temp;
		String s = "";
		boolean isTail = false;
		while (itr.hasNext()) {
			temp = itr.next();
			if (temp instanceof Definition) {
				if (((Definition) temp).getBody() instanceof LambdaExpression) {
					s = ((Definition) temp).getSymbol();
					isTail = true;
					lambdaChecker((LambdaExpression) ((Definition) temp).getBody(), s, isTail);
					s = "";
				} else {
					checker(((Definition) temp).getBody(), s, isTail);
				}
			} else {
				checker((Expression) temp, s, isTail);
			}
		}
	}

	public static void checker(Expression e, String s, boolean isTail) {
		if (e instanceof LambdaExpression) {
			checker(((LambdaExpression) e).getBody(), s, isTail);
		} else if (e instanceof IfExpression) {
			ifEval((IfExpression) e, s, isTail);
		} else if (e instanceof CondExpression) {
			condEval((CondExpression) e, s, isTail);
		} else if (e instanceof LetExpression) {
			letEval((LetExpression) e, s, isTail);
		} else if (e instanceof LetStarExpression) {
			letStarEval((LetStarExpression) e, s, isTail);
		} else if (e instanceof LetrecExpression) {
			letrecEval((LetrecExpression) e, s, isTail);
		} else if (e instanceof CallExpression) {
			callEval((CallExpression) e, s, isTail);
		}
	}

	private static void lambdaChecker(LambdaExpression exp, String s, boolean isTail) {
		Expression e = exp.getBody();
		checker(e, s, isTail);
	}

	private static void letEval(LetExpression e, String s, boolean isTail) {
		checker(e.getBody(), s, isTail);
	}

	private static void letStarEval(LetStarExpression e, String s, boolean isTail) {
		checker(e.getBody(), s, isTail);
	}

	private static void letrecEval(LetrecExpression e, String s, boolean isTail) {
		for (Pair<String, Expression> i : e.getBindings()) {
			if (i.second instanceof LambdaExpression) {
				s = i.first;
				lambdaChecker((LambdaExpression) i.second, s, isTail);
			}
		}
		s = "";
		checker(e.getBody(), s, isTail);
	}

	private static void ifEval(IfExpression e, String s, boolean isTail) {
		checker(e.getCondition(), s, false);
		checker(e.getThen(), s, isTail);
		checker(e.getElse(), s, isTail);
	}

	private static void condEval(CondExpression e, String s, boolean isTail) {
		for (int i = 0; i < e.getSize(); i++) {
			checker(e.getCond(i), s, false);
			checker(e.getBody(i), s, isTail);
		}
	}

	private static void callEval(CallExpression e, String s, boolean isTail) {
		if (e.getOperator() instanceof IdExpression) {
			String name = ((IdExpression) e.getOperator()).getId();
			if ((name.equals(s)) && isTail) {
				String unparsed = Unparser.unparse(e);
				System.out.println("tail recursion found: " + unparsed);
			}
		}
		for (Expression i : e.getOperands()) {
			checker(i, s, false);
		}
	}

}
