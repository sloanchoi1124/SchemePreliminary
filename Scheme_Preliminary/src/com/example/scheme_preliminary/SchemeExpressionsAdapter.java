package com.example.scheme_preliminary;

import java.util.Iterator;
import java.util.List;

import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SchemeExpressionsAdapter extends ArrayAdapter<Pair<String,Expression>> {

	public List<Pair<String,Expression>> list;
	public Context context;
	//private int resource;

	public SchemeExpressionsAdapter(Navigation_Activity activity, List<Pair<String, Expression>> list) {
		super(activity, R.layout.activity_navigation_row_layout, list);
		this.list = list;
		this.context = activity;
	}
	
	@SuppressWarnings("unchecked")
	public View getView(int position, View convertView, ViewGroup parent) {	    
	    // First let's verify the convertView is not null
	    if (convertView == null) {
	        // This a new view we inflate the new layout
	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        
	        convertView = inflater.inflate(R.layout.activity_navigation_row_layout, parent, false);	
	        
		    // Now we can fill the layout with the right values
	        TextView partView = (TextView) convertView.findViewById(R.id.part);
	        TextView callView = (TextView) convertView.findViewById(R.id.call);
	        TextView argsView = (TextView) convertView.findViewById(R.id.args);
	        
	        Pair<String, Expression> pair = this.list.get(position);
	        
	        Expression expression = pair.second;
	        if (expression != null) {
		        partView.setText(pair.first);
		        Pair<String, String> keyValueStringPair = pairFormat(expression);
		        callView.setText(keyValueStringPair.first);
		        argsView.setText(keyValueStringPair.second);
	        }
	        else {
	        	partView.setText("param");
	        	argsView.setText(pair.first);
	        }
	    }
		    return convertView;
	}

	    
	public static Pair<String, String> pairFormat(Expression expression) {
		if (expression instanceof CallExpression) {
        	CallExpression callExp = (CallExpression) expression;
        	String s = "";
        	Iterator<Expression> iter = callExp.getOperands().iterator();
        	Expression operand;
        	for (int i=0; i<4; i++) {
        		if (iter.hasNext()) operand = iter.next();
        		else break;
        		if (i != 0) s += "    ";
        		if (operand instanceof IdExpression ||
        			operand instanceof IntExpression)
        			s += pairFormat(operand).second;
        		else {
        			s += "...";
        			break;
        		}
        		if (i == 3) {
        			s += "...";
        		}
        	}
        	return new Pair<String, String>(pairFormat(callExp.getOperator()).second, s);
	    }
        else if (expression instanceof IfExpression) {
        	IfExpression ifExp = (IfExpression) expression;
        	return new Pair<String, String>("if", pairFormat(ifExp.getCondition()).second);
        }
        else if (expression instanceof LetExpression) {
        	LetExpression letExp = (LetExpression) expression;
        	String s = "(";
        	int args = 0;
        	Iterator<String> iter = letExp.getBindings().keySet().iterator();
        	String key;
        	Expression value;
        	while (iter.hasNext() && args < 4) {
        		key = iter.next();
        		s += "(" + key;
        		value = letExp.getBindings().get(key);
        		if (value instanceof IdExpression || value instanceof IntExpression)
        			s += " " + pairFormat(value).second + ")";
        		else
        			s += " ...)";
        	}
        	return new Pair<String, String>("let", s + ")    ...");
        }
        else if (expression instanceof LambdaExpression) {
        	LambdaExpression lambdaExp = (LambdaExpression) expression;
        	return new Pair<String, String>("lambda", lambdaExp.getParameters() + "    ...");
        }
        else if (expression instanceof IdExpression) {
        	IdExpression idExp = (IdExpression) expression;
        	return new Pair<String, String>("[ID]", idExp.getId());
        }
        else if (expression instanceof IntExpression) {
        	IntExpression intExp = (IntExpression) expression;
        	return new Pair<String, String>("[int]", Integer.toString(intExp.getValue()));
        }
        
	    else System.out.println("IT'S NOT RECOGNIZED AS A KNOWN EXPRESSION TYPE");
		return null;
	}
	
	public static String expressionType(Expression exp) {
		if (exp == null) return "Param";
		else if (exp instanceof CallExpression) return "Call";
		else if (exp instanceof IfExpression) return "If";
		else if (exp instanceof LetExpression) return "Let";
		else if (exp instanceof LambdaExpression) return "Lambda";
		else if (exp instanceof IdExpression) return "Id";
		else if (exp instanceof IntExpression) return "Int";
		else {
			System.out.println ("expression " + exp + " not recognized");
			return null;
		}
	}
	
}
