package dev.teamnight.command.standard;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dev.teamnight.command.AnnotatedCommand;
import dev.teamnight.command.Condition;
import dev.teamnight.command.IModule;
import dev.teamnight.command.IModuleAnalyzer;
import dev.teamnight.command.IRegisteredModule;
import dev.teamnight.command.annotations.Command;
import dev.teamnight.command.annotations.Hidden;
import dev.teamnight.command.annotations.Requires;

public class AnnotatedModuleAnalyzer implements IModuleAnalyzer {

	@Override
	public IRegisteredModule analyze(IModule theModule) {
		Class<?> theModuleClass = theModule.getClass();
		
		List<Requires> requires = new ArrayList<Requires>();
		
		for(Requires req : theModuleClass.getAnnotationsByType(Requires.class)) {
			if(!Condition.class.isAssignableFrom(req.value())) {
				throw new IllegalArgumentException("Requires must contain an implementation of dev.teamnight.command.Condition");
			}
			
			requires.add(req);
		}
		
		boolean isHidden = false;
		if(theModuleClass.isAnnotationPresent(Hidden.class)) {
			isHidden = true;
		}
		
		DefaultRegisteredModule module = new DefaultRegisteredModule(theModule, new ArrayList<>(), requires, isHidden);
		
		for(Method method : theModuleClass.getDeclaredMethods()) {
			if(method.isAnnotationPresent(Command.class)) {
				Command commandAnnotation = method.getAnnotation(Command.class);
				
				AnnotatedCommand command = new DefaultAnnotatedCommand(commandAnnotation.name(), commandAnnotation.usage(), commandAnnotation.description(), commandAnnotation.aliases(), null, method, theModule);
				command.getConditions().addAll(requires);
				
				module.getCommands().add(command);
			}
		}
		
		return module;
	}

}
