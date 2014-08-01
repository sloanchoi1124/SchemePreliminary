package com.example.scheme_preliminary.boxFragment;

import java.util.List;

import scheme_ast.DefOrExp;

public interface ClipboardCommunicator{
	public List<DefOrExp> passBufferToClipboard();
	public void chooseFromBuffer(DefOrExp deforexp);
}
