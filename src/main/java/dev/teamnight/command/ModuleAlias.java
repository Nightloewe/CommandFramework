package dev.teamnight.command;

import java.util.ArrayList;

public class ModuleAlias implements IModule {

	private String name = "";
	private ArrayList<ICommand> commandAliases = new ArrayList<ICommand>();

	@Override
	public String getName() {
		return this.name;
	}
	
	public ModuleAlias(String name) {
		this.name = name;
	}
	
	public ArrayList<ICommand> getAliases() {
		return this.commandAliases;
	}
	
	public void addAlias(ICommand command) {
		this.commandAliases.add(command);
	}
	
	public void removeAlias(ICommand command) {
		this.commandAliases.remove(command);
	}

}
