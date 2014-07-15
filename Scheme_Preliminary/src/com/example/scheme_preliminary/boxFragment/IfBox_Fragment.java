package com.example.scheme_preliminary.boxFragment;

import java.util.List;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.id;
import com.example.scheme_preliminary.R.layout;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.CallExpression;
import scheme_ast.IfExpression;
import unparser.ShallowUnparser;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IfBox_Fragment extends Fragment {
    private ActivityCommunicator myActivityCommunicator;
    private TextView condition;
    private TextView then;
    private TextView otherwise;
    private IfExpression ast;
    private boolean clickable;
    
    

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		ast=(IfExpression) myActivityCommunicator.passDefOrExpToFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_if_box__fragment, container, false);
		clickable=myActivityCommunicator.setClickabilityToFragment();
		condition=(TextView) v.findViewById(R.id.condition);
		condition.setText(ShallowUnparser.shallowUnparse(ast.getCondition(), 1));
		condition.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickable==false)
					myActivityCommunicator.destroySubsequentFragments();
				myActivityCommunicator.passDefOrExpToActivity(ast.getCondition());
				myActivityCommunicator.passLabelToActivity("if.condition/");
			}
		});
		
		then=(TextView) v.findViewById(R.id.then);
		then.setText(ShallowUnparser.shallowUnparse(ast.getThen(), 1));
		then.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickable==false)
					myActivityCommunicator.destroySubsequentFragments();
				myActivityCommunicator.passDefOrExpToActivity(ast.getThen());
				myActivityCommunicator.passLabelToActivity("if.then/");
			}
		});
		
		otherwise=(TextView) v.findViewById(R.id.otherwise);
		otherwise.setText(ShallowUnparser.shallowUnparse(ast.getElse(), 1));
		otherwise.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickable==false)
					myActivityCommunicator.destroySubsequentFragments();
				myActivityCommunicator.passDefOrExpToActivity(ast.getElse());
				myActivityCommunicator.passLabelToActivity("if.else/");
			}
		});
		return v;
	}
}
