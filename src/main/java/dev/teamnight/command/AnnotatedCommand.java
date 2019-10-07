package dev.teamnight.command;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dev.teamnight.nightbot.command.annotations.RequireCommandPermission;
import dev.teamnight.command.utils.PermissionUtil;
import dev.teamnight.nightbot.command.annotations.RequireBotSelfPermission;
import dev.teamnight.nightbot.command.annotations.RequireGuild;
import dev.teamnight.nightbot.command.annotations.RequireOwner;
import dev.teamnight.nightbot.command.annotations.RequireUserPermission;

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
	public boolean execute(CommandInfo commandInfo) {
		BotPermission.PermissionValue botPermission = PermissionUtil.canExecute(commandInfo);
		
		NightBot.logger().warn(botPermission.name() + ":" + botPermission.getReason());
		
		/**
		 * Requires Bot Owner
		 */
		if(method.getAnnotation(RequireOwner.class) != null) {
			if(!NightBot.get().getConfig().getOwnerIds().contains(commandInfo.getAuthor().getIdLong())) {
				commandInfo.getChannel().sendMessage(
						new EmbedBuilder().setDescription(commandInfo.getLang().NEED_OWNER.format(commandInfo.getAuthor().getAsMention())).setColor(Color.RED).build()
						).queue();
				return true;
			}
		}
		
		/**
		 * Requires permission for Bot member
		 */
		if(method.getAnnotation(RequireBotSelfPermission.class) != null) {
			RequireBotSelfPermission annotation = method.getAnnotation(RequireBotSelfPermission.class);
			
			if(commandInfo.getGuild().isPresent()) {
				for(Permission permission : annotation.guildPermission()) {
					if(!commandInfo.getGuild().get().getSelfMember().hasPermission(permission)) {
						commandInfo.getChannel().sendMessage(
								new EmbedBuilder().setDescription(commandInfo.getLang().BOT_NEED_SERVER_PERMISSION.format(commandInfo.getAuthor().getAsMention(), permission.getName())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
				
				for(Permission permission : annotation.channelPermission()) {
					TextChannel textChannel = (TextChannel) commandInfo.getChannel();
					if(!commandInfo.getGuild().get().getSelfMember().hasPermission(textChannel, permission)) {
						commandInfo.getChannel().sendMessage(
								new EmbedBuilder().setDescription(commandInfo.getLang().BOT_NEED_CHANNEL_PERMISSION.format(commandInfo.getAuthor().getAsMention(), permission.getName(), textChannel.getAsMention())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
			}
		}
		
		if(method.getAnnotation(RequireUserPermission.class) != null && botPermission == PermissionValue.UNSET) {
			RequireUserPermission annotation = method.getAnnotation(RequireUserPermission.class);
			
			if(commandInfo.getGuild().isPresent()) {
				for(Permission permission : annotation.guildPermission()) {
					if(!commandInfo.getGuildMember().get().hasPermission(permission)) {
						commandInfo.getChannel().sendMessage(
								new EmbedBuilder().setDescription(commandInfo.getLang().NEED_SERVER_PERMISSION.format(commandInfo.getAuthor().getAsMention(), permission.getName())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
				
				for(Permission permission : annotation.channelPermission()) {
					TextChannel textChannel = (TextChannel) commandInfo.getChannel();
					if(!commandInfo.getGuildMember().get().hasPermission(textChannel, permission)) {
						commandInfo.getChannel().sendMessage(
								new EmbedBuilder().setDescription(commandInfo.getLang().NEED_CHANNEL_PERMISSION.format(commandInfo.getAuthor().getAsMention(), permission.getName(), textChannel.getAsMention())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
			}
		}
		
		if(method.getAnnotation(RequireGuild.class) != null) {
			if(!commandInfo.getGuild().isPresent()) {
				commandInfo.getChannel().sendMessage(
						new EmbedBuilder().setDescription(commandInfo.getLang().REQUIRE_GUILD.format(commandInfo.getAuthor().getAsMention())).setColor(Color.RED).build()
						).queue();
				return true;
			}
		}
		
		if(botPermission == PermissionValue.DENY && method.getAnnotation(RequireOwner.class) == null) {
			new BotEmbedBuilder().setDescription(commandInfo.getAuthor().getAsMention() + " You can't execute this command because of presets by command permissions. `" + botPermission.getReason() + "`").withErrorColor().sendMessage(commandInfo.getChannel());
			return true;
		}
		
		if(method.getAnnotation(RequireCommandPermission.class) != null && botPermission != PermissionValue.ALLOW) {
			new BotEmbedBuilder().setDescription(commandInfo.getAuthor().getAsMention() + " You can't execute this command because you need a permission set to use this command.").withErrorColor().sendMessage(commandInfo.getChannel());
			return true;
		}
		
		try {
			boolean returnValue = (boolean) this.method.invoke(this.invokeObject, commandInfo);
			
			return returnValue;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
