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

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;

/**
 * An interface to provide the Command Framework with permissions in the global scope of the Bot,
 * and guild-specific permissions, the implementation can be chosen by the Bot creator, the
 * interface only needs the two methods returning Lists
 * @author Jonas
 *
 */
public interface PermissionProvider {

	/**
	 * The global-scope permissions of the Bot
	 * @return List<BotPermission> the Permission List
	 */
	public List<IPermission> getGlobalPermissions();
	
	/**
	 * The guild-specific permissions of the Bot
	 * @param Guild the Guild
	 * @return List<BotPermission> the Permission List
	 */
	public List<IPermission> getGuildPermissions(Guild guild);
	
	/**
	 * Returns Tribool.TRUE if the user is allowed to
	 * execute the command, Tribool.NEUTRAL if there is
	 * no permission and the default behaviour is required,
	 * and Tribool.FALSE if the command execution is denied.
	 * 
	 * @param {@link dev.teamnight.command.IContext} the Command Context
	 * @return {@link dev.teamnight.command.Tribool} the Tribool
	 */
	public Tribool canExecute(IContext ctx);
	
}
