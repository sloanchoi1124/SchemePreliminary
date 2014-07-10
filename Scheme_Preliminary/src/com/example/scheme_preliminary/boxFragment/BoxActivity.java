package com.example.scheme_preliminary.boxFragment;

import java.util.List;

import com.example.scheme_preliminary.ActivityCommunicator;
import com.example.scheme_preliminary.R;
import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.*;
import util.Pair;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class BoxActivity extends Activity implements ActivityCommunicator{
	
	private Bundle extras;
	private String schemeText;
	private Expression toReturnToFragment;
	private List<Pair<String, Expression>> toReturnBindings;
	private Fragment currentFrag;
	private FragmentTransaction ft;
	private RelativeLayout background;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box);
		background=(RelativeLayout) findViewById(R.id.background);
		extras=getIntent().getExtras();
		if(extras!=null)
		{
			schemeText=extras.getString("schemeText");
			List<Token> tokens = Lexer.lex(schemeText);
			Expression ast = Parser.parseExpression(tokens);
			passExpressionToActivity(ast);
		}
	}

	@Override
	public void passExpressionToActivity(Expression ast) {
		// TODO Auto-generated method stub
		toReturnToFragment=ast;
		ft=getFragmentManager().beginTransaction();
		if(ast instanceof CallExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new CallBox_Fragment();
				ft.add(R.id.background, currentFrag);
			}
			else
			{
				currentFrag=new CallBox_Fragment();
				ft.replace(R.id.background, currentFrag);
				ft.addToBackStack(null);
			}
			
			ft.commit();

		}
		else if(ast instanceof IfExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new IfBox_Fragment();
				ft.add(R.id.background, currentFrag);
			}
			else
			{
				currentFrag=new IfBox_Fragment();
				ft.replace(R.id.background, currentFrag);
				ft.addToBackStack(null);
			}
			
			ft.commit();
		}
		else if(ast instanceof LetExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new LetBox_Fragment();
				ft.add(R.id.background, currentFrag);
			}
			else
			{
				currentFrag=new LetBox_Fragment();
				ft.replace(R.id.background, currentFrag);
				ft.addToBackStack(null);
			}
			
			ft.commit();
		}
		else if(ast instanceof LambdaExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new LambdaBox_Fragment();
				ft.add(R.id.background, currentFrag);
			}
			else
			{
				currentFrag=new LambdaBox_Fragment();
				ft.replace(R.id.background, currentFrag);
				ft.addToBackStack(null);
			}
			
			ft.commit();
		}
		else if(ast instanceof IntExpression||ast instanceof IdExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new IntIdBox_Fragment();
				ft.add(R.id.background, currentFrag);
				
			}
			else
			{
				currentFrag=new IntIdBox_Fragment();
				ft.replace(R.id.background, currentFrag);
				ft.addToBackStack(null);
			}
			
			ft.commit();

		}
	}	


	@Override
	public Expression passExpressionToFragment() {
		// TODO Auto-generated method stub
		return toReturnToFragment;
	}


	@Override
	public void passBindingsToActivity(List<Pair<String, Expression>> bindings) {
		// TODO Auto-generated method stub
		toReturnBindings=bindings;
		ft=getFragmentManager().beginTransaction();
		currentFrag=new BindingsBox_Fragment();
		ft.replace(R.id.background, currentFrag);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public List<Pair<String, Expression>> passBindingsToFragment() {
		// TODO Auto-generated method stub
		return toReturnBindings;
	}

}
