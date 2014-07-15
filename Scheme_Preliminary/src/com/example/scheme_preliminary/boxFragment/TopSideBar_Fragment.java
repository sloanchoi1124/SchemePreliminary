package com.example.scheme_preliminary.boxFragment;

import java.util.List;

import com.example.scheme_preliminary.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class TopSideBar_Fragment extends Fragment {

	TopSideBarActivityCommunicator myActivityCommunicator;
	List<String> labelsList;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(TopSideBarActivityCommunicator) activity;
		labelsList=myActivityCommunicator.passLabelListToFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_top_side_bar__fragment, container, false);
		LinearLayout background=(LinearLayout) v.findViewById(R.id.top_side_bar_background);
		for(final String label:labelsList)
		{
			Button temp=new Button(v.getContext());
			temp.setText(label);
			background.addView(temp);
			temp.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myActivityCommunicator.passIndexToActivity(labelsList.indexOf(label));
				}
			});
		}
		return v;
	}
	
	public interface TopSideBarActivityCommunicator
	{
		public void initializeTopSideBar();
		public void passIndexToActivity(int i);
		public List<String> passLabelListToFragment();
	}


}
