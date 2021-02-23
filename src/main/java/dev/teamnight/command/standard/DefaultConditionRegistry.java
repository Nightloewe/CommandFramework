package dev.teamnight.command.standard;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import dev.teamnight.command.Condition;
import dev.teamnight.command.IConditionRegistry;

public class DefaultConditionRegistry implements IConditionRegistry {

	private Map<Class<?>, Condition> conditions = new ConcurrentHashMap<Class<?>, Condition>();
	
	@Override
	public Collection<Condition> getConditions() {
		return Collections.unmodifiableCollection(this.conditions.values());
	}

	@Override
	public Condition getCondition(Class<?> condition) {
		if(!condition.isAssignableFrom(Condition.class)) {
			throw new IllegalArgumentException("condition has to be of type dev.teamnight.command.Condition");
		}
		if(!this.conditions.containsKey(condition)) {
			Condition cond = null;
			try {
				cond = (Condition) condition.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
			
			this.conditions.put(condition, cond);
		}
		
		return this.conditions.get(condition);
	}
	
	@Override
	public void registerCondition(Condition condition) {
		Objects.requireNonNull(condition, "Condition can not be null");
		
		conditions.put(condition.getClass(), condition);
	}

	@Override
	public int size() {
		return this.conditions.size();
	}

	@Override
	public void clear() {
		this.conditions.clear();
	}

}
