package dev.teamnight.command;

public interface ICommand {

	/**
     * @return name of command
     */
	public String getName();
	
	/**
     * @return usage example
     */
	public String[] getUsage();
	
	/**
     * @return briefly command description
     */
	public String getDescription();
	
	/**
	 * @return String array with command aliases
	 */
	public String[] getAliases();
	
	/**
	 * @param ctx
	 * @return if success
	 */
	public boolean execute(IContext ctx);
	
}
