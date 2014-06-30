package com.example.scheme_preliminary;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;

public class String_Creator extends Activity implements OnClickListener{
    EditText ed;
    String schemeText;
    Intent activity_selector;
    Button go;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_string__creator);
		ed=(EditText) findViewById(R.id.editText1);
		go=(Button) findViewById(R.id.string_creator_done);
		go.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		activity_selector=new Intent(this,Activity_Selector.class);
		activity_selector.putExtra("schemeText",ed.getText().toString());
		startActivity(activity_selector);
	}


}
