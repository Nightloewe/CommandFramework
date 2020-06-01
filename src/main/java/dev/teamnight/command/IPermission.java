package dev.teamnight.command;

import java.util.Optional;

public interface IPermission {

	/**
	 * The id of the permission for backend-purposes
	 * @return
	 */
	public long getId();
	
	/**
	 * The ID of the guild this permission was created for
	 * @return Long the guild ID or {@code 0} if no guild is present
	 */
	public long getGuildId();
	
	/**
	 * The ID of the channel this permission sets the command or module for
	 * @return Long the channel ID or {@code 0} if no channel is present
	 */
	public long getChannelId();
	
	/**
	 * The ID of the role this permissions is set for
	 * @return Long the role ID or {@code 0} if it is not set for a role
	 */
	public long getRoleId();
	
	/**
	 * The ID of the user this permissions is set for
	 * @return Long the user ID or {@code 0} if it is not set for a user
	 */
	public long getUserId();
	
	/**
	 * The module name.
	 * @return {@link Optional} containing the module name or nothing
	 */
	public Optional<String> getModuleName();
	
	/**
	 * The corresponding module
	 * @return {@link Optional} containing the module or nothing
	 */
	public Optional<IModule> getModule();
	
	/**
	 * The command name
	 * @return {@link Optional} containing the command name or nothing
	 */
	public Optional<String> getCommandName();
	
	/**
	 * The command
	 * @return {@link} containing an {@link ICommand} or nothing
	 */
	public Optional<ICommand> getCommand();
	
	/**
	 * Either true or false. True allows a command or module to be used, false denies it
	 * @return boolean true or false
	 */
	public Tribool getAction();
}
