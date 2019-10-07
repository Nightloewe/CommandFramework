package dev.teamnight.nightbot.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.dv8tion.jda.api.Permission;

/**
 * Provides annotation to require a discord specific permission to do something as bot
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireBotSelfPermission {

	public Permission[] guildPermission() default {};
	
	public Permission[] channelPermission() default {};
	
}
