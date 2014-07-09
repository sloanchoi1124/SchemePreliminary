package com.example.scheme_preliminary;

import java.util.HashMap;
import java.util.List;

import scheme_ast.Expression;
import util.Pair;

public interface ActivityCommunicator {
	public void passExpressionToActivity(Expression ast);
	public Expression passExpressionToFragment();
	public void passBindingsToActivity(List<Pair<String, Expression>> bindings);
	public List<Pair<String, Expression>> passBindingsToFragment();
	
}
