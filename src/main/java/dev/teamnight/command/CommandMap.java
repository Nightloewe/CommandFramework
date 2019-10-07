package dev.teamnight.command;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
	public boolean dispatchCommand(CommandInfo info) {
		ICommand command = null;
		
		for(String name : this.commands.keySet()) {
			if(name.equalsIgnoreCase(info.getCommand())) {
				command = this.commands.get(name);
				
				if(PermissionUtil.isBlacklisted(info.getAuthor())) {
					new BotEmbedBuilder().setDescription(info.getAuthor().getAsMention() + " You are blacklisted by the bot. Please contact the bot owner for more information.").withErrorColor().sendMessage(info.getChannel());
					return true;
				}
				
				long preTime = System.currentTimeMillis();
				boolean success = command.execute(info);
				long usedTime = System.currentTimeMillis() - preTime;
				
//				if(!success) {
//					HelpModule helpModule = (HelpModule) NightBot.get().getModules().get("Help");
//					MessageEmbed embed = helpModule.getCommandHelp(name, info.getGuild().get());
//					info.getChannel().sendMessage(embed).queue();
//				}
				
				if(info.getGuild().isPresent())
					NightBot.get().getLogManager().logToChannel(info.getGuild().get(), EventType.COMMAND_EXECUTION, 
							new EmbedBuilder()
							.setTitle(info.getLang().TITLE_COMMAND_EXECUTED.format())
							.setDescription(info.getAuthor().getName() + "#" + info.getAuthor().getDiscriminator())
							.addField(info.getLang().COMMAND.format(), info.getMessage().getContentRaw(), false)
							.addField("Id", info.getMessage().getId(), false)
							.build());
				NightBot.logger().info("Executed Command (t: " + usedTime + "ms) " + info.getCommand() 
					+ "\nAuthor: " + info.getAuthor().getName() + "#" + info.getAuthor().getDiscriminator()
					+ "\nArgs: " + String.join(" ", info.getArguments())
					+ (info.getGuild().isPresent() ? "\nGuild Id: " + info.getGuild().get().getId() : ""));
				return true;
			}
		}
		NightBot.logger().info("Command " + info .getCommand() + " is not found!"
				+ "\nAuthor: " + info.getAuthor().getName() + "#" + info.getAuthor().getDiscriminator()
				+ (info.getGuild().isPresent() ? "\nGuild Id: " + info.getGuild().get().getId() : ""));
		return false;
	}

}
