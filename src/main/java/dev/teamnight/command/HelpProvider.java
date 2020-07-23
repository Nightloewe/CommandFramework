package dev.teamnight.command;

import net.dv8tion.jda.api.entities.Message;

/**
 * An interface to provide the Command Framework with a help message for failed commands
 * @author Nightloewe
 *
 */
@FunctionalInterface
public interface HelpProvider {

	/**
	 * Provides a help message which will be called once a command fails execution.
	 * @param Command Context created by the Framework
	 * @warning Command Context may not have implemented the mehtods getCommand, getArguments
	 * @return the Help Message
	 */
	public Message provideHelp(IContext ctx);
	
}
