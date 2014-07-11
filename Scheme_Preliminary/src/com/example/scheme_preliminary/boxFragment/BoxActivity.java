package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.scheme_preliminary.ActivityCommunicator;
import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.boxFragment.RightSideBar_Fragment.RightSideBarActivityCommunicator;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.*;
import util.Pair;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class BoxActivity extends Activity implements ActivityCommunicator,RightSideBarActivityCommunicator{
	
	private Bundle extras;
	private String schemeText;
	private Expression toReturnToFragment;
	private List<Pair<String, Expression>> toReturnBindings;
	private List<String> toReturnKeys;
	private Fragment currentFrag;
	private RightSideBar_Fragment rightSideBar;
	private Map<String,Expression> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box);
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		
		map=new HashMap<String,Expression>();
		String temp="(if (< (+ 1 2) 4) (let ((a 1) (b 2) (c 3)) (+ a b c)) (let ((x (lambda (a b c) (* a b c)))) (x 1 2 3)))";
		List<Token> tokens = Lexer.lex(temp);
		Expression ast1 = Parser.parse(tokens);
		
		map.put("example1", ast1);
		String temp1="(+ 2 2)";
		tokens = Lexer.lex(temp1);
		Expression ast2 = Parser.parse(tokens);
		map.put("example2", ast2);
		initializeRightSideBar();
	}

	@Override
	public void passExpressionToActivity(Expression ast) {
		// TODO Auto-generated method stub
		toReturnToFragment=ast;
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		if(ast instanceof CallExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new CallBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
			}
			else
			{
				currentFrag=new CallBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
			}
			
			ft.commit();

		}
		else if(ast instanceof IfExpression)
		{
			System.out.println("gonna start a new ifbox fragment");
			if(currentFrag==null)
			{
				currentFrag=new IfBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
			}
			else
			{
				currentFrag=new IfBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
			}
			ft.commit();
		}
		else if(ast instanceof LetExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new LetBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
			}
			else
			{
				currentFrag=new LetBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
			}
			
			ft.commit();
		}
		else if(ast instanceof LambdaExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new LambdaBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
			}
			else
			{
				currentFrag=new LambdaBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
			}
			
			ft.commit();
		}
		else if(ast instanceof IntExpression||ast instanceof IdExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new IntIdBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				
			}
			else
			{
				currentFrag=new IntIdBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
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
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		ft=getFragmentManager().beginTransaction();
		currentFrag=new BindingsBox_Fragment();
		ft.replace(R.id.center_screen_background, currentFrag);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public List<Pair<String, Expression>> passBindingsToFragment() {
		// TODO Auto-generated method stub
		return toReturnBindings;
	}

	//box activity take in the key from right side bar and then pass the corresponding expression to the center
	@Override
	public void passTopLevelExpressionToActivity(String s) {
		// TODO Auto-generated method stub
		System.out.println("got data from right side bar");
		System.out.println(s);
		if(map.get(s)!=null)
		{
			if(currentFrag!=null)
			{
				//clear the backstack
				FragmentManager fm=getFragmentManager();
				for(int i=0;i<fm.getBackStackEntryCount();i++)
				{
					fm.popBackStack();
				}
				//??
				currentFrag=null;
			}
		}
		passExpressionToActivity(map.get(s));
	}

	@Override
	public List passKeyListToRightSideBar() {
		// TODO Auto-generated method stub
		toReturnKeys=new ArrayList<String>();
		for(String key:map.keySet())
		{
			toReturnKeys.add(key);
		}
		return toReturnKeys;
	}

	@Override
	public void initializeRightSideBar() {
		// TODO Auto-generated method stub
		rightSideBar=new RightSideBar_Fragment();
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		ft.add(R.id.right_side_bar_background, rightSideBar);
		passKeyListToRightSideBar();
		ft.commit();
	}

}
