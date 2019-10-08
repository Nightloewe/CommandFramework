package dev.teamnight.command.standard;

import dev.teamnight.command.IContext;
import dev.teamnight.command.PrefixProvider;

public class DefaultPrefixProvider implements PrefixProvider {
	
	private String prefix = ".";
	
	public DefaultPrefixProvider(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String providePrefix(IContext ctx) {
		return this.prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
