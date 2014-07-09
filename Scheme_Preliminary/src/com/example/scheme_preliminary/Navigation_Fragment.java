package com.example.scheme_preliminary;

import java.util.ArrayList;
import java.util.List;

import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IfExpression;
import scheme_ast.IntExpression;
import scheme_ast.LambdaExpression;
import scheme_ast.LetExpression;
import util.Pair;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class Navigation_Fragment extends ListFragment {
	
	private MyListFragmentCommunicator mCallback;
	private Pair<String, Expression> listSource;
	
	public interface MyListFragmentCommunicator {
		public Pair<String, Expression> getListSource();
		public void onItemTouch(Pair<String, Expression> pair);
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
		try {
            this.mCallback = (MyListFragmentCommunicator) activity;
            this.listSource = this.mCallback.getListSource();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragmentCommunicator");
        }
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceBundle) {
		View v = inflater.inflate(R.layout.activity_navigation_fragment, container, false);
		
		if (createList(v, this.listSource)) // provides debug printing
			return v;
		else {
			return null;
		}
	}
	
	public boolean createList(View v, Pair<String, Expression> pair) {
        // Create the appropriate Adapter
		final SchemeExpressionsAdapter adapter;
		List<Pair<String, Expression>> list = new ArrayList<Pair<String, Expression>>();
		
		Expression sourceExpression  = this.listSource.second;
		if (sourceExpression == null) {
			Toast toast = Toast.makeText(this.getActivity(), this.listSource.first, Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		else if (sourceExpression instanceof IdExpression ||
				 sourceExpression instanceof IntExpression) {
			Toast toast = Toast.makeText(this.getActivity(),
					SchemeExpressionsAdapter.pairFormat(sourceExpression).second, Toast.LENGTH_SHORT);
			toast.show();
			return false;
		}
		else if (sourceExpression instanceof CallExpression) {
			list.add(new Pair<String,Expression>("op", ((CallExpression) sourceExpression).getOperator()));
			int argNumber = 0;
			for (Expression exp : ((CallExpression) sourceExpression).getOperands()) {
				list.add(new Pair<String,Expression>("arg_" + argNumber, exp));
				argNumber++;
			}
		}
		else if (sourceExpression instanceof IfExpression) {
			list.add(new Pair<String, Expression>("cond", ((IfExpression) sourceExpression).getCondition()));
			list.add(new Pair<String, Expression>("then", ((IfExpression) sourceExpression).getThen()));
			list.add(new Pair<String, Expression>("else", ((IfExpression) sourceExpression).getElse()));
		}
		else if (sourceExpression instanceof LambdaExpression) {
			for (String inp : ((LambdaExpression) sourceExpression).getParameters())
				list.add(new Pair<String, Expression>(inp, null));
			list.add(new Pair<String, Expression>("body", ((LambdaExpression) sourceExpression).getBody()));
		}
		else if (sourceExpression instanceof LetExpression) {
			List<Pair<String, Expression>> bindings = ((LetExpression) sourceExpression).getBindings();
			for (Pair<String, Expression> i: bindings) {
				list.add(new Pair<String, Expression>("bind " + i.first, i.second));
			}
			list.add(new Pair<String, Expression>("body", ((LetExpression) sourceExpression).getBody()));
		}
		else {
			System.out.println("Nothing more to show");
		}
		adapter = new SchemeExpressionsAdapter((Navigation_Activity) getActivity(), list);
		System.out.println(adapter.getCount());
		setListAdapter(adapter);
		System.out.println("set list adapter");
		return true;
	}
	
	

//	From when it is a ListFragment
	@Override
	public void onListItemClick(ListView listView, View v, int position, long id) {
		Pair<String, Expression> pair = (Pair<String, Expression>) listView.getItemAtPosition(position);
		this.mCallback.onItemTouch(pair);
	}
}

	