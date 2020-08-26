/**
 *    Copyright 2019-2020 Jonas Müller, Jannik Müller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.teamnight.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Jonas Müller
 */
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
