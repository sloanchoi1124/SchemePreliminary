package com.example.scheme_preliminary;

import java.util.List;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.Expression;
import scheme_ast.IntExpression;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import evaluator.Evaluator;

public class Evaluation_Activity extends Activity {
    TextView tv;
    Bundle extras;
    String schemeText;
    IntExpression result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation_);
		tv=(TextView) findViewById(R.id.activity_evaluation);
		extras=getIntent().getExtras();
		if(extras!=null)
		{
			schemeText=extras.getString("schemeText");
			List<Token> tokens=Lexer.lex(schemeText);
			Expression ast = Parser.parseExpression(tokens);
			if(ast!= null) 
			{
				//result=Evaluator.evaluate(ast);
				tv.setText("" + result.getValue());
			}
			
		}
	}



}
