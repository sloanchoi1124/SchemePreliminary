package evaluator;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Stack;

import scheme_ast.*;

public class Evaluator {

	private static HashMap<String, Expression> standardBindings;

	private static Stack<HashMap<String, Expression>> operatorInit() {
		Stack<HashMap<String, Expression>> maps = new Stack<HashMap<String, Expression>>();
		standardBindings = new HashMap<String, Expression>();
		standardBindings.put("+", new OperatorExpression("+", true, 2));
		standardBindings.put("*", new OperatorExpression("*", true, 2));
		standardBindings.put("-", new OperatorExpression("-", false, 2));
		standardBindings.put("/", new OperatorExpression("/", false, 2));
		standardBindings.put("<", new OperatorExpression("<", false, 2));
		standardBindings.put(">", new OperatorExpression(">", false, 2));
		standardBindings.put("=", new OperatorExpression("=", false, 2));
		standardBindings.put("!", new OperatorExpression("not", false, 1));
		standardBindings.put("odd?", new OperatorExpression("odd?", false, 1));
		standardBindings.put("remainder", new OperatorExpression("remainder", false, 2));
		standardBindings.put("quotient", new OperatorExpression("quotient", false, 2));
		maps.add(standardBindings);
		return maps;
	}

	public static IntExpression evaluate(Expression e) {
		Stack<HashMap<String, Expression>> maps = operatorInit();
		IntExpression result = (IntExpression) evaluate(e, maps);
		return result;
	}

	private static Expression evaluate(Expression e,
			Stack<HashMap<String, Expression>> maps) {
		if (e instanceof IfExpression) {
			System.out.println("If (...)");
			return ifEval((IfExpression) e, maps);
		} else if (e instanceof CallExpression) {
			callPrint((CallExpression)e, maps);
			return callEval((CallExpression) e, maps);
		} else if (e instanceof LetExpression) {
			System.out.println("let (...)");
			return letEval((LetExpression) e, maps);
		} else if (e instanceof IdExpression) {
			String key = ((IdExpression) e).getId();
			System.out.printf("IdExpression key: %s \n", key);
			return findBinding(key, maps);
		} else {
			return e;
		}
	}
	
	//print debugging methods, incomplete 
	private static void letPrint(LetExpression e) {
		
	}
	
	private static void callPrint(CallExpression e, Stack<HashMap<String, Expression>> maps) {
		if (e.getOperator() instanceof  OperatorExpression) {
			System.out.printf("(%s ...)", ((OperatorExpression) e.getOperator()).getName());
		} else {
			System.out.printf("(%s ...)", getName(e.getOperator(), maps));
		}
	}
	
	private static String getName(Expression e, Stack<HashMap<String, Expression>> maps) {
		for (HashMap<String, Expression> map : maps) {
			for (Entry<String, Expression> entry : map.entrySet()) {
				if (entry.getValue().equals(e)) {
					return entry.getKey();
				}
			}
		}
		return null;
	}
	
	private static void ifPrint(CallExpression e) {
		
	}

	private static Expression letEval(LetExpression e,
			Stack<HashMap<String, Expression>> maps) {
		HashMap<String, Expression> temp = e.getBindings();
		HashMap<String, Expression> bindings = new HashMap<String, Expression>();
		for (Entry<String, Expression> i : temp.entrySet()) {
			if (i.getValue() instanceof LambdaExpression) {
				bindings.put(i.getKey(), i.getValue());
			} else {
				Expression value = evaluate(i.getValue(), maps);
				bindings.put(i.getKey(), value);
			}
		}
		maps.push(bindings);
		Expression body = e.getBody();
		Expression result = evaluate(body, maps);
		maps.pop();
		return result;
	}

	private static Expression ifEval(IfExpression e,
			Stack<HashMap<String, Expression>> maps) {

		Expression v = evaluate(e.getCondition(), maps);
		if (v instanceof IntExpression) {
			if (((IntExpression) v).getValue() == 1) {
				System.out.println("evaluate the body of if: \n");
				Expression then = e.getThen();
				return evaluate(then, maps);
			} else {
				System.out.println("evaluate else: \n");
				Expression not = e.getElse();
				return evaluate(not, maps);
			}
		} else {
			return null;
		}
	}

