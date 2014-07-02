package com.example.scheme_preliminary.calculator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.scheme_preliminary.R;

import evaluator.Evaluator;

public class Calculator_Activity extends Activity {
	
	private enum Mode {
		WAITING, INTEGER, OTHER;
	}
	private final List<String> OPLIST = Arrays.asList(new String[] { "+", "*", "if" });
	
	private Mode mode;
	private Integer currentInt;
	private Stack<Pair<Expression, List<Expression>>> stack;
	private Expression fullExpression;
	
	private FrameLayout fragFrame;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_calculator);
	    
	    this.fragFrame = (FrameLayout) findViewById(R.id.FrameLayout1);
	    
	    Keypad_Fragment keypadFragment = new Keypad_Fragment();
        getFragmentManager().beginTransaction()
        					.add(R.id.FrameLayout1, keypadFragment, "keypad")
        					.commit();
	    
	    this.textView = (TextView) findViewById(R.id.textView1);
	    
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
    			
    			while (! this.stack.isEmpty() && topExpressionComplete()) {
    				Expression exp = popTopExpression();
    				text += ")";
    				
    				if (this.stack.isEmpty())
    					this.fullExpression = exp;
    				else
    					this.stack.peek().second.add(exp);
    			}
    			setMode(Mode.WAITING);
    		}
    		else { // adding to currentInt
    			this.currentInt = this.currentInt * 10 + Integer.parseInt(token);
    			text += token;
    		}
    	}
    	else if (this.mode.equals(Mode.WAITING)) {
    		if (OPLIST.contains(token)) { // if it's an operator
    			if (token.equals("if"))
    				this.stack.push(new Pair<Expression, List<Expression>>(new IfExpression(null, null, null), new LinkedList<Expression>()));
    			else
    				this.stack.push(new Pair<Expression, List<Expression>>(new IdExpression(token), new LinkedList<Expression>()));
    			if (text != "") text += " ";
    			text += "(" + token;
    		}
    		else if (getString(R.string.varCreate).equals(token)) {
    			// start creation fragment
    			return;
    			// (either the callback method onCreateVar will handle the next segment
    			// or the user will press back and need to enter new input)
    		}
    		else if (getString(R.string.varSelect).equals(token)) {
    			// start selection fragment
    			return;
    			// (either the callback method onSelectVar will handle the next segment
    			// or the user will press back and need to enter new input)
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
	
    private boolean topExpressionComplete() {
    	// Assumes !stack.isEmpty()
    	Pair<Expression, List<Expression>> pair = this.stack.peek();
    	if (pair.first instanceof IfExpression)
    		return pair.second.size() == 3;
    	else
    		return pair.second.size() == 2;
    }
    
    private Expression popTopExpression() {
    	Expression exp;
		Pair<Expression, List<Expression>> pair = this.stack.pop();
		if (pair.first instanceof IfExpression)
			exp = new IfExpression(pair.second.get(0), pair.second.get(1), pair.second.get(2));
		else
			exp = new CallExpression(pair.first, pair.second);
		return exp;
    }
    

    
	private void setMode(Mode mode) {
    	this.mode = mode;
    	if (mode.equals(Mode.WAITING)) {
    		disactivateButton(R.id.EnterButton);
    		activateButton(R.id.PlusButton);
    		activateButton(R.id.TimesButton);
    		activateButton(R.id.IfButton);
    		activateButton(R.id.varCreateButton);
    		activateButton(R.id.varSelectButton);
    	}
    	else if (mode.equals(Mode.INTEGER)) {
    		activateButton(R.id.EnterButton);
    		disactivateButton(R.id.PlusButton);
    		disactivateButton(R.id.TimesButton);
    		disactivateButton(R.id.IfButton);
    		disactivateButton(R.id.varCreateButton);
    		disactivateButton(R.id.varSelectButton);
    	}
    }
	
    private void activateButton(int id) {
    	View v = findViewById(id);
    	Button b;
    	if (v != null)
    		b = (Button) v;
    	else {
    		System.out.print("Can't find view with id " + id);
    		return;
    	}
    	b.setBackgroundResource(android.R.drawable.btn_default);
    	b.setClickable(true);
    }
    private void disactivateButton(int id) {
    	View v = findViewById(id);
    	Button b;
    	if (v != null)
    		b = (Button) v;
    	else {
    		System.out.print("Can't find view with id " + id);
    		return;
    	}
    	b.setBackgroundColor(Color.TRANSPARENT);
    	b.setClickable(false);
    }
	
}
