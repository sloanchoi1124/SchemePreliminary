package unparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import util.Pair;

public class ShallowUnparser {
	public static String shallowUnparse(Expression ast, int depth)
	{
		String result="";
	    if(ast instanceof IntExpression)
	    {
	    	result+=((Integer)((IntExpression)ast).getValue()).toString()+" ";
	    }
	    else if(ast instanceof IdExpression)
	    {
	    	result+=((IdExpression)ast).getId()+" ";
	    }
	    else if(ast instanceof CallExpression)
	    {
	    	//depth0 of the call expression is the depth0 of the operator
	    	if(depth<1)
	    	{
	    		result+="("+shallowUnparse(((CallExpression)ast).getOperator(),depth-1)+"...)";
	    	}
	    	else
	    	{
	    		result+="("+shallowUnparse(((CallExpression)ast).getOperator(),depth-1)+" ";
	    		for(Expression expression: ((CallExpression)ast).getOperands())
	    		{
	    			result+=shallowUnparse(expression,depth-1)+" ";
	    		}
	        result+=")";
	        }
	    }
	    else if(ast instanceof IfExpression)
	    {
	    	if(depth<1)
	    	{
	    		result+="(If(...) then(...) else(...))";
	    	}
	        else
	        {
	        	result+="(If ";
	            result+=shallowUnparse(((IfExpression)ast).getCondition(),depth-1)+" ";
	            result+=shallowUnparse(((IfExpression)ast).getThen(),depth-1)+" ";
	            result+=shallowUnparse(((IfExpression)ast).getElse(),depth-1);
	            result+=")";
	        }
	    }
	    else if(ast instanceof LetExpression)
	    {
	    	if(depth<1)
	    	{
	    		result+="(Let (...) body(...))";
	    	}
	        else
	        {
	        	result+="(";
	            result+=shallowBindings(((LetExpression)ast).getBindings(),depth-1)+" ";
	            result+=shallowUnparse(((LetExpression)ast).getBody(),depth-1);
	            result+=")";
	        }
	    }
	    else if(ast instanceof LambdaExpression)
	    {
	    	if(depth<1)
	    	{
	    		//this is probably the unicode of lambda
	    		String lambda="\u03BB";
	    		result+="("+lambda.toLowerCase()+" "+"(parameters...)"+"(body...)"+")";
	    	}
	    	else
	    	{
	    		String lambda="\u03BB";
	    		result+="("+lambda.toLowerCase()+" ";
	    		for(String parameter:((LambdaExpression)ast).getParameters())
	    		{
	    			result+=parameter+" ";
	    		}
	    		result+=shallowUnparse(((LambdaExpression)ast).getBody(),depth-1);
	    	}
	    }
	    return result;
	    }
	public static String shallowBindings(List<Pair<String, Expression>> list, int depth)
	{
		String result="";
	    result+="(";
	    for(Pair<String,Expression> entry:list)
	    {
	    	result+=entry.first+"->"+shallowUnparse(entry.second,depth-1)+" ";
	    }
	    result+=")";
	    return result;
	}
}
