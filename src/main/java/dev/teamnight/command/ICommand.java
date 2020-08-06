package dev.teamnight.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.internal.utils.tuple.Pair;

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
	
	public Pair<Permission[], Permission[]> getBotSelfPermissions();
	
	public Pair<Permission[], Permission[]> getUserPermissions();
	
	public boolean isRequireCommandPermission();
	
	public boolean isRequireOwner();
	
	/**
	 * @param ctx
	 * @return if success
	 */
	public boolean execute(IContext ctx);
	
}
