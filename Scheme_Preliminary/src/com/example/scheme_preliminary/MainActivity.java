package com.example.scheme_preliminary;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button1=(Button) findViewById(R.id.button1);
		Button button2=(Button) findViewById(R.id.button2);
		Button button3=(Button) findViewById(R.id.button3);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i=new Intent(this,Activity_Selector.class);
		String s;
		switch(v.getId())
		{
		case R.id.button1:
			s="(if (< 3 4) (* 5 6) (- 18 7))";
			break;
		case R.id.button2:
			s="(if (< 3 4) (* 5 6) (- 18 7))";
			break;
		case R.id.button3:
			s="(if (< 3 4) (* 5 6) (- 18 7))";
			break;
		default:
			s="(if (< 3 4) (* 5 6) (- 18 7))";
			break;
		}
		i.putExtra("schemeText",s);
		startActivity(i);
	}

}
