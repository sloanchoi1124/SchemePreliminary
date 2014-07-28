package com.example.scheme_preliminary.boxFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scheme_ast.AbstractLetExpression;
import scheme_ast.AndExpression;
import scheme_ast.BoolExpression;
import scheme_ast.CallExpression;
import scheme_ast.CondExpression;
import scheme_ast.ConsExpression;
import scheme_ast.DefOrExp;
import scheme_ast.Definition;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import scheme_ast.OrExpression;
import scheme_ast.Program;
import scheme_ast.StringExpression;
import unparser.Unparser;
import util.Pair;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.boxFragment.TopSideBar_Fragment.TopSideBarActivityCommunicator;
import com.example.scheme_preliminary.calculator.Calculator_Fragment;
import com.example.scheme_preliminary.calculator.Calculator_Fragment.Calculator_Fragment_Communicator;
import com.example.scheme_preliminary.calculator.Calculator_Fragment_Listener;

import file.io.FileUtils;

public class BoxActivity extends Activity implements ActivityCommunicator,
          TopSideBarActivityCommunicator,Calculator_Fragment_Communicator,
          Calculator_Fragment_Listener,ProgramLevelCommunicator{
	
	//these variables are used by ActivityCommunicator
	private DefOrExp toReturnToFragment;//<=> the current expression for the current fragment
	private List<Pair<String, Expression>> toReturnBindings;
	private List<String> toReturnKeys;
	private List<String> toReturnLabels;
	//----------------------------------------
	private Fragment currentFrag;
	private TopSideBar_Fragment topSideBar;
	//----------------------------------------
	private List<String> programNameList;//stores all the testing programs right now
	private String currentProgramName;
	private Program currentProgram;
	private Map<String, DefOrExp> map;//stores the map name->def/exp for the current program
	//----------------------------------------
	private List<Fragment> fragmentList;//keeps track of the fragments 
	private int currentIndex;
	//----------------------------------------
	private String currentExpressionTag;//can be used to track the current TOP-LEVEL expression in map
	//----------------------------------------
	private ListView left_drawer_background;
	//----------------------------------------
	private ListView right_drawer_background;
	//----------------------------------------
	private DefOrExp newDefOrExp;//the def/exp generated by the calculator
	private boolean addToProgram=false;
	private String replacementTag;//used to identify what to replace
	private int replacementIndex;//used to identify the specific index in a list for replace
	//----------------------------------------
	//these variables are used when you want to generate a new pair<exp,exp>
	private boolean pairGenerator;
	private Expression pair_1st;
	private Expression pair_2nd;
	private Pair<Expression,Expression> newCondPair;
	//----------------------------------------
	private boolean addNewDefOrExpInViewingFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box);
		
		if (savedInstanceState == null) {

			//------------------SD card interaction---------------------
//			Intent fileService = new Intent(this, ProjectFileSetup.class);
			
			
//			final BoxActivity ba = this;
//			Thread t = new Thread() {
//				public void run() {
//					startService(new Intent(ba, ProjectFileSetup.class)); // stops itself
//				}
//			};
//			t.start();
			
			FileUtils.fileTreeSetup(getAssets());
			
			
			//---------------------------------------
			//String toParse="(define THREE 3) (define square (lambda (x) (* x x))) (square THREE) (define sumSquares (lambda (a b) (+ (square a) (square b)))) (sumSquares THREE 4) (define factorial (lambda (n) (if (= n 0) 1 (* n (factorial (- n 1)))))) (factorial THREE)";
	//		String toParse="(define THREE 3) (define square (lambda (x) (* x x))) (square THREE) (define sumSquares (lambda (a b) (+ (square a) (square b)))) (sumSquares THREE 4) (define factorial (lambda (n) (if (= n 0) 1 (* n (factorial (- n 1)))))) (factorial THREE)";
	//		String toParse="(if (< 3 2) \"yes\" \"no\")";
	//		List<Token> tokens = Lexer.lex(toParse);
	//		Program program0=Parser.parse(tokens);
	//		String toParse1="(cond ((> 3 3) (+ 1 1)) ((< 3 3) (+ 1 1)) (else (+ 1 1)))";
	//		List<Token> tokens1 = Lexer.lex(toParse1);
	//		Program program1=Parser.parse(tokens1);
			//---------------------------------------
//			this.programList.add(program0);
//			this.programList.add(program1);
	
			//INITIALIZE DRAWERS BACKGROUND FROM BOTH SIDE-----
			this.left_drawer_background=(ListView) findViewById(R.id.drawer_left_list);
			this.right_drawer_background=(ListView) findViewById(R.id.drawer);
			//-------------------------------------
			initializeLeftSideDrawer();	
			initializeCenterScreen();
		}
	}
	
	//--------THE ACTION BAR---------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.box_activity_action, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		    case R.id.action_add:
		    	//-----EXPERIMENTAL-----
		    	final CharSequence[] items={"New DefOrExp","New Program","New Operand/Condition","New Pair_1st","New Pair_2nd"};
		    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
		    	builder.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					//hello
					public void onClick(DialogInterface dialog, int item) {
						// TODO Auto-generated method stub
						switch(item){
						case 0:
							//if the viewing fragment is currently popping up, then update it!
							if(currentProgram!=null)
							{
							    addNewDefOrExpInViewingFlag=true;
							    if(getFragmentManager().findFragmentByTag("evaluation_fragment")!=null)
							    	getFragmentManager().popBackStackImmediate();
								//go to the calculator fragment
						    	System.out.println("GENERATE THE CALCULATOR FRAGMENT");
						    	//generate a new interface to communicate between the activity and the calculator fragment
						    	if (getFragmentManager().findFragmentByTag("calculator") == null) {
							    		
							    	getFragmentManager().beginTransaction()
							    			.add(R.id.calculator_frame, new Calculator_Fragment(), "calculator")
							    			.addToBackStack(null)
					    					.commit();
							    	//automatically passes in the list of function names within a certain program
									
									
							    	((FrameLayout) findViewById(R.id.calculator_frame)).bringToFront();
							    	addToProgram=true;	
							    	//the new expression/definition should be added to the program
							    	
						    	}
							}
							else
							{
								Toast.makeText(getBaseContext(), "Please Choose A Program", Toast.LENGTH_SHORT).show();
							}
					    	
					    	break;
						case 1:
							System.out.println("BUILD A NEW PROGRAM");
							if (FileUtils.externalStorageWritable())
								FileUtils.createNewFile();
							else
								System.out.println("Error creating new file.");
							initializeLeftSideDrawer();
							initializeCenterScreen();
							break;
						case 2:
							if(currentProgram!=null)
							{
								if(toReturnToFragment instanceof CallExpression)
								{
									System.out.println("add a new operand to the call expression");
									//pop up the calculator here
							    	if (getFragmentManager().findFragmentByTag("calculator") == null) {
							    		
								    	getFragmentManager().beginTransaction()
								    			.add(R.id.calculator_frame, new Calculator_Fragment(), "calculator")
								    			.addToBackStack(null)
						    					.commit();
								    	//automatically passes in the list of function names within a certain program
										
										
								    	((FrameLayout) findViewById(R.id.calculator_frame)).bringToFront();	
								    	//the new expression/definition should be added to the program
								    	//add a new operand to call expression, specify in the replacement tag as "call.newOperand"
										replacementTag="call.newOperand";
							    	}
								}
								else if(toReturnToFragment instanceof AndExpression)
								{
									System.out.println("add a new condition to the and expression");
									//pop up the calculator here
							    	if (getFragmentManager().findFragmentByTag("calculator") == null) {
							    		
								    	getFragmentManager().beginTransaction()
								    			.add(R.id.calculator_frame, new Calculator_Fragment(), "calculator")
								    			.addToBackStack(null)
						    					.commit();
								    	//automatically passes in the list of function names within a certain program
										
										
								    	((FrameLayout) findViewById(R.id.calculator_frame)).bringToFront();	
								    	//the new expression/definition should be added to the program
								    	//add a new operand to call expression, specify in the replacement tag as "call.newOperand"
										replacementTag="and.newCondition";
							    	}
								}
								else if(toReturnToFragment instanceof OrExpression)
								{
									System.out.println("add a new condition to the or expression");
									//pop up the calculator here
							    	if (getFragmentManager().findFragmentByTag("calculator") == null) {
							    		
								    	getFragmentManager().beginTransaction()
								    			.add(R.id.calculator_frame, new Calculator_Fragment(), "calculator")
								    			.addToBackStack(null)
						    					.commit();
								    	//automatically passes in the list of function names within a certain program
										
										
								    	((FrameLayout) findViewById(R.id.calculator_frame)).bringToFront();	
								    	//the new expression/definition should be added to the program
								    	//add a new operand to call expression, specify in the replacement tag as "call.newOperand"
										replacementTag="or.newCondition";
							    	}
								}
								else
								{
									Toast.makeText(getBaseContext(), "You're Not Supposed To Add New Operand/Condition Here.", Toast.LENGTH_SHORT).show();
								}
							}

							else
							{
								Toast.makeText(getBaseContext(), "Please Choose A Program", Toast.LENGTH_SHORT).show();
							}
							break;
						case 3:
							if(currentProgram!=null)
							{
								if(toReturnToFragment instanceof CondExpression)
								{
									System.out.println("going to generate a new pair(left)");
									pairGenerator=true;
									replacementTag="pair.first";
									if (getFragmentManager().findFragmentByTag("calculator") == null) {
							    		
								    	getFragmentManager().beginTransaction()
								    			.add(R.id.calculator_frame, new Calculator_Fragment(), "calculator")
								    			.addToBackStack(null)
						    					.commit();
								    	//automatically passes in the list of function names within a certain program
										
										
								    	((FrameLayout) findViewById(R.id.calculator_frame)).bringToFront();	
									}
									//pop up the calculator here
								}
								else
								{
									Toast.makeText(getBaseContext(), "You're Not Supposed To Add A Pair Here!", Toast.LENGTH_SHORT).show();
								}
							}
							else
							{
								Toast.makeText(getBaseContext(), "Please Choose A Program", Toast.LENGTH_SHORT).show();
							}
							break;
						case 4:
							if(currentProgram!=null)
							{
								if(toReturnToFragment instanceof CondExpression)
								{
									System.out.println("going to generate a new pair(right)");
									pairGenerator=true;
									replacementTag="pair.second";
									if (getFragmentManager().findFragmentByTag("calculator") == null) {
							    		
								    	getFragmentManager().beginTransaction()
								    			.add(R.id.calculator_frame, new Calculator_Fragment(), "calculator")
								    			.addToBackStack(null)
						    					.commit();
								    	//automatically passes in the list of function names within a certain program
								    	((FrameLayout) findViewById(R.id.calculator_frame)).bringToFront();	
									}
								}
								else
								{
									Toast.makeText(getBaseContext(), "You're Not Supposed To Add A Pair Here!", Toast.LENGTH_SHORT).show();
								}

							}
							else
							{
								Toast.makeText(getBaseContext(), "Please Choose A Program", Toast.LENGTH_SHORT).show();
							}
							break;
						}
					}
				});
		    	AlertDialog alert=builder.create();
		    	alert.show();
		    	break;
		    case R.id.action_view:
		    	if(this.currentProgram!=null)
		    	{
			    	System.out.println("going to view!");
			    	if(getFragmentManager().findFragmentByTag("viewing_fragment")==null)
			    	{
			    		getFragmentManager().beginTransaction()
			    		.add(R.id.viewing_frame, new Viewing_Fragment(), "viewing_fragment")
			    		.addToBackStack(null)
			    		.commit();
			    		((FrameLayout)findViewById(R.id.viewing_frame)).bringToFront();
			    	}
		    	}
		    	else
		    	{
		    		Toast.makeText(this, "Plase Chosse A Program", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.action_evaluate:
		    	if(this.currentProgram!=null)
		    	{
			    	System.out.println("going to evaluate");
			    	if(getFragmentManager().findFragmentByTag("evaluation_fragment")==null)
			    	{
			    		getFragmentManager().beginTransaction()
			    		.add(R.id.evaluating_frame, new Evaluation_Fragment(), "evaluation_fragment")
			    		.addToBackStack(null)
			    		.commit();
			    		((FrameLayout)findViewById(R.id.evaluating_frame)).bringToFront();
			    	}
		    	}
		    	else
		    	{
		    		Toast.makeText(this, "Plase Choose A Program", Toast.LENGTH_SHORT).show();
		    	}
		    	break;
		    case R.id.action_save:
		    	FileUtils.save(currentProgramName, Unparser.unparse(currentProgram));
		}
		
		return true;
	}
	
	//----------------METHODS FOR LEFT SIDE DRAWER--------------------------
	public void initializeLeftSideDrawer(){
		this.currentProgramName = null;
		this.currentProgram=null;
		if (FileUtils.externalStorageWritable())
			FileUtils.createNewFile();
		else
			System.out.println("Error creating new file.");
		this.programNameList = FileUtils.getFileNames();
		this.left_drawer_background.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, this.programNameList));
		this.left_drawer_background.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				leftDrawerClickAction(position);
			}
		});
	}
	
	private void leftDrawerClickAction(int position) {
		if(currentProgramName == null || ! currentProgramName.equals(programNameList.get(position)))
		{
			currentProgramName=(programNameList.get(position));
			currentProgram = FileUtils.getProgramFromName(currentProgramName);
			map=initializeMap(currentProgram.getProgram());
			clearCenterScreen();
			clearTopSideBar();	
			initializeRightSideDrawer();

		}
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
	
	//----------------METHODS FOR RIGHT SIDE DRAWER-------------------------
	public void initializeRightSideDrawer(){
		toReturnKeys=new ArrayList<String>();
		for(String key:this.map.keySet())
			toReturnKeys.add(key);
		this.right_drawer_background.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toReturnKeys));
		this.right_drawer_background.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				String item=(String) parent.getItemAtPosition(position);
				System.out.println("item="+item);
				passTopLevelExpressionToCenterScreen(item);
			}
		});
	}
	
	public void passTopLevelExpressionToCenterScreen(String s)
	{
		currentIndex=0;
		System.out.println("THIS IS THE MESSAGE SENT FROM PASSTOPVELEXPRESSIONTOCENTERSCREEN");
		if(currentExpressionTag==null)
		{
			System.out.println("CENTER SCREEN INITIALIZATION");
			currentExpressionTag=s;
			if(map.get(s)!=null)
			{
				System.out.println("successfully find the top level expression");
				passDefOrExpToActivity(map.get(s));
				initializeTopSideBar();
			}

		}
		else
		{
			System.out.println("CENTER SCREEN RE-INITIALIZATION");
			//if the same top-level expression is pressed, ignore
			
				//System.out.println("currentexpression tag != item0");
				if(map.get(s)!=null)
				{
					fragmentList.clear();
					passDefOrExpToActivity(map.get(s));
					initializeTopSideBar();
					currentExpressionTag=s;
				}
			
		}
	}
	
	//----------------THESE METHODS ARE FOR CENTER SCREEN ADJUSTMENT--------------
	public void initializeCenterScreen()
	{
		this.fragmentList=new ArrayList<Fragment>();
		this.currentIndex=-1;
	}
	
	public void clearCenterScreen()
	{
		for(Fragment temp:fragmentList)
		{
			getFragmentManager().beginTransaction().remove(temp).commit();
		}
		this.fragmentList.clear();
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
				//ft.addToBackStack(null);
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
				//ft.addToBackStack(null);
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
				//ft.addToBackStack(null);
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
				//ft.addToBackStack(null);
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
				//ft.addToBackStack(null);
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
				//ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
			ft.commit();
		}
		else if(ast instanceof IntExpression||ast instanceof IdExpression||ast instanceof BoolExpression
				||ast instanceof StringExpression||ast instanceof ConsExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new IntIdBoolStringConsBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new IntIdBoolStringConsBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				//ft.addToBackStack(null);
				fragmentList.add(currentFrag);
			}
			ft.commit();
		}
		else if(ast instanceof CondExpression)
		{
			if(currentFrag==null)
			{
				currentFrag=new CondBox_Fragment();
				ft.add(R.id.center_screen_background, currentFrag);
				fragmentList.add(currentFrag);
			}
			else
			{
				currentFrag=new CondBox_Fragment();
				ft.replace(R.id.center_screen_background, currentFrag);
				//ft.addToBackStack(null);
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
		{
			System.out.printf("currentIndex=%d,fragment.size()=%d\n",currentIndex,fragmentList.size());
			return false;
		}
		else
		{
			System.out.printf("currentIndex=%d,fragment.size()=%d\n",currentIndex,fragmentList.size());
			return true;
		}
	}

	@Override
	public void destroySubsequentFragments() {
		// TODO Auto-generated method stub
		fragmentList=fragmentList.subList(0, currentIndex+1);
		initializeTopSideBar();
	}
	
	@Override
	public void inputReplacementByCalculator() {
		// TODO Auto-generated method stub
		System.out.println("GENERATE THE CALCULATOR FRAGMENT FOR EDITING");
		if(getFragmentManager().findFragmentByTag("calculator")==null)
		{
			getFragmentManager().beginTransaction()
			.add(R.id.calculator_frame, new Calculator_Fragment(), "calculator")
			.addToBackStack(null)
			.commit();
			((FrameLayout) findViewById(R.id.calculator_frame)).bringToFront();
		}
		
	}

	@Override
	public void passReplacementTag(String tag) {
		// TODO Auto-generated method stub
		this.replacementTag=tag;
	}

	@Override
	public void passReplacementTag(String tag, int index) {
		// TODO Auto-generated method stub
		this.replacementTag=tag;
		this.replacementIndex=index;
	}

	
	//----------------METHODS FROM CALCULATOR FRAGMENT COMMUNICATOR---------------------
	public void receiveDefOrExp(DefOrExp defOrExp) {
		if(addToProgram==true)
		{
			System.out.println("ADD TO PROGRAM == TRUE");
			this.currentProgram.getProgram().add(defOrExp);
			getFragmentManager().popBackStackImmediate();
			map=initializeMap(currentProgram.getProgram());
			initializeRightSideDrawer();
			addToProgram=false;
			if(addNewDefOrExpInViewingFlag==true)
			{
				System.out.println("addnewdeforexpinviewing ==true");
				updateViewingFragment();
				addNewDefOrExpInViewingFlag=false;
			}
		}
		else if(this.pairGenerator==true)
		{
			//generate two expressions,exp0,exp1, specify with tag
			if(this.toReturnToFragment instanceof CondExpression)
			{
				System.out.println("NOW I'M INSIDE RECEIVE DEFOREXP");
				if(this.replacementTag.equals("pair.first"))
					this.pair_1st=(Expression) defOrExp;
				if(this.replacementTag.equals("pair.second"))
					this.pair_2nd=(Expression) defOrExp;
				if(this.pair_1st!=null&&this.pair_2nd!=null)
				{
					this.newCondPair=new Pair<Expression,Expression>(this.pair_1st,this.pair_2nd);
					this.pair_1st=null;
					this.pair_2nd=null;
					//update the condexpression and condbox after!!!!
					((CondExpression) this.toReturnToFragment).getAllPairs()
					.add(((CondExpression) this.toReturnToFragment).getSize()-1, this.newCondPair);
					this.pairGenerator=false;
					//after that, regenerate the condbox page!!!
					CondBox_Fragment newFrag=new CondBox_Fragment();
					FragmentTransaction ft=getFragmentManager().beginTransaction();
					ft.replace(R.id.center_screen_background, newFrag);
					ft.addToBackStack(null);
					fragmentList.remove(fragmentList.size()-1);
					fragmentList.add(newFrag);
					ft.commit();
					this.currentFrag=newFrag;
					this.replacementTag=null;
				}
			}

		}
		else
		{
			this.newDefOrExp=defOrExp;
			if(toReturnToFragment instanceof Definition)
			{
				System.out.println("GOING TO REPLACE THE BODY PART OF A DEFINITION");
				((Definition) toReturnToFragment).setBody((Expression) this.newDefOrExp);
				DefinitionBox_Fragment newFrag=new DefinitionBox_Fragment();
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.replace(R.id.center_screen_background, newFrag);
				ft.addToBackStack(null);
				fragmentList.remove(fragmentList.size()-1);
				fragmentList.add(newFrag);
				ft.commit();
				this.currentFrag=newFrag;
				this.replacementTag=null;
			}
			else if(toReturnToFragment instanceof LambdaExpression)
			{
				System.out.println("GOING TO REPLACE THE BODY OF A LAMBDA EXPRESSION");
				((LambdaExpression) toReturnToFragment).setBody((Expression) this.newDefOrExp);
				LambdaBox_Fragment newFrag=new LambdaBox_Fragment();
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.replace(R.id.center_screen_background, newFrag);
				ft.addToBackStack(null);
				fragmentList.remove(fragmentList.size()-1);
				fragmentList.add(newFrag);
				ft.commit();
				this.currentFrag=newFrag;
				this.replacementTag=null;
				
			}
			else if(toReturnToFragment instanceof AbstractLetExpression)
			{
				if(this.replacementTag==null)
				{
					System.out.println("GOING TO REPLACE THE BODY OF A LET EXPRESSION");
					((AbstractLetExpression) toReturnToFragment).setBody((Expression) this.newDefOrExp);
					LetBox_Fragment newFrag=new LetBox_Fragment();
					FragmentTransaction ft=getFragmentManager().beginTransaction();
					ft.replace(R.id.center_screen_background, newFrag);
					ft.addToBackStack(null);
					fragmentList.remove(fragmentList.size()-1);
					fragmentList.add(newFrag);
					ft.commit();
					this.currentFrag=newFrag;
					this.replacementTag=null;
				}
				else if(this.replacementTag.equals("bindings"))
				{
					System.out.println(fragmentList);
					System.out.printf("right now the replacementIndex is %d\n",this.replacementIndex);
					String pairTag=this.toReturnBindings.get(this.replacementIndex).first;
					((LetExpression) toReturnToFragment).getBindings().set(this.replacementIndex, 
							new Pair<String,Expression>(pairTag,(Expression) this.newDefOrExp));

					//only recreate the let box
					
					LetBox_Fragment newFrag=new LetBox_Fragment();
					FragmentTransaction ft=getFragmentManager().beginTransaction();
					ft.replace(R.id.center_screen_background, newFrag);
					ft.addToBackStack(null);
					fragmentList.remove(fragmentList.size()-1);
					fragmentList.add(newFrag);
					ft.commit();
					this.currentFrag=newFrag;
					this.replacementTag=null;
				}
			}
			else if(toReturnToFragment instanceof IfExpression)
			{
				System.out.println("GOING TO REPLACE A PART OF AN IF EXPRESSION");
				if(this.replacementTag.equals("if.condition"))
				{
					((IfExpression) toReturnToFragment).setCondition((Expression) this.newDefOrExp);
				}
				else if(this.replacementTag.equals("if.then"))
				{
					((IfExpression) toReturnToFragment).setThen((Expression) this.newDefOrExp);
				}
				else if(this.replacementTag.equals("if.else"))
				{
					((IfExpression) toReturnToFragment).setElse((Expression) this.newDefOrExp);
				}
				IfBox_Fragment newFrag=new IfBox_Fragment();
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.replace(R.id.center_screen_background, newFrag);
				ft.addToBackStack(null);
				fragmentList.remove(fragmentList.size()-1);
				fragmentList.add(newFrag);
				ft.commit();
				this.currentFrag=newFrag;
				this.replacementTag=null;
			}
			else if(toReturnToFragment instanceof CallExpression)
			{
				System.out.println("GOING TO REPLACE A PART OF CALL EXPRESSION");
				System.out.printf("Call_Expression, expression tag=%s\n",this.replacementTag);
				if(this.replacementTag.equals("call.operator"))
				{
					((CallExpression) toReturnToFragment).setOperator((Expression) this.newDefOrExp);
				}
				else if(this.replacementTag.equals("call.operand"))
				{
					((CallExpression) toReturnToFragment).getOperands().set(this.replacementIndex, (Expression) this.newDefOrExp);
				}

				else if(this.replacementTag.equals("call.newOperand"))
				{
					((CallExpression) toReturnToFragment).getOperands().add((Expression) this.newDefOrExp);
				}
				CallBox_Fragment newFrag=new CallBox_Fragment();
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.replace(R.id.center_screen_background, newFrag);
				ft.addToBackStack(null);
				fragmentList.remove(fragmentList.size()-1);
				fragmentList.add(newFrag);
				ft.commit();
				this.currentFrag=newFrag;
				this.replacementTag=null;
			}
			else if(toReturnToFragment instanceof AndExpression)
			{
				//generate a new condition aftermath
				if(this.replacementTag.equals("and.newCondition"))
				{
					((AndExpression) toReturnToFragment).getConditions().add((Expression) this.newDefOrExp);
					
				}
				else
				{
					System.out.println("GOING TO REPLACE A PART OF AND EXPRESSION");
					((AndExpression) toReturnToFragment).getConditions().set(this.replacementIndex, (Expression) this.newDefOrExp);
					
				}
				AndOrBox_Fragment newFrag=new AndOrBox_Fragment();
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.replace(R.id.center_screen_background, newFrag);
				ft.addToBackStack(null);
				fragmentList.remove(fragmentList.size()-1);
				fragmentList.add(newFrag);
				ft.commit();
				this.currentFrag=newFrag;
				this.replacementTag=null;

			}
			else if(toReturnToFragment instanceof OrExpression)
			{
				if(this.replacementTag.equals("or.newCondition"))
				{
					((OrExpression) toReturnToFragment).getConditions().add((Expression) this.newDefOrExp);
				}
				else
				{
					System.out.println("GOING TO REPLACE A PART OF OR EXPRESSION");
					((OrExpression) toReturnToFragment).getConditions().set(this.replacementIndex, (Expression) this.newDefOrExp);
				}
				AndOrBox_Fragment newFrag=new AndOrBox_Fragment();
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.replace(R.id.center_screen_background, newFrag);
				ft.addToBackStack(null);
				fragmentList.remove(fragmentList.size()-1);
				fragmentList.add(newFrag);
				ft.commit();
				this.currentFrag=newFrag;
				this.replacementTag=null;
			}
			else if(toReturnToFragment instanceof CondExpression)
			{
				//replacing either the left or right part of a pair
				if(this.replacementTag.equals("cond.left"))
				{
					((CondExpression) this.toReturnToFragment).getAllPairs().set(this.replacementIndex, 
							new Pair<Expression, Expression>(
									(Expression) this.newDefOrExp,
									((CondExpression) this.toReturnToFragment).getBody(this.replacementIndex)));
				}
				else if(this.replacementTag.equals("cond.right"))
				{
					((CondExpression) this.toReturnToFragment).getAllPairs().set(this.replacementIndex, 
							new Pair<Expression,Expression>(
									((CondExpression) this.toReturnToFragment).getCond(this.replacementIndex),
									(Expression) this.newDefOrExp));
				}
				CondBox_Fragment newFrag=new CondBox_Fragment();
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.replace(R.id.center_screen_background, newFrag);
				ft.addToBackStack(null);
				fragmentList.remove(fragmentList.size()-1);
				fragmentList.add(newFrag);
				ft.commit();
				this.currentFrag=newFrag;
				this.replacementTag=null;
			}

		}
	}
	
	
	public List<String> getBindings() {
		List<DefOrExp> defOrExps = this.currentProgram.getProgram();
		List<String> bindings = new ArrayList<String>();
		for (DefOrExp def : defOrExps) {
			if (def instanceof Definition) {
				bindings.add(((Definition) def).getSymbol());
			}
		}
		return bindings;
	}

	//----------------METHODS FROM CALCULATOR FRAGMENT LISTENER---------------------

	public void onStringCreated(String id) {
		((Calculator_Fragment) getFragmentManager().findFragmentByTag("calculator")).onStringCreated(id);
	}
	public List<String> getListSource() {
		return ((Calculator_Fragment) getFragmentManager().findFragmentByTag("calculator")).getListSource();
	}
	public void onIdSelected(String id) {
		((Calculator_Fragment) getFragmentManager().findFragmentByTag("calculator")).onIdSelected(id);
	}
	public void onOpSelected(View v) {
		((Calculator_Fragment) getFragmentManager().findFragmentByTag("calculator")).onOpSelected(v);
	}
	public void onKeypadCreated() {
		((Calculator_Fragment) getFragmentManager().findFragmentByTag("calculator")).onKeypadCreated();
	}
	public void onNewFragmentNeeded(View v) {
		((Calculator_Fragment) getFragmentManager().findFragmentByTag("calculator")).onNewFragmentNeeded(v);
	}
    public void onButtonClick(View v) {
    	((Calculator_Fragment) getFragmentManager().findFragmentByTag("calculator")).onButtonClick(v);
    }
	// getSystemService is a standard Activity method
	
	//---------------METHODS FOR TOP SIDE BAR-------------------------------
	@Override
	public void passIndexToActivity(int i) {
		// TODO Auto-generated method stub
		//look into the fragment list and get the corresponding fragment on the list
		//need to deal with clicking the current 
		if(currentFrag!=fragmentList.get(i))
		{
			System.out.printf("currentIndex=%d,i=%d,don't ignore\n",currentIndex,i);
			currentIndex=i;
			System.out.println(currentIndex);
			FragmentTransaction ft=getFragmentManager().beginTransaction();
			currentFrag=fragmentList.get(currentIndex);
			System.out.println("GOING TO PRINT INFORMATION FROM SET CLICKABILITY TO FRAGMENT!");
			setClickabilityToFragment();
			ft.replace(R.id.center_screen_background, currentFrag);
			ft.commit();
			
		}
		else
		{
			System.out.println("should ignore here!!!!!!");
		}

	}
	
	@Override
	public void initializeTopSideBar() {
		// TODO Auto-generated method stub
		//if the fragmentList is just initialized
		//just changed <=1 to <1
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
	
	public void clearTopSideBar()
	{
		toReturnLabels=new ArrayList<String>();
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
	public Program getProgramFromActivity() {
		// TODO Auto-generated method stub
		return this.currentProgram;
	}
	
	//this is used when the user is adding new deforexp while viewing!
	@Override
	public void updateViewingFragment() {
		// TODO Auto-generated method stub
		if(getFragmentManager().findFragmentByTag("viewing_fragment")!=null)
    	{
    		System.out.println("GOING TO HAVE A NEW VIEWING FRAGMENT!!");
			getFragmentManager().beginTransaction()
    		.replace(R.id.viewing_frame, new Viewing_Fragment(), "viewing_fragment")
    		.addToBackStack(null)
    		.commit();
    		((FrameLayout)findViewById(R.id.viewing_frame)).bringToFront();
    	}
	}
	
}

