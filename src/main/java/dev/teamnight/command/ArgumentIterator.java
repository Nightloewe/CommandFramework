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

import java.util.Iterator;

/**
 * @author Jonas Müller
 */
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
