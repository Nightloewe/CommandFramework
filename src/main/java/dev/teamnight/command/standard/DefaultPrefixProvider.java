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
package dev.teamnight.command.standard;

import dev.teamnight.command.IContext;
import dev.teamnight.command.PrefixProvider;

/**
 * Default implementation of PrefixProvider
 * providing a fixed prefix.
 * 
 * @author Jonas Müller
 */
public class DefaultPrefixProvider implements PrefixProvider {
	
	private String prefix = ".";
	
	public DefaultPrefixProvider(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String providePrefix(IContext ctx) {
		return this.prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
