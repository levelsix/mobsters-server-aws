package com.lvl6.mobsters.conditions.framework;


public abstract class AbstractTypedConditionFactory<T>
	implements IConditionBuilderState<T>
{
	private AbstractConditionsBuilder<T> contextCallback;

	public final void init(final AbstractConditionsBuilder<T> contextCallback) {
		this.contextCallback = contextCallback;
	}

	protected void failNotImplemented() {
		contextCallback.noCondition();
	}
	
	protected void handleSuccess( T conditionBuilt ) {
		contextCallback.addCondition(conditionBuilt);
	}
}
