package dev.teamnight.command;

public abstract class Module implements IModule {
	
	public Module(CommandFramework cf) {
		cf.registerCommands(this);
	}
	
	public abstract String getName();

}
