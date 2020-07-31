package dev.teamnight.command;

public abstract class Condition {
	
	protected IContext ctx;
	
	public Condition(IContext ctx) {
		this.ctx = ctx;
	}
	
	public abstract boolean canExecute();
	
	public abstract String errorMessage();
	
}
