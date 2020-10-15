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

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.internal.utils.tuple.Pair;

/**
 * A command that can be executed by the Command Framework
 * once a user calls it.
 * @author Jonas
 */
public interface ICommand {

	/**
     * @return name of command
     */
	public String getName();
	
	/**
     * @return usage example
     */
	public String[] getUsage();
	
	/**
     * @return briefly command description
     */
	public String getDescription();
	
	/**
	 * @return String array with command aliases
	 */
	public String[] getAliases();
	
	/**
	 * Returns a pair of two arrays containing discord
	 * permissions that are required by the bot itself
	 * in order to operate this command.
	 * 
	 * The first array contains permissions that are
	 * required server-wide.
	 * The second array contains permissions that are
	 * required in the channel the bot is receiving
	 * the command.
	 * 
	 * @return {@link net.dv8tion.jda.internal.utils.tuple.Pair<Permission[], Permission[]>}
	 */
	public Pair<Permission[], Permission[]> getBotSelfPermissions();
	
	/**
	 * Returns a pair of two arrays containing discord
	 * permissions that are required by the user in
	 * order to execute this command.
	 * 
	 * The first array contains permissions that are
	 * required server-wide.
	 * The second array contains permissions that are
	 * required in the channel the bot is receiving
	 * the command.
	 * 
	 * @return {@link net.dv8tion.jda.internal.utils.tuple.Pair<Permission[], Permission[]>}
	 */
	public Pair<Permission[], Permission[]> getUserPermissions();
	
	/**
	 * Determines whether a specific permission has
	 * to be set by an server administrator in
	 * order to execute this command.
	 * 
	 * Commands with this flag can only be executed
	 * by users that have the ADMINISTRATOR
	 * permission.
	 * 
	 * @return true if a command permission is required
	 */
	public boolean isRequireCommandPermission();
	
	/**
	 * Determines whether this command can only be
	 * run by the bot owner.
	 * 
	 * @return true if only the owner is allowed to execute
	 */
	public boolean isRequireOwner();
	
	/**
	 * Executes the command with a given Command
	 * Context.
	 * 
	 * @param Context ctx
	 * @return true if success
	 */
	public boolean execute(IContext ctx);
	
}
