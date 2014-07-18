package com.example.scheme_preliminary.boxFragment;

import java.util.HashMap;
import java.util.List;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.id;
import com.example.scheme_preliminary.R.layout;

import scheme_ast.LetExpression;
import unparser.ShallowUnparser;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LetBox_Fragment extends Fragment {
    private LetExpression ast;
	private TextView bindings;
    private TextView body;
    private ActivityCommunicator myActivityCommunicator;
    private boolean clickable;
    
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		ast=(LetExpression) myActivityCommunicator.passDefOrExpToFragment();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_let_box__fragment, container, false);
		clickable=myActivityCommunicator.setClickabilityToFragment();
		bindings=(TextView) v.findViewById(R.id.bindings);
		body=(TextView) v.findViewById(R.id.body_let);
		bindings.setText(ShallowUnparser.shallowBindings(ast.getBindings(), 0));
		bindings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("gotta pass bindings to activity");
				if(clickable==false)
					myActivityCommunicator.destroySubsequentFragments();
				myActivityCommunicator.passBindingsToActivity(ast.getBindings());
				myActivityCommunicator.passLabelToActivity("let.bindings/");
			}
		});
		
		body.setText(ShallowUnparser.shallowUnparse(ast.getBody(), 1));
		body.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickable==false)
					myActivityCommunicator.destroySubsequentFragments();
				myActivityCommunicator.passDefOrExpToActivity(ast.getBody());
				myActivityCommunicator.passLabelToActivity("let.body/");
			}
		});
		
		body.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				ast.setBody(myActivityCommunicator.getReplacementFromCalculator());
				return false;
			}
		});
		return v;
	}


}
