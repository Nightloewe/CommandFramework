package dev.teamnight.command;

import java.util.List;

import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

public class FrameworkBuilder {

	protected static FrameworkBuilder newBuilder() {
		return new FrameworkBuilder();
	}
	
	public FrameworkBuilder allowDM(boolean allowDM) {
		return this;
	}
	
	public FrameworkBuilder addBlockedGuilds(Guild...guilds) {
		return this;
	}
	
	public FrameworkBuilder addBlockedGuilds(List<Guild> guilds) {
		return this;
	}
	
	public FrameworkBuilder addBlockedUsers(List<User>...users) {
		return this;
	}
	
	public FrameworkBuilder withPrefix(String prefix) {
		return this;
	}
	
	public FrameworkBuilder withPrefixProvider(PrefixProvider provider) {
		return this;
	}
	
	public FrameworkBuilder allowMention(boolean allowMention) {
		return this;
	}
	
	public FrameworkBuilder useDefaultDelimiter() {
		return this;
	}
	
	public FrameworkBuilder withDelimiter(char delimiter) {
		return this;
	}
	
	public FrameworkBuilder allowBots(boolean allowBots) {
		return this;
	}
	
	public FrameworkBuilder addOwners(User...owner) {
		return this;
	}
	
	public FrameworkBuilder addOwners(List<User>...owners) {
		return this;
	}
	
	public FrameworkBuilder withLanguageProvider(LanguageProvider provider) {
		return this;
	}
	
	public FrameworkBuilder withCustomContext(IContext context) {
		return this;
	}
	
	public FrameworkBuilder withCustomCommandMap(ICommandMap map) {
		return this;
	}
	
	public FrameworkBuilder withPermissionProvider(PermissionProvider provider) {
		return this;
	}
	
	public FrameworkBuilder setLogger(Logger logger) {
		return this;
	}
	
	public FrameworkBuilder registerClient(ShardManager manager) {
		return this;
	}
	
	public CommandFramework build() {
		return new CommandFramework();
	}
}
