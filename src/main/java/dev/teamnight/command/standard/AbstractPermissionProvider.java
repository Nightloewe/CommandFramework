package dev.teamnight.command.standard;

import java.util.List;

import dev.teamnight.command.AnnotatedCommand;
import dev.teamnight.command.ICommand;
import dev.teamnight.command.IContext;
import dev.teamnight.command.IPermission;
import dev.teamnight.command.PermissionProvider;
import dev.teamnight.command.Tribool;
import dev.teamnight.command.utils.PermissionUtil;

/**
 * Provides an implementation for the Permission Check which uses weights
 * to determine the importance of Permissions.
 * 
 * @author Jonas
 * @since 0.1.1
 */
public abstract class AbstractPermissionProvider implements PermissionProvider {

	@Override
	public Tribool canExecute(IContext ctx) {
		Tribool result = Tribool.NEUTRAL;
		String module = null;
		ICommand cmd = ctx.getCmdFramework().getCommandMap().getCommand(ctx.getCommand());
		
		if(cmd instanceof AnnotatedCommand) {
			AnnotatedCommand annotatedCommand = (AnnotatedCommand) cmd;
			
			module = annotatedCommand.getModule();
		}
		
		List<IPermission> globalPermissions = ctx.getCmdFramework().getPermProvider().getGlobalPermissions();
		
		for(IPermission permission : globalPermissions) {
			if(permission.getCommand().isPresent()) {
				if(permission.getCommand().get().equalsIgnoreCase(cmd.getName())) {
					result = permission.getAction();
				}
			} else if(permission.getModule().isPresent()) {
				if(permission.getModule().get().equals(module)) {
					result = permission.getAction();
				}
			}
		}
		
		int weight = 0;
		
		if(ctx.getGuild().isPresent()) {
			if(ctx.getGuildMember().get().isOwner())
				return Tribool.TRUE;
			
			List<IPermission> guildPermissions = ctx.getCmdFramework().getPermProvider().getGuildPermissions(ctx.getGuild().get());
			
			for(IPermission permission : guildPermissions) {
				if(permission.getCommand().isPresent()) {
					if(!permission.getCommand().get().equalsIgnoreCase(cmd.getName())) {
						continue;
					}
				} else {
					if(!permission.getModule().isPresent()) {
						throw new IllegalArgumentException("The permission has no module or command present");
					}
					
					if(!permission.getModule().get().equals(module)) {
						continue;
					}
				}
				
				if(permission.getChannelId() != 0L) {
					if(permission.getChannelId() != ctx.getChannel().getIdLong()) {
						continue;
					}
				}
				
				if(permission.getRoleId() != 0L) {
					if(!PermissionUtil.hasRole(permission.getRoleId(), ctx.getGuildMember().get())) {
						continue;
					}
				}
				
				if(permission.getUserId() != 0L) {
					if(permission.getUserId() != ctx.getAuthor().getIdLong()) {
						continue;
					}
				}
				
				int permissionWeight = getWeight(permission);
				
				if(permissionWeight < weight) {
					continue;
				}
				
				weight = permissionWeight;
				result = permission.getAction();
			}
		}
		return result;
	}
	
	/**
	 * Determines the weight of a permission in order to compare it to others.
	 * Can be used for a comparator or to check if a permission overrides a
	 * beforehand result.
	 * 
	 * <p>
	 * The weights are as follow:
	 * 1 - Guild Module Permission
	 * 2 - Guild Command Permission
	 * 3 - Guild Text-Channel specific Module Permission
	 * 4 - Guild Text-Channel specific Command Permission
	 * 5 - Guild Role specific Module Permission
	 * 6 - Guild Role specific Command Permission
	 * 7 - Guild Role & Text-Channel specific Module Permission
	 * 8 - Guild Role & Text-Channel specific Command Permission
	 * 9 - User-specific Permission
	 * </p>
	 * 
	 * Higher weight means more importance.
	 * 
	 * @param {@link dev.teamnight.command.IPermission}
	 * @return Integer the weight
	 */
	public int getWeight(IPermission permission) {
		int weight = 0;
		
		if(permission.getUserId() != 0L) {
			return 9;
		}
		
		if(permission.getCommand().isPresent()) {
			weight = 2;
		} else if(permission.getModule().isPresent()) {
			weight = 1;
		} else {
			throw new IllegalArgumentException("Command nor module present for permission " + permission.getId());
		}
		
		if(permission.getChannelId() != 0L) {
			weight = weight + 2;
		}
		
		if(permission.getRoleId() != 0L) {
			weight = weight + 4;
		}
		
		return weight;
	}

}
