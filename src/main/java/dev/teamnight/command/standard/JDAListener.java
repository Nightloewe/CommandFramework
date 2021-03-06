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

import dev.teamnight.command.CommandFramework;
import dev.teamnight.command.IContext;
import dev.teamnight.command.utils.BotEmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * JDA Listener to listen for commands and
 * prepare some things for the Command Framework.
 * 
 * @author Jonas Müller
 */
public class JDAListener extends ListenerAdapter {

	private CommandFramework cf;

	public JDAListener(CommandFramework commandFramework) {
		this.cf = commandFramework;
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		for(long id : cf.getBotOwners()) {
			User user = event.getJDA().retrieveUserById(id).complete();
			
			cf.getBotOwnerList().add(user);
		}
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		if(this.cf.getBlockedGuilds().contains(event.getGuild().getIdLong())) {
			event.getGuild().getOwner().getUser().openPrivateChannel().queue((ch) -> {
				new BotEmbedBuilder().setDescription(this.cf.getLangProvier().provideString(event.getGuild(), "GUILD_BLACKLISTED")).withErrorColor().sendMessage(ch);
				event.getGuild().leave().queue();
			}, (e) -> {
				event.getGuild().leave().queue();
			});
		}
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		//Prevent Bots from issueing commands if bots aren't allowed
		if(e.getAuthor().isBot() && !this.cf.isAllowBots())
			return;
		
		//Only allow messages from guild channels or DMs
		if(!(e.isFromType(ChannelType.TEXT)||e.isFromType(ChannelType.PRIVATE)))
			return;
		
		if(e.isFromType(ChannelType.PRIVATE) && !this.cf.isAllowDM()) {
			new BotEmbedBuilder().setDescription(this.cf.getLangProvier().provideString(null, "NO_DM")).withErrorColor().sendMessage(e.getChannel());
			return;
		}
		
		IContext ctx = new PrefixContext(this.cf, e.getMessage());
		
		String commandPrefix = this.cf.getPrefixProvider().providePrefix(ctx);
		
		if(e.getMessage().getContentRaw().startsWith(commandPrefix) || 
				(this.cf.isAllowMentionCmd() && e.getMessage().getContentRaw().startsWith(e.getJDA().getSelfUser().getAsMention()))) {
			ctx = new Context((PrefixContext) ctx);
			this.cf.getCommandMap().dispatchCommand(ctx);
		}
	}

}
