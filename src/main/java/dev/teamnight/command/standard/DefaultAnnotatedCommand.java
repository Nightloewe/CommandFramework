package dev.teamnight.command.standard;

import java.lang.reflect.Method;
import java.util.List;

import dev.teamnight.command.AnnotatedCommand;
import dev.teamnight.command.annotations.Requires;

/**
 * The default implementation of AnnotatedCommand.
 * 
 * @author Jonas Müller
 */
public class DefaultAnnotatedCommand extends AnnotatedCommand {

	public DefaultAnnotatedCommand(String name, String[] usage, String description, Method method,
			Object invokeObject) {
		super(name, usage, description, method, invokeObject);
	}
	
	public DefaultAnnotatedCommand(String name, String[] usage, String description, String[] aliases, List<Requires> requires, Method method, Object invokeObject) {
		super(name, usage, description, aliases, requires, method, invokeObject);
	}

}
