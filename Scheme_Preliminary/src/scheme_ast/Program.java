package scheme_ast;

import java.util.List;

public class Program {
	
	private List<DefOrExp> program;
	
	public Program(List<DefOrExp> l){
		this.program = l;
	}
	
	public List<DefOrExp> getProgram(){
		return this.program;
	}
}
