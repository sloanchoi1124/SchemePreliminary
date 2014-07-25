package unparser;

import java.util.List;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.AndExpression;
import scheme_ast.BoolExpression;
import scheme_ast.CallExpression;
import scheme_ast.CondExpression;
import scheme_ast.ConsExpression;
import scheme_ast.DefOrExp;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import scheme_ast.LetStarExpression;
import scheme_ast.LetrecExpression;
import scheme_ast.NullExpression;
import scheme_ast.OrExpression;
import scheme_ast.Program;
import scheme_ast.StringExpression;
import util.Pair;

public class ShallowUnparser {
	public static String shallowUnparse(Expression ast, int depth)
	{
		String result="";
	    if(ast instanceof IntExpression)
	    {
	    	result+=((IntExpression)ast).getValue().toString()+" ";
	    }
	    else if(ast instanceof IdExpression)
	    {
	    	result+=((IdExpression)ast).getId()+" ";
	    }
	    else if(ast instanceof BoolExpression)
	    {
	    	Boolean temp=((BoolExpression)ast).getValue();
	    	if(temp==true)
	    		result+="#t" ;
	    	else
	    		result+="#f ";
	    }
	    else if(ast instanceof StringExpression)
	    {
	    	result+=((StringExpression)ast).toString();
	    }
	    else if(ast instanceof NullExpression)
	    {
	    	result+="'()";
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
	    else if(ast instanceof AndExpression)
	    {
	    	if(depth<1)
	    		result+="(and...)";
	    	else
	    	{
	    		result+="(and ";
	    		for(Expression expression:((AndExpression) ast).getConditions())
	    			result+=shallowUnparse(expression,depth-1)+" ";
	    	}
	    	result+=")";
	    }
	    else if(ast instanceof OrExpression)
	    {
	    	if(depth<1)
	    		result+="(or...)";
	    	else
	    	{
	    		result+="(and ";
	    		for(Expression expression:((OrExpression) ast).getConditions())
	    			result+=shallowUnparse(expression,depth-1)+" ";
	    	}
	    	result+=")";
	    }
	    else if(ast instanceof IfExpression)
	    {
	    	if(depth<1)
	    	{
	    		result+="(if(...) then(...) else(...))";
	    	}
	        else
	        {
	        	result+="(if ";
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
	    		result+="(let (...) body(...))";
	    	}
	        else
	        {
	        	result+="(";
	            result+=shallowBindings(((LetExpression)ast).getBindings(),depth-1)+" ";
	            result+=shallowUnparse(((LetExpression)ast).getBody(),depth-1);
	            result+=")";
	        }
	    }
	    else if(ast instanceof LetrecExpression)
	    {
	    	if(depth<1)
	    		result+="(letrec (...) body(...))";
	    	else
	    	{
	        	result+="(";
	            result+=shallowBindings(((LetrecExpression)ast).getBindings(),depth-1)+" ";
	            result+=shallowUnparse(((LetrecExpression)ast).getBody(),depth-1);
	            result+=")";
	    	}
	    }
	    else if(ast instanceof LetStarExpression)
	    {
	    	if(depth<1)
	    		result+="(let* (...) body(...))";
	    	else
	    	{
	        	result+="(";
	            result+=shallowBindings(((LetStarExpression)ast).getBindings(),depth-1)+" ";
	            result+=shallowUnparse(((LetStarExpression)ast).getBody(),depth-1);
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
	    else if(ast instanceof CondExpression)
	    {
	    	if(depth<1)
	    		result+="(cond ...)";
	    	else
	    	{
	    		result+="(cond ";
	    		for(Pair<Expression,Expression> pair:((CondExpression) ast).getAllPairs())
	    			result+="("+shallowUnparse(pair.first,depth-1)+" "+shallowUnparse(pair.second,depth-1)+")";
	    		result+=")";
	    	}
	    }
	    else if(ast instanceof ConsExpression)
	    {
	    	if(depth<1)
	    		result+="(cons...)";
	    	else
	    		result+="(cons"+shallowUnparse(((ConsExpression) ast).car(),depth-1)+" "
	    	            +shallowUnparse(((ConsExpression) ast).cdr(),depth-1)+")";
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
	
	public static void main(String args[])
	{
		String cons="(cons 0 (cons 1 2))";
		List<Token> tokens1 = Lexer.lex(cons);
		System.out.println(tokens1);
		Program program1=Parser.parse(tokens1);
		for(DefOrExp temp:program1.getProgram())
		{
			if(temp instanceof Expression)
				System.out.println(ShallowUnparser.shallowUnparse((Expression)temp,0));
		}
	}
}
