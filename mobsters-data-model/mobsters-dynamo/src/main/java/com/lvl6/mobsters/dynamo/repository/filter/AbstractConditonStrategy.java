package com.lvl6.mobsters.dynamo.repository.filter;

import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;

import com.amazonaws.services.dynamodbv2.model.Condition;

abstract class AbstractConditonStrategy implements IConditionStrategy, IConditionFactoryContext,
IBooleanConditionBuilder, IIntConditionBuilder {
	
	private final IConditionFactoryState NO_CURRENT_FIELD_STATE = new ConditionFactoryNoFieldState(this);
	private final IConditionFactoryState BOOLEAN_FIELD_STATE = new ConditionFactoryBooleanState(this);
	private final IConditionFactoryState INT_FIELD_STATE = new ConditionFactoryIntState(this);

	private IConditionFactoryState state;
	private String fieldName;
	
	protected AbstractConditonStrategy() {
		state = NO_CURRENT_FIELD_STATE;
	}

	/* 
	 * IConditonStrategy interface -- Used by Repository-associated FilterBuilder implementations to activate its
	 *                                IConditionFactoryState supporting the I***ConditionBuilder interface a Director
	 *                                argument's apply() method is expecting to be given as its sole argument.
	 */
	@Override
	public final void filterIntField(String fieldName, Director<IIntConditionBuilder> director) {
		this.fieldName = fieldName;
		this.state = INT_FIELD_STATE;

		director.apply(this);
		checkState(this.state == NO_CURRENT_FIELD_STATE, "No field condition was added for %s", fieldName);
	}

	@Override
	public final void filterBooleanField(String fieldName, Director<IBooleanConditionBuilder> director) {
		this.fieldName = fieldName;
		this.state = BOOLEAN_FIELD_STATE;

		director.apply(this);
		checkState(this.state == NO_CURRENT_FIELD_STATE, "No field condition was added for %s", fieldName);
	}
	
	/** IFactoryStateCallback -- Methods that enable the various factory state delegates to submit exactly  
	 *                           one Condition once activated.  Concrete handling of the Condition is delegated
	 *                           to a lightweight subclass. */
	@Override
	public final void addCondition( final Condition newCondition ) {
		checkState(fieldName != null, "No field has been targetted since latest condition to have been added");
	
		handleAddCondition( fieldName, newCondition );
		state = NO_CURRENT_FIELD_STATE;
		fieldName = null;
	}
	
	protected abstract void handleAddCondition( final String fieldName, final Condition newCondition );

	@Override
	public final void noCondition( ) {
		checkState(fieldName != null, "No field has been targetted since latest condition to have been added");

		state = NO_CURRENT_FIELD_STATE;
		fieldName = null;
	}

	/** I<Type>ConditionBuilder -- Director-consumed interfaces for each supported field type.  Delegates to the
	 *                             currently active ICondityionFactoryState, which uses IConditionFactoryContext 
	 *                             to both submit a Condition and return this object to the no-field state *****/

	/** IBooleanConditionBuilder -- ConditionFactoryBooleanState delegation for boolean fields ******/

	@Override
	public final void isTrue() {
		state.isTrue();
	}

	@Override
	public final void isFalse() {
		state.isFalse();
	}

	@Override
	public final void is(boolean truthiness) {
		state.is(truthiness);
	}

	@Override
	public final void isNot(boolean falsiness) {
		state.isNot(falsiness);
	}

	/** IIntConditionBuilder -- ConditionFactoryIntState delegation for int fields ********************/

	@Override
	public final void in(Collection<Integer> matchValues) {
		state.in(matchValues);
	}

	@Override
	public final void in(int... matchValues) {
		state.in(matchValues);
	}

	@Override
	public final void is(int value) {
		state.is(value);
	}

	@Override
	public final void isNot(int value) {
		state.isNot(value);
	}

	@Override
	public final void greaterThan(int value) {
		state.greaterThan(value);
	}

	@Override
	public final void greaterThanOrEqualTo(int value) {
		state.greaterThanOrEqualTo(value);
	}

	@Override
	public final void lessThan(int value) {
		state.lessThan(value);
	}

	@Override
	public final void lessThanOrEqualTo(int value) {
		state.lessThanOrEqualTo(value);
	}

	@Override
	public final void between(int lower, int upper) {
		state.between(lower, upper);
	}
}
