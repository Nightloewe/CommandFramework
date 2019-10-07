package dev.teamnight.nightbot.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.dv8tion.jda.api.Permission;

/**
 * Provides annotation to require a specific discord permission to use this command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireUserPermission {

	public Permission[] guildPermission() default {};
	
	public Permission[] channelPermission() default {};
	
}
