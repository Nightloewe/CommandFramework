package dev.teamnight.command;

import java.util.Collection;
import java.util.List;

public interface IConditionRegistry {

	public Collection<Condition> getConditions();
	
	public Condition getCondition(Class<?> condition);
	
	public void registerCondition(Condition condition);
	
	public int size();
	
	public void clear();
}
