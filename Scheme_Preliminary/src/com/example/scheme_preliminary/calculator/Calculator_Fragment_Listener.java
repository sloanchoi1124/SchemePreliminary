package com.example.scheme_preliminary.calculator;

import java.util.List;

import android.view.View;

public interface Calculator_Fragment_Listener {
	
	// from IdCreator
	public void onStringCreated(String id);
	public Object getSystemService(String name);
	
	// from IdSelector
	public List<String> getListSource();
	public void onIdSelected(String id);
	
	// from OpSelector
	public void onOpSelected(View v);
	
	// from KeypadCreator
	public void onKeypadCreated();
	
}