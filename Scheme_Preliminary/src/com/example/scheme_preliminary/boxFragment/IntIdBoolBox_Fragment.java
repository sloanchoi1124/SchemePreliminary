package com.example.scheme_preliminary.boxFragment;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.id;
import com.example.scheme_preliminary.R.layout;

import scheme_ast.BoolExpression;
import scheme_ast.Expression;
import scheme_ast.IdExpression;
import scheme_ast.IntExpression;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IntIdBoolBox_Fragment extends Fragment {
	private ActivityCommunicator myActivityCommunicator;
	private Expression ast;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ActivityCommunicator) activity;
		ast=(Expression) myActivityCommunicator.passDefOrExpToFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_int_id_box__fragment, container, false);
		TextView int_id_textview=(TextView) v.findViewById(R.id.int_id_text);
		if(ast instanceof IntExpression)
		{
			int_id_textview.setText(((Integer)((IntExpression) ast).getValue()).toString());
		}
		else if(ast instanceof IdExpression)
		{
			int_id_textview.setText(((IdExpression) ast).getId());
		}
		else if(ast instanceof BoolExpression)
		{
			if(((BoolExpression) ast).getValue())
				int_id_textview.setText("TRUE");
			else
				int_id_textview.setText("FALSE");
		}
		return v;
	}


}
