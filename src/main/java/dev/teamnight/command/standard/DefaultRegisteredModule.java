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

import java.util.List;

import dev.teamnight.command.ICommand;
import dev.teamnight.command.IModule;
import dev.teamnight.command.IRegisteredModule;
import dev.teamnight.command.annotations.Requires;

/**
 * Default implementation of IRegisteredModule.
 * 
 * @author Jonas Müller
 */
public class DefaultRegisteredModule implements IRegisteredModule {

	private IModule module;
	private List<ICommand> commands;
	private List<Requires> conditions;
	private boolean hidden;
	
	public DefaultRegisteredModule(IModule base, List<ICommand> commands, List<Requires> conditions) {
		this(base, commands, conditions, false);
	}
	
	public DefaultRegisteredModule(IModule base, List<ICommand> commands, List<Requires> conditions, boolean isHidden) {
		this.module = base;
		this.commands = commands;
		this.conditions = conditions;
		this.hidden = isHidden;
	}
	
	@Override
	public String getName() {
		return module.getName();
	}

	@Override
	public List<Requires> getConditions() {
		return this.conditions;
	}

	@Override
	public List<ICommand> getCommands() {
		return this.commands;
	}
	
	@Override
	public boolean isHidden() {
		return this.hidden;
	}

}
