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

/**
 * Some utilities for IPermission
 * 
 * @author Jonas Müller
 */
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
//	public static Tribool canExecute(IContext ctx) {
//		Tribool result = Tribool.NEUTRAL;
//		String module = null;
//		ICommand cmd = ctx.getCmdFramework().getCommandMap().getCommand(ctx.getCommand());
//		
//		if(cmd instanceof AnnotatedCommand) {
//			AnnotatedCommand annotatedCommand = (AnnotatedCommand) cmd;
//			
//			module = annotatedCommand.getModule();
//		}
//		
//		List<IPermission> globalPermissions = ctx.getCmdFramework().getPermProvider().getGlobalPermissions();
//		
//		for(IPermission permission : globalPermissions) {
//			if(permission.getCommand().isPresent()) {
//				if(permission.getCommand().get().equalsIgnoreCase(cmd.getName())) {
//					result = permission.getAction();
//				}
//			} else if(permission.getModule().isPresent()) {
//				if(permission.getModule().get().equals(module)) {
//					result = permission.getAction();
//				}
//			}
//		}
//		
//		int weight = 0;
//		
//		if(ctx.getGuild().isPresent()) {
//			if(ctx.getGuildMember().get().isOwner())
//				return Tribool.TRUE;
//			
//			List<IPermission> guildPermissions = ctx.getCmdFramework().getPermProvider().getGuildPermissions(ctx.getGuild().get());
//			
//			for(IPermission permission : guildPermissions) {
//				if(permission.getCommand().isPresent()) {
//					if(!permission.getCommand().get().equalsIgnoreCase(cmd.getName())) {
//						continue;
//					}
//				} else {
//					if(!permission.getModule().isPresent()) {
//						throw new IllegalArgumentException("The permission has no module or command present");
//					}
//					
//					if(!permission.getModule().get().equals(module)) {
//						continue;
//					}
//				}
//				
//				if(permission.getChannelId() != 0L) {
//					if(permission.getChannelId() != ctx.getChannel().getIdLong()) {
//						continue;
//					}
//				}
//				
//				if(permission.getRoleId() != 0L) {
//					if(!hasRole(permission.getRoleId(), ctx.getGuildMember().get())) {
//						continue;
//					}
//				}
//				
//				if(permission.getUserId() != 0L) {
//					if(permission.getUserId() != ctx.getAuthor().getIdLong()) {
//						continue;
//					}
//				}
//				
//				int permissionWeight = getWeight(permission);
//				
//				if(permissionWeight < weight) {
//					continue;
//				}
//				
//				weight = permissionWeight;
//				result = permission.getAction();
//			}
//		}
//		return result;
//	}
//	
//	/**
//	 * 1: Check guild module perm (overwritable)
//	 * 2: Check guild command perm (overwritable)
//	 * 3: Check guild module textchannel perm (overwritable)
//	 * 4: Check guild command textchannel perm (overwritable)
//	 * 5: Check guild module role perm (overwritable)
//	 * 6: Check guild command role perm (overwritable)
//	 * 7: Check guild module role textchannel perm (overwritable)
//	 * 8: Check guild command role textchannel perm (overwritable)
//	 * 9: Check guild user perm (not-overwritable) (IGNORING - BREAKS LOOP)
//	 * s
//	 * Determines the weight of a permission in order to compare it to others.
//	 * Can be used for a comparator or to check if a permission overrides a
//	 * beforehand result.
//	 * 
//	 * @param {@link dev.teamnight.command.IPermission}
//	 */
//	public static int getWeight(IPermission permission) {
//		int weight = 0;
//		
//		if(permission.getUserId() != 0L) {
//			return 9;
//		}
//		
//		if(permission.getCommand().isPresent()) {
//			weight = 2;
//		} else if(permission.getModule().isPresent()) {
//			weight = 1;
//		} else {
//			throw new IllegalArgumentException("Command nor module present for permission " + permission.getId());
//		}
//		
//		if(permission.getChannelId() != 0L) {
//			weight = weight + 2;
//		}
//		
//		if(permission.getRoleId() != 0L) {
//			weight = weight + 4;
//		}
//		
//		return weight;
//	}
	
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
	
	/**
	 * Checks whether a permission is equal in all variables except the action
	 * @param {@link IPermission} the Permission
	 * @param {@link IPermission} the other Permission to compare to
	 * @return true if the permissions are equal and false if not
	 */
	@Deprecated
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
		
		if(permission.getModule().isPresent()) {
			if(other.getModule().isPresent()) {
				if(permission.getModule().get().equals(other.getModule().get())) {
					equal = true;
				}
			} else {
				return false;
			}
		} else {
			if(other.getModule().isPresent()) {
				return false;
			} else {
				equal = true;
			}
		}
		
		if(permission.getCommand().isPresent()) {
			if(other.getCommand().isPresent()) {
				if(permission.getCommand().get().equals(other.getCommand().get())) {
					equal = true;
				}
			} else {
				return false;
			}
		} else {
			if(other.getCommand().isPresent()) {
				return false;
			} else {
				equal = true;
			}
		}
		
		return equal;
	}
	
	@Deprecated
	private static String getLevel(IPermission permission) {
		StringBuilder levelBuilder = new StringBuilder();
		boolean modulePresent = permission.getModule().isPresent();
		boolean commandPresent = permission.getCommand().isPresent();
		
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
	
	@Deprecated
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
	
	@Deprecated
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
}
