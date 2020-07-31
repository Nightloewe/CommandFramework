package dev.teamnight.command;

public interface IArgumentProcessor {

	public Arguments process(String messageRaw);
	
	public Arguments process(String[] message);
	
}
