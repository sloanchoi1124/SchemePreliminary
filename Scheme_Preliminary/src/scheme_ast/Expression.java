package scheme_ast;

import util.Uid;

public abstract class Expression {

	private final Uid mUid;
	
	public Expression() {
		mUid = new Uid();
	}
	
	public Uid getUid() {
		return mUid;
	}
	
}
