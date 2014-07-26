package com.lvl6.mobsters.conditions.dynamo;

import java.util.Collections;
import java.util.List;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.lvl6.mobsters.conditions.framework.AbstractTypedConditionFactory;
import com.lvl6.mobsters.conditions.framework.IBooleanConditionFactory;

class BooleanConditionFactory
	extends AbstractTypedConditionFactory<Condition>
	implements IBooleanConditionFactory<Condition>
{
    private final static List<AttributeValue> TRUE = 
    	Collections.singletonList(
    		new AttributeValue()
    			.withN("1"));
    
    private final static List<AttributeValue> FALSE = 
    	Collections.singletonList(
    		new AttributeValue()
    			.withN("0"));
    
    
	@Override
	public void isTrue() {
		handleSuccess(
			new Condition()
				.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(TRUE));
	}

	@Override
	public void isFalse() {
		handleSuccess(
			new Condition()
				.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(FALSE));
	}
}
