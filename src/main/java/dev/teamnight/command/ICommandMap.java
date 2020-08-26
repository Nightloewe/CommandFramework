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

import java.util.HashMap;
import java.util.List;

/**
 * An interface to hold an registry of all
 * commands that can be executed by a user.
 * @author Jonas
 */
public interface ICommandMap {

	/**
     * Registers all commands in given list
     * @param List<Command>
     */
	public void registerAll(List<ICommand> commands);
	
	/**
     * Registers a command
     * @param Command
     */
	public void register(ICommand command);
	
	/**
     * Returns command with given name
     * @param String
     */
	public ICommand getCommand(String name);
	
	/**
	 * @return Command HashMap
	 */
	public HashMap<String, ICommand> getCommands();
	
	/**
     * Executes a registered command
     * @param String
     */
	public boolean dispatchCommand(IContext ctx);
}
