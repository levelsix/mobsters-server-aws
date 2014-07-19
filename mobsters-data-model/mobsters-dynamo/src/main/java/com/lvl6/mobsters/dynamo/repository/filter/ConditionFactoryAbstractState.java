package com.lvl6.mobsters.dynamo.repository.filter;

import java.util.Collection;

abstract class ConditionFactoryAbstractState implements IConditionFactoryState {
	protected final IConditionFactoryContext contextCallback;

	protected ConditionFactoryAbstractState(final IConditionFactoryContext contextCallback) {
		this.contextCallback = contextCallback;
	}

	@Override
	public void isTrue() {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void isFalse() {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void is(boolean truthiness) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void isNot(boolean falsiness) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void in(Collection<Integer> matchValues) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void in(int... matchValues) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void is(int value) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void isNot(int value) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void greaterThan(int value) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void greaterThanOrEqualTo(int value) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void lessThan(int value) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void lessThanOrEqualTo(int value) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}

	@Override
	public void between(int lower, int upper) {
		contextCallback.noCondition();
		throw new UnsupportedOperationException();
	}
}
