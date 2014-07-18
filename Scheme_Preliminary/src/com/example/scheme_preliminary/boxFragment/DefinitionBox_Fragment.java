package com.example.scheme_preliminary.boxFragment;

import scheme_ast.Definition;
import unparser.ShallowUnparser;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.layout;
import com.example.scheme_preliminary.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DefinitionBox_Fragment extends Fragment {
    private ActivityCommunicator myActivityCommunicator;
    private Definition ast;
    private boolean clickable;
    
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		ast=(Definition) myActivityCommunicator.passDefOrExpToFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_definition_box__fragment, container, false);
		clickable=myActivityCommunicator.setClickabilityToFragment();
		TextView symbol=(TextView) v.findViewById(R.id.definition_symbol);
		symbol.setText(ast.getSymbol());
		TextView body=(TextView) v.findViewById(R.id.definition_body);
		body.setText(ShallowUnparser.shallowUnparse(ast.getBody(), 1));
		body.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickable==false)
					myActivityCommunicator.destroySubsequentFragments();
				myActivityCommunicator.passDefOrExpToActivity(ast.getBody());
				myActivityCommunicator.passLabelToActivity("def.body/");
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
