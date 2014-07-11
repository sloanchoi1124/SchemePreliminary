package com.example.scheme_preliminary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import scheme_ast.DefOrExp;
import scheme_ast.Definition;
import scheme_ast.Expression;
import scheme_ast.Program;
import util.Pair;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Navigation_Fragment_Program extends ListFragment {
	
	private ProgramFragmentCommunicator mCallback;
	private Program sourceProgram;
	
	public interface ProgramFragmentCommunicator {
		public Program getSourceProgram();
		public void onExpressionTouch(Pair<String, Expression> pair);
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
		try {
            this.mCallback = (ProgramFragmentCommunicator) activity;
            this.sourceProgram = this.mCallback.getSourceProgram();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ProgramFragmentCommunicator");
        }
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceBundle) {
		View v = inflater.inflate(R.layout.activity_navigation_fragment, container, false);
		
		if (createList()) // provides debug printing
			return v;
		else {
			return null;
		}
	}
	
	public boolean createList() {
        // Create the appropriate Adapter
		final SchemeExpressionsAdapter adapter;
		List<Pair<String, Expression>> list = new ArrayList<Pair<String, Expression>>();
		
		List<DefOrExp> dOEList = this.sourceProgram.getProgram();
		Iterator<DefOrExp> iter = dOEList.iterator();
		int index = -1;
		DefOrExp defOrExp;
		while (iter.hasNext()) {
			defOrExp = iter.next();
			index++;
			if (defOrExp instanceof Expression)
//				list.add(new Pair<String, Expression>(SchemeExpressionsAdapter.expressionType((Expression) defOrExp), 
//													 (Expression) defOrExp));
				list.add(new Pair<String, Expression>("expAt(" + Integer.toString(index) + ")", (Expression) defOrExp));
			else if (defOrExp instanceof Definition) {
				Definition def = (Definition) defOrExp;
				list.add(new Pair<String, Expression>("def " + def.getSymbol(), def.getBody()));
			}
		}
		
		
		adapter = new SchemeExpressionsAdapter((Navigation_Activity) this.mCallback, list);
		System.out.println(adapter.getCount());
		setListAdapter(adapter);
		System.out.println("set list adapter");
		return true;
	}
	
	

//	From when it is a ListFragment
	@Override
	public void onListItemClick(ListView listView, View v, int position, long id) {
		Pair<String, Expression> pair = (Pair<String, Expression>) listView.getItemAtPosition(position);
		this.mCallback.onExpressionTouch(pair);
	}
}
