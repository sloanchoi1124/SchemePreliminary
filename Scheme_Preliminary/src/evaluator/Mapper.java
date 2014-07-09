package evaluator;

/* This method takes in an expression, goes through the expression recursively. Produce a map which memorizes names of bindings which are defined 
 * in LetExpression, and their upper expression (the LetExpression that bindings are defined.)
 * 
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import util.Pair;
import util.Uid;

public class Mapper {
	
	private static HashMap<Uid, HashSet<String>> memo;
	
	public static HashMap<Uid, HashSet<String>> getMap(Expression e) {
		memo = new HashMap<Uid, HashSet<String>>();
		HashSet<String> workingSet = new HashSet<String>();
		evaluate(e, workingSet);		
		return memo;
	}

	public static void evaluate(Expression e, HashSet<String> workingSet) {
		memo.put(e.getUid(), workingSet);
		System.out.println("putting ");
		if (e instanceof IfExpression) {
			ifCheck((IfExpression) e, workingSet);
		} else if (e instanceof CallExpression) {
			callCheck((CallExpression) e, workingSet);
		} else if (e instanceof LetExpression) {
			letCheck((LetExpression) e, workingSet);
		} else if (e instanceof LambdaExpression) {
			lambdaCheck((LambdaExpression) e, workingSet);
		} else {}
	}

	private static void letCheck(LetExpression e, HashSet<String> workingSet) {
		List<Pair<String, Expression>> temp = e.getBindings();
		HashSet<String> copy = new HashSet<String>(workingSet);
		for (Pair<String, Expression> i : temp)	{
			String variable = i.first;
			copy.add(variable);
			evaluate(i.second, copy);
		}
		evaluate(e.getBody(), copy);
	}
	
	private static void lambdaCheck(LambdaExpression e, HashSet<String> workingSet) {		
		HashSet<String> copy = new HashSet<String>(workingSet);		
		for (String paras : e.getParameters()) {
			copy.add(paras);
		}
		evaluate(e.getBody(), copy);
	}

	private static void ifCheck(IfExpression e, HashSet<String> workingSet) {		
		evaluate(e.getCondition(), workingSet);
		evaluate(e.getElse(), workingSet);
		evaluate(e.getThen(), workingSet);
	}
	
	private static void callCheck(CallExpression e, HashSet<String> workingSet) {		
		System.out.println("CallCheck");
		evaluate(e.getOperator(), workingSet);
		for (Expression i : e.getOperands()) {
			evaluate(i, workingSet);
		}
	}
}
