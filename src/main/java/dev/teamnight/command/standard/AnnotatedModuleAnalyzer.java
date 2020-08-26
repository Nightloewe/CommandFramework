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

import dev.teamnight.command.AnnotatedCommand;
import dev.teamnight.command.Condition;
import dev.teamnight.command.IModule;
import dev.teamnight.command.IModuleAnalyzer;
import dev.teamnight.command.IRegisteredModule;
import dev.teamnight.command.annotations.Command;
import dev.teamnight.command.annotations.Hidden;
import dev.teamnight.command.annotations.Requires;

/**
 * The default implementation of the module analyzer
 * analyzing some annotations and registering them.
 * 
 * @author Jonas Müller
 */
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
