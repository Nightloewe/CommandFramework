package dev.teamnight.command.standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.teamnight.command.ICommand;
import dev.teamnight.command.IModule;
import dev.teamnight.command.ITopLevelModule;
import dev.teamnight.command.annotations.Requires;

public class DefaultTopLevelModule extends DefaultModule implements ITopLevelModule {

	private List<IModule> subModules;
	
	public DefaultTopLevelModule(Class<?> moduleClass, String name, List<ICommand> commands, List<Requires> conditions) {
		this(moduleClass, name, commands, conditions, false);
	}
	
	public DefaultTopLevelModule(Class<?> moduleClass, String name, List<ICommand> commands, List<Requires> conditions, boolean isHidden) {
		super(moduleClass, name, commands, conditions, isHidden);
		this.subModules = new ArrayList<IModule>();
	}

	@Override
	public List<IModule> getSubModules() {
		return Collections.unmodifiableList(this.subModules);
	}
	
	@Override
	public void addSubModule(IModule module) {
		this.subModules.add(module);
	}

}
