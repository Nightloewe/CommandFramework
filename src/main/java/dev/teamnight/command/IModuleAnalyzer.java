package dev.teamnight.command;

@FunctionalInterface
public interface IModuleAnalyzer {

	IRegisteredModule analyze(IModule theModule);
	
}
