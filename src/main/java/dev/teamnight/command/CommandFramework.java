package dev.teamnight.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import dev.teamnight.command.standard.JDAListener;
import dev.teamnight.command.standard.Module;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

public class CommandFramework {

	private Logger logger; //Implemented
	
	private ICommandMap map; //Implemented
	
	private IArgumentProcessor argProcessor;
	private IModuleAnalyzer moduleAnalyzer;
	
	private List<ICommandListener> listeners; //Implemented
	private Map<String, IRegisteredModule> modules;
	
	private List<Long> blockedGuilds; //Implemented
	private List<Long> blockedUsers; //Implemented
	
	private List<Long> botOwners; //Implemented
	private List<User> botOwnerList;
	
	private PrefixProvider prefixProvider; //Implemented
	private LanguageProvider langProvier; //Implemented
	private PermissionProvider permProvider; //Implemented
	private HelpProvider helpProvider;
	
	private ShardManager shardManager; //no need
	
	private boolean allowDM; //Implemented
	private boolean allowMentionCmd; //Implemented
	private boolean allowBots; //Implemented
	
	public static FrameworkBuilder newBuilder() {
		return FrameworkBuilder.newBuilder();
	}
	
	public CommandFramework(Logger logger, ShardManager manager, ICommandMap commandMap, 
			IArgumentProcessor processor, IModuleAnalyzer moduleAnalyzer,
			List<Long> owners, List<Long> blockedUsers, List<Long> blockedGuilds, 
			List<ICommandListener> listeners, PrefixProvider prefixProvider, 
			LanguageProvider languageProvider, PermissionProvider permissionProvider, 
			HelpProvider helpProvider, boolean allowDM, boolean allowMention, boolean allowBots) {
		this.logger = logger;
		
		this.map = commandMap;
		
		this.argProcessor = processor;
		this.moduleAnalyzer = moduleAnalyzer;
		
		this.modules = new HashMap<String, IRegisteredModule>();
		
		this.blockedGuilds = blockedGuilds;
		this.blockedUsers = blockedUsers;
		
		this.botOwners = owners;
		this.botOwnerList = new ArrayList<User>();
		
		this.listeners = listeners;
		
		this.prefixProvider = prefixProvider;
		this.langProvier = languageProvider;
		this.permProvider = permissionProvider;
		this.helpProvider = helpProvider;
		
		this.allowDM = allowDM;
		this.allowMentionCmd = allowMention;
		this.allowBots = allowBots;
		
		if(shardManager != null) {
			this.setShardManager(manager);
		}
	}
	
	/**
	 * 
	 * @return the logger
	 */
	public Logger logger() {
		return this.logger;
	}
	
	/**
	 * 
	 * @return the Command Map
	 */
	public ICommandMap getCommandMap() {
		return this.map;
	}
	
	/**
	 * @return the Command Processor
	 */
	public IArgumentProcessor getArgumentProcessor() {
		return this.argProcessor;
	}
	
	/**
	 * 
	 * @return the blockedGuilds
	 */
	public List<Long> getBlockedGuilds() {
		return this.blockedGuilds;
	}

	/**
	 * @return the blockedUsers
	 */
	public List<Long> getBlockedUsers() {
		return blockedUsers;
	}

	/**
	 * @return the botOwners
	 */
	public List<Long> getBotOwners() {
		return botOwners;
	}
	
	/**
	 * @return a list of {@link User} of the botOwners
	 */
	public List<User> getBotOwnerList() {
		return botOwnerList;
	}
	
	/**
	 * 
	 * @return List<ICommandListener> the listeners
	 */
	public List<ICommandListener> getListeners() {
		return listeners;
	}
	
	/**
	 * @return List<IRegisteredModule> the modules
	 */
	public Map<String, IRegisteredModule> getModules() {
		return modules;
	}

	/**
	 * Analyzes a module and registers all commands within.
	 * @param {@link dev.teamnight.command.IModule} the module
	 */
	public void addModule(IModule module) {
		IRegisteredModule regModule = this.moduleAnalyzer.analyze(module);
		
		regModule.getCommands().forEach(cmd -> this.getCommandMap().register(cmd));
		
		this.modules.put(regModule.getName(), regModule);
	}
	
	/**
	 * @see {@link dev.teamnight.command.CommandFramework#addModule(IModule)}
	 */
	public void registerCommands(IModule module) {
		this.addModule(module);
	}

	/**
	 * @return the prefixProvider
	 */
	public PrefixProvider getPrefixProvider() {
		return prefixProvider;
	}

	/**
	 * @return the langProvier
	 */
	public LanguageProvider getLangProvier() {
		return langProvier;
	}

	/**
	 * @return the permProvider
	 */
	public PermissionProvider getPermProvider() {
		return permProvider;
	}
	
	/**
	 * @return the helpProvider
	 */
	public HelpProvider getHelpProvider() {
		return helpProvider;
	}

	/**
	 * @return the shardManager
	 */
	public ShardManager getShardManager() {
		return shardManager;
	}
	
	/**
	 * Sets the shard manager and registers the local
	 * message listener.
	 * @param {@link net.dv8tion.jda.api.sharding.ShardManager} the shard Manager
	 */
	public void setShardManager(ShardManager shardManager) {
		this.shardManager = shardManager;
		
		this.shardManager.addEventListener(new JDAListener(this));
	}

	/**
	 * @return the allowDM
	 */
	public boolean isAllowDM() {
		return allowDM;
	}

	/**
	 * @return the allowMentionCmd
	 */
	public boolean isAllowMentionCmd() {
		return allowMentionCmd;
	}

	/**
	 * @return the allowBots
	 */
	public boolean isAllowBots() {
		return allowBots;
	}

	public boolean isOwner(User user) {
		return this.botOwners.contains(user.getIdLong());
	}
}
