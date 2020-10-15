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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dev.teamnight.command.annotations.TopLevelModule;
import dev.teamnight.command.AnnotatedCommand;
import dev.teamnight.command.Condition;
import dev.teamnight.command.IModule;
import dev.teamnight.command.IModuleAnalyzer;
import dev.teamnight.command.annotations.Command;
import dev.teamnight.command.annotations.Hidden;
import dev.teamnight.command.annotations.Requires;
import dev.teamnight.command.annotations.SubModule;

/**
 * The default implementation of the module analyzer
 * analyzing some annotations and registering them.
 * 
 * @author Jonas Müller
 */
public class AnnotatedModuleAnalyzer implements IModuleAnalyzer {
	
	@Override
	public IModule analyze(Object theModule) {
		Class<?> theModuleClass = theModule.getClass();
		
		String name = null;
		
		TopLevelModule moduleAnno = null;
		SubModule subModuleAnno = null;
		
		//Check if the module is a top-level module or a submodule
		if(theModuleClass.isAnnotationPresent(TopLevelModule.class)) {
			moduleAnno = theModuleClass.getAnnotation(TopLevelModule.class);
			name = moduleAnno.name();
		} else if(theModuleClass.isAnnotationPresent(SubModule.class)) {
			subModuleAnno = theModuleClass.getAnnotation(SubModule.class);
			name = subModuleAnno.name();
		}
		
		//Collect all conditions that should be applied to all commands
		List<Requires> requires = new ArrayList<Requires>();
		
		for(Requires req : theModuleClass.getAnnotationsByType(Requires.class)) {
			if(!Condition.class.isAssignableFrom(req.value())) {
				throw new IllegalArgumentException("Requires must contain an implementation of dev.teamnight.command.Condition");
			}
			
			requires.add(req);
		}
		
		//Collect all conditions from baseModule
		if(subModuleAnno != null) {
			for(Requires req : subModuleAnno.baseModule().getAnnotationsByType(Requires.class)) {
				if(!Condition.class.isAssignableFrom(req.value())) {
					throw new IllegalArgumentException("Requires must contain an implementation of dev.teamnight.command.Condition");
				}
				
				requires.add(req);
			}
		}
		
		//Check if the module should be hidden
		boolean isHidden = false;
		if(theModuleClass.isAnnotationPresent(Hidden.class)) {
			isHidden = true;
		}
		
		//Create the module depending whether it is top-level or not
		DefaultModule module = null;
		
		if(moduleAnno != null) {
			module = new DefaultTopLevelModule(theModuleClass, name, new ArrayList<>(), requires, isHidden);
		} else {
			module = new DefaultModule(theModuleClass, name, new ArrayList<>(), requires, isHidden);
		}
		
		//Search for all commands in the module class
		for(Method method : theModuleClass.getDeclaredMethods()) {
			if(method.isAnnotationPresent(Command.class)) {
				Command commandAnnotation = method.getAnnotation(Command.class);
				
				AnnotatedCommand command = new DefaultAnnotatedCommand(commandAnnotation.name(), commandAnnotation.usage(), commandAnnotation.description(), commandAnnotation.aliases(), null, name, method, theModule);
				command.getConditions().addAll(requires);
				
				module.getCommands().add(command);
			}
		}
		
		return module;
	}

}
