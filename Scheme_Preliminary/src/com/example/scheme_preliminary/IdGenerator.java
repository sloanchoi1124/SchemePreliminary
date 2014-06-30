package com.example.scheme_preliminary;

public class IdGenerator {
	public int initial;
	
	public IdGenerator()
	{
		this.initial=0;
	}
	
	public int newId()
	{
		this.initial++;
		return this.initial;
	}
}
