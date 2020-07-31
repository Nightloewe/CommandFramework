package dev.teamnight.command;

import java.util.List;

public interface IArgumentProcessor {

	public Arguments process(String messageRaw);
	
	public Arguments process(List<String> args);
	
}
