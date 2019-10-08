package dev.teamnight.command.standard;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dev.teamnight.command.BotPermission;
import dev.teamnight.command.PermissionProvider;
import net.dv8tion.jda.api.entities.Guild;

//TODO: Implement DefaultPermissionProvider
public class DefaultPermissionProvider implements PermissionProvider {

	private List<BotPermission> globalPermission = new LinkedList<BotPermission>();
	
	private Map<Guild, BotPermissionList> guildPermissions = new HashMap<Guild, BotPermissionList>();
	
	public class BotPermissionList {
		
		private List<BotPermission> permissions = new LinkedList<BotPermission>();
		
	}

	@Override
	public List<BotPermission> getGlobalPermissions() {
		return Collections.emptyList();
	}



	@Override
	public List<BotPermission> getGuildPermissions(Guild guild) {
		return Collections.emptyList();
	}
	
}
