package com.lvl6.mobsters.conditions.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.lvl6.mobsters.conditions.framework.AbstractConditionsBuilder;

public final class QueryFilterConditionStrategy 
	extends AbstractConditionsBuilder<Condition> 
{
	private final DynamoDBQueryExpression<?> partialQuery;
	
	public QueryFilterConditionStrategy(DynamoDBQueryExpression<?> partialQuery) {
		super(
			new IntConditionFactory(),
			new StringConditionFactory(),
			new BooleanConditionFactory()
		);
		
		this.partialQuery = partialQuery;	
	}

	/** Template method for handling the final product of a typed condition builder interaction. */
	@Override
	protected void handleAddCondition( final String fieldName, final Condition newCondition ) {
		partialQuery
			.withQueryFilterEntry(fieldName, newCondition)
			.withConsistentRead(true);
	}
}
