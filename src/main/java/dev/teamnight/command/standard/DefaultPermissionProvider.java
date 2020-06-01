package dev.teamnight.command.standard;

import java.util.Collections;
import java.util.List;

import dev.teamnight.command.IPermission;
import dev.teamnight.command.PermissionProvider;
import net.dv8tion.jda.api.entities.Guild;

public class DefaultPermissionProvider implements PermissionProvider {

	@Override
	public List<IPermission> getGlobalPermissions() {
		return Collections.emptyList();
	}



	@Override
	public List<IPermission> getGuildPermissions(Guild guild) {
		return Collections.emptyList();
	}
	
}
