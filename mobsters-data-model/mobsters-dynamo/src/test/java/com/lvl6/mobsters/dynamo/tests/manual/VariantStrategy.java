package com.lvl6.mobsters.dynamo.tests.manual;

public interface VariantStrategy<P, C>
{
	String getNextParent( int expectedChildCount );

	ChildDataAttrs addNextChild();

	void saveChildren();

	BaseParentChildRepository<P, C> getRepository();
}
