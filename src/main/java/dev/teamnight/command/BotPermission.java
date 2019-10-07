package dev.teamnight.command;

public class BotPermission {

	private int id;
	private long guildId;
	private long channelId;
	private long roleId;
	private long userId;
	private String module, command, action;
	
	/**
	 * Normal Constructor to set values self
	 */
	public BotPermission() {
	}
	
	/**
	 * Constructor to disable module globally
	 * @param id
	 * @param module
	 * @param action
	 */
	public BotPermission(int id, String module, boolean action) {
		this(id, 0, 0, 0, 0, module, null, (action ? "enable" : "disable"));
	}
	
	/**
	 * Constructor to disable command globally
	 * @param id
	 * @param command
	 * @param action
	 */
	public BotPermission(int id, String command, String action) {
		this(id, 0, 0, 0, 0, null, command, action);
	}
	
	/**
	 * Constructor to blacklist/whitelist guild
	 * @param id
	 * @param guildId
	 * @param action
	 */
	public BotPermission(int id, long guildId, String action) {
		this(id, guildId, 0, 0, 0, null, null, action);
	}
	
	/**
	 * Constructor to blacklist/whitelist user
	 * @param id
	 * @param userId
	 * @param action
	 */
	public BotPermission(int id, Long userId, String action) {
		this(id, 0, 0, 0, userId, null, null, action);
	}
	
	/**
	 * Constructor to disable modules on guild
	 * @param id
	 * @param guildId
	 * @param module
	 * @param action
	 */
	public BotPermission(int id, long guildId, String module, boolean action) {
		this(id, guildId, 0, 0, 0, module, null, (action ? "enable" : "disable"));
	}
	
	/**
	 * Constructor to disable command on guild
	 * @param id
	 * @param guildId
	 * @param command
	 * @param action
	 */
	public BotPermission(int id, long guildId, String command, String action) {
		this(id, guildId, 0, 0, 0, null, command, action);
	}
	
	/**
	 * Constructor to disable module in guild textchannel
	 * @param id
	 * @param guildId
	 * @param channelId
	 * @param module
	 * @param action
	 */
	public BotPermission(int id, long guildId, long channelId, String module, boolean action) {
		this(id, guildId, channelId, 0, 0, module, null, (action ? "allow" : "deny"));
	}
	
	/**
	 * Constructor to disable command in guild textchannel
	 * @param id
	 * @param guildId
	 * @param channelId
	 * @param command
	 * @param action
	 */
	public BotPermission(int id, long guildId, long channelId, String command, String action) {
		this(id, guildId, channelId, 0, 0, null, command, action);
	}
	
	/**
	 * Constructor to allow/deny module for role
	 * @param id
	 * @param guildId
	 * @param module
	 * @param roleId
	 * @param action
	 */
	public BotPermission(int id, long guildId, String module, long roleId, boolean action) {
		this(id, guildId, 0, roleId, 0, module, null, (action ? "allow" : "deny"));
	}
	
	/**
	 * Constructor to allow/deny command for role
	 * @param id
	 * @param guildId
	 * @param command
	 * @param roleId
	 * @param action
	 */
	public BotPermission(int id, long guildId, String command, long roleId, String action) {
		this(id, guildId, 0, roleId, 0, null, command, action);
	}
	
	/**
	 * Constructor to allow/deny module for role in textchannel
	 * @param id
	 * @param guildId
	 * @param channelId
	 * @param roleId
	 * @param module
	 * @param action
	 */
	public BotPermission(int id, long guildId, long channelId, long roleId, String module, boolean action) {
		this(id, guildId, channelId, roleId, 0, module, null, (action ? "allow" : "deny"));
	}
	
	/**
	 * Constructor to allow/deny command for role in textchannel
	 * @param id
	 * @param guildId
	 * @param channelId
	 * @param roleId
	 * @param command
	 * @param action
	 */
	public BotPermission(int id, long guildId, long channelId, long roleId, String command, String action) {
		this(id, guildId, channelId, roleId, 0, null, command, action);
	}
	
	/**
	 * Constructor to allow/deny command for user in guild
	 * @param id
	 * @param guildId
	 * @param userId
	 * @param command
	 * @param action
	 */
	public BotPermission(int id, long guildId, Long userId, String command, String action) {
		this(id, guildId, 0, 0, userId, null, command, action);
	}
	
	private BotPermission(int id, long guildId, long channelId, long roleId, long userId, String module, String command, String action) {
		this.id = id;
		this.guildId = guildId;
		this.channelId = channelId;
		this.roleId = roleId;
		this.userId = userId;
		this.module = module;
		this.command = command;
		this.action = action;
	}
	
	public int getId() {
		return id;
	}
	
	public long getGuildId() {
		return guildId;
	}
	
	public long getChannelId() {
		return channelId;
	}
	
	public long getRoleId() {
		return roleId;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public String getModule() {
		return module;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}
	
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public void setModule(String module) {
		this.module = module;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public enum PermissionValue {
		ALLOW,
		DENY,
		UNSET;
		
		private String reason = "";
		
		public PermissionValue setReason(String reason) {
			this.reason = reason;
			return this;
		}
		
		public String getReason() {
			return reason;
		}
	}
	
}
