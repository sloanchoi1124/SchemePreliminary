package com.example.scheme_preliminary;

import java.util.HashMap;

import scheme_ast.Expression;

public interface ActivityCommunicator {
	public void passExpressionToActivity(Expression ast);
	public Expression passExpressionToFragment();
	public void passBindingsToActivity(HashMap<String, Expression> bindings);
	public HashMap<String,Expression> passBindingsToFragment();
	
}
