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
package dev.teamnight.command.standard;

import java.text.MessageFormat;
import java.util.Optional;

import dev.teamnight.command.Arguments;
import dev.teamnight.command.CommandFramework;
import dev.teamnight.command.IContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * The default implementation of the Command Context
 * 
 * @author Jonas Müller
 */
public class Context implements IContext {

	private CommandFramework cf;
	
	private Optional<Guild> guild;
	private Optional<Member> guildMember;
	private Message message;
	private Arguments arguments;
	private String command;
	private String botPrefix;

	private JDA jda;
	
	public Context(PrefixContext prefixContext) {
		this.cf = prefixContext.getCmdFramework();
		this.message = prefixContext.getMessage();
		this.jda = message.getJDA();
		this.guild = Optional.ofNullable(message.getGuild());
		this.guildMember = Optional.ofNullable(message.getMember());
		
		this.botPrefix = prefixContext.getPrefix();
		
		String[] args = message.getContentRaw().split(" ");
		
		if(args.length < 1) {
			throw new IllegalArgumentException("Message does not contain command string or anything else.");
		}
		
		this.command = args[0].substring(this.botPrefix.length());
		this.arguments = this.cf.getArgumentProcessor().process(message.getContentRaw());
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
		return this.command;
	}
	
	@Override
	public Arguments getArguments() {
		return this.arguments;
	}

	@Override
	public String[] getArgumentsRaw() {
		return this.arguments.toList().toArray(new String[this.arguments.size()]);
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

	@Override
	public void printHelp() {
		this.getChannel().sendMessage(this.cf.getHelpProvider().provideHelp(this)).queue();
	}
	
}
