package com.example.scheme_preliminary;

import com.example.scheme_preliminary.calculator.Calculator_Activity;

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
		Button button0=(Button) findViewById(R.id.button0);
		Button button1=(Button) findViewById(R.id.LambdaButton);
		Button button2=(Button) findViewById(R.id.DividedByButton);
		Button button3=(Button) findViewById(R.id.button3);
		Button button4=(Button) findViewById(R.id.button4);
		button0.setOnClickListener(this);
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
		case R.id.button0:
			i=new Intent(this,Activity_Selector.class);
			s="(let " +
				    "((modExp " + 
				    	      "(lambda (base exponent modulus) " +
				    	        "(let " +
				    	            "((modExpRec " +
				    	              "(lambda (a sq x) " +
				    	                "(if (= x 0) " +
				    	                    "a " +
				    	                    "(let " +
				    	                        "((newA  " +
				    	                          "(if (odd? x) " +
				    	                              "(remainder (* a sq) modulus) " +
				    	                              "a)) " +
				    	                         "(newSq (remainder (* sq sq) modulus)) " +
				    	                         "(newX (quotient x 2))) " +
				    	                      "(modExpRec newA newSq newX)))))) " +
				    	          "(modExpRec 1 (remainder base modulus) exponent))))) " +
				    	  "(modExp 2 100 101))";
			i.putExtra("schemeText",s);
			break;
		case R.id.LambdaButton:
			i=new Intent(this,Activity_Selector.class);
			s="(if (< 3 4) (if (< 3 4) (if (< 3 4) (* 5 6) (- 18 7)) (- 18 7)) (- 18 7))";
			i.putExtra("schemeText",s);
			break;
		case R.id.DividedByButton:
			i=new Intent(this,Activity_Selector.class);
			s="(if (< 3 4) (* 5 6) (- 18 7))";
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
