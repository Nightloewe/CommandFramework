/**
 *    Copyright 2019-2020 Jonas Müller, Jannik Müller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.teamnight.command;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.teamnight.command.annotations.RequireBotSelfPermission;
import dev.teamnight.command.annotations.RequireCommandPermission;
import dev.teamnight.command.annotations.RequireGuild;
import dev.teamnight.command.annotations.RequireOwner;
import dev.teamnight.command.annotations.RequireUserPermission;
import dev.teamnight.command.annotations.Requires;
import dev.teamnight.command.annotations.SubModule;
import dev.teamnight.command.annotations.TopLevelModule;
import dev.teamnight.command.utils.BotEmbedBuilder;
import dev.teamnight.command.utils.PermissionUtil;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.utils.tuple.Pair;

/**
 * @author Jonas Müller
 */
public abstract class AnnotatedCommand implements ICommand {
	
	private static Logger LOGGER = LogManager.getLogger(AnnotatedCommand.class);

	protected final String name;
	protected final String[] usage;
	protected final String[] aliases;
	protected final String description;
	protected final String module;
	
	protected final Method method;
	protected final Object invokeObject;
	
	/**
	 * Conditions
	 */
	private List<Requires> conditions;
	private RequireBotSelfPermission botSelfPermission;
	private RequireUserPermission userPermission;
	private boolean requirePresetCommandPermission = false;
	private boolean requireOwner = false;
	private boolean requireGuild = false;

	/**
	 * Constructor for creating an AnnotatedCommand without any aliases
	 * @param String name the name of the Command
	 * @param String[] usage the usages of the Command
	 * @param String description the description locale string for this Command
	 * @param Method the method to be invoked
	 * @param Object invokeObject the object that the method is invoked on
	 */
	public AnnotatedCommand(String name, String[] usage, String description, Method method, Object invokeObject) {
		this(name, usage, description, null, null, null, method, invokeObject);
	}
	
