package scheme_ast;

import java.util.List;


// examples:
//
// addition:     OperatorExpression("+", true, 2)
// not:          OperatorExpression("not", false, 1)
// less than:    OperatorExpression("<", false, 2)


public class OperatorExpression extends Expression {
	private String mName;
	private boolean mAcceptsLists;
	private int mArity;
	
	public OperatorExpression(String name, boolean acceptsLists, int arity) {
		super();
		mName = name;
		mAcceptsLists = acceptsLists;
		mArity = arity;
	}
	
	public String getName() {
		return mName;
	}
	
	public boolean acceptsLists() {
		return mAcceptsLists;
	}
	
	public int getArity() {
		return mArity;
	}
}
