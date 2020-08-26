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
package dev.teamnight.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark any method that is the executor of a class.
 * 
 * @author Jonas Müller
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

	public String name();
	
	public String[] usage() default {};
	
	public String description();
	
	public String[] aliases() default {};
	
	/**
	 * @deprecated This part of the interface will be removed in future, as the SubModule class is the new successor to the Category. \n
	 * or create a new class for the SubModule instead. Please register your submodules in your Module, the interface will provide a default method for this.
	 * @return Category of the Command
	 */
	public String category() default "";
	
}
