package com.example.scheme_preliminary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.scheme_preliminary.calculator.Calculator_Fragment;

import file.io.FileChooser;
import file.io.ProjectFileSetup;

public class MainActivity extends Activity implements OnClickListener{

	public final static int FILE_SELECT_REQUEST = 042;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startService(new Intent(this, ProjectFileSetup.class)); // stops itself
		
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("1");
		if (requestCode == FILE_SELECT_REQUEST && resultCode == RESULT_OK) {
//			String path = data.getData().getEncodedPath();
			String path = data.getStringExtra("path");
			System.out.println("YO");
			try {
				File f = new File(path);
			    BufferedReader reader = new BufferedReader(new FileReader(f));
				StringBuilder buf = new StringBuilder();
			    String str;
			    while ((str = reader.readLine()) != null) {
			    	buf.append(str + "\n");
			    }
			    reader.close();
			    
				Intent i = new Intent(this, Activity_Selector.class);
				i.putExtra("schemeText", buf.toString());
				startActivity(i);
			}
			catch (IOException ioe) {
				System.out.println(ioe);
				ioe.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent i;
		String s;
		switch(v.getId())
		{
		case R.id.button0:
//			Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
//		    chooserIntent.setType("file/*");
//		    System.out.println(chooserIntent.getType());
//		    chooserIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//		    startActivityForResult(chooserIntent, FILE_SELECT_REQUEST);
			i = new Intent(this, FileChooser.class);
			i.putExtra("absolutePath", Environment.getExternalStorageDirectory().getPath() + "/com.example.scheme_preliminary/");
			startActivityForResult(i, FILE_SELECT_REQUEST);
			System.out.println("Started activity");
		    return;
		case R.id.LambdaButton:
			i=new Intent(this,Activity_Selector.class);
			//s="(if (< 3 4) (if (< 3 4) (if (< 3 4) (* 5 6) (- 18 7)) (- 18 7)) (- 18 7))";
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
		case R.id.DividedByButton:
			i=new Intent(this,Activity_Selector.class);
			//s="(+ 1 2)";
			//s="(if (< 3 4) (* 5 6) (- 18 7))";
			//s="( let ( x 1 ) ( + x 1 ) )";
			//s="(let ((a 1) (b 2) (c 3)) (+ a b c))";
			//s="(lambda (a b c) (* a b c))";
			//s="(< 3 (+ 1 3))";
			s="(if (< (+ 1 2) 4) (let ((a 1) (b 2) (c 3)) (+ a b c)) (let ((x (lambda (a b c) (* a b c)))) (x 1 2 3)))";
			i.putExtra("schemeText",s);
			break;
		case R.id.button3:
			i=new Intent(this,String_Creator.class);
			/*
			s="(if (< 3 4) (let ((a 1) (b 2) (c 3)) (+ a b c)) (let ((x (lambda (a b c) (* a b c)))) (x 1 2 3)))";
			*/
			break;
		case R.id.button4:
			i=new Intent(this,Calculator_Fragment.class);
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
