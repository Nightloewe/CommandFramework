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

/**
 * An interface to provide the Command Framework with a prefix for specified contexts.
 * @author Nightloewe
 *
 */
@FunctionalInterface
public interface PrefixProvider {

	/**
	 * Provides a prefix, will be called the Framework when checking if a message is a command.
	 * @param Command Context created by the Framework
	 * @warning Command Context may not have implemented the mehtods getCommand, getArguments
	 * @return the Prefix
	 */
	public String providePrefix(IContext ctx);
	
}