	public AnnotatedCommand(String name, String[] usage, String description, String[] aliases, List<Requires> requires, String module, Method method, Object invokeObject) {
		this.name = name;
		this.usage = usage;
		this.description = description;
		this.method = method;
		this.invokeObject = invokeObject;
		this.aliases = aliases;
		this.conditions = requires != null ? requires : new ArrayList<Requires>();
		
		if(module == null) {
			Class<?> clazz = invokeObject.getClass();
			if(clazz.isAnnotationPresent(TopLevelModule.class)) {
				this.module = clazz.getAnnotation(TopLevelModule.class).name();
			} else if(clazz.isAnnotationPresent(SubModule.class)) {
				this.module = clazz.getAnnotation(SubModule.class).name();
			} else {
				throw new IllegalArgumentException("Module can not be null");
			}
		} else {
			this.module = module;
		}
		
		if(method.isAnnotationPresent(Requires.class)) {
			Requires condition = method.getAnnotation(Requires.class);
			
			if(!Condition.class.isAssignableFrom(condition.value())) {
				throw new IllegalArgumentException("Requires must contain an implementation of dev.teamnight.command.Condition");
			}
			
			this.conditions.add(condition);
		}
		
		if(method.isAnnotationPresent(RequireGuild.class)) {
			requireGuild = true;
		}
		
		if(method.isAnnotationPresent(RequireBotSelfPermission.class)) {
			botSelfPermission = method.getAnnotation(RequireBotSelfPermission.class);
		}
		
		if(method.isAnnotationPresent(RequireUserPermission.class)) {
			userPermission = method.getAnnotation(RequireUserPermission.class);
		}
		
		if(method.isAnnotationPresent(RequireCommandPermission.class)) {
			requirePresetCommandPermission = true;
		}
		
		if(method.isAnnotationPresent(RequireOwner.class)) {
			requireOwner = true;
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
	
	public List<Requires> getConditions() {
		return conditions;
	}
	
	public RequireBotSelfPermission getBotSelfPermission() {
		return botSelfPermission;
	}
	
	public Pair<Permission[], Permission[]> getBotSelfPermissions() {
		return botSelfPermission != null ? Pair.of(botSelfPermission.guildPermission(), botSelfPermission.channelPermission()) : null;
	}
	
	public RequireUserPermission getUserPermission() {
		return userPermission;
	}
	
	public Pair<Permission[], Permission[]> getUserPermissions() {
		return userPermission != null ? Pair.of(userPermission.guildPermission(), userPermission.channelPermission()) : null;
	}
	
	public boolean isRequireCommandPermission() {
		return requirePresetCommandPermission;
	}
	
	public boolean isRequireOwner() {
		return requireOwner;
	}

	@Override
	public boolean execute(IContext ctx) {
		Tribool permissionResult = ctx.getCmdFramework().getPermProvider().canExecute(ctx);
		
		/**
		 * Requires Bot Owner
		 */
		if(requireOwner) {
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
		if(botSelfPermission != null) {
			if(ctx.getGuild().isPresent()) {
				for(Permission permission : botSelfPermission.guildPermission()) {
					if(!ctx.getGuild().get().getSelfMember().hasPermission(permission)) {
						ctx.getChannel().sendMessage(
								new BotEmbedBuilder().setDescription(ctx.getLocalizedString("BOT_NEED_SERVER_PERMISSION", ctx.getAuthor().getAsMention(), permission.getName())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
				
				for(Permission permission : botSelfPermission.channelPermission()) {
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
		if(userPermission != null && permissionResult == Tribool.NEUTRAL) {
			if(ctx.getGuild().isPresent()) {
				for(Permission permission : userPermission.guildPermission()) {
					if(!ctx.getGuildMember().get().hasPermission(permission)) {
						ctx.getChannel().sendMessage(
								new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_SERVER_PERMISSION", ctx.getAuthor().getAsMention(), permission.getName())).setColor(Color.RED).build()
								).queue();
						return true;
					}
				}
				
				for(Permission permission : userPermission.channelPermission()) {
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
		if(requireGuild) {
			if(!ctx.getGuild().isPresent()) {
				ctx.getChannel().sendMessage(
						new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_GUILD", ctx.getAuthor().getAsMention())).setColor(Color.RED).build()
						).queue();
				return true;
			}
		}
		
		if(permissionResult == Tribool.FALSE && !requireOwner) {
			new BotEmbedBuilder().setDescription(ctx.getLocalizedString("PERMISSION_DENIED", ctx.getAuthor().getAsMention())).withErrorColor().sendMessage(ctx.getChannel());
			return true;
		}
		
		if(requirePresetCommandPermission && permissionResult != Tribool.TRUE) {
			new BotEmbedBuilder().setDescription(ctx.getLocalizedString("NEED_PRESET_PERMISSION", ctx.getAuthor().getAsMention())).withErrorColor().sendMessage(ctx.getChannel());
			return true;
		}
		
		if(conditions.size() > 0) {
			for(Requires requires : this.conditions) {
				try {
					Condition cond = (Condition) requires.value().getConstructor(IContext.class).newInstance(ctx);
					
					boolean canExecute = cond.canExecute();
					
					if(!canExecute) {
						new BotEmbedBuilder().setDescription(ctx.getLocalizedString(cond.errorMessage(), ctx.getAuthor().getAsMention())).withErrorColor().sendMessage(ctx.getChannel());
						return true;
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					new BotEmbedBuilder().setDescription("An error occured while checking the conditions for the command execution.").withErrorColor().sendMessage(ctx.getChannel());
					e.printStackTrace();
				}
			}
		}
		
		try {
			boolean returnValue = (boolean) this.method.invoke(this.invokeObject, ctx);
			
			return returnValue;
		} catch(InvocationTargetException e) {
			new BotEmbedBuilder()
				.setDescription("An error occured while executing this command.")
				.withErrorColor()
				.sendMessage(ctx.getChannel());
			LOGGER.error("An error occured while executing the command " + this.getName() + ": " + e.getTargetException().getLocalizedMessage());
			e.getTargetException().printStackTrace();
		} catch (IllegalAccessException | IllegalArgumentException e) {
			new BotEmbedBuilder()
				.setDescription("`" + e.getMessage() + "` -> Error occured while invoking command.")
				.withErrorColor()
				.sendMessage(ctx.getChannel());
			LOGGER.error("An error occured while executing the command " + this.getName() + ": " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
}
