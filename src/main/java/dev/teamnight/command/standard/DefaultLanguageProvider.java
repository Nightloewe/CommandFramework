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

import dev.teamnight.command.LanguageProvider;
import net.dv8tion.jda.api.entities.Guild;

/**
 * A default implementation of the language provider
 * throwing an UnsupportedOperationException.
 * 
 * Don't use this defaults or don't use localization.
 * 
 * @author Jonas Müller
 */
public class DefaultLanguageProvider implements LanguageProvider {

	@Override
	public String provideString(Guild guild, String key) {
		throw new UnsupportedOperationException();
	}

}
