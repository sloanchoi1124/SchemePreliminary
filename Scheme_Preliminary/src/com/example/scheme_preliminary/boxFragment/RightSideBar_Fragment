package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;
import java.util.List;

import com.example.scheme_preliminary.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RightSideBar_Fragment extends Fragment {

	private  RightSideBarActivityCommunicator myActivityCommunicator;
	private List<String> keysList;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(RightSideBarActivityCommunicator) activity;
		keysList=myActivityCommunicator.passKeyListToRightSideBar();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_right_side_bar__fragment, container, false);
		//keysList=myActivityCommunicator.passKeyListToRightSideBar();
		System.out.println("got something from the activity");
		System.out.println(keysList);
		
		LinearLayout background=(LinearLayout) v.findViewById(R.id.rightsidebar_background);
		final ArrayList<TextView> textList=new ArrayList<TextView>();
		for(String s:keysList)
		{
			final TextView temp=new TextView(v.getContext());
			temp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			temp.setText(s);
			temp.setTextSize(20);
			background.addView(temp);
			textList.add(temp);
			temp.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(temp.getText().toString());
					myActivityCommunicator.passTopLevelExpressionToActivity(temp.getText().toString());
					temp.setBackgroundColor(Color.YELLOW);
					for(TextView tv:textList)
					{
						if(tv!=temp)
							tv.setBackgroundColor(Color.LTGRAY);
					}
				}
			});
		}
		
		return v;
	}
	
	public interface RightSideBarActivityCommunicator
	{
		public void initializeRightSideBar();
		//pass the key to the map to box activity
		public void passTopLevelExpressionToActivity(String s);
		//take in the list of keys from box activity
		public List<String> passKeyListToRightSideBar();
	}
	
}
