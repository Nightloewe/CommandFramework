package dev.teamnight.command;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dev.teamnight.command.BotPermission.PermissionValue;
import dev.teamnight.command.annotations.RequireBotSelfPermission;
import dev.teamnight.command.annotations.RequireCommandPermission;
import dev.teamnight.command.annotations.RequireGuild;
import dev.teamnight.command.annotations.RequireOwner;
import dev.teamnight.command.annotations.RequireUserPermission;
import dev.teamnight.command.utils.BotEmbedBuilder;
import dev.teamnight.command.utils.PermissionUtil;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

public class AnnotatedCommand implements ICommand {

	private String name;
	private String[] usage;
	private String description;
	private String module;
	
	private Method method;
	
	private Object invokeObject;
	private String[] aliases;
	
	public AnnotatedCommand(String name, String[] usage, String description, Method method, Object invokeObject) {
		this.name = name;
		this.usage = usage;
		this.description = description;
		this.method = method;
		this.invokeObject = invokeObject;
		
		if(invokeObject instanceof IModule) {
			IModule module = (IModule) invokeObject;
			this.module = module.getName();
		}
	}
	
	public AnnotatedCommand(String name, String[] usage, String description, String[] aliases, Method method, Object invokeObject) {
		this(name, usage, description, method, invokeObject);
		this.aliases = aliases;
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
		BotPermission.PermissionValue botPermission = PermissionUtil.canExecute(ctx);
		
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
		
		if(method.getAnnotation(RequireUserPermission.class) != null && botPermission == PermissionValue.UNSET) {
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
		
		if(method.getAnnotation(RequireGuild.class) != null) {
			if(!ctx.getGuild().isPresent()) {
				ctx.getChannel().sendMessage(
						new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_GUILD", ctx.getAuthor().getAsMention())).setColor(Color.RED).build()
						).queue();
				return true;
			}
		}
		
		//TODO: needs to be replaced by LocalizedString
		if(botPermission == PermissionValue.DENY && method.getAnnotation(RequireOwner.class) == null) {
			new BotEmbedBuilder().setDescription(ctx.getAuthor().getAsMention() + " You can't execute this command because of presets by command permissions. `" + botPermission.getReason() + "`").withErrorColor().sendMessage(ctx.getChannel());
			return true;
		}
		
		//TODO: needs to be replaced by LocalizedString
		if(method.getAnnotation(RequireCommandPermission.class) != null && botPermission != PermissionValue.ALLOW) {
			new BotEmbedBuilder().setDescription(ctx.getAuthor().getAsMention() + " You can't execute this command because you need a permission set to use this command.").withErrorColor().sendMessage(ctx.getChannel());
			return true;
		}
		
		try {
			boolean returnValue = (boolean) this.method.invoke(this.invokeObject, ctx);
			
			return returnValue;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
