package dev.teamnight.command;

import java.util.Optional;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * An interface representing the Context where a command is executed.
 * @author Jonas
 *
 */
public interface IContext {
	public CommandFramework getCmdFramework();
	
	public JDA getJDA();
	
	public Optional<Guild> getGuild();
	
	public Optional<Member> getGuildMember();
	
	public Message getMessage();
	
	public User getAuthor();
	
	public MessageChannel getChannel();
	
	public String getCommand();
	
	public String[] getArguments();
	
	public String getPrefix();
	
	public String getLocalizedString(String key, Object...replacements);
	
	public void printHelp();
}
