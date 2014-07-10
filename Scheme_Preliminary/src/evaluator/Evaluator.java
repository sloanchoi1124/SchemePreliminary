package evaluator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import scheme_ast.CallExpression;
import scheme_ast.DefOrExp;
import scheme_ast.Definition;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import scheme_ast.OperatorExpression;
import scheme_ast.Program;
import util.Pair;

public class Evaluator {

	private static Environment initializeEnv() {
		Environment env = new Environment();
		// we do not really need to use OperatorExpression any more;
		// we can just map the id to an idExpression; or even leave it null as an encoding?
		env.put("+", new OperatorExpression("+", true, 2), null);
		env.put("*", new OperatorExpression("*", true, 2), null);
		env.put("-", new OperatorExpression("-", false, 2), null);
		env.put("/", new OperatorExpression("/", false, 2), null);
		env.put("<", new OperatorExpression("<", false, 2), null);
		env.put(">", new OperatorExpression(">", false, 2), null);
		env.put("=", new OperatorExpression("=", false, 2), null);
		env.put("!", new OperatorExpression("not", false, 1), null);
		env.put("odd?", new OperatorExpression("odd?", false, 1), null);
		env.put("remainder", new OperatorExpression("remainder", false, 2), null);
		env.put("quotient", new OperatorExpression("quotient", false, 2), null);
		return env;
	}
	
	public static IntExpression evaluate(Program p) {
		Iterator<DefOrExp> itr = p.getProgram().iterator();
		DefOrExp temp;
		IntExpression step_result;
		Stack<IntExpression> list = new Stack<IntExpression>();
		Environment general_envr =initializeEnv();
		while (itr.hasNext()) {
			temp = itr.next();
			if (temp instanceof Definition) {
				general_envr.put(((Definition) temp).getSymbol(), ((Definition) temp).getBody(), general_envr);
				System.out.println(((Definition) temp).getSymbol());
			} else {
				step_result = (IntExpression) (evaluate(((Expression)temp), general_envr));
				list.add(step_result);
				System.out.println(step_result.getValue());
			}
			
		}
		return list.pop();
	}
	
	private static Expression evaluate(Expression e, Environment env) {
		if (e instanceof IntExpression || e instanceof LambdaExpression) {
			return e;
		} else if (e instanceof IfExpression) {
			return ifEval((IfExpression) e, env);
		} else if (e instanceof CallExpression) {
			return callEval((CallExpression) e, env);
		} else if (e instanceof LetExpression) {
			return letEval((LetExpression) e, env);
		} else if (e instanceof IdExpression) {
			String id = ((IdExpression) e).getId();
			System.out.println(id);
			return env.getExpression(id);
		} else {
			return null; // error!!
		}
	}
		
	
	private static Expression letEval(LetExpression e, Environment env) {
		Environment augmentedEnv = new Environment(env); // full copy
		for (Pair<String, Expression> i : e.getBindings()) {
                    // bindings are evaluated in original environment
                    augmentedEnv.put(i.first, evaluate(i.second, env), env);
		}
		return evaluate(e.getBody(), augmentedEnv);
	}

	
	private static Expression ifEval(IfExpression e, Environment env) {
		Expression v = evaluate(e.getCondition(), env);
		if (v instanceof IntExpression) {
			if (((IntExpression) v).getValue() != 0) {
				return evaluate(e.getThen(), env);
			} else {
				return evaluate(e.getElse(), env);
			}
		} else {
			return null; // ERROR - not int/bool
		}
	}
	
	private static Expression callEval(CallExpression e,
			Environment envr) {
		if (e.getOperator() instanceof IdExpression && evaluate(e.getOperator(), envr) instanceof LambdaExpression) {
			LambdaExpression operator = (LambdaExpression)evaluate(e.getOperator(), envr);
			Environment origin = envr.getEnvironment(((IdExpression)e.getOperator()).getId());
			List<Expression> numbers = e.getOperands();
			if (operator.getParameters().size() != numbers.size()) {
				// error
				return null;
			}
			int i = 0;
			Environment copy = new Environment(origin);
			for (String para : operator.getParameters()) {
				Expression value = evaluate(numbers.get(i), envr);
				copy.put(para, value, envr);
				i++;
			}
			Expression result = evaluate( operator.getBody(), copy);
			return result;
		}
		Expression operator = evaluate(e.getOperator(), envr);		
		if (operator instanceof OperatorExpression) {
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
				IntExpression i = (IntExpression) evaluate(items.get(0), envr);
				int temp = i.getValue();
				if (temp % 2 == 0) {
					return f;
				} 
				return t;
			}

			Expression i1 = evaluate(items.get(0), envr);
			Expression i2 = evaluate(items.get(1), envr);
			int item1 = ((IntExpression) i1).getValue();
			int item2 = ((IntExpression) i2).getValue();
			int result = 0;

			// operator: remainder		
			if (id.equals("remainder")) {
				return new IntExpression (item1 % item2);
			}

			// operators: <, >, =, return 1 for true, 0 for false	
			else if (id.equals("=")) {
				if (item1 == item2) {
					return t;
				} else {
					return f;
				}
			}
			else if (id.equals("<")) {
				if (item1 < item2) {
					return t;
				} else {
					return f;
				}
			}			
			else if (id.equals(">")) {
				if (item1 > item2) {
					return t;
				} else {
					return f;
				}
			}

			// operators: +, -, * /
			else if (id.equals("+")) {
				for (Expression item : items) {
					result = result
							+ ((IntExpression) evaluate(item, envr)).getValue();
				}
			}

			else if (id.equals("-")) {
				result = ((IntExpression) evaluate(items.get(0), envr))
						.getValue();
				for (int i = 1; i < items.size(); i++) {
					Expression item = items.get(i);
					result = result
							- ((IntExpression) evaluate(item, envr)).getValue();
				}
			}
			else if (id.equals("*")) {
				result = 1;
				for (Expression item : items) {
					result = result
							* ((IntExpression) evaluate(item, envr)).getValue();
				}
			}
			else if (id.equals("quotient")) {
				result = ((IntExpression) evaluate(items.get(0), envr))
						.getValue();
				for (int i = 1; i < items.size(); i++) {
					Expression item = items.get(i);
					result = result
							/ ((IntExpression) evaluate(item, envr)).getValue();
				}
			}			
			else {
				// error: operator not defined;
				return null;
			}
			return new IntExpression(result);
		}		
		else {
			// error: operator neither Lambda nor Operator
			return null;
		}
	}
	
}
