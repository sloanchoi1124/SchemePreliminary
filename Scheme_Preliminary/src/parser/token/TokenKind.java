package parser.token;

public enum TokenKind {
    LPAREN("("),
    RPAREN(")"),
    ID("[ID]"),
    INT("[NUM]"),
    TRUE("#t"),
    FALSE("#f"),
    IF("if"),
    AND("and"),
    OR("or"),
    LAMBDA("lambda"),
    LET("let"),
    LETREC("letrec"),
    LETSTAR("let*"),
    DEFINE("define");
    
    private String mRep;
    
    private TokenKind(String rep) {
        mRep = rep;
    }
    
    public String toString() {
        return mRep;
    }
}
