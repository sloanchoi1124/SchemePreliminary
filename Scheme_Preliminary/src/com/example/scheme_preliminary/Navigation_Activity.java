package com.example.scheme_preliminary;

import java.util.LinkedList;

import parser.Lexer;
import parser.Parser;

import scheme_ast.Expression;
import scheme_ast.Program;
import util.Pair;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.scheme_preliminary.Navigation_Fragment_Expression.ExpressionFragmentCommunicator;
import com.example.scheme_preliminary.Navigation_Fragment_Program.ProgramFragmentCommunicator;

public class Navigation_Activity extends Activity implements ProgramFragmentCommunicator, ExpressionFragmentCommunicator {

	private String ROOT_TEXT;
	private TextView navPath;
	private TextView expressionType;
	private Program sourceProgram;
	private LinkedList<Pair<String,Expression>> listSourceList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation_);
		Bundle b = getIntent().getExtras();
		String schemeText;
		if (b != null) {
			schemeText = b.getString("schemeText");
		} else {
			return;
		}

		this.sourceProgram = Parser.parse(Lexer.lex(schemeText));
        
		this.ROOT_TEXT = "/";
        this.navPath = (TextView) findViewById(R.id.nav_path);
	    this.navPath.setMovementMethod(new ScrollingMovementMethod());
        this.navPath.setText(this.ROOT_TEXT);
        this.expressionType = (TextView) findViewById(R.id.expression_type);
        this.expressionType.setText("ROOT");
        
        getFragmentManager().beginTransaction().add(R.id.fragment_container,
        											new Navigation_Fragment_Program(),
        											this.navPath.getText().toString()).commit();
//        System.out.println("Activity created");
	}

	public Program getSourceProgram() {
		return this.sourceProgram;
	}
	
	public void onExpressionTouch(Pair<String, Expression> pair) {
		// create singleton Expression list if possible
        this.listSourceList = new LinkedList<Pair<String,Expression>>();
        this.ROOT_TEXT = "/" + pair.first + "/";
		this.navPath.setText(this.ROOT_TEXT);
        this.expressionType.setText(SchemeExpressionsAdapter.expressionType(pair.second));
//        newFragment(new Pair<String, Expression>(null, pair.second), null);
        newFragment(pair, null);
	}
	
	public void onBackPressed() {
//		System.out.println(this.titleView.getText());
		if ((this.navPath.getText().toString()).equals(this.ROOT_TEXT) == false) {
			popFromPath();
			this.listSourceList.removeLast();
			if (this.getSourceExpression() != null)
				this.expressionType.setText(SchemeExpressionsAdapter.expressionType(this.getSourceExpression().second));
		}
		else {
			this.ROOT_TEXT = "/";
			this.navPath.setText(this.ROOT_TEXT);
			this.expressionType.setText("ROOT");
		}
		super.onBackPressed();
//			finish();
	}
	
	public void newFragment(Pair<String, Expression> pair, Bundle savedInstanceState) {
		if (findViewById(R.id.fragment_container) != null) {
			// If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) return;
            else newFragment(pair, true);
		}
		else System.out.println("Can't find fragment_container");
	}
	
	public void newFragment(Pair<String, Expression> pair, boolean firstFragment) {
		this.listSourceList.add(pair);
		Navigation_Fragment_Expression fragment = new Navigation_Fragment_Expression();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		if (firstFragment)
//			transaction.add(R.id.fragment_container, fragment,
//					this.navPath.getText().toString());
//		else {
		transaction.replace(R.id.fragment_container, fragment,
				this.navPath.getText().toString());
		transaction.addToBackStack(null);
//		}
		transaction.commit();
	}
	
	public Pair<String, Expression> getSourceExpression() {
		return this.listSourceList.getLast();
	}
	
	// Called when item in object is touched
	public void onItemTouch(Pair<String, Expression> pair) {
		addToPath(pair);
		this.expressionType.setText(SchemeExpressionsAdapter.expressionType(pair.second));
		newFragment(pair, false);
	}
	private void addToPath(Pair<String, Expression> pair) {
		String text = this.navPath.getText().toString();
		if (text.endsWith("/"))
			this.navPath.setText(text + this.expressionType.getText() + "." + pair.first + "/");
		else
			this.navPath.setText(text + "." + pair.first + "/");
	}
	private void popFromPath() {
		String s = this.navPath.getText().toString();
		int pos = s.lastIndexOf("/", s.length()-2);
			if (pos == 0 || pos == -1) {
				pos = s.lastIndexOf(".");
				if (pos == -1) {
					System.out.println("Why is this still being called?");
					return;
				}
			}
		s = s.substring(0, pos+1);
		if (s.endsWith(".")) s = s.substring(0, s.length()-1);
		this.navPath.setText(s);
	}
	

}



//if (getListAdapter() != null) {
//ListView listView = (ListView) findViewById(android.R.id.list);
//  
//  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//  	@SuppressLint("NewApi") @Override
//  	public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//
//			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//			String title = (String) ((TextView) findViewById(R.id.nav_path)).getText();
//			Object item = parent.getItemAtPosition(position);
//  		if (item instanceof JSONPair) {
//  			JSONPair pair = (JSONPair) item;
//  			intent.putExtra("json", pair.getValue().toString());
//  			intent.putExtra("title", title + " / " + pair.getKey());
//  		}
//  		else {
//  			intent.putExtra("json", item.toString());
//  			intent.putExtra("title", String.format(title + "[%d]", position));
//  		}
//  		startActivity(intent);
//  	}
//  });
//  
//}
