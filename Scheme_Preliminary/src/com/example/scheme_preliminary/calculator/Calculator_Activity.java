package com.example.scheme_preliminary.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import unparser.Unparser;
import util.Pair;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.scheme_preliminary.R;

import evaluator.Evaluator;

public class Calculator_Activity extends Activity implements Calculator_Fragment_Listener {
	
	private enum Mode {
		WAITING, INTEGER, BINDING_STRING, OTHER;
	}
	private final Object doneMarker = Integer.valueOf(-42);
	private LinkedList<String> OPLIST = new LinkedList<String>(Arrays.asList(new String[] { "+", "*", "-", "quotient", "if", "let", "lambda" }));
	
	private LinkedList<String> bindings;
	private int[] numberButtonIds;
	
	private Mode mode;
	private Integer currentInt;
	private Stack<Pair<Expression, List<Object>>> stack;
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
	    
        this.bindings = new LinkedList<String>();
        
        this.numberButtonIds = new int[] { R.id.Button0, R.id.Button1, R.id.Button2,
        								   R.id.Button3, R.id.Button4, R.id.Button5,
        								   R.id.Button6, R.id.Button7, R.id.Button8, R.id.Button9 };
        
	    this.textView = (TextView) findViewById(R.id.textView1);
	    this.textView.setMovementMethod(new ScrollingMovementMethod());

