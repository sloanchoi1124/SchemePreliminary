package parser.token;

public enum TokenKind {
	NULL("'()"),
    LPAREN("("),
    RPAREN(")"),
    ID("[ID]"),
    INT("[NUM]"),
    STR("[STR]"),
    TRUE("#t"),
    FALSE("#f"),
    IF("if"),
    AND("and"),
    OR("or"),
    LAMBDA("lambda"),
    LET("let"),
    LETREC("letrec"),
    LETSTAR("let*"),
    DEFINE("define"),
	COND("cond");
    
    private String mRep;
    
    private TokenKind(String rep) {
        mRep = rep;
    }
    
    public String toString() {
        return mRep;
    }
}
