package com.lvl6.mobsters.dynamo.repository.filter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.Condition;

public final class QueryFilterConditionStrategy extends AbstractConditonStrategy {
	private final DynamoDBQueryExpression<?> partialQuery;
	
	public QueryFilterConditionStrategy(DynamoDBQueryExpression<?> partialQuery) {
		super();
		this.partialQuery = partialQuery;	
	}

	/** IStateCallback -- Method that allow the various state delegates to submit a Condition and 
	 *                    return context to the no field state */
	@Override
	protected void handleAddCondition( final String fieldName, final Condition newCondition ) {
		partialQuery
			.withQueryFilterEntry(fieldName, newCondition)
			.withConsistentRead(true);
	}
}
