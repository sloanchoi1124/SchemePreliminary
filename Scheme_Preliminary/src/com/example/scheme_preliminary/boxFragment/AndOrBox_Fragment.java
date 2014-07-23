package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;

import scheme_ast.AndExpression;
import scheme_ast.Expression;
import scheme_ast.OrExpression;
import unparser.ShallowUnparser;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.id;
import com.example.scheme_preliminary.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AndOrBox_Fragment extends Fragment {

	private Expression ast;
	private ArrayList<TextView> conditions;
	private TextView head;
	private TextView operator;
	private ActivityCommunicator myActivityCommunicator;
	private boolean clickable;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		if(myActivityCommunicator.passDefOrExpToFragment() instanceof AndExpression)
			ast=(AndExpression) myActivityCommunicator.passDefOrExpToFragment();
		else if(myActivityCommunicator.passDefOrExpToFragment() instanceof OrExpression)
			ast=(OrExpression) myActivityCommunicator.passDefOrExpToFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_and_or_box__fragment, container, false);
		clickable=myActivityCommunicator.setClickabilityToFragment();
		head=(TextView) v.findViewById(R.id.head_and_or);
		head.setTextSize(20);
		operator=(TextView) v.findViewById(R.id.operator_and_or);
		operator.setTextSize(20);
		LinearLayout conditions_layout=(LinearLayout) v.findViewById(R.id.conditions);
		conditions=new ArrayList<TextView>();
		if(ast instanceof AndExpression)
		{
			head.setText("AndExpression");
			operator.setText("AND");
			for(final Expression expression:((AndExpression)ast).getConditions())
			{
				TextView temp=new TextView(v.getContext());
				RelativeLayout.LayoutParams paramsTemp=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsTemp.setMargins(0, 20, 0, 20);
				temp.setLayoutParams(paramsTemp);
				temp.setText(ShallowUnparser.shallowUnparse(expression, 1));
				conditions.add(temp);
				temp.setTextSize(20);
				temp.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(clickable==false)
						{
							myActivityCommunicator.destroySubsequentFragments();
						}
						myActivityCommunicator.passDefOrExpToActivity(expression);
						int i=((AndExpression)ast).getConditions().indexOf(expression);
						myActivityCommunicator.passLabelToActivity("and.conditions"+((Integer)i).toString()+"/");
					}
				});
				temp.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						myActivityCommunicator.passReplacementTag("and", 
								((AndExpression) ast).getConditions().indexOf(expression));
						myActivityCommunicator.inputReplacementByCalculator();
						return true;
					}
				});
				conditions_layout.addView(temp);
			}
		}
		if(ast instanceof OrExpression)
		{
			head.setText("OrExpression");
			operator.setText("OR");
			for(final Expression expression:((OrExpression)ast).getConditions())
			{
				TextView temp=new TextView(v.getContext());
				RelativeLayout.LayoutParams paramsTemp=new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
				paramsTemp.setMargins(0, 20, 0, 20);
				temp.setLayoutParams(paramsTemp);
				temp.setText(ShallowUnparser.shallowUnparse(expression, 1));
				conditions.add(temp);
				temp.setTextSize(20);
				temp.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(clickable==false)
						{
							myActivityCommunicator.destroySubsequentFragments();
						}
						myActivityCommunicator.passDefOrExpToActivity(expression);
						int i=((OrExpression)ast).getConditions().indexOf(expression);
						myActivityCommunicator.passLabelToActivity("and.conditions"+((Integer)i).toString()+"/");
					}
				});
				temp.setOnLongClickListener(new View.OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						myActivityCommunicator.passReplacementTag("or", 
								((OrExpression) ast).getConditions().indexOf(expression));
						myActivityCommunicator.inputReplacementByCalculator();
						return false;
					}
				});
				conditions_layout.addView(temp);
			}
		}
		return v;
	}

}
