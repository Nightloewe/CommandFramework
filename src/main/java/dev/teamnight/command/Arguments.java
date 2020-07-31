package dev.teamnight.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Arguments implements Iterable<String> {

	private final String[] args;
	private ArgumentIterator it;
	
	public Arguments(String[] args) {
		this.args = args;
		this.it = (ArgumentIterator) iterator();
	}
	
	public String get(int index) {
		return null;
	}
	
	public String next() {
		return it.next();
	}
	
	public boolean hasNext() {
		return it.hasNext();
	}
	
	public int nextCount() {
		return this.args.length - it.getPosition() - 1;
	}
	
	public String[] getRemaining() {
		String[] remaining = new String[this.nextCount()];
		
		int i = 0;
		while(it.hasNext()) {
			String s = it.next();
			remaining[i] = s;
			
			++i;
		}
		
		return remaining;
	}
	
	public String join(CharSequence delimiter) {
		return String.join(delimiter, this.args);
	}
	
	public String joinRemaining(CharSequence delimiter) {
		String[] remaining = this.getRemaining();
		
		return String.join(delimiter, remaining);
	}
	
	public List<String> toList() {
		return Arrays.asList(this.args);
	}
	
	public int size() {
		return this.args.length;
	}

	@Override
	public Iterator<String> iterator() {
		return new ArgumentIterator(this.args);
	}
	
}
