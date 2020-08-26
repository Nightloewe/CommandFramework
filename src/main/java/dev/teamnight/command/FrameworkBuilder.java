package dev.teamnight.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.teamnight.command.standard.AnnotatedModuleAnalyzer;
import dev.teamnight.command.standard.CommandMap;
import dev.teamnight.command.standard.DefaultArgumentProcessor;
import dev.teamnight.command.standard.DefaultHelpProvider;
import dev.teamnight.command.standard.DefaultLanguageProvider;
import dev.teamnight.command.standard.DefaultPermissionProvider;
import dev.teamnight.command.standard.DefaultPrefixProvider;
import dev.teamnight.command.utils.BotEmbedBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class FrameworkBuilder {

	private boolean allowDM = false;
	private boolean allowMentionCmd = false;
	private boolean allowBots = false;
	
	@SuppressWarnings("unused")
	private char delimiter = ' ';
	
	private List<Long> blockedGuilds = new ArrayList<Long>();
	private List<Long> blockedUsers = new ArrayList<Long>();
	
	private List<Long> owners = new ArrayList<Long>();
	
	private List<ICommandListener> listeners = new ArrayList<ICommandListener>();
	
	private PrefixProvider prefixProvider;
	private LanguageProvider langProvider;
	private PermissionProvider permProvider;
	private HelpProvider helpProvider;
	
	private ICommandMap commandMap;
	private IArgumentProcessor argumentProcessor;
	private IModuleAnalyzer moduleAnalyzer;
	
	private Logger logger;
	
	private ShardManager shardManager;
	private boolean registerLater = false;
	
	protected static FrameworkBuilder newBuilder() {
		return new FrameworkBuilder();
	}
	
	public FrameworkBuilder allowRainbowColors(boolean allowRainbowColors) {
		BotEmbedBuilder.setRainbowColorsEnabled(true);
		return this;
	}
	
	public FrameworkBuilder allowDM(boolean allowDM) {
		this.allowBots = allowDM;
		return this;
	}
	
	public FrameworkBuilder addBlockedGuilds(long...guilds) {
		for(long g : guilds) {
			this.blockedGuilds.add(g);
		}
		return this;
	}
	
	public FrameworkBuilder addBlockedGuilds(List<Long> guilds) {
		this.blockedGuilds.addAll(guilds);
		return this;
	}
	
	public FrameworkBuilder addBlockedUsers(long...users) {
		for(long u : users) {
			this.blockedUsers.add(u);
		}
		return this;
	}
	
	public FrameworkBuilder addBlockedUsers(List<Long> users) {
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
	
	public FrameworkBuilder withLanguageProvider(LanguageProvider provider) {
		this.langProvider = provider;
		return this;
	}
	
	public FrameworkBuilder withPermissionProvider(PermissionProvider provider) {
		this.permProvider = provider;
		return this;
	}
	
	public FrameworkBuilder withHelpProvider(HelpProvider provider) {
		this.helpProvider = provider;
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
	
	public FrameworkBuilder addOwners(long...owners) {
		for(long u : owners) {
			this.owners.add(u);
		}
		return this;
	}
	
	public FrameworkBuilder addOwners(List<Long> owners) {
		this.owners.addAll(owners);
		return this;
	}
	
	public FrameworkBuilder addListener(ICommandListener listener) {
		this.listeners.add(listener);
		return this;
	}
	
	public FrameworkBuilder withCustomCommandMap(ICommandMap map) {
		this.commandMap = map;
		return this;
	}
	
	public FrameworkBuilder withCustomArgumentProcessor(IArgumentProcessor processor) {
		this.argumentProcessor = processor;
		return this;
	}
	
	public FrameworkBuilder withCustomModuleAnalyzer(IModuleAnalyzer moduleAnalyzer) {
		this.moduleAnalyzer = moduleAnalyzer;
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
	
	public FrameworkBuilder registerClientLater() {
		this.registerLater = true;
		return this;
	}
	
	public CommandFramework build() {
		if(this.argumentProcessor == null)
			this.argumentProcessor = new DefaultArgumentProcessor();
		
		if(this.moduleAnalyzer == null)
			this.moduleAnalyzer = new AnnotatedModuleAnalyzer();
		
		if(this.prefixProvider == null)
			this.prefixProvider = new DefaultPrefixProvider(".");
		
		if(this.langProvider == null)
			this.langProvider = new DefaultLanguageProvider();
		
		if(this.permProvider == null)
			this.permProvider = new DefaultPermissionProvider();
		
		if(this.helpProvider == null)
			this.helpProvider = new DefaultHelpProvider();
		
		if(this.commandMap == null)
			this.commandMap = new CommandMap();
		
		if(this.logger == null)
			this.logger = LogManager.getLogger();
		
		if(this.shardManager == null && !this.registerLater)
			throw new IllegalArgumentException("shardManager can not be null, you need to provide it or call registerClientLater()");
		
		return new CommandFramework(logger, shardManager, commandMap, argumentProcessor, moduleAnalyzer, 
				owners, blockedUsers, blockedGuilds, listeners, 
				prefixProvider, langProvider, permProvider, helpProvider, 
				allowDM, allowMentionCmd, allowBots);
	}
}
