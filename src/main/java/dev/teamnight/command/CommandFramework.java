/**
 *    Copyright 2019-2020 Jonas Müller, Jannik Müller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.teamnight.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import dev.teamnight.command.annotations.SubModule;
import dev.teamnight.command.standard.DefaultConditionRegistry;
import dev.teamnight.command.standard.JDAListener;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

/**
 * @author Jonas Müller
 */
public class CommandFramework {

	private Logger logger; //Implemented
	
	private ICommandMap map; //Implemented
	
	private IArgumentProcessor argProcessor;
	private IModuleAnalyzer moduleAnalyzer;
	
	private IConditionRegistry registry;
	
	private List<ICommandListener> listeners; //Implemented
	private Map<String, IModule> topLevelModules;
	
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
		
		this.registry = new DefaultConditionRegistry();
		
		this.topLevelModules = new HashMap<String, IModule>();
		
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
	 * @return the Condition Registry
	 */
	public IConditionRegistry getConditionRegistry() {
		return registry;
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
	
	public void addCommandListener(ICommandListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * @return List<IRegisteredModule> the modules
	 */
	public Map<String, IModule> getModules() {
		return topLevelModules;
	}

	/**
	 * Analyzes a module and registers all commands within.
	 * @param {@link dev.teamnight.command.IModule} the module
	 */
	public void addModule(Object module) {
		IModule regModule = this.moduleAnalyzer.analyze(module);
		
		regModule.getCommands().forEach(cmd -> this.getCommandMap().register(cmd));
		
		if(!(regModule instanceof ITopLevelModule)) {
			SubModule annotation = module.getClass().getAnnotation(SubModule.class);
			
			boolean foundTopLevelModule = false;
			for(IModule mod : this.topLevelModules.values()) {
				if(mod.getModuleClass() == annotation.baseModule()) {
					if(!(mod instanceof ITopLevelModule)) {
						throw new IllegalArgumentException("A submodule can not be the base module of another submodule");
					}
					
					foundTopLevelModule = true;
					ITopLevelModule tlm = (ITopLevelModule) mod;
					
					tlm.addSubModule(regModule);
				}
			}
			
			if(!foundTopLevelModule) {
				throw new IllegalStateException("A submodule can only be added if a top-level-module was registered");
			}

			return;
		}
		
		this.topLevelModules.put(regModule.getName(), regModule);
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
