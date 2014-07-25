package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;

import com.example.scheme_preliminary.R;

import scheme_ast.CondExpression;
import scheme_ast.ConsExpression;
import scheme_ast.Definition;
import scheme_ast.Expression;
import unparser.ShallowUnparser;
import util.Pair;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CondBox_Fragment extends Fragment {
	private ActivityCommunicator myActivityCommunicator;
	private CondExpression ast;
	private boolean clickable;
	private LinearLayout pair1_layout;
	private LinearLayout pair2_layout;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		if(myActivityCommunicator.passDefOrExpToFragment() instanceof CondExpression)
			ast=(CondExpression) myActivityCommunicator.passDefOrExpToFragment();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_cond_box__fragment, container, false);
		clickable=this.myActivityCommunicator.setClickabilityToFragment();
		pair1_layout=(LinearLayout) v.findViewById(R.id.pair1_background);
		pair2_layout=(LinearLayout) v.findViewById(R.id.pair2_background);
		for(int i=0;i<ast.getSize();i++)
		{
			final int currentIndex=i;
			TextView left=new TextView(v.getContext());
			TextView right=new TextView(v.getContext());
			left.setText(ShallowUnparser.shallowUnparse(ast.getCond(i), 1));
			right.setText(ShallowUnparser.shallowUnparse(ast.getBody(i), 1));
			left.setTextSize(20);
			right.setTextSize(20);
			left.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(clickable==false)
						myActivityCommunicator.destroySubsequentFragments();
					myActivityCommunicator.passDefOrExpToActivity(ast.getCond(currentIndex));
					myActivityCommunicator.passLabelToActivity("cond.pair"+((Integer)currentIndex).toString()+"pair/");
				}
			});
			right.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(clickable==false)
						myActivityCommunicator.destroySubsequentFragments();
					myActivityCommunicator.passDefOrExpToActivity(ast.getBody(currentIndex));
					myActivityCommunicator.passLabelToActivity("cond.pair"+((Integer)currentIndex).toString()+"body/");
				}
			});
			left.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					myActivityCommunicator.passReplacementTag("cond.left", currentIndex);
					myActivityCommunicator.inputReplacementByCalculator();
					return true;
				}
			});
			right.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					myActivityCommunicator.passReplacementTag("cond.right", currentIndex);
					myActivityCommunicator.inputReplacementByCalculator();
					return true;
				}
			});
			pair1_layout.addView(left);
			pair2_layout.addView(right);
		}
		return v;
	}

	
}
