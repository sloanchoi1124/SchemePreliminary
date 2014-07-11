package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.id;
import com.example.scheme_preliminary.R.layout;

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
import util.Pair;
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
//THIS IS AN EXPERIMENTAL DYNAMIC BOX LAYOUT GENERATOR;
//CURRENTLY IT IS ONLY KEPT FOR REFERENCE 
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
		final IntExpression intAst=(IntExpression) ast;
    	final RelativeLayout toReturn=new RelativeLayout(this);
    	
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		toReturn.setLayoutParams(paramsReturn);
		
		final TextView dotdotdot=new TextView(this);
		dotdotdot.setText("(int...)");
		toReturn.addView(dotdotdot);
		
		final TextView tv=new TextView(this);
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(params);
		
		toReturn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toReturn.getChildCount()<=1)
				{
//				    tv.setText(Unparser.unparse(intAst));
					toReturn.addView(tv);
					toReturn.removeView(dotdotdot);
				}
		}});

		toReturn.setId(idGenerator);
		idGenerator++;
    	return toReturn;
    	
    }
	private RelativeLayout idBox(Expression ast)
	{
		final IdExpression idAst=(IdExpression) ast;
    	final RelativeLayout toReturn=new RelativeLayout(this);
    	
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		toReturn.setLayoutParams(paramsReturn);
		
		final TextView dotdotdot=new TextView(this);
		dotdotdot.setText("(id...)");
		toReturn.addView(dotdotdot);
		final TextView tv=new TextView(this);
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(params);
		
		toReturn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toReturn.getChildCount()<=1)
				{
					//if tv has not been set with text
//					tv.setText(Unparser.unparse(idAst));
					toReturn.addView(tv);
					toReturn.removeView(dotdotdot);
				}
	
		}});

		toReturn.setId(idGenerator);
		idGenerator++;
    	return toReturn;
	}
	
	private RelativeLayout callBox(Expression e, int d)
	{
		final CallExpression ast=(CallExpression) e;
		final int depth=d;
		
		final RelativeLayout toReturn=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
	    paramsReturn.setMargins(10*depth, 20, 20, 20);
		toReturn.setLayoutParams(paramsReturn);
		
		final TextView dotdotdot=new TextView(this);
		toReturn.addView(dotdotdot);
		dotdotdot.setText("(call expression...)");
		
		toReturn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					final RelativeLayout toReturnOperator=createBox(((CallExpression)ast).getOperator(),depth+1);
					//if all the operands arn't recursive, then put them in one line
					//else...
					boolean line=true;
					for(Expression expression:((CallExpression)ast).getOperands())
					{
						if(!(expression instanceof IntExpression||expression instanceof IdExpression))
						{
							line=false;
							break;
						}
					}
					
					//if temp is true, then put them in one line
					if(line==true)
					{
						final RelativeLayout toReturnAbove=new RelativeLayout(v.getContext());
						//--------------------------------------------------
						//WHAT IS THE CONTEXT OF THIS TORETURNABOVE???????????
						//--------------------------------------------------
						
						RelativeLayout.LayoutParams paramsReturnAbove=new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
						toReturnAbove.setLayoutParams(paramsReturnAbove);
						
						
						//--------add operator
						toReturnAbove.addView(toReturnOperator);
						int previousId=toReturnOperator.getId();
						
						//--------add operands
						for(Expression expression:((CallExpression)ast).getOperands())
						{
							RelativeLayout current=createBox(expression);
							RelativeLayout.LayoutParams paramsCurrent=(RelativeLayout.LayoutParams) current.getLayoutParams();
							paramsCurrent.addRule(RelativeLayout.RIGHT_OF,previousId);
							current.setLayoutParams(paramsCurrent);
							toReturnAbove.addView(current);
							previousId=current.getId();
							System.out.println("added "+current.toString());
						}
						toReturn.addView(dummyBox());
						toReturn.addView(toReturnAbove);
					    toReturn.removeView(dotdotdot);
				    }
					
					else
					{
						System.out.println("will print in a column");
						//OPERANDS
						//----------------------------------------------------------------
						final RelativeLayout toReturnAbove=new RelativeLayout(v.getContext());
						RelativeLayout.LayoutParams paramsOperands=new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
						//------SET THE MARGINS OF OPERANDS ACCORDING TO THE DEPTH PASSED IN!
						toReturnAbove.setLayoutParams(paramsOperands);
						toReturnAbove.addView(toReturnOperator);
						int previousId=toReturnOperator.getId();

						for(Expression expression:((CallExpression)ast).getOperands())
						{
							RelativeLayout current=createBox(expression,depth+1);
							RelativeLayout.LayoutParams currentLayoutParams=(RelativeLayout.LayoutParams) current.getLayoutParams();
							currentLayoutParams.addRule(RelativeLayout.BELOW,previousId);
							current.setLayoutParams(currentLayoutParams);
							toReturnAbove.addView(current);
							previousId=current.getId();
						}

						//-------ADD VIEWS ON THE BACKGROUND
						toReturn.addView(toReturnAbove);
						toReturn.removeView(dotdotdot);
					}
				
			}	
		});
		
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
		
	}
	
	/*
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
		//if all the operands arn't recursive, then put them in one line
		//else...
		boolean line=true;
		for(Expression expression:((CallExpression)ast).getOperands())
		{
			if(!(expression instanceof IntExpression||expression instanceof IdExpression))
			{
				line=false;
				break;
			}
		}
		
		//if temp is true, then put them in one line
		if(line==true)
		{
			
			//I'm adding this dummy box in order to set the on click listener
			toReturn.addView(dummyBox());
			final RelativeLayout toReturnAbove=new RelativeLayout(this);
			RelativeLayout.LayoutParams paramsReturnAbove=new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			toReturnAbove.setLayoutParams(paramsReturnAbove);
			toReturnAbove.addView(toReturnOperator);
			int previousId=toReturnOperator.getId();
			for(Expression expression:((CallExpression)ast).getOperands())
			{
				RelativeLayout current=createBox(expression);
				RelativeLayout.LayoutParams paramsCurrent=(RelativeLayout.LayoutParams) current.getLayoutParams();
				paramsCurrent.addRule(RelativeLayout.RIGHT_OF,previousId);
				current.setLayoutParams(paramsCurrent);
				toReturnAbove.addView(current);
				previousId=current.getId();
				System.out.println("added "+current.toString());
			}
			
			toReturn.addView(toReturnAbove);
			
			final TextView dotdotdot=new TextView(this);
			dotdotdot.setText("(call expression...)");
			toReturn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
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
								toReturn.addView(dummyBox());
								toReturn.addView(toReturnAbove);
								toReturn.removeView(dotdotdot);
							}
						});
					}
				}
				
			});
		}
		else
		{
			System.out.println("will print in a column");
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

			for(Expression expression:((CallExpression)ast).getOperands())
			{
				RelativeLayout current=createBox(expression,depth+1);
				RelativeLayout.LayoutParams currentLayoutParams=(RelativeLayout.LayoutParams) current.getLayoutParams();
				currentLayoutParams.addRule(RelativeLayout.BELOW,previousId);
				current.setLayoutParams(currentLayoutParams);
				toReturnOperands.addView(current);
				previousId=current.getId();
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
		}
		
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}
    */
	private RelativeLayout ifBox(Expression e,int d)
	{
		final IfExpression ast=(IfExpression) e;
		final int depth=d;
		
		final RelativeLayout toReturn=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
	    paramsReturn.setMargins(10*depth, 20, 20, 20);
		toReturn.setLayoutParams(paramsReturn);
		
		final TextView dotdotdot=new TextView(this);
		toReturn.addView(dotdotdot);
		dotdotdot.setText("(if expression...)");
		
		toReturn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//-----actually i have no idea why i need to manually increment the idgenerator here------
				idGenerator++;
				//----------------------------------------------------------------------------------------
				final TextView head=new TextView(v.getContext());
				RelativeLayout.LayoutParams temp=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				//temp.addRule(RelativeLayout.BELOW, head.getId());
				head.setText("if");
				head.setTextColor(Color.RED);
				head.setLayoutParams(temp);
				head.setId(idGenerator);
				idGenerator++;
				
				final RelativeLayout conditionReturn=new RelativeLayout(v.getContext());
				RelativeLayout.LayoutParams paramsCondition=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsCondition.addRule(RelativeLayout.BELOW,head.getId());
				conditionReturn.setLayoutParams(paramsCondition);
				conditionReturn.setId(idGenerator);
				idGenerator++;
				
				
				final RelativeLayout thenReturn=new RelativeLayout(v.getContext());
				RelativeLayout.LayoutParams paramsThen=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsThen.addRule(RelativeLayout.BELOW,conditionReturn.getId());
				thenReturn.setLayoutParams(paramsThen);
				thenReturn.setId(idGenerator);
				idGenerator++;
				
				final RelativeLayout elseReturn=new RelativeLayout(v.getContext());
				RelativeLayout.LayoutParams paramsElse=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsElse.addRule(RelativeLayout.BELOW,thenReturn.getId());
				elseReturn.setLayoutParams(paramsElse);
				elseReturn.setId(idGenerator);
				idGenerator++;
				
				final TextView dotdotdot_condition=new TextView(v.getContext());
				dotdotdot_condition.setText("(condition...)");
				conditionReturn.addView(dotdotdot_condition);
				final TextView dotdotdot_then=new TextView(v.getContext());
				dotdotdot_then.setText("(then...)");
				thenReturn.addView(dotdotdot_then);
				final TextView dotdotdot_else=new TextView(v.getContext());
				dotdotdot_else.setText("(else...)");
				elseReturn.addView(dotdotdot_else);
				
				
				toReturn.addView(head);
				toReturn.addView(conditionReturn);
				toReturn.addView(thenReturn);
				toReturn.addView(elseReturn);
				toReturn.removeView(dotdotdot);
				
				conditionReturn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						conditionReturn.addView(createBox(ast.getCondition()));
						conditionReturn.removeView(dotdotdot_condition);
					}	
				});
				
				thenReturn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						thenReturn.addView(createBox(ast.getThen()));
						thenReturn.removeView(dotdotdot_then);
					}
				});
				
				elseReturn.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						elseReturn.addView(createBox(ast.getElse()));
						elseReturn.removeView(dotdotdot_else);
					}
					
				});
				
				
				
			}
			
		});
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}
	/*
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
	*/
	
	private RelativeLayout letBox(Expression e,int d)
	{
		final LetExpression ast=(LetExpression) e;
		final int depth=d;
		final RelativeLayout toReturn=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsReturn.setMargins(10*depth, 20, 20, 20);
		toReturn.setLayoutParams(paramsReturn);
		
		final TextView dotdotdot=new TextView(this);
		dotdotdot.setText("(let...)");
		toReturn.addView(dotdotdot);
		
		toReturn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//------head--------
				idGenerator++;
				final TextView head=new TextView(v.getContext());
				RelativeLayout.LayoutParams headParams=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				head.setLayoutParams(headParams);
				head.setTextColor(Color.RED);
				head.setText("let");
				head.setId(idGenerator);
				idGenerator++;
				//-----head--------
				
				final RelativeLayout bindingsReturn=new RelativeLayout(v.getContext());
				RelativeLayout.LayoutParams paramsBindings=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsBindings.addRule(RelativeLayout.BELOW,head.getId());
				bindingsReturn.setLayoutParams(paramsBindings);
				bindingsReturn.setId(idGenerator);
				idGenerator++;
				
				final RelativeLayout bodyReturn=new RelativeLayout(v.getContext());
				RelativeLayout.LayoutParams paramsBody=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsBody.addRule(RelativeLayout.BELOW,bindingsReturn.getId());
				bodyReturn.setLayoutParams(paramsBody);
				bodyReturn.setId(idGenerator);
				idGenerator++;
				
				final TextView dotdotdot_bindings=new TextView(v.getContext());
				dotdotdot_bindings.setText("(bindings...)");
				bindingsReturn.addView(dotdotdot_bindings);
				
				final TextView dotdotdot_body=new TextView(v.getContext());
				dotdotdot_body.setText("(body...)");
				bodyReturn.addView(dotdotdot_body);
				toReturn.addView(head);
				toReturn.addView(bindingsReturn);
				toReturn.addView(bodyReturn);
				toReturn.removeView(dotdotdot);
				
				bindingsReturn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						bindingsReturn.addView(bindingsBox((HashMap<String, Expression>) ast.getBindings(),depth+1));
						bindingsReturn.removeView(dotdotdot_bindings);
					}	
				});
				bodyReturn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						bodyReturn.addView(createBox(ast.getBody()));
						bodyReturn.removeView(dotdotdot_body);
						
					}
				});
			}	
		});
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}
	
	/*
    private RelativeLayout letBox(Expression ast,int depth)
    {
    	
    	//-------------BASE------------
    	final RelativeLayout toReturn=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
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
		RelativeLayout bindings=bindingsBox(((LetExpression)ast).getBindings(),depth+1);
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
    //STILL NEED TO SET AN ONCLICK LISTENER TO BINDINGS!
    */
    //bindingsBox is a helper method for letBox
    private RelativeLayout bindingsBox(HashMap<String, Expression> bindings,int depth)
    {
    	final RelativeLayout toReturn=new RelativeLayout(this);
    	RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    	paramsReturn.setMargins(10*depth+10, 30, 0, 0);
    	toReturn.setLayoutParams(paramsReturn);
    	
    	final LinearLayout toReturnAbove=new LinearLayout(this);
    	LayoutParams paramsAbove=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
    	toReturnAbove.setLayoutParams(paramsAbove);
    	toReturnAbove.setOrientation(LinearLayout.VERTICAL);
    	
    	for(Map.Entry<String, Expression> entry: bindings.entrySet())
    	{
    		String key=entry.getKey();
    		Expression value=entry.getValue();
    		LinearLayout ll=new LinearLayout(this);
    		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
    		ll.setOrientation(LinearLayout.HORIZONTAL);
    		TextView t=new TextView(this);
    		t.setText(key+"->  ");
    		t.setTextColor(Color.BLUE);
    		RelativeLayout v=createBox(value);
    		ll.addView(t);
    		ll.addView(v);
    		toReturnAbove.addView(ll);
    	}
    	toReturn.addView(dummyBox()	);
    	toReturn.addView(toReturnAbove);
    	toReturn.setId(idGenerator);
    	
    	final TextView dotdotdot=new TextView(this);
    	dotdotdot.setText("(bindings...)");
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
							toReturn.addView(dummyBox());
							toReturn.addView(toReturnAbove);
							toReturn.removeView(dotdotdot);
						}
					});
				}
			}});
    	idGenerator++;
    	return toReturn;
    }
    
    private RelativeLayout parameterBox(ArrayList<String> parameters,int depth)
    {
    	final RelativeLayout toReturn=new RelativeLayout(this);
    	RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
    			RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    	paramsReturn.setMargins(10*depth,30, 0, 0);
    	toReturn.setLayoutParams(paramsReturn);
    	
    	String s="";
    	for(String string: parameters)
    	{
    		s+=" "+string;
    	}
    	final TextView tv=new TextView(this);
    	tv.setText(s);
    	toReturn.addView(dummyBox());
    	toReturn.addView(tv);
    	toReturn.setId(idGenerator);
    	
    	final TextView dotdotdot=new TextView(this);
    	dotdotdot.setText("(parameters...)");
    	toReturn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
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
							toReturn.addView(dummyBox());
							toReturn.addView(tv);
							toReturn.removeView(dotdotdot);
						}
					});
				}
			}
    		
    	});
    	
    	idGenerator++;
    	return toReturn;
    }
    /*
    private RelativeLayout lambdaBox(Expression e,int d)
    {
    	final LambdaExpression ast=(LambdaExpression) e;
    	final int depth=d;
    	
    	final RelativeLayout toReturn=new RelativeLayout(this);
    	RelativeLayout.LayoutParams paramsReturn=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    	paramsReturn.setMargins(10*depth, 20, 20, 20);
    	toReturn.setLayoutParams(paramsReturn);
    	
    	final TextView dotdotdot=new TextView(this);
		dotdotdot.setText("(let...)");
		toReturn.addView(dotdotdot);
		
		toReturn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//------head--------
				final TextView head=new TextView(v.getContext());
				RelativeLayout.LayoutParams headParams=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				head.setLayoutParams(headParams);
				head.setTextColor(Color.RED);
				head.setText("Lambda");
				head.setId(idGenerator);
				idGenerator++;
				//-----head--------
				
				final RelativeLayout parametersReturn=new RelativeLayout(v.getContext());
				RelativeLayout.LayoutParams paramsBindings=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsBindings.addRule(RelativeLayout.BELOW,head.getId());
				parametersReturn.setLayoutParams(paramsBindings);
				parametersReturn.setId(idGenerator);
				idGenerator++;
				
				final RelativeLayout bodyReturn=new RelativeLayout(v.getContext());
				RelativeLayout.LayoutParams paramsBody=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsBody.addRule(RelativeLayout.BELOW,parametersReturn.getId());
				bodyReturn.setLayoutParams(paramsBody);
				bodyReturn.setId(idGenerator);
				idGenerator++;
				
				final TextView dotdotdot_parameters=new TextView(v.getContext());
				dotdotdot_parameters.setText("(parameters...)");
				parametersReturn.addView(parametersReturn);
				
				final TextView dotdotdot_body=new TextView(v.getContext());
				dotdotdot_body.setText("(body...)");
				bodyReturn.addView(dotdotdot_body);
				
				toReturn.addView(head);
				toReturn.addView(parametersReturn);
				toReturn.addView(bodyReturn);
				toReturn.removeView(dotdotdot);
				
				parametersReturn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						parametersReturn.addView(parameterBox(ast.getParameters(),depth+1));
						parametersReturn.removeView(dotdotdot_parameters);
					}	
				});
				bodyReturn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						bodyReturn.addView(createBox(ast.getBody()));
						bodyReturn.removeView(dotdotdot_body);
						
					}
				});
			}
		});
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
    }
    */
    
    private RelativeLayout lambdaBox(Expression ast,int depth)
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
		head.setText("lambda");
		head.setId(idGenerator);
		idGenerator++;
		//-------------HEAD-----------------
		
		//-------------TORETURNABOVE
		final RelativeLayout toReturnAbove=new RelativeLayout(this);
		RelativeLayout.LayoutParams paramsAbove=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsAbove.addRule(RelativeLayout.BELOW, head.getId());
		toReturnAbove.setLayoutParams(paramsAbove);
		
		RelativeLayout parameters=parameterBox(((LambdaExpression)ast).getParameters(),depth+1);
		
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
	
	private RelativeLayout dummyBox()
	{
		RelativeLayout toReturn=new RelativeLayout(this);
		toReturn.setId(idGenerator);
		idGenerator++;
		return toReturn;
	}

}
