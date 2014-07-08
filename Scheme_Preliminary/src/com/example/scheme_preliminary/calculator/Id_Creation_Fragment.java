package com.example.scheme_preliminary.calculator;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.scheme_preliminary.R;

public class Id_Creation_Fragment extends Fragment implements OnClickListener {
	
	public interface IdCreator {
		public void onStringCreated(String id);
		public Object getSystemService(String name);
	}
	
	IdCreator mCallback;
	InputMethodManager imm;
	EditText editText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceBundle) {
		View v = inflater.inflate(R.layout.activity_calculator_id_creation, container, false);
//		View v = inflater.inflate(R.layout.activity_string__creator, container, false);
        return v;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
            this.mCallback = (IdCreator) activity;
    		this.imm = (InputMethodManager) this.mCallback.getSystemService(Context.INPUT_METHOD_SERVICE);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IdCreator");
        }
	}
	
	@Override
	public void onStart() {
		super.onStart();
		editText = (EditText) getView().findViewById(R.id.editText2);
		editText.setBackgroundColor(Color.WHITE);
		editText.setHint("Set ID");
		((Button) getView().findViewById(R.id.goButton)).setOnClickListener(this);
//		((Button) getView().findViewById(R.id.goButton)).setBackgroundColor(Color.WHITE);
	}
	
	@Override 
	public void onResume() {
		super.onResume();
		
		editText.requestFocus();
		this.imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
	}
	
	@Override
	public void onClick(View v) {
		this.imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
		this.mCallback.onStringCreated(this.editText.getText().toString());
	}

}
