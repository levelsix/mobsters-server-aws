package com.lvl6.mobsters.conditions.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.lvl6.mobsters.conditions.framework.AbstractConditionsBuilder;

public final class ScanFilterConditionStrategy 
	extends AbstractConditionsBuilder<Condition>
{
	private final DynamoDBScanExpression partialScan;
	
	public ScanFilterConditionStrategy(DynamoDBScanExpression partialScan) {
		super(
			new IntConditionFactory(),
			new StringConditionFactory(),
			new BooleanConditionFactory()
		);
		
		this.partialScan = partialScan;	
	}

	/** IStateCallback -- Method that allow the various state delegates to submit a Condition and 
	 *                    return context to the no field state */
	@Override
	protected void handleAddCondition( final String fieldName, final Condition newCondition ) {
		partialScan.withFilterConditionEntry(fieldName, newCondition);
	}
}