	    this.mode = Mode.WAITING;
	    this.currentInt = null;
	    this.stack = new Stack<Pair<Expression, List<Object>>>();
	    this.fullExpression = null;
	}
	
	@Override
	public void onKeypadCreated() {
    	setMode(this.mode);
	}
	@Override
	public void onOpSelected(View v) {
		getFragmentManager().popBackStackImmediate();
		onButtonClick(v);
	}
	@Override
	public void onIdSelected(String id) {
		getFragmentManager().popBackStackImmediate();
		handleToken(id, true);
	}
	@Override
	public List<String> getListSource() {
		return this.bindings;
	}
	@Override
	public void onStringCreated(String id) {
		getFragmentManager().popBackStackImmediate();
		if (!id.equals(""))
			handleToken(id, true);
	}
	
	private void createFragmentOfType(Class<? extends Fragment> c) {
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
		if (getString(R.string.varCreate).equals(token)) {
			createFragmentOfType(Id_Creation_Fragment.class);
		}
		else if (getString(R.string.varSelect).equals(token))
			createFragmentOfType(Id_Selection_Fragment.class);
		else if (getString(R.string.opSelect).equals(token))
			createFragmentOfType(Op_Selection_Fragment.class);
	}
	
    public void onButtonClick(View v) {
    	String token = ((Button) v).getText().toString();
    	handleToken(token, false);
    }
    
    public void handleToken(String token, boolean isString) {
    	String text = this.textView.getText().toString();
    	token = visualRepresentation(token);
    	System.out.println("Mode: " + this.mode);
    	System.out.println("Token: " + token);
    	
    	switch (this.mode) {
    	case INTEGER:
    		if (token.equals("Enter")) {
    			IntExpression num = new IntExpression(this.currentInt);
    			this.currentInt = null;
    			handleExpressionEntered(num);
    			condenseStackWhilePossible();
    			if (! this.stack.isEmpty() && this.stack.peek().first instanceof LetExpression)
    				setMode(Mode.BINDING_STRING);
    			else setMode(Mode.WAITING);
    		}
    		else { // adding to currentInt
    			this.currentInt = this.currentInt * 10 + Integer.parseInt(token);
    			text += token;
    		}
    		break;
    		
    	case WAITING:
    		if (OPLIST.contains(token)) { // if it's an operator
    			if (this.fullExpression != null) {
    				text = "";
    				this.fullExpression = null;
    			}
    			if (text != "") text += " ";
    			text += "(" + token;
    			if (token.equals("if"))
    				this.stack.push(new Pair<Expression, List<Object>>(new IfExpression(null, null, null), new ArrayList<Object>()));
    			else if (token.equals("let")) {
    				this.stack.push(new Pair<Expression, List<Object>>(new LetExpression(null, null), new ArrayList<Object>()));
    				setMode(Mode.BINDING_STRING);
//    				System.out.println("Current mode is: " + this.mode);
    			}
    			else if (token.equals("lambda")) {
    				this.stack.push(new Pair<Expression, List<Object>>(new LambdaExpression(null, null), new ArrayList<Object>()));
    				setMode(Mode.BINDING_STRING);
    			}
    			else
    				this.stack.push(new Pair<Expression, List<Object>>(new IdExpression(token), new ArrayList<Object>()));
    		}
    		else if (token.equals("Enter")) {
    			this.stack.peek().second.add(doneMarker);
    			condenseStackWhilePossible();
    			// if you land on an uncondensed LetExpression, it's waiting for more bindings
    			if (! this.stack.isEmpty() && this.stack.peek().first instanceof LetExpression)
    				setMode(Mode.BINDING_STRING);
    			else setMode(Mode.WAITING);
    		}
    		else { // it's the start of a number or variable ID
    			if (this.fullExpression != null)
    				this.fullExpression = null;
    			if (isString) {
        			IdExpression id = new IdExpression(token);
        			
        			handleExpressionEntered(id);
        			condenseStackWhilePossible();
    			}
    			else {
	    			this.currentInt = Integer.parseInt(token);
	    			setMode(Mode.INTEGER);
    			}
    		}
    		break;
    		
    	case BINDING_STRING:
    		if (token.equals("Enter")) {
    			this.stack.peek().second.add(this.doneMarker);
//    			if (this.stack.peek().first instanceof LetExpression) {  this or lambda should always be the case for BINDING_STRING
				Iterator<Object> iter = this.stack.peek().second.iterator();
				Object obj;
				String s;
    			while (iter.hasNext()) {
    				obj = iter.next();
					if (obj instanceof String) {
						s = (String) obj;
						if (iter.next() instanceof LambdaExpression) {
							this.OPLIST.add(s);
						}
						this.bindings.add(s);
					}
				}
//    			}
    			setMode(Mode.WAITING);
    		}
    		else {
				this.stack.peek().second.add(token);
				if (this.stack.peek().first instanceof LetExpression)
					setMode(Mode.WAITING);
				else // Lambda
					setMode(Mode.BINDING_STRING);
    		}
			break;
			
		default:
			System.out.println("?");
    	}
    	
    	
    	if (this.fullExpression != null) {
    		if (this.fullExpression instanceof IdExpression) 
    			text = ("ID: " + ((IdExpression) this.fullExpression).getId());
    		else {
    			try {
//    				text = Integer.toString(Evaluator.evaluate(this.fullExpression).getValue());
//    				text = Unparser.unparse(this.fullExpression);
//    				text += "\n=\n" + Evaluator.evaluate(this.fullExpression).getValue();
    			}
    			catch (Exception e) {
    				text += ("\n\nCannot be evaluated");
    				System.out.println(e);
    			}
    		}
    	}
    	else text = unparseStack();
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
    	Pair<Expression, List<Object>> pair = this.stack.peek();
		int size = pair.second.size();
    	if (pair.first instanceof IfExpression)
    		return size == 3;
    	else if (pair.first instanceof LetExpression ||
    			pair.first instanceof LambdaExpression) {
    		return (size > 1 && pair.second.get(size-2) instanceof Integer);
    	}
    	else
    		return pair.second.get(size-1) instanceof Integer;
    }
    
    @SuppressWarnings("unchecked")
	private Expression popTopExpression() {
    	Expression exp;
		Pair<Expression, List<Object>> pair = this.stack.pop();
		if (pair.first instanceof IfExpression)
			exp = new IfExpression((Expression) pair.second.get(0), (Expression) pair.second.get(1), (Expression) pair.second.get(2));
		else if (pair.first instanceof LetExpression) {
			List<Pair<String, Expression>> localBindings = new ArrayList<Pair<String, Expression>>();
			List<Object> args = pair.second;
			String binding;
			Expression boundExpression;
			while (args.size() > 2) {
				binding = (String) args.remove(0);
				boundExpression = (Expression) args.remove(0);
				this.bindings.removeLastOccurrence(binding);
				if (boundExpression instanceof LambdaExpression)
					this.OPLIST.removeLastOccurrence(binding);
				localBindings.add(new Pair<String, Expression>(binding, boundExpression));
			}
			args.remove(0); // 'finished' marker
			exp = new LetExpression(localBindings, (Expression) args.remove(0));
		}
		else if (pair.first instanceof LambdaExpression) {
			ArrayList<String> localParams = new ArrayList<String>();
			List<Object> args = pair.second;
			String param;
			while (args.size() > 2) {
				param = (String) args.remove(0);
				this.bindings.removeLastOccurrence(param);
				localParams.add(param);
			}
			args.remove(0); // 'finished' marker
			exp = new LambdaExpression(localParams, (Expression) args.remove(0));
		}
		else { // presumable Call expression
			pair.second.remove(pair.second.size()-1);
			exp = new CallExpression(pair.first, (List<Expression>)(List<?>) pair.second);
		}
		return exp;
    }
    
    private void handleExpressionEntered(Expression exp) {
		if (this.stack.isEmpty())
			this.fullExpression = exp;
		else {
			this.stack.peek().second.add(exp);
		}
    }
    
	private void condenseStackWhilePossible() {
		while (! this.stack.isEmpty() && topExpressionComplete()) {
			Expression top = popTopExpression();
			
			if (this.stack.isEmpty())
				this.fullExpression = top;
			else
				this.stack.peek().second.add(top);
		}
    }
    
	private void setMode(Mode mode) {
    	this.mode = mode;
    	if (mode.equals(Mode.WAITING)) {
    		if (!this.stack.isEmpty() && this.stack.peek().first instanceof IdExpression)
    			activateButton(R.id.EnterButton);
    		else
    			disactivateButton(R.id.EnterButton);
    		activateButton(R.id.opSelectButton);
    		activateButton(R.id.IfButton);
    		activateButton(R.id.varCreateButton);
    		activateButton(R.id.varSelectButton);
    		for (int number : this.numberButtonIds)
    			activateButton(number); // activate number buttons
    	}
    	else if (mode.equals(Mode.INTEGER)) {
    		disactivateButton(R.id.opSelectButton);
    		disactivateButton(R.id.IfButton);
    		disactivateButton(R.id.varCreateButton);
    		disactivateButton(R.id.varSelectButton);
    		activateButton(R.id.EnterButton);
    		for (int number : this.numberButtonIds)
    			activateButton(number);
    	}
    	else if (mode.equals(Mode.BINDING_STRING)) {
    		disactivateButton(R.id.opSelectButton);
    		disactivateButton(R.id.IfButton);
    		disactivateButton(R.id.varSelectButton);
    		for (int number : this.numberButtonIds) 
    			disactivateButton(number);
    		if (! this.stack.isEmpty() &&
    				(this.stack.peek().first instanceof LetExpression || this.stack.peek().first instanceof LambdaExpression) &&
    				this.stack.peek().second.size() >= 1)
    			activateButton(R.id.EnterButton);
    		else disactivateButton(R.id.EnterButton);
    		activateButton(R.id.varCreateButton);
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
    
    
//    private String unparsedFullExpressionWithoutNewlines() {
//    	return unparsedExpressionWithoutNewlines(this.fullExpression);
//    }
//    private String unparsedExpressionWithoutNewlines(Expression exp) {
//    	String s = Unparser.unparse(exp);
//    	String s2 = "";
//    	char c;
//    	for (int i=0, n=s.length(); i<n; i++) {
//    		if ((c = s.charAt(i)) != '\n' && c != '\t') s2 += c;
//    	}
//    	s = "";
//    	String sub;
//    	for (int i=0, n=s2.length()-1; i<n; i++) {
//    		if ((sub = (s2.substring(i, i+2))) != " )" && sub != "  ") s += s2.charAt(i);
//    	}
//    	if (s2.length() > 0)
//    		s += s2.charAt(s2.length()-1);
//    	return s;
//    }
    
    private String unparseStack() {
		@SuppressWarnings("unchecked")
		List<Pair<Expression, List<Object>>> list = (List<Pair<Expression, List<Object>>>) this.stack.clone();
		return unparseStack(list);
    }
    
    private String unparseStack(List<Pair<Expression, List<Object>>> list) {
    	String s = "";
    	Pair<Expression, List<Object>> pair;
    	if (list.isEmpty()) {
    		if (this.currentInt != null)
    			return " " + this.currentInt + "...";
    		else
    			return " ?";
    	}
    	else pair = list.remove(0);
    	if (pair.first instanceof IdExpression) {
    		s += "(" + ((IdExpression) pair.first).getId() + " ";
        	for (Object obj : pair.second) {
        		if (obj instanceof Expression)
//        			s += Unparser.unparse((Expression) obj);
        			s+="";
        		else if (obj instanceof Integer) {
        		}
        		else
        			s += (String) obj + " ";
        	}
    	}
    	else if (pair.first instanceof LetExpression) {
    		s += "(let (";
        	for (int i=0; i<pair.second.size(); i++) {
        		Object obj = pair.second.get(i);
        		if (obj instanceof Expression) {
//        			s += Unparser.unparse((Expression) obj);
        			s += ")";
        		}
        		else if (obj instanceof Integer) {
        			s += ")";
        		}
        		else
        			s += "(" + (String) obj + " ";
        	}
    	}
    	else if (pair.first instanceof LambdaExpression) {
    		s += "(lambda (";
        	for (int i=0; i<pair.second.size(); i++) {
        		Object obj = pair.second.get(i);
        		if (obj instanceof Expression) {
//        			s += Unparser.unparse((Expression) obj);
        			s += ")";
        		}
        		else if (obj instanceof Integer) {
        			s += " )";
        		}
        		else
        			s += " " + (String) obj;
        	}
    	}
    	else if (pair.first instanceof IfExpression) {
    		s += "(if" + ((IdExpression) pair.first).getId() + " ";
    		if (pair.second.size() > 0) {
//    			s += Unparser.unparse((Expression) pair.second.get(0));
    			if (pair.second.size() > 1)
//    				s += Unparser.unparse((Expression) pair.second.get(1));
    				s+="";
    		}
    	}
    	return s + unparseStack(list);
    }
    

}
