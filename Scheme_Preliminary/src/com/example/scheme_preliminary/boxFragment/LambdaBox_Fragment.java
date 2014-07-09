package com.example.scheme_preliminary.boxFragment;

import java.util.List;

import com.example.scheme_preliminary.ActivityCommunicator;
import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.id;
import com.example.scheme_preliminary.R.layout;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.CallExpression;
import scheme_ast.LambdaExpression;
import unparser.ShallowUnparser;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LambdaBox_Fragment extends Fragment {
	
    private LambdaExpression ast;
    private TextView parameter;
    private TextView body;
    private ActivityCommunicator myActivityCommunicator;
    
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		ast=(LambdaExpression) myActivityCommunicator.passExpressionToFragment();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_lambda_box__fragment, container, false);
		
		
		parameter=(TextView) v.findViewById(R.id.parameters);
		body=(TextView) v.findViewById(R.id.body_lambda);
		
		String parameter_text="";
		for(String temp:ast.getParameters())
		{
			parameter_text+=temp+" ";
		}
		parameter.setText(parameter_text);
		
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
