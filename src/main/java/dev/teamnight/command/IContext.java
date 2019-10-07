package dev.teamnight.command;

import java.util.Optional;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public interface IContext {

	public CommandFramework getCmdFramework();
	
	public Optional<Guild> getGuild();
	
	public Optional<Member> getGuildMember();
	
	public Message getMessage();
	
	public String getCommand();
	
	public String[] getArguments();
	
	public String getPrefix();
}
