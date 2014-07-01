package parser.token;

public enum TokenKind {
    LPAREN("("),
    RPAREN(")"),
    ID("[ID]"),
    INT("[NUM]"),
    IF("if"),
    LAMBDA("lambda"),
    LET("let");
    
    private String mRep;
    
    private TokenKind(String rep) {
        mRep = rep;
    }
    
    public String toString() {
        return mRep;
    }
}
