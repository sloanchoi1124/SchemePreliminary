package com.example.scheme_preliminary;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Activity_Selector extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__selector);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity__selector, menu);
		return true;
	}

}
