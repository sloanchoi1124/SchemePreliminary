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
		Button button4=(Button) findViewById(R.id.button4);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i;
		String s;
		switch(v.getId())
		{
		case R.id.button1:
			i=new Intent(this,Activity_Selector.class);
			s="(if (< 3 4) (if (< 3 4) (if (< 3 4) (* 5 6) (- 18 7)) (- 18 7)) (- 18 7))";
			i.putExtra("schemeText",s);
			break;
		case R.id.button2:
			i=new Intent(this,Activity_Selector.class);
			//s="(if (< 3 4) (* 5 6) (- 18 7))";
			s="(if (< 3 4) (let ((a 1) (b 2) (c 3)) (+ a b c)) (let ((x (lambda (a b c) (* a b c)))) (x 1 2 3)))";
			i.putExtra("schemeText",s);
			break;
		case R.id.button3:
			i=new Intent(this,String_Creator.class);
			/*
			s="(if (< 3 4) (let ((a 1) (b 2) (c 3)) (+ a b c)) (let ((x (lambda (a b c) (* a b c)))) (x 1 2 3)))";
			*/
			break;
		case R.id.button4:
			i=new Intent(this,Calculator_Activity.class);
			break;
		default:
			i=new Intent(this,Activity_Selector.class);
			s="(if (< 3 4) (* 5 6) (- 18 7))";
			i.putExtra("schemeText",s);
			break;
		}
		startActivity(i);
	}

}
