package com.example.scheme_preliminary;

import com.example.scheme_preliminary.boxFragment.BoxActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class Activity_Selector extends Activity implements OnClickListener{
    Button button1,button2,button3,button4;
    Bundle extras;
    String schemeText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__selector);
		button1=(Button) findViewById(R.id.button1_activity_selector);
		button2=(Button) findViewById(R.id.button2_activity_selector);
		button3=(Button) findViewById(R.id.button3_activity_selector);
		button4=(Button) findViewById(R.id.button4_activity_selector);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		extras=getIntent().getExtras();
		if(extras!=null)
			schemeText=extras.getString("schemeText");
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.button1_activity_selector:
			Intent view=new Intent(this,Viewing_Activity.class);
			if(extras!=null)
				view.putExtra("schemeText", schemeText);
			startActivity(view);
			break;
		case R.id.button2_activity_selector:
			Intent evaluate=new Intent(this,Evaluation_Activity.class);
			if(extras!=null)
				evaluate.putExtra("schemeText", schemeText);
			startActivity(evaluate);
			break;
		case R.id.button3_activity_selector:
			Intent navigate=new Intent(this,Navigation_Activity.class);
			if(extras!=null)
				navigate.putExtra("schemeText", schemeText);
			startActivity(navigate);
			break;
		case R.id.button4_activity_selector:
			Intent box=new Intent(this,BoxActivity.class);
			if(extras!=null)
				box.putExtra("schemeText",schemeText);
			startActivity(box);
			break;
		default:
			break;
		}

	}



}
