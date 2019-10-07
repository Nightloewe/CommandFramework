package dev.teamnight.command;

import org.apache.logging.log4j.Logger;

public class CommandFramework {

	private Logger logger;
	
	private ICommandMap map;
	
	public static FrameworkBuilder newBuilder() {
		return FrameworkBuilder.newBuilder();
	}
	
	public Logger logger() {
		return this.logger;
	}
	
}
