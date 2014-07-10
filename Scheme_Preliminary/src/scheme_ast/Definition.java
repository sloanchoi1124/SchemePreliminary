package scheme_ast;

public class Definition extends DefOrExp{
	
	private String mSymbol;
	private Expression mBody;
	
	public Definition(String s, Expression e){
		super();
		this.mBody = e;
		this.mSymbol = s;
	}
	
	public String getSymbol(){
		return this.mSymbol;
	}
	
	public Expression getBody(){
		return this.mBody;
	}
}
