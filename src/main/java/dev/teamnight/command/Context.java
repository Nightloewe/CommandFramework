package dev.teamnight.command;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Context implements IContext {

	private CommandFramework cf;
	
	private Optional<Guild> guild;
	private Optional<Member> guildMember;
	private Message message;
	private String[] arguments;
	private String command;
	private String botPrefix;
	
	public Context(CommandFramework framework, Message message) {
		this.cf = framework;
		this.message = message;
		this.guild = Optional.ofNullable(message.getGuild());
		this.guildMember = Optional.ofNullable(message.getMember());
		
		this.botPrefix = this.cf.getPrefixProvider().providePrefix(this);
		
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
	
	@Override
	public CommandFramework getCmdFramework() {
		return this.cf;
	}

	@Override
	public Optional<Guild> getGuild() {
		return this.guild;
	}

	@Override
	public Optional<Member> getGuildMember() {
		return this.guildMember;
	}

	@Override
	public Message getMessage() {
		return this.message;
	}

	@Override
	public User getAuthor() {
		return this.message.getAuthor();
	}

	@Override
	public MessageChannel getChannel() {
		return this.message.getChannel();
	}

	@Override
	public String getCommand() {
		return this.command;
	}

	@Override
	public String[] getArguments() {
		return this.arguments;
	}

	@Override
	public String getPrefix() {
		return this.botPrefix;
	}

	@Override
	public String getLocalizedString(String key, Object... args) {
		try {
			return MessageFormat.format(this.cf.getLangProvier().provideString(this.guild.get(), key), args);
		} catch(IllegalArgumentException e) {
			return "IllegalArgumentException: " + e.getMessage();
		}
	}

	
	
}
