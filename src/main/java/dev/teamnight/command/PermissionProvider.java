package dev.teamnight.command;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;

public interface PermissionProvider {

	public List<BotPermission> getGlobalPermissions();
	
	public List<BotPermission> getGuildPermissions(Guild guild);
	
}
