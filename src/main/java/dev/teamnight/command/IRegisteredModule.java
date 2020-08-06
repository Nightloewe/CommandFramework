package dev.teamnight.command;

import java.util.List;

import dev.teamnight.command.annotations.Requires;

public interface IRegisteredModule extends IModule {
	
	public List<Requires> getConditions();
	
	public List<ICommand> getCommands();
	
}
