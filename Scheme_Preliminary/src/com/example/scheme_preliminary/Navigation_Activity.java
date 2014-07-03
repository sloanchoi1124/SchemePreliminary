package com.example.scheme_preliminary;

import java.util.LinkedList;
import util.Pair;

import parser.Lexer;
import parser.Parser;
import scheme_ast.Expression;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

public class Navigation_Activity extends Activity implements Navigation_Fragment.MyListFragmentCommunicator {

	private String ROOT_TEXT;
	private TextView navPath;
	private TextView expressionType;
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

		// create singleton Expression list if possible
        this.listSourceList = new LinkedList<Pair<String,Expression>>();
        
        Expression exp;
        if ((exp = Parser.parse(Lexer.lex(schemeText))) != null) {
            // Set the title (path)
        	this.ROOT_TEXT = "/" + SchemeExpressionsAdapter.expressionType(exp);
            this.navPath = (TextView) findViewById(R.id.nav_path);
            this.navPath.setText(ROOT_TEXT);
            this.expressionType = (TextView) findViewById(R.id.expression_type);
            this.expressionType.setText(SchemeExpressionsAdapter.expressionType(exp));
            newFragment(new Pair<String, Expression>(null, exp), savedInstanceState);
        }
        else {
        	System.out.println("Parsing returned null");
        	return;
        }
        
        
//        System.out.println("Activity created");
	}

	public void onBackPressed() {
//		System.out.println(this.titleView.getText());
		super.onBackPressed();
		if (((String) this.navPath.getText()).equals(ROOT_TEXT) == false) {
			popFromPath();
			this.listSourceList.removeLast();
			this.expressionType.setText(SchemeExpressionsAdapter.expressionType(this.getListSource().second));
		}
		else finish();
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
		Navigation_Fragment fragment = new Navigation_Fragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		if (firstFragment)
			transaction.add(R.id.fragment_container, fragment,
					(String) this.navPath.getText());
		else {
			transaction.replace(R.id.fragment_container, fragment,
					(String) this.navPath.getText());
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
	
	public Pair<String, Expression> getListSource() {
		return this.listSourceList.getLast();
	}
	
	// Called when item in object is touched
	public void onItemTouch(Pair<String, Expression> pair) {
		addToPath(pair);
		this.expressionType.setText(SchemeExpressionsAdapter.expressionType(pair.second));
		newFragment(pair, false);
	}
	private void addToPath(Pair<String, Expression> pair) {
		String text = (String) this.navPath.getText();
		if (text.endsWith("/"))
			this.navPath.setText(text + this.expressionType.getText() + "." + pair.first + "/");
		else
			this.navPath.setText(text + "." + pair.first + "/");
	}
	private void popFromPath() {
		String s = (String) this.navPath.getText();
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
