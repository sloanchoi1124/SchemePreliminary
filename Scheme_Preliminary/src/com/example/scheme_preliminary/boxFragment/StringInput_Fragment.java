package com.example.scheme_preliminary.boxFragment;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.R.layout;
import com.example.scheme_preliminary.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class StringInput_Fragment extends Fragment {

	private EditText myEditText;
	private Button go;
	private TextView tv;
	private StringInputCommunicator myActivityCommunicaotr;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicaotr=(StringInputCommunicator) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_string_input__fragment, container, false);
		tv=(TextView) v.findViewById(R.id.textView1);
		tv.setTextSize(20);
		myEditText=(EditText) v.findViewById(R.id.stringInputEditText);
		go=(Button) v.findViewById(R.id.stringInputGo);
		go.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myActivityCommunicaotr.passStringToActivity(myEditText.getText().toString());
			}
		});
		return v;
	}

    public interface StringInputCommunicator
    {
	   public void passStringToActivity(String s);
	}


}
