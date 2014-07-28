package com.example.scheme_preliminary.boxFragment;

import scheme_ast.Program;

public interface ProgramLevelCommunicator {
	public Program getProgramFromActivity();
	public void updateViewingFragment();
}
