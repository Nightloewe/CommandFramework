package dev.teamnight.command;

public abstract class Module implements IModule {
	
	public Module() {
		//TODO: Automatically register commands
	}
	
	public abstract String getName();

}
