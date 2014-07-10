package com.example.scheme_preliminary;


import java.util.List;

import parser.Lexer;
import parser.Parser;
import parser.token.Token;
import scheme_ast.Expression;
import unparser.Unparser;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Viewing_Activity extends Activity {
    TextView tv;
    Bundle extras;
    String schemeText;
    String result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewing_);
		tv=(TextView) findViewById(R.id.activity_viewing);
		extras=getIntent().getExtras();
		if(extras!=null)
		{
			schemeText=extras.getString("schemeText");
			List<Token> tokens = Lexer.lex(schemeText);
			Expression ast = Parser.parseExpression(tokens);
			if(ast!= null) 
			{
				result=Unparser.unparse(ast);
				tv.setText(result);
			}
			
		}
	}


}
