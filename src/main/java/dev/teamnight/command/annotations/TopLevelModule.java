package dev.teamnight.command.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation provided to mark a class as a command module.
 * The annotation requires a name to bet set for the
 * module.
 * 
 * @author Jonas
 */
@Retention(RUNTIME)
@Target({ TYPE })
public @interface TopLevelModule {
	
	String name();
	
}
