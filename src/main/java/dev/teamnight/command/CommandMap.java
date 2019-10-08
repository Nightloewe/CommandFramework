package dev.teamnight.command;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import dev.teamnight.command.utils.BotEmbedBuilder;
import dev.teamnight.command.utils.PermissionUtil;

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
				
//				if(!success) {
//					HelpModule helpModule = (HelpModule) NightBot.get().getModules().get("Help");
//					MessageEmbed embed = helpModule.getCommandHelp(name, info.getGuild().get());
//					info.getChannel().sendMessage(embed).queue();
//				}
				
				ctx.getCmdFramework().getListeners().forEach((ICommandListener) -> {
					new Thread(() -> {
						ICommandListener.onCommand(ctx, success);
					}).start();
				});
				ctx.getCmdFramework().logger().info("Executed Command (t: " + usedTime + "ms) " + ctx.getCommand() 
					+ "\nAuthor: " + ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator()
					+ "\nArgs: " + String.join(" ", ctx.getArguments())
					+ (ctx.getGuild().isPresent() ? "\nGuild Id: " + ctx.getGuild().get().getId() : ""));
				return true;
			}
		}
		ctx.getCmdFramework().logger().info("Command " + ctx.getCommand() + " is not found!"
				+ "\nAuthor: " + ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator()
				+ (ctx.getGuild().isPresent() ? "\nGuild Id: " + ctx.getGuild().get().getId() : ""));
		return false;
	}

}
