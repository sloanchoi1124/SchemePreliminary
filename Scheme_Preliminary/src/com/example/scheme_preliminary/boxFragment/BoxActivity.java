package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.boxFragment.TopSideBar_Fragment.TopSideBarActivityCommunicator;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.*;
import util.Pair;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class BoxActivity extends Activity implements ActivityCommunicator,TopSideBarActivityCommunicator{

	private DefOrExp toReturnToFragment;
	private List<Pair<String, Expression>> toReturnBindings;
	private List<String> toReturnKeys;
	private List<String> toReturnLabels;
	//----------------------------------------
	private Fragment currentFrag;
	private TopSideBar_Fragment topSideBar;
	//----------------------------------------
	private Map<String, DefOrExp> map;
	private Program program;
	//----------------------------------------
	private List<Fragment> fragmentList;
	private int currentIndex;
	//----------------------------------------
	private String currentExpressionTag;
	//----------------------------------------
	private ListView drawer_background;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box);
		//-----------BUILD A MAP FROM THE PROGRAM PASSED IN-------------
		String toParse="(define THREE 3) (define square (lambda (x) (* x x))) (square THREE) (define sumSquares (lambda (a b) (+ (square a) (square b)))) (sumSquares THREE 4) (define factorial (lambda (n) (if (= n 0) 1 (* n (factorial (- n 1)))))) (factorial THREE)";
		List<Token> tokens = Lexer.lex(toParse);
		program=Parser.parse(tokens);
		List<DefOrExp> programList=program.getProgram();
		map=new HashMap<String,DefOrExp>();
		int i=0;
		for(DefOrExp temp:programList)
		{
			if(temp instanceof Definition)
				map.put(((Definition) temp).getSymbol(), temp);
			else
			{
				map.put("Exp"+((Integer)i).toString(), temp);
				i++;
			}
		}
		//----------BUILD A MAP FROM THE PROGRAM PASSED IN--------------
		
		//----------SET UP THE NAVIGATION DRAWER------------------------
		drawer_background=(ListView) findViewById(R.id.drawer);
		initializeRightSideDrawer();
		//----------SET UP THE NAVIGATION DRAWER------------------------
		
		fragmentList=new ArrayList<Fragment>();
		currentIndex=-1;
	}

	@Override
	public void passDefOrExpToActivity(DefOrExp ast) {
		// TODO Auto-generated method stub
		toReturnToFragment=ast;
		currentIndex++;
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		if(ast instanceof Definition)
		{
			if(currentFrag==null)
			{
				currentFrag=new DefinitionBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new DefinitionBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
			ft.commit();
		}
		else if(ast instanceof CallExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new CallBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new CallBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
			ft.commit();
		}
		else if(ast instanceof IfExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new IfBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new IfBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
			ft.commit();
		}
		else if(ast instanceof LetExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new LetBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new LetBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
			ft.commit();
		}
		else if(ast instanceof LambdaExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new LambdaBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new LambdaBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
			ft.commit();
		}
		else if(ast instanceof IntExpression||ast instanceof IdExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new IntIdBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new IntIdBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
			ft.commit();
		}
	}	

	@Override
	public DefOrExp passDefOrExpToFragment() {
		// TODO Auto-generated method stub
		return toReturnToFragment;
	}

	@Override
	public List<Pair<String, Expression>> passBindingsToFragment() {
		// TODO Auto-generated method stub
		return toReturnBindings;
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
		fragmentList.add(currentFrag);
		ft.commit();
	}

	public void initializeRightSideDrawer(){
		toReturnKeys=new ArrayList<String>();
		for(String key:map.keySet())
			toReturnKeys.add(key);
		drawer_background.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toReturnKeys));
		drawer_background.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String item=(String) parent.getItemAtPosition(position);
				passTopLevelExpressionToCenterScreen(item);
				//passTopLevelExpressionToActivity(item);
			}
		});
	}
	
	public void passTopLevelExpressionToCenterScreen(String s)
	{
		System.out.println("THIS IS THE MESSAGE SENT FROM PASSTOPVELEXPRESSIONTOCENTERSCREEN");
		if(currentExpressionTag==null)
		{
			System.out.println("CENTER SCREEN INITIALIZATION");
			currentExpressionTag=s;
			if(map.get(s)!=null)
			{
				passDefOrExpToActivity(map.get(s));
				initializeTopSideBar();
			}
		}
		else
		{
			System.out.println("CENTER SCREEN RE-INITIALIZATION");
			//if the same top-level expression is pressed, ignore
			if(currentExpressionTag.equals(s)==false)
			{
				if(map.get(s)!=null)
				{
					fragmentList.clear();
					passDefOrExpToActivity(map.get(s));
					initializeTopSideBar();
					currentExpressionTag=s;
				}
			}
		}
	}

	
	
	@Override
	public void passLabelToActivity(String label) {
		// TODO Auto-generated method stub
		toReturnLabels.add(label);
		passLabelListToFragment();
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		topSideBar=new TopSideBar_Fragment();
		ft.replace(R.id.top_side_bar_background, topSideBar);
		ft.commit();
	}
	
	@Override
	public void passIndexToActivity(int i) {
		// TODO Auto-generated method stub
		//look into the fragment list and get the corresponding fragment on the list
		currentIndex=i;
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		currentFrag=fragmentList.get(currentIndex);
		setClickabilityToFragment();
		ft.replace(R.id.center_screen_background, currentFrag);
		ft.commit();
	}

	@Override
	public void initializeTopSideBar() {
		// TODO Auto-generated method stub
		//if the fragmentList is just initialized
		if(fragmentList.size()<=1)
		{
			toReturnLabels=new ArrayList<String>();
			toReturnLabels.add("root");
		}
		//re-initialize the top-side-bar
		else
		{
			toReturnLabels=toReturnLabels.subList(0, currentIndex+1);
		}
		passLabelListToFragment();
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		if(topSideBar==null)
		{
			topSideBar=new TopSideBar_Fragment();
			ft.add(R.id.top_side_bar_background,topSideBar);
		}
		else
		{
			topSideBar=new TopSideBar_Fragment();
			ft.replace(R.id.top_side_bar_background,topSideBar);
		}
		ft.commit();
	}
	@Override
	public List<String> passLabelListToFragment() {
		// TODO Auto-generated method stub
		return toReturnLabels;
	}

	@Override
	public boolean setClickabilityToFragment() {
		// TODO Auto-generated method stub
		if(currentIndex<fragmentList.size()-1)
			return false;
		else
			return true;
	}

	@Override
	public void destroySubsequentFragments() {
		// TODO Auto-generated method stub
		fragmentList=fragmentList.subList(0, currentIndex+1);
		initializeTopSideBar();
	}

}

