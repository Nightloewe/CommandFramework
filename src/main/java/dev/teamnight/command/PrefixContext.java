package dev.teamnight.command;

import java.text.MessageFormat;
import java.util.Optional;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class PrefixContext implements IContext {

	private CommandFramework cf;
	
	private Optional<Guild> guild;
	private Optional<Member> guildMember;
	private Message message;
	private String prefix;
	
	private JDA jda;
	
	public PrefixContext(CommandFramework framework, Message message) {
		this.cf = framework;
		this.message = message;
		this.jda = message.getJDA();
		this.guild = Optional.ofNullable(message.getGuild());
		this.guildMember = Optional.ofNullable(message.getMember());
		
		this.prefix = this.cf.getPrefixProvider().providePrefix(this);
	}
	
	@Override
	public CommandFramework getCmdFramework() {
		return this.cf;
	}

	@Override
	public JDA getJDA() {
		return this.jda;
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
		throw new UnsupportedOperationException("PrefixContext does not provide the command");
	}

	@Override
	public String[] getArguments() {
		throw new UnsupportedOperationException("PrefixContext does not provide args");
	}

	@Override
	public String getPrefix() {
		return this.prefix;
	}

	@Override
	public String getLocalizedString(String key, Object... args) {
		try {
			return MessageFormat.format(this.cf.getLangProvier().provideString(this.guild.get(), key), args);
		} catch(IllegalArgumentException e) {
			return "IllegalArgumentException: " + e.getMessage();
		}
	}

	@Override
	public void printHelp() {
		this.getChannel().sendMessage(this.cf.getHelpProvider().provideHelp(this)).queue();
	}

}
