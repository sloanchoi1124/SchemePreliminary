package com.example.scheme_preliminary;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IntExpression;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import evaluator.Evaluator;

public class Calculator_Activity extends Activity {
	
	private enum Mode {
		WAITING, INTEGER, OTHER;
	}
	private final String OPSTRING = "+*";
	
	private Mode mode;
	private Integer currentInt;
	private Stack<Pair<Expression, List<Expression>>> stack;
	private Expression fullExpression;
	
	private TextView textView;
	private Drawable defaultBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_calculator);
	    this.textView = (TextView) findViewById(R.id.textView1);
	    View unused = findViewById(R.id.UnusedButton);
	    this.defaultBackground = unused.getBackground();
	    unused.setClickable(false);
	    unused.setBackgroundColor(Color.TRANSPARENT);
	    
	    setMode(Mode.WAITING); // this.mode = Mode.WAITING
	    this.currentInt = null;
	    this.stack = new Stack<Pair<Expression, List<Expression>>>();
	    this.fullExpression = null;
	}
			
    public void onButtonClick(View v) {
    	String token = ((Button) v).getText().toString();
    	String text = this.textView.getText().toString();

		if (this.fullExpression != null) {
			text = "";
			this.fullExpression = null;
		}
    	
    	if (this.mode.equals(Mode.INTEGER)) {
    		if (token.equals("Enter")) {
    			IntExpression num = new IntExpression(this.currentInt);
    			if (this.stack.isEmpty())
    				this.fullExpression = num;
    			else {
    				this.stack.peek().second.add(num);
    			}
    			
    			while (! this.stack.isEmpty() && this.stack.peek().second.size() == 2) {
    				Pair<Expression, List<Expression>> pair = this.stack.pop();
    				CallExpression call = new CallExpression(pair.first, pair.second);
    				text += ")";
    				
    				if (this.stack.isEmpty())
    					this.fullExpression = call;
    				else
    					this.stack.peek().second.add(call);
    			}
    			setMode(Mode.WAITING);
    		}
    		else { // adding to currentInt
    			this.currentInt = this.currentInt * 10 + Integer.parseInt(token);
    			text += token;
    		}
    	}
    	else if (this.mode.equals(Mode.WAITING)) {
    		if (OPSTRING.indexOf(token) != -1) { // if it's an operator
    			this.stack.push(new Pair<Expression, List<Expression>>(new IdExpression(token), new LinkedList<Expression>()));
    			if (text != "") text += " ";
    			text += "(" + token;
    		}
    		else { // it's the start of a number
    			if (! text.equals(""))
    				text += " ";
    			text += token;
    			this.currentInt = Integer.parseInt(token);
    			setMode(Mode.INTEGER);
    		}
    	}
    	
    	
    	
    	if (this.fullExpression != null) text += ("\n=\n" + Evaluator.evaluate(this.fullExpression).getValue());
//    	if (this.fullExpression != null) text = Unparser.unparse(this.fullExpression) + "\n=\n" + Evaluator.evaluate(this.fullExpression);
    	this.textView.setText(text);
    }
	
	private void setMode(Mode mode) {
    	this.mode = mode;
    	Button b;
    	if (mode.equals(Mode.WAITING)) {
    		b = (Button) findViewById(R.id.EnterButton);
    		b.setBackgroundColor(Color.TRANSPARENT);
    		b.setClickable(false);
    		
    		b = (Button) findViewById(R.id.Plus);
    		b.setBackgroundResource(android.R.drawable.btn_default);
    		b.setClickable(true);
    		
    		b = (Button) findViewById(R.id.Times);
    		b.setBackgroundResource(android.R.drawable.btn_default);
    		b.setClickable(true);
    	}
    	else if (mode.equals(Mode.INTEGER)) {
    		b = (Button) findViewById(R.id.EnterButton);
    		b.setBackgroundResource(android.R.drawable.btn_default);
    		b.setClickable(true);
    		
    		b = (Button) findViewById(R.id.Plus);
    		b.setBackgroundColor(Color.TRANSPARENT);
    		b.setClickable(false);
    		
    		b = (Button) findViewById(R.id.Times);
    		b.setBackgroundColor(Color.TRANSPARENT);
    		b.setClickable(false);
    	}
    }
	
	
	
	
    
}
