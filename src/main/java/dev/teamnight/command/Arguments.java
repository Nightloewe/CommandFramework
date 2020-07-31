package dev.teamnight.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Arguments implements Iterable<String> {

	private final String[] args;
	private final Map<String, String> namedArgs;
	
	private ArgumentIterator it;
	
	public Arguments(String[] args, Map<String, String> namedArgs) {
		this.args = args;
		this.namedArgs = namedArgs;
		this.it = (ArgumentIterator) iterator();
	}
	
	public String get(int index) {
		if(index > args.length) {
			return null;
		}
		
		return args[index];
	}
	
	public String getNamed(String key) {
		return this.namedArgs.get(key);
	}
	
	public String next() {
		return it.next();
	}
	
	public boolean hasNext() {
		return it.hasNext();
	}
	
	public int nextCount() {
		return this.args.length - it.getPosition();
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
