package dev.teamnight.command.standard;

import dev.teamnight.command.HelpProvider;
import dev.teamnight.command.IContext;
import dev.teamnight.command.utils.BotEmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class DefaultHelpProvider implements HelpProvider {

	@Override
	public Message provideHelp(IContext ctx) {
		return new MessageBuilder(
				new BotEmbedBuilder().appendDescription("Failed executing command " + ctx.getCommand()).withErrorColor().build()
				).build();
	}

}
