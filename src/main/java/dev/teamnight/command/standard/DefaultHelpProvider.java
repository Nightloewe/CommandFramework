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

import dev.teamnight.command.HelpProvider;
import dev.teamnight.command.IContext;
import dev.teamnight.command.utils.BotEmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

/**
 * Default implementation of the help provider
 * providing no help.
 * 
 * @author Jonas Müller
 *
 */
public class DefaultHelpProvider implements HelpProvider {

	@Override
	public Message provideHelp(IContext ctx) {
		return new MessageBuilder(
				new BotEmbedBuilder().appendDescription("Failed executing command " + ctx.getCommand()).withErrorColor().build()
				).build();
	}

}
