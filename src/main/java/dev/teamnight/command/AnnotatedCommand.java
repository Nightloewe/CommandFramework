package dev.teamnight.command;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.teamnight.command.annotations.RequireBotSelfPermission;
import dev.teamnight.command.annotations.RequireCommandPermission;
import dev.teamnight.command.annotations.RequireGuild;
import dev.teamnight.command.annotations.RequireOwner;
import dev.teamnight.command.annotations.RequireUserPermission;
import dev.teamnight.command.annotations.Requires;
import dev.teamnight.command.utils.BotEmbedBuilder;
import dev.teamnight.command.utils.PermissionUtil;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class AnnotatedCommand implements ICommand {
	
	private static Logger LOGGER = LogManager.getLogger(AnnotatedCommand.class);

	protected final String name;
	protected final String[] usage;
	protected final String[] aliases;
	protected final  String description;
	protected final  String module;
	
	protected final Method method;
	protected final Object invokeObject;
	
	public AnnotatedCommand(String name, String[] usage, String description, Method method, Object invokeObject) {
		this(name, usage, description, null, method, invokeObject);
	}
	
	public AnnotatedCommand(String name, String[] usage, String description, String[] aliases, Method method, Object invokeObject) {
		this.name = name;
		this.usage = usage;
		this.description = description;
		this.method = method;
		this.invokeObject = invokeObject;
		this.aliases = aliases;
		
		if(invokeObject instanceof IModule) {
			IModule module = (IModule) invokeObject;
			this.module = module.getName();
		} else {
			this.module = null;
		}
		
		if(method.isAnnotationPresent(Requires.class)) {
			Requires condition = method.getAnnotation(Requires.class);
			
			if(!Condition.class.isAssignableFrom(condition.value())) {
				throw new IllegalArgumentException("Requires must contain an implementation of dev.teamnight.command.Condition");
			}
		}
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String[] getUsage() {
		return this.usage;
	}

	@Override
	public String getDescription() {
		return this.description;
	}
	
	@Override
	public String[] getAliases() {
		return this.aliases;
	}
	
	public String getModule() {
		return module;
	}

	@Override
	public boolean execute(IContext ctx) {
		Tribool permissionResult = PermissionUtil.canExecute(ctx);
		
		/**
		 * Requires Bot Owner
		 */
		if(method.getAnnotation(RequireOwner.class) != null) {
			if(!PermissionUtil.isBotOwner(ctx, ctx.getAuthor())) {
				ctx.getChannel().sendMessage(
						new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_OWNER", ctx.getAuthor().getAsMention())).setColor(Color.RED).build()
						).queue();
				return true;
			}
		}
		
		/**
		 * Requires permission for Bot member
		 */
		if(method.getAnnotation(RequireBotSelfPermission.class) != null) {
			RequireBotSelfPermission annotation = method.getAnnotation(RequireBotSelfPermission.class);
			
			if(ctx.getGuild().isPresent()) {
				for(Permission permission : annotation.guildPermission()) {
					if(!ctx.getGuild().get().getSelfMember().hasPermission(permission)) {
						ctx.getChannel().sendMessage(
								new BotEmbedBuilder().setDescription(ctx.getLocalizedString("BOT_NEED_SERVER_PERMISSION", ctx.getAuthor().getAsMention(), permission.getName())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
				
				for(Permission permission : annotation.channelPermission()) {
					TextChannel textChannel = (TextChannel) ctx.getChannel();
					if(!ctx.getGuild().get().getSelfMember().hasPermission(textChannel, permission)) {
						ctx.getChannel().sendMessage(
								new BotEmbedBuilder().setDescription(ctx.getLocalizedString("BOT_NEED_CHANNEL_PERMISSION", ctx.getAuthor().getAsMention(), permission.getName(), textChannel.getAsMention())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
			}
		}
		
		/**
		 * Requires that the user has a specific Discord Permission
		 */
		if(method.getAnnotation(RequireUserPermission.class) != null && permissionResult == Tribool.NEUTRAL) {
			RequireUserPermission annotation = method.getAnnotation(RequireUserPermission.class);
			
			if(ctx.getGuild().isPresent()) {
				for(Permission permission : annotation.guildPermission()) {
					if(!ctx.getGuildMember().get().hasPermission(permission)) {
						ctx.getChannel().sendMessage(
								new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_SERVER_PERMISSION", ctx.getAuthor().getAsMention(), permission.getName())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
				
				for(Permission permission : annotation.channelPermission()) {
					TextChannel textChannel = (TextChannel) ctx.getChannel();
					if(!ctx.getGuildMember().get().hasPermission(textChannel, permission)) {
						ctx.getChannel().sendMessage(
								new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_CHANNEL_PERMISSION", ctx.getAuthor().getAsMention(), permission.getName(), textChannel.getAsMention())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
			}
		}
		
		/**
		 * Requires that the command gets executed in a guild
		 */
		if(method.getAnnotation(RequireGuild.class) != null) {
			if(!ctx.getGuild().isPresent()) {
				ctx.getChannel().sendMessage(
						new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_GUILD", ctx.getAuthor().getAsMention())).setColor(Color.RED).build()
						).queue();
				return true;
			}
		}
		
		if(permissionResult == Tribool.FALSE && method.getAnnotation(RequireOwner.class) == null) {
			new BotEmbedBuilder().setDescription(ctx.getLocalizedString("PERMISSION_DENIED", ctx.getAuthor().getAsMention())).withErrorColor().sendMessage(ctx.getChannel());
			return true;
		}
		
		if(method.getAnnotation(RequireCommandPermission.class) != null && permissionResult != Tribool.TRUE) {
			new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_PRESET_PERMISSION", ctx.getAuthor().getAsMention())).withErrorColor().sendMessage(ctx.getChannel());
			return true;
		}
		
		if(method.isAnnotationPresent(Requires.class)) {
			Requires requires = method.getAnnotation(Requires.class);
			
			try {
				Condition cond = (Condition) requires.value().getConstructor(IContext.class).newInstance(ctx);
				
				boolean canExecute = cond.canExecute();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				new BotEmbedBuilder().setDescription("An error occured while checking the conditions for the command execution.").withErrorColor().sendMessage(ctx.getChannel());
				e.printStackTrace();
			}
		}
		
		try {
			boolean returnValue = (boolean) this.method.invoke(this.invokeObject, ctx);
			
			return returnValue;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			new BotEmbedBuilder()
				.setDescription("An error occured while executing this command.")
				.withErrorColor()
				.sendMessage(ctx.getChannel());
			LOGGER.error("An error occured while executing the command " + this.getName() + ": " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
}
