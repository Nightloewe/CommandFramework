package dev.teamnight.command.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation to mark a class as the
 * submodule of another class marked
 * as a module using the {@link TopLevelModule}
 * annotation.
 * 
 * @author Jonas
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface SubModule {

	String name();
	
	Class<?> baseModule();
	
}
