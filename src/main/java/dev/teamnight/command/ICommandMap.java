package dev.teamnight.command;

import java.util.HashMap;
import java.util.List;

public interface ICommandMap {

	/**
     * Registers all commands in given list
     * @param List<Command>
     */
	public void registerAll(List<ICommand> commands);
	
	/**
     * Registers a command
     * @param Command
     */
	public void register(ICommand command);
	
	/**
     * Returns command with given name
     * @param String
     */
	public ICommand getCommand(String name);
	
	/**
	 * @return Command HashMap
	 */
	public HashMap<String, ICommand> getCommands();
	
	/**
     * Executes a registered command
     * @param String
     */
	public boolean dispatchCommand(CommandInfo info);
}
