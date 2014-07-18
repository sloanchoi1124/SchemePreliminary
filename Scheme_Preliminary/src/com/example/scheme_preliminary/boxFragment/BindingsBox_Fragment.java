package com.example.scheme_preliminary.boxFragment;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.id;
import com.example.scheme_preliminary.R.layout;

import scheme_ast.*;
import unparser.ShallowUnparser;
import util.Pair;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class BindingsBox_Fragment extends Fragment {
	private List<Pair<String, Expression>> bindings;
	private ActivityCommunicator myActivityCommunicator;
	private boolean clickable;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_bindings_box__fragment, container, false);
		bindings=myActivityCommunicator.passBindingsToFragment();
		clickable=myActivityCommunicator.setClickabilityToFragment();
		LinearLayout valuesbackground=(LinearLayout) v.findViewById(R.id.valuesbackground);
		LinearLayout keysbackground=(LinearLayout) v.findViewById(R.id.keysbackground);
		for(final Pair<String,Expression> entry:bindings)
		{
			TextView valueTemp=new TextView(v.getContext());
			valueTemp.setText(ShallowUnparser.shallowUnparse(entry.second, 1));
			valueTemp.setTextSize(20);
			valueTemp.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(clickable==false)
						myActivityCommunicator.destroySubsequentFragments();
					myActivityCommunicator.passDefOrExpToActivity(entry.second);
					int i=bindings.indexOf(entry);
					myActivityCommunicator.passLabelToActivity("binding"+((Integer)i).toString()+"/");
				}
			});
			valueTemp.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					Pair<String,Expression> newEntry=new Pair<String, Expression>(entry.first,myActivityCommunicator.getReplacementFromCalculator());
					bindings.set(bindings.indexOf(entry), newEntry);
					return false;
				}
			});
			valuesbackground.addView(valueTemp);
			TextView keyTemp=new TextView(v.getContext());
			keyTemp.setText(entry.first);
			keyTemp.setTextSize(20);
			keysbackground.addView(keyTemp);
		}
		return v;
	}

}
