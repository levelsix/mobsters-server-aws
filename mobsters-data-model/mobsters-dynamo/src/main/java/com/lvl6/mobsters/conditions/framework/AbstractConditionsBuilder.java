package com.lvl6.mobsters.conditions.framework;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Preconditions;
import com.lvl6.mobsters.conditions.Director;
import com.lvl6.mobsters.conditions.IBooleanConditionBuilder;
import com.lvl6.mobsters.conditions.IConditionsBuilder;
import com.lvl6.mobsters.conditions.IIntConditionBuilder;
import com.lvl6.mobsters.conditions.IStringConditionBuilder;

/**
 * 
 * @author jheinnic
 *
 * @param <T>
 */
public abstract class AbstractConditionsBuilder<T>
	implements IConditionsBuilder
{
	private final IIntConditionBuilder intFieldState;
	private final IStringConditionBuilder stringFieldState;
	private final IBooleanConditionBuilder booleanFieldState;
	private String fieldName;
	
	protected AbstractConditionsBuilder(
		IIntConditionBuilder intFieldState,
		IStringConditionBuilder stringFieldState, 
		IBooleanConditionBuilder booleanFieldState
	) {
		this.fieldName = null;
		this.intFieldState = intFieldState;
		this.stringFieldState = stringFieldState;
		this.booleanFieldState = booleanFieldState;
	}

	/* 
	 * IConditonStrategy interface -- Used by Repository-associated FilterBuilder implementations to activate its
	 *                                IConditionFactoryState supporting the I***ConditionBuilder interface a Director
	 *                                argument's apply() method is expecting to be given as its sole argument.
	 */

	@Override
	public final void filterIntField(String fieldName, Director<IIntConditionBuilder> director) {
		this.fieldName = fieldName;
		director.apply(intFieldState);
		checkState(this.fieldName == null, "No field condition was added for %s", fieldName);
	}

	@Override
	public final void filterBooleanField(String fieldName, Director<IBooleanConditionBuilder> director) {
		this.fieldName = fieldName;
		director.apply(booleanFieldState);
		checkState(this.fieldName == null, "No field condition was added for %s", fieldName);
	}
	
	@Override
	public final void filterStringField(String fieldName, Director<IStringConditionBuilder> director) {
		this.fieldName = fieldName;
		director.apply(stringFieldState);
		checkState(this.fieldName == null, "No field condition was added for %s", fieldName);
	}
	
	/** IFactoryStateCallback -- Methods that enable the various factory state delegates to submit exactly  
	 *                           one Condition once activated.  Concrete handling of the Condition is delegated
	 *                           to a lightweight subclass. */
	final void addCondition( final T newCondition ) {
		checkState(fieldName != null, "No field has been targetted since latest condition to have been added");
	
		handleAddCondition( fieldName, newCondition );

		fieldName = null;
	}
	
	protected abstract void handleAddCondition( final String fieldName, final T newCondition );

	final void noCondition( ) {
		Preconditions.checkNotNull(fieldName, "No field has been targetted since latest condition to have been added");

		final String fieldStr = fieldName;
		fieldName = null;

		throw new UnsupportedOperationException(
			String.format(
				"Implementation does not support requested condition on current field.  fieldName=%s", 
				fieldStr));
	}
}
