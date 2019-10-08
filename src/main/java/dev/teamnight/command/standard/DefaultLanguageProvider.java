package dev.teamnight.command.standard;

import dev.teamnight.command.LanguageProvider;
import net.dv8tion.jda.api.entities.Guild;

public class DefaultLanguageProvider implements LanguageProvider {

	@Override
	public String provideString(Guild guild, String key) {
		throw new UnsupportedOperationException();
	}

}
