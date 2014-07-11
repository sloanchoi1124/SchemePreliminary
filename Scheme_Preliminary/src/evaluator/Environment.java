package evaluator;

import java.util.HashMap;
import util.Pair;
import scheme_ast.Expression;

public class Environment {
	private HashMap<String, Pair<Expression, Environment>> mEnv;
	
	public Environment() {
		mEnv = new HashMap<String, Pair<Expression, Environment>>();
	}
	
	public Environment(Environment source) { // copy constructor 
		mEnv = new HashMap<String, Pair<Expression, Environment>>(source.mEnv);
	}
	
	public void put(String id, Expression e, Environment env) {
		mEnv.put(id, new Pair<Expression, Environment>(e, env));
	}
	
	public Expression getExpression(String id) {
		return mEnv.get(id).first;
	}
	
	public Environment getEnvironment(String id) {
		return mEnv.get(id).second;
	}
	
	public Pair<Expression, Environment> getPair(String id) {
		return mEnv.get(id);
	}
}
