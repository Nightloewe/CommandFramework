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

import java.util.Optional;

/**
 * A permission for commands or general-purpose.
 * 
 * @author Jonas
 */
public interface IPermission {

	/**
	 * The id of the permission for backend-purposes
	 * @return
	 */
	public long getId();
	
	/**
	 * The ID of the guild this permission was created for
	 * @return Long the guild ID or {@code 0} if no guild is present
	 */
	public long getGuildId();
	
	/**
	 * The ID of the channel this permission sets the command or module for
	 * @return Long the channel ID or {@code 0} if no channel is present
	 */
	public long getChannelId();
	
	/**
	 * The ID of the role this permissions is set for
	 * @return Long the role ID or {@code 0} if it is not set for a role
	 */
	public long getRoleId();
	
	/**
	 * The ID of the user this permissions is set for
	 * @return Long the user ID or {@code 0} if it is not set for a user
	 */
	public long getUserId();
	
	/**
	 * The module name.
	 * @return {@link Optional} containing the module name or nothing
	 */
	public Optional<String> getModule();
	
	/**
	 * The command name
	 * @return {@link Optional} containing the command name or nothing
	 */
	public Optional<String> getCommand();
	
	/**
	 * Either true or false. True allows a command or module to be used, false denies it
	 * @return boolean true or false
	 */
	public Tribool getAction();
}
