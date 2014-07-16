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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private List<Program> programList;
	private Program currentProgram;
	private Map<String, DefOrExp> map;
	//----------------------------------------
	private List<Fragment> fragmentList;
	private int currentIndex;
	//----------------------------------------
	private String currentExpressionTag;
	//----------------------------------------
	private ListView left_drawer_background;
	//----------------------------------------
	private ListView right_drawer_background;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box);
		
		String toParse="(define THREE 3) (define square (lambda (x) (* x x))) (square THREE) (define sumSquares (lambda (a b) (+ (square a) (square b)))) (sumSquares THREE 4) (define factorial (lambda (n) (if (= n 0) 1 (* n (factorial (- n 1)))))) (factorial THREE)";
		List<Token> tokens = Lexer.lex(toParse);
		this.currentProgram=Parser.parse(tokens);
		String toParse1="(define THREE 4)";
		List<Token> tokens1 = Lexer.lex(toParse1);
		Program currentProgram1=Parser.parse(tokens1);
		//---------------------------------------
		
		this.programList=new ArrayList<Program>();
		this.programList.add(this.currentProgram);
		this.programList.add(currentProgram1);
		
		//INITIALIZE DRAWERS FROM BOTH SIDE-----
		this.left_drawer_background=(ListView) findViewById(R.id.drawer_left);
		this.right_drawer_background=(ListView) findViewById(R.id.drawer);
		//--------------------------------------
		initializeLeftSideDrawer();		
		this.fragmentList=new ArrayList<Fragment>();
		this.currentIndex=-1;
	}
	//----------------METHODS FOR LEFT SIDE DRAWER--------------------------
	public void initializeLeftSideDrawer(){
		List<String> tags=new ArrayList<String>();
		for(int i=0;i<this.programList.size();i++)
			tags.add("Program"+((Integer)i).toString());
		this.left_drawer_background.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,tags));
		this.left_drawer_background.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				currentProgram=programList.get(position);
				map=initializeMap(initializeDefOrExpList(currentProgram,null));
				initializeRightSideDrawer();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.box_activity_action, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		    case R.id.action_add:
		    	//go to the calculator fragment
		    	System.out.println("GENERATE THE CALCULATOR FRAGMENT");
		    	//generate a new interface to communicate between the activity and the calculator fragment
		    	//pass in the list of function names within a certain program
		    	break;
		}
		return true;
	}

	private List<DefOrExp> initializeDefOrExpList(Program program,DefOrExp doe)
	{
		List<DefOrExp> temp=program.getProgram();
		if(doe!=null)
			temp.add(doe);
		return temp;
	}
	
	public HashMap<String,DefOrExp> initializeMap(List<DefOrExp> programList)
	{
		HashMap<String,DefOrExp> temp=new HashMap<String,DefOrExp>();
		int i=0;
		for(DefOrExp doe:programList)
		{
			if(doe instanceof Definition)
			{
				temp.put(((Definition) doe).getSymbol(), doe);
			}	
			else
			{
			    temp.put("Exp"+((Integer)i).toString(), doe);
				i++;
			}
		}
		return temp;
	}
	
	
	//----------------METHOD FROM ACTIVITY COMMUNICATOR---------------------
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
		else if(ast instanceof AndExpression||ast instanceof OrExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new AndOrBox_Fragment();
				ft.add(R.id.center_screen_background,currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new AndOrBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
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
		else if(ast instanceof IntExpression||ast instanceof IdExpression||ast instanceof BoolExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new IntIdBoolBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new IntIdBoolBox_Fragment();
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
		currentFrag=new BindingsBox_Fragment();
		ft.replace(R.id.center_screen_background, currentFrag);
		ft.addToBackStack(null);
		fragmentList.add(currentFrag);
		ft.commit();
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
	
	//----------------METHOD FROM ACTIVITY COMMUNICATOR---------------------
	

	
	//----------------METHODS FOR RIGHT SIDE DRAWER-------------------------
	public void initializeRightSideDrawer(){
		toReturnKeys=new ArrayList<String>();
		for(String key:map.keySet())
			toReturnKeys.add(key);
		this.right_drawer_background.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toReturnKeys));
		this.right_drawer_background.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				String item=(String) parent.getItemAtPosition(position);
				passTopLevelExpressionToCenterScreen(item);
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
	//----------------METHODS FOR RIGHT SIDE DRAWER-------------------------
	
	
	//---------------METHODS FOR TOP SIDE BAR-------------------------------
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
	//---------------METHODS FOR TOP SIDE BAR-------------------------------
}

