package dev.teamnight.command.utils;

import java.util.LinkedList;
import java.util.List;

import dev.teamnight.command.BotPermission;
import dev.teamnight.command.ICommand;
import dev.teamnight.command.IContext;
import dev.teamnight.command.standard.AnnotatedCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class PermissionUtil {

	private static String[] permissionLevels = {"MODULE_GLOBAL", "COMMAND_GLOBAL", "MODULE_GUILD", "COMMAND_GUILD", "MODULE_TEXTCHANNEL", "COMMAND_TEXTCHANNEL", "MODULE_ROLE", "COMMAND_ROLE", "MODULE_TEXTCHANNEL_ROLE", "COMMAND_TEXTCHANNEL_ROLE", "COMMAND_USER"};
	
	/**
	 * Checks if all values are equal but the Permission Action (allow/deny) is different
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean isEqual(BotPermission first, BotPermission second) {
		if(first.getId() == second.getId())
			return true;
		
		if(first.getGuildId() == second.getGuildId() && first.getChannelId() == second.getChannelId() && first.getRoleId() == first.getRoleId() && first.getUserId() == second.getUserId()) {
			if(first.getModule() != null && second.getModule() != null) {
				if(first.getModule().equalsIgnoreCase(second.getModule())) {
					return true;
				}
			} else if(first.getCommand() != null && second.getCommand() != null) {
				if(first.getCommand().equalsIgnoreCase(second.getCommand())) {
					return true;
				}
			} else {
				if((first.getAction().equalsIgnoreCase("whitelist") || first.getAction().equalsIgnoreCase("blacklist")) && (second.getAction().equalsIgnoreCase("whitelist") || second.getAction().equalsIgnoreCase("blacklist"))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if first role given by id is higher than the second role given by id in the Discord guild
	 * @param guild
	 * @param firstRoleId
	 * @param secondRoleId
	 * @return
	 */
	public static boolean isRoleHigher(Guild guild, long firstRoleId, long secondRoleId) {
		Role firstRole = guild.getRoleById(firstRoleId);
		Role secondRole = guild.getRoleById(secondRoleId);
		
		if(firstRole == null || secondRole == null) return false;
		
		return firstRole.canInteract(secondRole);
	}
	
	private static boolean isSameLevel(BotPermission.PermissionValue value, BotPermission.PermissionValue beforeValue) {
		int firstPos = 0;
		int secondPos = -1;
		
		String firstString = "";
		String secondString = "";
		
		if(value.getReason().contains("ALLOWED")) {
			firstString = value.getReason().substring(0, value.getReason().length() - 8);
		} else {
			firstString = value.getReason().substring(0, value.getReason().length() - 7);
		}
		
		if(beforeValue.getReason().contains("ALLOWED")) {
			secondString = value.getReason().substring(0, value.getReason().length() - 8);
		} else {
			secondString = value.getReason().substring(0, value.getReason().length() - 7);
		}
		
		for(int i = 0; i < permissionLevels.length; i++) {
			String permissionLevel = permissionLevels[i];
			
			if(firstString.equalsIgnoreCase(permissionLevel)) firstPos = i;
			if(secondString.equalsIgnoreCase(permissionLevel)) secondPos = i;
		}
		
		return firstPos == secondPos;
	}
	
	/**
	 * 1: Check global module perm (not-overwritable)
	 * 2: Check global command perm (not-overwritable)
	 * 3: Check guild module perm (overwritable)
	 * 4: Check guild command perm (overwritable)
	 * 5: Check guild module textchannel perm (overwritable)
	 * 6: Check guild command textchannel perm (overwritable)
	 * 7: Check guild module role perm (overwritable)
	 * 8: Check guild command role perm (overwritable)
	 * 9: Check guild module role textchannel perm (overwritable)
	 * 10: Check guild command role textchannel perm (overwritable)
	 * 11: Check guild user perm (not-overwritable) (IGNORING - BREAKS LOOP)
	 */
	public static BotPermission.PermissionValue canExecute(IContext ctx) {
		BotPermission.PermissionValue val = BotPermission.PermissionValue.UNSET;
		
		String module = null;
		ICommand cmd = ctx.getCmdFramework().getCommandMap().getCommand(ctx.getCommand());
		
		if(cmd instanceof AnnotatedCommand) {
			AnnotatedCommand annotatedCommand = (AnnotatedCommand) cmd;
			
			module = annotatedCommand.getModule();
		}
		
		List<BotPermission> globalPermissions = ctx.getCmdFramework().getPermProvider().getGlobalPermissions();
		
		for(BotPermission perm : globalPermissions) {
			if(perm.getModule() != null) {
				if(perm.getModule().equalsIgnoreCase(module)) {
					if(perm.getAction().equalsIgnoreCase("enable")) {
						val = BotPermission.PermissionValue.ALLOW.setReason("MODULE_GLOBAL_ALLOWED");
					} else {
						return BotPermission.PermissionValue.DENY.setReason("MODULE_GLOBAL_DENIED");
					}
				}
			}
			
			if(perm.getCommand() != null) {
				if(perm.getCommand().equalsIgnoreCase(ctx.getCommand())) {
					if(perm.getAction().equalsIgnoreCase("enable")) {
						val = BotPermission.PermissionValue.ALLOW.setReason("COMMAND_GLOBAL_ALLOWED");
					} else {
						return BotPermission.PermissionValue.DENY.setReason("COMMAND_GLOBAL_DENIED");
					}
				}
			}
		}
		
		if(ctx.getGuild().isPresent()) {
			
			if(ctx.getGuildMember().get().isOwner())
				return BotPermission.PermissionValue.ALLOW.setReason("GUILD_OWNER");
			
			BotPermission acquiredPermission = null;
			BotPermission.PermissionValue beforeVal = null;
			
			for(BotPermission permission : ctx.getCmdFramework().getPermProvider().getGuildPermissions(ctx.getGuild().get())) {
				beforeVal = val;
				if(permission.getChannelId() != 0L) {
					if(ctx.getChannel().getIdLong() != permission.getChannelId()) continue;
					if(permission.getRoleId() != 0L) {
						//Permission should not apply if user does not have role
						if(!PermissionUtil.hasRole(permission.getRoleId(), ctx.getGuildMember().get())) continue;
						
						if(permission.getModule() != null && module != null) {
							if(val.getReason().startsWith("COMMAND_TEXTCHANNEL_ROLE_") || !module.equalsIgnoreCase(permission.getModule()))
								continue;
							
							if(permission.getAction().equalsIgnoreCase("allow")) {
								val = BotPermission.PermissionValue.ALLOW.setReason("MODULE_TEXTCHANNEL_ROLE_ALLOWED");
							} else {
								val = BotPermission.PermissionValue.DENY.setReason("MODULE_TEXTCHANNEL_ROLE_DENIED");
							}
						} else {
							if(!ctx.getCommand().equalsIgnoreCase(permission.getCommand())) continue;
							
							if(permission.getAction().equalsIgnoreCase("allow")) {
								val = BotPermission.PermissionValue.ALLOW.setReason("COMMAND_TEXTCHANNEL_ROLE_ALLOWED");
							} else {
								val = BotPermission.PermissionValue.DENY.setReason("COMMAND_TEXTCHANNEL_ROLE_DENIED");
							}
						}
						
						//Permission should not apply if role before is higher and level is not equal
						if(acquiredPermission != null && PermissionUtil.isSameLevel(val, beforeVal) && PermissionUtil.isRoleHigher(ctx.getGuild().get(), acquiredPermission.getRoleId(), permission.getRoleId())) {
							val = beforeVal;
							continue;
						}
					} else {
						if(val.getReason().startsWith("MODULE_ROLE_") 
								|| val.getReason().startsWith("COMMAND_ROLE_") 
								|| val.getReason().startsWith("MODULE_TEXTCHANNEL_ROLE_") 
								|| val.getReason().startsWith("COMMAND_TEXTCHANNEL_ROLE_"))
							continue;
						
						if(permission.getModule() != null && module != null) {
							if(val.getReason().equalsIgnoreCase("COMMAND_TEXTCHANNEL_ALLOWED") || val.getReason().equalsIgnoreCase("COMMAND_TEXTCHANNEL_DENIED") 
									|| !module.equalsIgnoreCase(permission.getModule()))
								continue;
							
							if(permission.getAction().equalsIgnoreCase("allow")) {
								val = BotPermission.PermissionValue.ALLOW.setReason("MODULE_TEXTCHANNEL_ALLOWED");
							} else {
								val = BotPermission.PermissionValue.DENY.setReason("MODULE_TEXTCHANNEL_ROLE_DENIED");
							}
						} else {
							if(!ctx.getCommand().equalsIgnoreCase(permission.getCommand())) continue;
							
							if(permission.getAction().equalsIgnoreCase("allow")) {
								val = BotPermission.PermissionValue.ALLOW.setReason("COMMAND_TEXTCHANNEL_ALLOWED");
							} else {
								val = BotPermission.PermissionValue.DENY.setReason("COMMAND_TEXTCHANNEL_ROLE_DENIED");
							}
						}
					}
				} else {
					if(permission.getRoleId() != 0L) {
						if(!PermissionUtil.hasRole(permission.getRoleId(), ctx.getGuildMember().get())) continue;
						
						if(val.getReason().startsWith("MODULE_TEXTCHANNEL_ROLE_") || val.getReason().startsWith("COMMAND_TEXTCHANNEL_ROLE_"))
							continue;
						
						if(permission.getModule() != null && module != null) {
							if(val.getReason().equalsIgnoreCase("COMMAND_TEXTCHANNEL_ALLOWED")
									|| val.getReason().equalsIgnoreCase("COMMAND_TEXTCHANNEL_DENIED")
									|| !module.equalsIgnoreCase(permission.getModule()))
								continue;
							
							if(permission.getAction().equalsIgnoreCase("allow")) {
								val = BotPermission.PermissionValue.ALLOW.setReason("MODULE_ROLE_ALLOWED");
							} else {
								val = BotPermission.PermissionValue.DENY.setReason("MODULE_ROLE_DENIED");
							}
						} else {
							if(!ctx.getCommand().equalsIgnoreCase(permission.getCommand())) continue;
							
							if(permission.getAction().equalsIgnoreCase("allow")) {
								val = BotPermission.PermissionValue.ALLOW.setReason("COMMAND_ROLE_ALLOWED");
							} else {
								val = BotPermission.PermissionValue.DENY.setReason("COMMAND_ROLE_DENIED");
							}
						}
						
						//Permission should not apply if role before is higher and level is not equal
						if(acquiredPermission != null && PermissionUtil.isSameLevel(val, beforeVal) && PermissionUtil.isRoleHigher(ctx.getGuild().get(), acquiredPermission.getRoleId(), permission.getRoleId())) {
							val = beforeVal;
							continue;
						}
					} else if(permission.getUserId() != 0L) {
						if(!ctx.getCommand().equalsIgnoreCase(permission.getCommand()) || ctx.getAuthor().getIdLong() != permission.getUserId()) continue;
						
						if(permission.getAction().equalsIgnoreCase("allow")) {
							val = BotPermission.PermissionValue.ALLOW.setReason("COMMAND_USER_ALLOWED");
						} else {
							val = BotPermission.PermissionValue.DENY.setReason("COMMAND_USER_DENIED");
						}
						
						break; // ----------------------------------------- // LOW LEVELED PERM
					} else {
						if(val.getReason().startsWith("MODULE_TEXTCHANNEL_") || val.getReason().startsWith("COMMAND_TEXTCHANNEL_") || val.getReason().startsWith("MODULE_ROLE_") || val.getReason().startsWith("COMMAND_ROLE_"))
							continue;
						
						if(permission.getModule() != null && module != null) {
							if(val.getReason().startsWith("COMMAND_GUILD_") || !module.equalsIgnoreCase(permission.getModule()))
								continue;
							
							if(permission.getAction().equalsIgnoreCase("allow")) {
								val = BotPermission.PermissionValue.ALLOW.setReason("MODULE_GUILD_ALLOWED");
							} else {
								val = BotPermission.PermissionValue.DENY.setReason("MODULE_GUILD_DENIED");
							}
						} else {
							if(!ctx.getCommand().equalsIgnoreCase(permission.getCommand())) continue;
							
							if(permission.getAction().equalsIgnoreCase("allow")) {
								val = BotPermission.PermissionValue.ALLOW.setReason("COMMAND_GUILD_ALLOWED");
							} else {
								val = BotPermission.PermissionValue.DENY.setReason("COMMAND_GUILD_DENIED");
							}
						}
					}
				}
				acquiredPermission = permission;
			}
		}
		return val;
	}
	
	public static boolean isBlacklisted(IContext ctx, boolean guild) {
		return false;
	}
	
	public static boolean isPermissionExisting(LinkedList<BotPermission> permissions, BotPermission permission) {
		for(BotPermission permission2 : permissions) {
			if(PermissionUtil.isEqual(permission, permission2)) {
				if(permission.getAction().equalsIgnoreCase(permission2.getAction())) {
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
