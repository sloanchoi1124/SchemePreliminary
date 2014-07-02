package com.example.scheme_preliminary.calculator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scheme_preliminary.R;

public class Keypad_Fragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceBundle) {
        // Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.calculator_keypad, container, false);
        return v;
	}
	
}
