package dev.teamnight.command;

/**
 * An interface to provide the Command Framework with a prefix for specified contexts.
 * @author Nightloewe
 *
 */
public interface PrefixProvider {

	/**
	 * Provides a prefix, will be called the Framework when checking if a message is a command.
	 * @param Command Context created by the Framework
	 * @warning Command Context may not have implemented the mehtods getCommand, getArguments
	 * @return the Prefix
	 */
	public String providePrefix(IContext ctx);
	
}
