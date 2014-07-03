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
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.calculator.Id_Creation_Fragment.IdCreator;
import com.example.scheme_preliminary.calculator.Id_Selection_Fragment.IdSelector;
import com.example.scheme_preliminary.calculator.Keypad_Fragment.KeypadCreator;
import com.example.scheme_preliminary.calculator.Op_Selection_Fragment.OpSelector;

import evaluator.Evaluator;

public class Calculator_Activity extends Activity implements KeypadCreator, IdCreator, IdSelector, OpSelector {
	
	private enum Mode {
		WAITING, INT_OR_ID, BIND_ID, OTHER;
	}
	private final List<String> OPLIST = Arrays.asList(new String[] { "+", "*", "-", "quotient", "if", "let", "lambda" });
	private int[] numberButtonIds;
	
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
	    this.fragFrame.setBackgroundColor(Color.argb(255, 200, 200, 200));
	    
	    Keypad_Fragment keypadFragment = new Keypad_Fragment();
        getFragmentManager().beginTransaction()
        					.add(R.id.FrameLayout1, keypadFragment, "keypad")
        					.commit();
	    
        
        this.numberButtonIds = new int[] { R.id.Button0, R.id.Button1, R.id.Button2,
        								   R.id.Button3, R.id.Button4, R.id.Button5,
        								   R.id.Button6, R.id.Button7, R.id.Button8, R.id.Button9 };
        
	    this.textView = (TextView) findViewById(R.id.textView1);

	    this.currentInt = null;
	    this.stack = new Stack<Pair<Expression, List<Expression>>>();
	    this.fullExpression = null;
	}
	
	@Override
	public void onKeypadCreated() {
    	setMode(Mode.WAITING);
	}
	@Override
	public void onOpSelected(View v) {
		onButtonClick(v);
	}
	@Override
	public void onIdSelected(String id) {
		getFragmentManager().popBackStackImmediate();
		handleToken(id, true);
	}
	@Override
	public List<String> getListSource() {
//		return new LinkedList<String>(); // temporary
		return Arrays.asList(new String[] { "var_1", "var_2", "some_other_variable", "etc..." });
	}
	@Override
	public void onIdCreated(String id) {
		getFragmentManager().popBackStackImmediate();
		if (!id.equals(""))
			handleToken(id, true);
	}
	
	private void createFragmentOfType(Class c) {
		Fragment frag;
		if (c.equals(Id_Selection_Fragment.class))
			frag = new Id_Selection_Fragment();
		else if (c.equals(Id_Creation_Fragment.class))
			frag = new Id_Creation_Fragment();
		else if (c.equals(Op_Selection_Fragment.class))
			frag = new Op_Selection_Fragment();
		else return;
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout1, frag);
		transaction.addToBackStack(null);
		transaction.commit();
	}
			
	public void onNewFragmentNeeded(View v) {
		String token = ((Button) v).getText().toString();
		if (getString(R.string.varCreate).equals(token))
			createFragmentOfType(Id_Creation_Fragment.class);
		else if (getString(R.string.varSelect).equals(token))
			createFragmentOfType(Id_Selection_Fragment.class);
		else if (getString(R.string.opSelect).equals(token))
			createFragmentOfType(Op_Selection_Fragment.class);
	}
	
    public void onButtonClick(View v) {
    	String token = ((Button) v).getText().toString();
    	handleToken(token, false);
    }
    
    public void handleToken(String token, boolean isId) {
    	String text = this.textView.getText().toString();
    	token = visualRepresentation(token);
    	System.out.println(token);

    	if (this.mode.equals(Mode.INT_OR_ID)) {
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
    			if (this.fullExpression != null) {
    				text = "";
    				this.fullExpression = null;
    			}
    			if (text != "") text += " ";
    			text += "(" + token;
    			if (token.equals("if"))
    				this.stack.push(new Pair<Expression, List<Expression>>(new IfExpression(null, null, null), new LinkedList<Expression>()));
    			else
    				this.stack.push(new Pair<Expression, List<Expression>>(new IdExpression(token), new LinkedList<Expression>()));
    		}
    		else { // it's the start of a number or variable ID
    			if (this.fullExpression != null) {
    				text = "";
    				this.fullExpression = null;
    			}
    			if (! text.equals(""))
    				text += " ";
    			text += token;
    			if (isId) {
    				System.out.println("THE ID WE'RE INSERTING IS " + token);
        			IdExpression id = new IdExpression(token);
        			if (this.stack.isEmpty())
        				this.fullExpression = id;
        			else {
        				this.stack.peek().second.add(id);
        			}
        			
        			while (! this.stack.isEmpty() && topExpressionComplete()) {
        				Expression exp = popTopExpression();
        				text += ")";
        				
        				if (this.stack.isEmpty())
        					this.fullExpression = exp;
        				else
        					this.stack.peek().second.add(exp);
        			}
    			}
    			else {
	    			this.currentInt = Integer.parseInt(token);
	    			setMode(Mode.INT_OR_ID);
    			}
    		}
    	}
    	
    	if (this.fullExpression != null) {
    		if (this.fullExpression instanceof IdExpression) 
    			text = ("ID: " + ((IdExpression) this.fullExpression).getId());
    		else {
    			try {
    				text += ("\n=\n" + Evaluator.evaluate(this.fullExpression).getValue());
    			}
    			catch (Exception e) {
    				text += ("\n\nCannot be evaluated");
    				System.out.println(e);
    			}
    		}
    					
    	}
//    	if (this.fullExpression != null) text = Unparser.unparse(this.fullExpression) + "\n=\n" + Evaluator.evaluate(this.fullExpression);
    	this.textView.setText(text);
    }
    
    private String visualRepresentation(String token) {
    	System.out.println(getString(R.string.DividedBy));
//    	if (getString(R.string.DividedBy).equals(token))
    	if (token.equals("/"))
    		return "quotient";
    	else if (getString(R.string.lambda).equals(token))
    		return "lambda";
    	else return token;
    	
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
    		activateButton(R.id.opSelectButton);
    		activateButton(R.id.IfButton);
    		activateButton(R.id.varCreateButton);
    		activateButton(R.id.varSelectButton);
    		for (int number : this.numberButtonIds)
    			activateButton(number); // activate number buttons
    	}
    	else if (mode.equals(Mode.INT_OR_ID)) {
    		activateButton(R.id.EnterButton);
    		disactivateButton(R.id.opSelectButton);
    		disactivateButton(R.id.IfButton);
    		disactivateButton(R.id.varCreateButton);
    		disactivateButton(R.id.varSelectButton);
    		for (int number : this.numberButtonIds)
    			activateButton(number);
    	}
    	else if (mode.equals(Mode.BIND_ID)) {
    		disactivateButton(R.id.EnterButton);
    		disactivateButton(R.id.opSelectButton);
    		disactivateButton(R.id.IfButton);
    		activateButton(R.id.varCreateButton);
    		activateButton(R.id.varSelectButton);
    		for (int number : this.numberButtonIds) 
    			disactivateButton(number);
    	}
    }
	
    private void activateButton(int id) {
    	View v = this.fragFrame.findViewById(id);
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
    	View v = this.fragFrame.findViewById(id);
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
