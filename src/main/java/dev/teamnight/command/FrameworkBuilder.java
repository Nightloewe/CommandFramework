package dev.teamnight.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.teamnight.command.standard.DefaultLanguageProvider;
import dev.teamnight.command.standard.DefaultPermissionProvider;
import dev.teamnight.command.standard.DefaultPrefixProvider;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

public class FrameworkBuilder {

	private boolean allowDM = false;
	private boolean allowMentionCmd = false;
	private boolean allowBots = false;
	
	private char delimiter = ' ';
	
	private List<Guild> blockedGuilds = new ArrayList<Guild>();
	private List<User> blockedUsers = new ArrayList<User>();
	
	private List<User> owners = new ArrayList<User>();
	
	private List<ICommandListener> listeners = new ArrayList<ICommandListener>();
	
	private PrefixProvider prefixProvider;
	private LanguageProvider langProvider;
	private PermissionProvider permProvider;
	
	private ICommandMap commandMap;
	
	private Logger logger;
	
	private ShardManager shardManager;
	
	protected static FrameworkBuilder newBuilder() {
		return new FrameworkBuilder();
	}
	
	public FrameworkBuilder allowDM(boolean allowDM) {
		this.allowBots = allowDM;
		return this;
	}
	
	public FrameworkBuilder addBlockedGuilds(Guild...guilds) {
		for(Guild g : guilds) {
			this.blockedGuilds.add(g);
		}
		return this;
	}
	
	public FrameworkBuilder addBlockedGuilds(List<Guild> guilds) {
		this.blockedGuilds.addAll(guilds);
		return this;
	}
	
	public FrameworkBuilder addBlockedUsers(User...users) {
		for(User u : users) {
			this.blockedUsers.add(u);
		}
		return this;
	}
	
	public FrameworkBuilder addBlockedUsers(List<User> users) {
		this.blockedUsers.addAll(users);
		return this;
	}
	
	public FrameworkBuilder withPrefix(String prefix) {
		if(this.prefixProvider == null)
			this.useDefaultPrefixProvider();
		
		if(this.prefixProvider instanceof DefaultPrefixProvider) {
			((DefaultPrefixProvider)this.prefixProvider).setPrefix(prefix);
		}
		return this;
	}
	
	private void useDefaultPrefixProvider() {
		this.prefixProvider = new DefaultPrefixProvider(".");
	}

	public FrameworkBuilder withPrefixProvider(PrefixProvider provider) {
		this.prefixProvider = provider;
		return this;
	}
	
	public FrameworkBuilder allowMention(boolean allowMention) {
		this.allowMentionCmd = allowMention;
		return this;
	}
	
	@Deprecated
	public FrameworkBuilder useDefaultDelimiter() {
		return this.withDelimiter(' ');
	}
	
	@Deprecated
	public FrameworkBuilder withDelimiter(char delimiter) {
		this.delimiter = delimiter;
		return this;
	}
	
	public FrameworkBuilder allowBots(boolean allowBots) {
		this.allowBots = allowBots;
		return this;
	}
	
	public FrameworkBuilder addOwners(User...owners) {
		for(User u : owners) {
			this.owners.add(u);
		}
		return this;
	}
	
	public FrameworkBuilder addOwners(List<User> owners) {
		this.owners.addAll(owners);
		return this;
	}
	
	public FrameworkBuilder addListener(ICommandListener listener) {
		this.listeners.add(listener);
		return this;
	}
	
	public FrameworkBuilder withLanguageProvider(LanguageProvider provider) {
		this.langProvider = provider;
		return this;
	}
	
	public FrameworkBuilder withCustomCommandMap(ICommandMap map) {
		this.commandMap = map;
		return this;
	}
	
	public FrameworkBuilder withPermissionProvider(PermissionProvider provider) {
		this.permProvider = provider;
		return this;
	}
	
	public FrameworkBuilder setLogger(Logger logger) {
		this.logger = logger;
		return this;
	}
	
	public FrameworkBuilder registerClient(ShardManager manager) {
		this.shardManager = manager;
		return this;
	}
	
	public CommandFramework build() {
		if(this.prefixProvider == null)
			this.prefixProvider = new DefaultPrefixProvider(".");
		
		if(this.langProvider == null)
			this.langProvider = new DefaultLanguageProvider();
		
		if(this.permProvider == null)
			this.permProvider = new DefaultPermissionProvider();
		
		if(this.commandMap == null)
			this.commandMap = new CommandMap();
		
		if(this.logger == null)
			this.logger = LogManager.getLogger();
		
		if(this.shardManager == null)
			throw new IllegalArgumentException("shardManager can not be null; It must be provided within the Builder");
		
		return new CommandFramework(logger, shardManager, commandMap, owners, blockedUsers, blockedGuilds, listeners, prefixProvider, langProvider, permProvider, allowDM, allowMentionCmd, allowBots);
	}
}
