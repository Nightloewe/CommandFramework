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

import java.util.Optional;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * An interface representing the Context where a command is executed.
 * @author Jonas Müller
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
	
	public Arguments getArguments();
	
	public String[] getArgumentsRaw();
	
	public String getPrefix();
	
	public String getLocalizedString(String key, Object...replacements);
	
	public void printHelp();
}
