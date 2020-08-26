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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import dev.teamnight.command.Arguments;
import dev.teamnight.command.IArgumentProcessor;

/**
 * An extended argument processor than the 
 * DefaultArgumentProcessor that also processes
 * named arguments defined using `name=arg`.
 * 
 * A command may define named arguments as optional
 * arguments.
 * 
 * @author Jonas Müller
 */
public class NamedArgumentProcessor implements IArgumentProcessor {

	@Override
	public Arguments process(String messageRaw) {
		ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(messageRaw.split(" ")));
		argsList.remove(0);
		
		return this.process(argsList);
	}

	@Override
	public Arguments process(List<String> args) {
		ArrayList<String> arguments = new ArrayList<String>();
		HashMap<String, String> namedArguments = new HashMap<String, String>();
		
		boolean argumentParsing = false;
		String namedArg = null;
		
		Pattern namedArgPattern = Pattern.compile("^[A-Za-z0-9]+=.+$");
		
		for(int i = 0; i < args.size(); i++) {
			String param = args.get(i);
			
			if(namedArgPattern.matcher(param).matches()) {
				namedArg = param.split("=")[0];
			}
				
			if(param.startsWith("\"")) {
				argumentParsing = true;
				
				ParamsLoop:
				for(int j = i + 1; j < args.size(); j++) {
					String secondParam = args.get(j);
					
					if(secondParam.endsWith("\"")) {
						param += " " + secondParam.substring(0, secondParam.length() - 1);
						param = param.substring(1);
						
						argumentParsing = false;
						i = j; //to set counter to another value
						break ParamsLoop;
					} else {
						param += " " + secondParam;
					}
				}
			}
			
			if(argumentParsing) {
				throw new IllegalArgumentException("Command argument is starting with \" but isn't ending with \"");
			}
			
			if(namedArg != null) {
				namedArguments.put(namedArg, param);
				namedArg = null;
			} else {
				arguments.add(param);
			}
		}
		
		return new Arguments(arguments.toArray(new String[arguments.size()]), namedArguments);
	}

}
