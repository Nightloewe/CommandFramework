package dev.teamnight.command.standard;

import java.util.List;

import dev.teamnight.command.ICommand;
import dev.teamnight.command.IModule;
import dev.teamnight.command.IRegisteredModule;
import dev.teamnight.command.annotations.Requires;

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
