package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;
import java.util.List;

import com.example.scheme_preliminary.ActivityCommunicator;
import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.id;
import com.example.scheme_preliminary.R.layout;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.CallExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import unparser.ShallowUnparser;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CallBox_Fragment extends Fragment {
	
	private CallExpression ast;
	private TextView operator;
	private ArrayList<TextView> operands;
	private ActivityCommunicator myActivityCommunicator;
	

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		ast=(CallExpression) myActivityCommunicator.passExpressionToFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_call_box__fragment, container, false);
		
		TextView operator=(TextView) v.findViewById(R.id.operator);
		operator.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//this is revised intentionally!!
				myActivityCommunicator.passExpressionToActivity(ast.getOperator());
			}
		});
		
	    operator.setText(ShallowUnparser.shallowUnparse(ast.getOperator(), 1));
	    LinearLayout operands_layout=(LinearLayout) v.findViewById(R.id.operands);
		operands=new ArrayList<TextView>();
		for(final Expression expression: ast.getOperands())
		{
			TextView temp=new TextView(v.getContext());
			temp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			temp.setText(ShallowUnparser.shallowUnparse(expression, 1));
			operands.add(temp);
			temp.setTextSize(20);
			temp.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myActivityCommunicator.passExpressionToActivity(expression);
				}
			});
			operands_layout.addView(temp);
		}
		return v;
	}
}
