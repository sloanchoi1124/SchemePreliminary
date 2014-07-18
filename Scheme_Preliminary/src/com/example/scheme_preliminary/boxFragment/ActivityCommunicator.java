package com.example.scheme_preliminary.boxFragment;

import java.util.HashMap;
import java.util.List;

import scheme_ast.DefOrExp;
import scheme_ast.Expression;
import util.Pair;

public interface ActivityCommunicator {
	public void passDefOrExpToActivity(DefOrExp ast);
	public DefOrExp passDefOrExpToFragment();
	public void passBindingsToActivity(List<Pair<String, Expression>> bindings);
	public List<Pair<String, Expression>> passBindingsToFragment();
	public void passLabelToActivity(String label);
	public boolean setClickabilityToFragment();
	public void destroySubsequentFragments();
	public void inputReplacementByCalculator();
	public Expression getReplacementFromCalculator();
}
