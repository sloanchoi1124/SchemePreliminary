package com.example.scheme_preliminary.boxFragment;

import java.util.List;

import com.example.scheme_preliminary.ActivityCommunicator;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class LetBox_Fragment extends Fragment {
    private LetExpression ast;
	private TextView bindings;
    private TextView body;
    private ActivityCommunicator myActivityCommunicator;
    
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		ast=(LetExpression) myActivityCommunicator.passExpressionToFragment();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_let_box__fragment, container, false);
		bindings=(TextView) v.findViewById(R.id.bindings);
		body=(TextView) v.findViewById(R.id.body_let);
		bindings.setText(ShallowUnparser.shallowBindings(ast.getBindings(), 0));
		bindings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("gotta pass bindings to activity");
				
				myActivityCommunicator.passBindingsToActivity(ast.getBindings());
			}
		});
		
		body.setText(ShallowUnparser.shallowUnparse(ast.getBody(), 1));
		body.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myActivityCommunicator.passExpressionToActivity(ast.getBody());
			}
		});
		return v;
	}


}
