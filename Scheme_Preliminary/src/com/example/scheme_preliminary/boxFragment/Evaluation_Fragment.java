package com.example.scheme_preliminary.boxFragment;

import scheme_ast.Program;
import unparser.Unparser;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.layout;
import com.example.scheme_preliminary.R.menu;

import evaluator.Evaluator;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Evaluation_Fragment extends Fragment {
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
		View v = inflater.inflate(R.layout.activity_evaluation__fragment, container, false);
		this.text=(TextView) v.findViewById(R.id.evaluationfragment_text);
		this.text.setText(Unparser.unparse(Evaluator.evaluate(this.program)));
		this.text.setTextSize(30);
		return v;
	}

}
