package com.example.scheme_preliminary;

import java.util.List;

import com.tiny_schemer.evaluator.Evaluator;
import com.tiny_schemer.parser.Lexer;
import com.tiny_schemer.parser.Parser;
import com.tiny_schemer.parser.token.Token;
import com.tiny_schemer.scheme_ast.Expression;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class Evaluation_Activity extends Activity {
    TextView tv;
    Bundle extras;
    String schemeText;
    int result;
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
			Expression ast = Parser.parse(tokens);
			if(ast!= null) 
			{
				result=Evaluator.evaluate(ast);
				tv.setText(((Integer)result).toString());
			}
			
		}
	}



}
