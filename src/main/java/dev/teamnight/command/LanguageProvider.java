package dev.teamnight.command;

import net.dv8tion.jda.api.entities.Guild;

public interface LanguageProvider {

	public String provideString(Guild guild, String key);
	
}
