package dev.teamnight.command.standard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import dev.teamnight.command.Arguments;
import dev.teamnight.command.IArgumentProcessor;

public class DefaultArgumentProcessor implements IArgumentProcessor {

	@Override
	public Arguments process(String messageRaw) {
		List<String> argsList = Arrays.asList(messageRaw.split(" "));
		argsList.remove(0);
		
		return this.process(argsList);
	}

	@Override
	public Arguments process(List<String> args) {
		ArrayList<String> arguments = new ArrayList<String>();
		
		boolean argumentParsing = false;
		
		for(int i = 0; i < arguments.size(); i++) {
			String param = arguments.get(i);
			
			if(param.startsWith("\"")) {
				argumentParsing = true;
				
				ParamsLoop:
				for(int j = i + 1; j < arguments.size(); j++) {
					String secondParam = arguments.get(j);
					
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
			
			args.add(param);
		}
		
		return new Arguments(args.toArray(new String[args.size()]), new HashMap<String, String>());
	}

}
