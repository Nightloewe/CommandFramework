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

import java.util.Collections;
import java.util.List;

import dev.teamnight.command.IContext;
import dev.teamnight.command.IPermission;
import dev.teamnight.command.PermissionProvider;
import dev.teamnight.command.Tribool;
import net.dv8tion.jda.api.entities.Guild;

/**
 * Default implementation of PermissionProvider
 * providing empty lists and returning Tribool.NEUTRAL
 * when a command checks execution permission.
 * 
 * @author Jonas Müller
 *
 */
public class DefaultPermissionProvider implements PermissionProvider {

	@Override
	public List<IPermission> getGlobalPermissions() {
		return Collections.emptyList();
	}
	@Override
	public List<IPermission> getGuildPermissions(Guild guild) {
		return Collections.emptyList();
	}

	@Override
	public Tribool canExecute(IContext ctx) {
		return Tribool.NEUTRAL;
	}
	
}
