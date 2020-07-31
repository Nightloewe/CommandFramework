package dev.teamnight.command;

import java.util.Iterator;

public class ArgumentIterator implements Iterator<String> {

	private int position = 0;
	
	private String[] args;
	
	public ArgumentIterator(String[] args) {
		this.args = args;
	}

	@Override
	public boolean hasNext() {
		return args.length > position;
	}

	@Override
	public String next() {
		String arg = this.args[position];
		
		++position;
		
		return arg;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
}
