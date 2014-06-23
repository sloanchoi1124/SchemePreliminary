package com.example.scheme_preliminary;


import java.util.List;

import com.tiny_schemer.parser.Lexer;
import com.tiny_schemer.parser.Parser;
import com.tiny_schemer.parser.token.*;
import com.tiny_schemer.scheme_ast.*;
import com.tiny_schemer.unparser.Unparser;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

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
			Expression ast = Parser.parse(tokens);
			if(ast!= null) 
			{
				result=Unparser.unparse(ast);
				tv.setText(result);
			}
			
		}
	}


}
