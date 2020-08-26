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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import dev.teamnight.command.ICommand;
import dev.teamnight.command.ICommandMap;
import dev.teamnight.command.IContext;
import dev.teamnight.command.utils.BotEmbedBuilder;
import dev.teamnight.command.utils.PermissionUtil;

/**
 * The default implementation of the Command Map
 * 
 * @author Jonas Müller
 */
public class CommandMap implements ICommandMap {

	private LinkedHashMap<String, ICommand> commands = new LinkedHashMap<String, ICommand>();
	
	@Override
	public void registerAll(List<ICommand> commands) {
		for(ICommand command : commands) {
			this.commands.put(command.getName(), command);
			
			for(String alias : command.getAliases()) {
				this.commands.put(alias, command);
			}
		}
	}

	@Override
	public void register(ICommand command) {
		this.commands.put(command.getName(), command);
		
		for(String alias : command.getAliases()) {
			this.commands.put(alias, command);
		}
	}

	@Override
	public ICommand getCommand(String name) {
		return this.commands.get(name);
	}
	
	public HashMap<String, ICommand> getCommands() {
		return commands;
	}

	@Override
	public boolean dispatchCommand(IContext ctx) {
		ICommand command = null;
		
		for(String name : this.commands.keySet()) {
			if(name.equalsIgnoreCase(ctx.getCommand())) {
				command = this.commands.get(name);
				
				if(PermissionUtil.isBlacklisted(ctx, false)) {
					new BotEmbedBuilder().setDescription(ctx.getLocalizedString("USER_BLACKLISTED", ctx.getAuthor().getAsMention())).withErrorColor().sendMessage(ctx.getChannel());
					return true;
				}
				
				long preTime = System.currentTimeMillis();
				boolean success = command.execute(ctx);
				long usedTime = System.currentTimeMillis() - preTime;
				
				if(!success) {
					ctx.getChannel().sendMessage(ctx.getCmdFramework().getHelpProvider().provideHelp(ctx)).queue();
				}
				
				ctx.getCmdFramework().getListeners().forEach((ICommandListener) -> {
					new Thread(() -> {
						ICommandListener.onCommand(ctx, success);
					}).start();
				});
				ctx.getCmdFramework().logger().info("Executed Command (t: " + usedTime + "ms) \"" + ctx.getCommand() + " " + String.join(" ", ctx.getArguments())
					+ "\" by " + ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator()
					+ (ctx.getGuild().isPresent() ? " in Guild " + ctx.getGuild().get().getId() : ""));
				return true;
			}
		}
		ctx.getCmdFramework().logger().info("Command " + ctx.getCommand() + " was not found. Tried by "
				+ " " + ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator()
				+ (ctx.getGuild().isPresent() ? " in Guild " + ctx.getGuild().get().getId() : ""));
		return false;
	}

}
