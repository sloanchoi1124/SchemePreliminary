package com.example.scheme_preliminary.calculator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scheme_preliminary.R;

public class Op_Selection_Fragment extends Fragment {
	
	Calculator_Fragment_Listener mCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_calculator_oppad, container, false);
		return v;
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
            this.mCallback = (Calculator_Fragment_Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OpSelector");
        }
	}
	
	@Override
	public void onStart() {
		super.onStart();
//		getView().setBackgroundColor(Color.LTGRAY);
	}

}