	private static Expression lambdaEval(CallExpression e,
			Stack<HashMap<String, Expression>> maps) {
		LambdaExpression lam = (LambdaExpression) e.getOperator();
		List<Expression> numbers = e.getOperands();
		if (lam.getParameters().size() != numbers.size()) {
			// error
			return null;
		}
		HashMap<String, Expression> assignments = new HashMap<String, Expression>();
		int i = 0;
		for (String para : lam.getParameters()) {
			Expression value = evaluate(numbers.get(i), maps);
			assignments.put(para, value);
			i++;
		}
		maps.push(assignments);
		Expression body = lam.getBody();
		Expression result = evaluate(body, maps);
		maps.pop();
		return result;
	}

	private static Expression callEval(CallExpression e,
			Stack<HashMap<String, Expression>> maps) {
		Expression operator = evaluate(e.getOperator(), maps);
		if (operator instanceof LambdaExpression) {
			CallExpression temp = new CallExpression(operator, e.getOperands());
			return lambdaEval(temp, maps);
		} else if (operator instanceof OperatorExpression) {
			List<Expression> items = e.getOperands();
			OperatorExpression oprt = (OperatorExpression) operator;
			String id = oprt.getName();
			if ((!oprt.acceptsLists()) && items.size() != oprt.getArity()) {
				return null;
			}

			if ((oprt.acceptsLists()) && items.size() < oprt.getArity()) {
				return null;
			}
			
			IntExpression t = new IntExpression(1);
			IntExpression f = new IntExpression(0);
			
			if (id.equals("odd?")) {
				IntExpression i = (IntExpression) evaluate(items.get(0), maps);
				int temp = i.getValue();
				if (temp % 2 == 0) {
					return f;
				} 
				return t;
			}
			
			Expression i1 = evaluate(items.get(0), maps);
			Expression i2 = evaluate(items.get(1), maps);
			int item1 = ((IntExpression) i1).getValue();
			int item2 = ((IntExpression) i2).getValue();
			
			
			
			// operator: reminder		
			if (id.equals("reminder")) {
				IntExpression result = new IntExpression (item1 % item2);
				return result;
			}
			
			// operators: <, >, =, return 1 for true, 0 for false	
			if (id.equals("=")) {
				if (item1 == item2) {
					return t;
				} else {
					return f;
				}
			}
			if (id.equals("<")) {
				if (item1 < item2) {
					return t;
				} else {
					return f;
				}
			}
			if (id.equals(">")) {
				if (item1 > item2) {
					return t;
				} else {
					return f;
				}
			}

			// operators: +, -, * /
			int result = 0;
			if (id.equals("+")) {
				for (Expression item : items) {
					result = result
							+ ((IntExpression) evaluate(item, maps)).getValue();
				}
			}
			if (id.equals("-")) {
				result = ((IntExpression) evaluate(items.get(0), maps))
						.getValue();
				for (int i = 1; i < items.size(); i++) {
					Expression item = items.get(i);
					result = result
							- ((IntExpression) evaluate(item, maps)).getValue();
				}
			}
			if (id.equals("*")) {
				result = 1;
				for (Expression item : items) {
					result = result
							* ((IntExpression) evaluate(item, maps)).getValue();
				}
			}
			if (id.equals("quotient")) {
				result = ((IntExpression) evaluate(items.get(0), maps))
						.getValue();
				for (int i = 1; i < items.size(); i++) {
					Expression item = items.get(i);
					result = result
							/ ((IntExpression) evaluate(item, maps)).getValue();
				}
			}
			IntExpression out = new IntExpression(result);
			return out;
		} else {
			return null;
		}
	}

	private static Expression findBinding(String key,
			Stack<HashMap<String, Expression>> maps) {
		ListIterator<HashMap<String, Expression>> li = maps.listIterator(maps
				.size());
		while (li.hasPrevious()) {
			HashMap<String, Expression> map = li.previous();
			if (map.containsKey(key)) {
				return (Expression) map.get(key);
			}
		}		
		return null;
	}
}
