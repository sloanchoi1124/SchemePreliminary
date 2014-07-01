package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import scheme_ast.*;

public class AstExamples {
	
	public List<Expression> examples;

	public AstExamples() {
		Expression x0 = new IntExpression(0);
		Expression x1 = new IntExpression(1);
		Expression x3 = new IntExpression(3);
		Expression x4 = new IntExpression(4);
		Expression x5 = new IntExpression(7);
		Expression x10 = new IntExpression(10);
		
		Expression xEq = new IdExpression("=");
		Expression xLt = new IdExpression("<");
		Expression xMinus = new IdExpression("-");
		Expression xPlus = new IdExpression("+");
		Expression xTimes = new IdExpression("*");
		
		Expression xN = new IdExpression("n");
		Expression xX = new IdExpression("x");
		Expression xY = new IdExpression("y");
		
		Expression simpleIf = new IfExpression(
			new CallExpression(
					xLt,
					new ArrayList<Expression>(Arrays.asList(x3, x4))),
			new CallExpression(
					xTimes,
					new ArrayList<Expression>(Arrays.asList(x4, x5))),
			new CallExpression(
					xMinus, 
					new ArrayList<Expression>(Arrays.asList(x10, x5))));

		
		HashMap<String, Expression> simpleLetBindings = new HashMap<String, Expression>();
		simpleLetBindings.put("x", x3);
		simpleLetBindings.put("y", new CallExpression(
				xTimes,
				new ArrayList<Expression>(Arrays.asList(x4, x5))));
		
		Expression simpleLet = new LetExpression(simpleLetBindings, 
				new CallExpression(xPlus,
							new ArrayList<Expression>(Arrays.asList(xX, xY))));

		
		HashMap<String, Expression> bindings = new HashMap<String, Expression>();
		bindings.put("f", (new LambdaExpression(
				new ArrayList<String>(Arrays.asList("x")),
				new CallExpression(xPlus, new ArrayList<Expression>(Arrays.asList(xX, x1))))));
		
		Expression simpleLambda = new LetExpression(bindings, new CallExpression(new IdExpression("f"), new ArrayList<Expression>(Arrays.asList(x1))));
		
		Expression xFact = new IdExpression("fact");
		Expression xFactMinus1 =
				new CallExpression(xMinus,
						new ArrayList<Expression>(Arrays.asList(xN, x1)));
		Expression xFactRecurse =
				new CallExpression(
						xFact,
						new ArrayList<Expression>(Arrays.asList(xFactMinus1)));
		Expression xFactLambda = new LambdaExpression(
				new ArrayList<String>(Arrays.asList("n")),
				new IfExpression(
						new CallExpression(
								xEq,
								new ArrayList<Expression>(Arrays.asList(xN, x0))),
						x1,
						new CallExpression(
								xTimes,
								new ArrayList<Expression>(
										Arrays.asList(xN, xFactRecurse)))));
		HashMap<String, Expression> factBindings =
				new HashMap<String, Expression>();
		factBindings.put("fact", xFactLambda);
				
		Expression factorial = new LetExpression(
				factBindings,
				new CallExpression(
					xFact,
					new ArrayList<Expression>(Arrays.asList(x5))));

		
		examples = new ArrayList<Expression>(Arrays.asList(
				simpleIf,
				simpleLet,
				simpleLambda,
				factorial));
	}
		
}
