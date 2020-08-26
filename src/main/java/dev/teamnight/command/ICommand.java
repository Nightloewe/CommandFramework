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
	
	public Pair<Permission[], Permission[]> getBotSelfPermissions();
	
	public Pair<Permission[], Permission[]> getUserPermissions();
	
	public boolean isRequireCommandPermission();
	
	public boolean isRequireOwner();
	
	/**
	 * @param ctx
	 * @return if success
	 */
	public boolean execute(IContext ctx);
	
}
