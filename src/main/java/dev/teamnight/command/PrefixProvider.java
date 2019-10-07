package dev.teamnight.command;

import net.dv8tion.jda.api.entities.Guild;

public interface PrefixProvider {

	//TODO: Change to CommandArgs or Context
	public String providePrefix(Guild guild);
	
}
