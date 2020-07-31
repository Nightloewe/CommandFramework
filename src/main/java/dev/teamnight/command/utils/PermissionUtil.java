package dev.teamnight.command.utils;

import java.util.LinkedList;
import java.util.List;

import dev.teamnight.command.AnnotatedCommand;
import dev.teamnight.command.ICommand;
import dev.teamnight.command.IContext;
import dev.teamnight.command.IPermission;
import dev.teamnight.command.Tribool;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class PermissionUtil {

	private static final String[] permissionLevels = {
			"MODULE_GUILD", 
			"COMMAND_GUILD", 
			"MODULE_TEXTCHANNEL", 
			"COMMAND_TEXTCHANNEL", 
			"MODULE_ROLE", 
			"COMMAND_ROLE", 
			"MODULE_TEXTCHANNEL_ROLE", 
			"COMMAND_TEXTCHANNEL_ROLE", 
			"COMMAND_USER"};
	
	/**
	 * Checks whether a permission is equal in all variables except the action
	 * @param {@link IPermission} the Permission
	 * @param {@link IPermission} the other Permission to compare to
	 * @return true if the permissions are equal and false if not
	 */
	public static boolean isEqual(IPermission permission, IPermission other) {
		boolean equal = false;
		
		equal = permission.getId() == other.getId() &&
				permission.getGuildId() == other.getGuildId() &&
				permission.getChannelId() == other.getChannelId() && 
				permission.getRoleId() == other.getRoleId() && 
				permission.getUserId() == other.getUserId();
		
		if(!equal) {
			return false;
		}
		
		if(permission.getModuleName().isPresent()) {
			if(other.getModuleName().isPresent()) {
				if(permission.getModuleName().get().equals(other.getModuleName().get())) {
					equal = true;
				}
			} else {
				return false;
			}
		} else {
			if(other.getModuleName().isPresent()) {
				return false;
			} else {
				equal = true;
			}
		}
		
		if(permission.getCommandName().isPresent()) {
			if(other.getCommandName().isPresent()) {
				if(permission.getCommandName().get().equals(other.getCommandName().get())) {
					equal = true;
				}
			} else {
				return false;
			}
		} else {
			if(other.getCommandName().isPresent()) {
				return false;
			} else {
				equal = true;
			}
		}
		
		return equal;
	}
	
	private static String getLevel(IPermission permission) {
		StringBuilder levelBuilder = new StringBuilder();
		boolean modulePresent = permission.getModuleName().isPresent();
		boolean commandPresent = permission.getCommandName().isPresent();
		
		if(modulePresent) {
			levelBuilder.append("MODULE_");
		} else if(commandPresent) {
			levelBuilder.append("COMMAND_");
		} else {
			throw new IllegalArgumentException("The permission has no module or command present");
		}
		
		if(permission.getGuildId() != 0L) {
			boolean rolePresent = permission.getRoleId() != 0L;
			boolean userPresent = permission.getUserId() != 0L;
			
			if(permission.getChannelId() != 0L) {
				if(rolePresent) {
					levelBuilder.append("TEXTCHANNEL_ROLE");
				} else {
					levelBuilder.append("TEXTCHANNEL");
				}
			} else {
				if(rolePresent) {
					levelBuilder.append("ROLE");
				} else if(userPresent) {
					if(modulePresent) {
						throw new IllegalArgumentException("A module permission is not allowed for users");
					}
					levelBuilder.append("USER");
				} else {
					levelBuilder.append("GUILD");
				}
			}
		} else {
			throw new IllegalArgumentException("Guild Id of a IPermission can not be null");
		}
		
		return levelBuilder.toString();
	}
	
	@SuppressWarnings("unused")
	private static boolean isSameLevel(IPermission permission, IPermission other) {
		int firstPos = 0;
		int secondPos = -1;
		
		String firstLevel = getLevel(permission);
		String secondLevel = getLevel(other);
		
		for(int i = 0; i < permissionLevels.length; i++) {
			String permissionLevel = permissionLevels[i];
			
			if(firstLevel.equalsIgnoreCase(permissionLevel)) firstPos = i;
			if(secondLevel.equalsIgnoreCase(permissionLevel)) secondPos = i;
		}
		
		return firstPos == secondPos;
	}
	
	/**
	 * 1: Check guild module perm (overwritable)
	 * 2: Check guild command perm (overwritable)
	 * 3: Check guild module textchannel perm (overwritable)
	 * 4: Check guild command textchannel perm (overwritable)
	 * 5: Check guild module role perm (overwritable)
	 * 6: Check guild command role perm (overwritable)
	 * 7: Check guild module role textchannel perm (overwritable)
	 * 8: Check guild command role textchannel perm (overwritable)
	 * 9: Check guild user perm (not-overwritable) (IGNORING - BREAKS LOOP)
	 */
	public static Tribool canExecute(IContext ctx) {
		Tribool result = Tribool.NEUTRAL;
		String module = null;
		ICommand cmd = ctx.getCmdFramework().getCommandMap().getCommand(ctx.getCommand());
		
		if(cmd instanceof AnnotatedCommand) {
			AnnotatedCommand annotatedCommand = (AnnotatedCommand) cmd;
			
			module = annotatedCommand.getModule();
		}
		
		List<IPermission> globalPermissions = ctx.getCmdFramework().getPermProvider().getGlobalPermissions();
		
		for(IPermission permission : globalPermissions) {
			if(permission.getCommandName().isPresent()) {
				if(permission.getCommandName().get().equalsIgnoreCase(cmd.getName())) {
					result = permission.getAction();
				}
			} else if(permission.getModuleName().isPresent()) {
				if(permission.getModuleName().get().equals(module)) {
					result = permission.getAction();
				}
			}
		}
		
		if(ctx.getGuild().isPresent()) {
			if(ctx.getGuildMember().get().isOwner())
				return Tribool.TRUE;
			
			List<IPermission> guildPermissions = ctx.getCmdFramework().getPermProvider().getGuildPermissions(ctx.getGuild().get());
			
			for(IPermission permission : guildPermissions) {
				if(permission.getCommandName().isPresent()) {
					if(!permission.getCommandName().get().equalsIgnoreCase(cmd.getName())) {
						continue;
					}
				} else {
					if(!permission.getModuleName().isPresent()) {
						throw new IllegalArgumentException("The permission has no module or command present");
					}
					
					if(!permission.getModuleName().get().equals(module)) {
						continue;
					}
				}
				
				if(permission.getChannelId() != 0L) {
					if(permission.getChannelId() != ctx.getChannel().getIdLong()) {
						continue;
					}
				}
				
				if(permission.getRoleId() != 0L) {
					if(!hasRole(permission.getRoleId(), ctx.getGuildMember().get())) {
						continue;
					}
				}
				
				if(permission.getUserId() != 0L) {
					if(permission.getUserId() != ctx.getAuthor().getIdLong()) {
						continue;
					}
				}
				
				result = permission.getAction();
			}
		}
		return result;
	}
	
	public static boolean isBlacklisted(IContext ctx) {
		return isBlacklisted(ctx, false);
	}
	
	public static boolean isBlacklisted(IContext ctx, boolean guild) {
		if(guild) {
			if(ctx.getCmdFramework().getBlockedGuilds().contains(ctx.getGuild().get().getIdLong()))
				return true;
		} else {
			if(ctx.getCmdFramework().getBlockedUsers().contains(ctx.getAuthor().getIdLong()))
				return true;
		}
		return false;
	}
	
	public static boolean containsPermission(LinkedList<IPermission> permissions, IPermission permission) {
		for(IPermission permArr : permissions) {
			if(PermissionUtil.isEqual(permission, permArr)) {
				if(permission.getAction() == permArr.getAction()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean hasRole(long roleId, Member member) {
		for(Role role : member.getRoles()) {
			if(role.getIdLong() == roleId) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isBotOwner(IContext ctx, User user) {
		return ctx.getCmdFramework().isOwner(user);
	}
}
