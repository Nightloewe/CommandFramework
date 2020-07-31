package dev.teamnight.command.standard;

import dev.teamnight.command.CommandFramework;
import dev.teamnight.command.IModule;

public abstract class Module implements IModule {
	
	public Module(CommandFramework cf) {
		cf.registerCommands(this);
	}
	
	public abstract String getName();

}
