package com.example.scheme_preliminary.boxFragment;

import com.example.scheme_preliminary.R;

import scheme_ast.Program;
import unparser.Unparser;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Viewing_Fragment extends Fragment {

	private Program program;
	private TextView text;
	private ProgramLevelCommunicator myActivityCommunicator;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.myActivityCommunicator=(ProgramLevelCommunicator) activity;
		this.program=((ProgramLevelCommunicator) myActivityCommunicator).getProgramFromActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_viewing__fragment, container, false);
		this.text=(TextView) v.findViewById(R.id.viewingfragment_textview);
		this.text.setText(Unparser.unparse(this.program));
		this.text.setTextSize(20);
		return v;
	}
	


}
