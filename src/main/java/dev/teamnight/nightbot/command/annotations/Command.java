package dev.teamnight.nightbot.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

	public String name();
	
	public String[] usage() default {};
	
	public String description();
	
	public String[] aliases() default {};
	
	/**
	 * @deprecated This part of the interface will be removed in future, as the SubModule class is the new successor to the Category. \n
	 * or create a new class for the SubModule instead. Please register your submodules in your Module, the interface will provide a default method for this.
	 * @return Category of the Command
	 */
	public String category() default "";
	
}
