package com.example.scheme_preliminary;

import java.util.HashMap;
import java.util.List;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import unparser.Unparser;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Navigation_Box_Activity extends Activity {
    LinearLayout base;
    Expression ast;
    Bundle extras;
    String schemeText;
    ScrollView sv;
    LinearLayout background;
    int idGenerator;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation__box_);
		extras=getIntent().getExtras();
		if(extras!=null)
		{
			schemeText=extras.getString("schemeText");
			List<Token> tokens=Lexer.lex(schemeText);
			ast = Parser.parse(tokens);
			if(ast!= null) 
			{
				sv=(ScrollView) findViewById(R.id.scrollView1);
				background=(LinearLayout) findViewById(R.id.ll_base);
				background.addView(createBox(ast));//null pointer exception
				idGenerator=0;
			}
		}		
	}

	public RelativeLayout createBox(Expression ast)
	{
		return createBox(ast,1);
	}
	
	public RelativeLayout createBox(Expression ast,int depth)
	{ 
		if(ast instanceof IntExpression)
		{
			return intBox(ast);
		}
		else if(ast instanceof IdExpression)
		{
			return idBox(ast);
		}
		else if(ast instanceof CallExpression)
		{
			return callBox(ast,depth);
		}
		else if(ast instanceof IfExpression)
		{
			return ifBox(ast,depth);
		}
		else if(ast instanceof LetExpression)
		{
			return letBox(ast,depth);
		}
		else if(ast instanceof LambdaExpression)
		{
			return lambdaBox(ast,depth);
		}
		else
			return blankBox();
	}	
	
	private RelativeLayout intBox(Expression ast)
	{
		RelativeLayout toReturn=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		toReturn.setLayoutParams(paramsReturn);
		
		TextView tv=new TextView(this);
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		tv.setLayoutParams(params);

		tv.setText(Unparser.unparse(ast));
		toReturn.addView(tv);
		
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}

	private RelativeLayout idBox(Expression ast)
	{
		RelativeLayout toReturn=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		toReturn.setLayoutParams(paramsReturn);
		
		TextView tv=new TextView(this);
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		tv.setLayoutParams(params);
		tv.setText(Unparser.unparse(ast));
		toReturn.addView(tv);
		
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}
	
	private RelativeLayout callBox(Expression ast,int depth)
	{
		final RelativeLayout toReturn=new RelativeLayout(this);

		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		//------SET THE MARGINS ACCORDING TO THE DEPTH PASSED IN!
		paramsReturn.setMargins(10*depth, 20, 20, 20);
		toReturn.setLayoutParams(paramsReturn);
		//------SET THE MARGINS ACCORDING TO THE DEPTH PASSED IN!
		
		final RelativeLayout toReturnOperator=createBox(((CallExpression)ast).getOperator(),depth+1);
		
		//OPERANDS
		final RelativeLayout toReturnOperands=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsOperands=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		//------SET THE MARGINS OF OPERANDS ACCORDING TO THE DEPTH PASSED IN!
		paramsOperands.setMargins(10, 0, 0, 0);
		toReturnOperands.setLayoutParams(paramsOperands);
		//------SET THE MARGINS ACCORDING TO THE DEPTH PASSED IN!
		
		//-------------------------------------------

		RelativeLayout blank=createBox(null);
		toReturnOperands.addView(blank);
		int previousId=blank.getId();
		//NEED FURTHER REVISE!! 
		//current may not need to be assigned with an id because it is assigned with an id when it is created!
		for(Expression expression:((CallExpression)ast).getOperands())
		{
			RelativeLayout current=createBox(expression,depth+1);
			RelativeLayout.LayoutParams currentLayoutParams=(RelativeLayout.LayoutParams) current.getLayoutParams();
			currentLayoutParams.addRule(RelativeLayout.BELOW,previousId);
			System.out.printf("WE'RE PRINTING THE PREVIOUS ID HERE! %d\n",previousId);
			current.setLayoutParams(currentLayoutParams);
			toReturnOperands.addView(current);
			previousId=current.getId();
			//id always keeps track of the previously added element
		}

		//-------ADD VIEWS ON THE BACKGROUND
		toReturn.addView(toReturnOperator);
		toReturn.addView(toReturnOperands);
		
		final TextView dotdotdot=new TextView(this);
		dotdotdot.setText("...");
		
		toReturn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toReturn.getChildCount()>1)
				{
					toReturn.removeAllViews();
					toReturn.addView(dotdotdot);	
				}
				else
				{
					dotdotdot.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							toReturn.addView(toReturnOperator);
							toReturn.addView(toReturnOperands);
							toReturn.removeView(dotdotdot);
						}
					});
				}
			}	
		});
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}

	private RelativeLayout ifBox(Expression ast,int depth)
	{
		//--------------BASE----------------
		final RelativeLayout toReturn=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		//left top right bottom
		paramsReturn.setMargins(10*depth, 20, 20, 20);
		toReturn.setLayoutParams(paramsReturn);
		//--------------BASE----------------
		
		
		//--------------HEAD----------------
		final TextView head=new TextView(this);
		RelativeLayout.LayoutParams headParams=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		head.setLayoutParams(headParams);
		head.setTextColor(Color.RED);
		head.setText("if");
		head.setId(idGenerator);
		idGenerator++;
		//-------------HEAD-----------------
		
		
		//-------------TO RETURN ABOVE------
		final RelativeLayout toReturnAbove=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsAbove=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		toReturnAbove.setLayoutParams(paramsAbove);
		
		RelativeLayout condition=createBox(((IfExpression)ast).getCondition(),depth+1);
		RelativeLayout then=createBox(((IfExpression)ast).getThen(),depth+1);
		RelativeLayout otherwise=createBox(((IfExpression)ast).getElse(),depth+1);
		
		toReturnAbove.addView(condition);
		
		RelativeLayout.LayoutParams then_params=(RelativeLayout.LayoutParams)then.getLayoutParams();
		then_params.addRule(RelativeLayout.BELOW,condition.getId());
		System.out.println(condition.getId());
		then.setLayoutParams(then_params);
		toReturnAbove.addView(then);
		
		RelativeLayout.LayoutParams otherwise_params=(RelativeLayout.LayoutParams)otherwise.getLayoutParams();
		otherwise_params.addRule(RelativeLayout.BELOW,then.getId());
		otherwise.setLayoutParams(otherwise_params);
		toReturnAbove.addView(otherwise);
		
		toReturn.addView(head);
		toReturn.addView(toReturnAbove);
		
		final TextView dotdotdot=new TextView(this);
		dotdotdot.setText("if...");
		
		toReturn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toReturn.getChildCount()>1)
				{
					toReturn.removeAllViews();
					toReturn.addView(dotdotdot);	
				}
				else
				{
					dotdotdot.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							toReturn.addView(head);
							toReturn.addView(toReturnAbove);
							toReturn.removeView(dotdotdot);
						}
					});
				}
			}	
		});
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}
	
    private RelativeLayout letBox(Expression ast,int depth)
    {
    	//-------------BASE------------
    	final RelativeLayout toReturn=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		//left top right bottom
		paramsReturn.setMargins(10*depth, 20, 20, 20);
		toReturn.setLayoutParams(paramsReturn);
		//-------------BASE------------
		
		//-------------HEAD------------
		final TextView head=new TextView(this);
		RelativeLayout.LayoutParams headParams=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		head.setLayoutParams(headParams);
		head.setTextColor(Color.RED);
		head.setText("let");
		head.setId(idGenerator);
		idGenerator++;
		//------------HEAD-------------
		
		//------------TO RETURN ABOVE------------
		final RelativeLayout toReturnAbove=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsAbove=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsAbove.addRule(RelativeLayout.BELOW, head.getId());
		toReturnAbove.setLayoutParams(paramsAbove);
		
		//a RelativeLayout for bindings 
		//a RelativeLayout for body(which is an expression)
		RelativeLayout bindings=bindingsBox(((LetExpression)ast).getBindings());
		toReturnAbove.addView(bindings);
		
		RelativeLayout body=createBox(((LetExpression) ast).getBody(),depth+1);
		RelativeLayout.LayoutParams paramsBody=(RelativeLayout.LayoutParams) body.getLayoutParams();
		paramsBody.addRule(RelativeLayout.BELOW, bindings.getId());
		toReturnAbove.addView(body);
		
		toReturn.addView(head);
		toReturn.addView(toReturnAbove);
		
		final TextView dotdotdot=new TextView(this);
		dotdotdot.setText("let...");
		
		toReturn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toReturn.getChildCount()>1)
				{
					toReturn.removeAllViews();
					toReturn.addView(dotdotdot);	
				}
				else
				{
					dotdotdot.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							toReturn.addView(head);
							toReturn.addView(toReturnAbove);
							toReturn.removeView(dotdotdot);
						}
					});
				}
			}	
		});
		toReturn.setId(idGenerator);
		idGenerator++;
		
    	return toReturn;
    }
    //----------------------------------------
    //bindingsBox is a helper method for letBox to generate a relativelayout for bindings
    private RelativeLayout bindingsBox(HashMap<String,Expression> bindings)
    {
    	RelativeLayout toReturn=new RelativeLayout(this);
    	RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    	paramsReturn.setMargins(10, 0, 0, 0);
    	//set the layout here
    	LinearLayout keys=new LinearLayout(this);
    	LinearLayout values=new LinearLayout(this);
    	
    	LinearLayout.LayoutParams paramsKeys=(LinearLayout.LayoutParams)keys.getLayoutParams();
    	paramsKeys.weight=1;
    	keys.setLayoutParams(paramsKeys);
    	keys.setOrientation(LinearLayout.VERTICAL);
    	
    	LinearLayout.LayoutParams paramsValues=(LinearLayout.LayoutParams) values.getLayoutParams();
    	paramsValues.weight=2;
    	values.setLayoutParams(paramsValues);
    	values.setOrientation(LinearLayout.VERTICAL);
    	
    	for(String s:bindings.keySet())
    	{
    		TextView tv=new TextView(this);
    		tv.setText(s);
    		keys.addView(tv);
    	}
    	
    	for(Expression expression:bindings.values())
    	{
    		values.addView(createBox(expression));
    	}
    	
    	LinearLayout toReturnAbove=new LinearLayout(this);
    	LinearLayout.LayoutParams paramsAbove=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    	paramsAbove.weight=3;
    	toReturnAbove.setLayoutParams(paramsAbove);
    	toReturnAbove.setOrientation(LinearLayout.HORIZONTAL);
    	toReturnAbove.addView(keys);
    	toReturnAbove.addView(values);
    	toReturn.addView(toReturnAbove);
    	toReturn.setId(idGenerator);
    	idGenerator++;
    	return toReturn;
    }
    public RelativeLayout lambdaBox(Expression ast,int depth)
    {
    	//--------------BASE----------------
    	final RelativeLayout toReturn=new RelativeLayout(this);
    	RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    	paramsReturn.setMargins(10*depth, 20, 20, 20);
    	toReturn.setLayoutParams(paramsReturn);
    	//--------------BASE----------------
    	
    	//--------------HEAD----------------
    	final TextView head=new TextView(this);
    	RelativeLayout.LayoutParams headParams=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		head.setLayoutParams(headParams);
		head.setTextColor(Color.RED);
		head.setText("let");
		head.setId(idGenerator);
		idGenerator++;
		//-------------HEAD-----------------
		
		//-------------TORETURNABOVE
		final RelativeLayout toReturnAbove=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsAbove=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsAbove.addRule(RelativeLayout.BELOW, head.getId());
		toReturnAbove.setLayoutParams(paramsAbove);
		
		LinearLayout parameters=new LinearLayout(this);
		parameters.setId(idGenerator);
		idGenerator++;
		parameters.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		parameters.setOrientation(LinearLayout.VERTICAL);
		
		for(String s:((LambdaExpression)ast).getParameters())
		{
			TextView tv=new TextView(this);
			tv.setText(s);
			parameters.addView(tv);
		}
		
		RelativeLayout body=createBox(((LambdaExpression)ast).getBody(),depth+1);
		RelativeLayout.LayoutParams paramsBody=(RelativeLayout.LayoutParams) body.getLayoutParams();
		paramsBody.addRule(RelativeLayout.BELOW, parameters.getId());
		body.setLayoutParams(paramsBody);
		
		toReturnAbove.addView(parameters);
		toReturnAbove.addView(body);
		
		toReturn.addView(head);
		toReturn.addView(toReturnAbove);
		
		final TextView dotdotdot=new TextView(this);
		dotdotdot.setText("lambda...");
		
		toReturn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toReturn.getChildCount()>1)
				{
					toReturn.removeAllViews();
					toReturn.addView(dotdotdot);	
				}
				else
				{
					dotdotdot.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							toReturn.addView(head);
							toReturn.addView(toReturnAbove);
							toReturn.removeView(dotdotdot);
						}
					});
				}
			}	
		});
		toReturn.setId(idGenerator);
		idGenerator++;
    	return toReturn;
    }


	public RelativeLayout blankBox()
	{
		RelativeLayout toReturn=new RelativeLayout(this);
		TextView blank=new TextView(this);
		blank.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		//blank.setText("THIS IS A BLANK BOX!!!");
		blank.setBackgroundColor(Color.TRANSPARENT);
		toReturn.addView(blank);
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}

}
