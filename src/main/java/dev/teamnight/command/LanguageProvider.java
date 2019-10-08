package dev.teamnight.command;

import net.dv8tion.jda.api.entities.Guild;

/**
 * An interface to provide the Command Framework with localized strings,
 * the Bot author may choose his way of implementing localization.
 * @author Jonas
 *
 */
public interface LanguageProvider {

	/**
	 * 
	 * @param Guild the Guild
	 * @param String the language Key
	 * @return a localized String
	 * TODO: allow replacing
	 */
	public String provideString(Guild guild, String key);
	
}
