package dev.teamnight.command;

import java.lang.reflect.Member;
import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.Optional;

public class CommandInfo {

	private Optional<Guild> guild;
	private Optional<Member> guildMember;
	private Message message;
	private String[] arguments;
	private String command;
	private Language lang;
	private String botPrefix;
	
	public CommandInfo(Message message) {
		this.message = message;
		this.guild = Optional.ofNullable(message.getGuild());
		this.guildMember = Optional.ofNullable(message.getMember());
		
		Setting defaultBotPrefix = NightBot.get().getGlobalSetting("CommandPrefix");
		
		if(this.guild.isPresent()) {
			ConnectedGuild g = NightBot.get().getConnectedGuild(this.guild.get());
			
			Setting language = g.getSettings().get("Language");
			Setting botPrefix = g.getSettings().get("CommandPrefix");
			this.botPrefix = (botPrefix != null ? botPrefix.getValue() : defaultBotPrefix.getValue());
			
			this.lang = NightBot.get().getLanguage((language != null ? language.getValue() : "en_EN"));
		} else {
			this.lang = NightBot.get().getDefaultLanguage();
		}
		
		String[] params = message.getContentRaw().split(" ");
		
		ArrayList<String> paramsList = new ArrayList<String>(Arrays.asList(params));
		
		ArrayList<String> args = new ArrayList<String>();
		
		this.command = paramsList.get(0).substring(this.botPrefix.length());
		paramsList.remove(0);
		
		boolean argumentParsing = false;
		
		for(int i = 0; i < paramsList.size(); i++) {
			String param = paramsList.get(i);
			
			if(param.startsWith("\"")) {
				argumentParsing = true;
				
				ParamsLoop:
				for(int j = i + 1; j < paramsList.size(); j++) {
					String secondParam = paramsList.get(j);
					
					if(secondParam.endsWith("\"")) {
						param += " " + secondParam.substring(0, secondParam.length() - 1);
						param = param.substring(1);
						
						argumentParsing = false;
						i = j; //to set counter to another value
						break ParamsLoop;
					} else {
						param += " " + secondParam;
					}
				}
				
			}
			
			if(argumentParsing) {
				throw new IllegalArgumentException("Command argument is starting with \" but isn't ending with \"");
			}
			
			args.add(param);
		}
		
		this.arguments = args.toArray(new String[args.size()]);
	}
	
	public Message getMessage() {
		return message;
	}
	
	public Optional<Guild> getGuild() {
		return guild;
	}
	
	public Optional<Member> getGuildMember() {
		return guildMember;
	}
	
	@HostAccess.Export
	public User getAuthor() {
		return this.message.getAuthor();
	}
	
	public String[] getArguments() {
		return arguments;
	}
	
	public String getCommand() {
		return command;
	}

	@HostAccess.Export
	public MessageChannel getChannel() {
		return this.message.getChannel();
	}
	
	public Language getLang() {
		return lang;
	}
	
	public String getBotPrefix() {
		return botPrefix;
	}
	
	public void setGuild(Optional<Guild> guild) {
		this.guild = guild;
	}
	
	public void setGuildMember(Optional<Member> guildMember) {
		this.guildMember = guildMember;
	}
	
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void setBotPrefix(String botPrefix) {
		this.botPrefix = botPrefix;
	}
	
}
