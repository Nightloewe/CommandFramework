package dev.teamnight.command.standard;

import java.lang.reflect.Method;

import dev.teamnight.command.AnnotatedCommand;

public class DefaultAnnotatedCommand extends AnnotatedCommand {

	public DefaultAnnotatedCommand(String name, String[] usage, String description, Method method,
			Object invokeObject) {
		super(name, usage, description, method, invokeObject);
	}
	
	public DefaultAnnotatedCommand(String name, String[] usage, String description, String[] aliases, Method method, Object invokeObject) {
		super(name, usage, description, aliases, method, invokeObject);
	}

}
