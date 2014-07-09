package com.lvl6.mobsters.dynamo.repository.filter;

import java.util.Collections;
import java.util.List;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

public class ConditionFactoryBooleanState extends ConditionFactoryAbstractState {
    private final static List<AttributeValue> TRUE = 
    	Collections.singletonList(
    		new AttributeValue()
    			.withN("1"));
    
    private final static List<AttributeValue> FALSE = 
    	Collections.singletonList(
    		new AttributeValue()
    			.withN("0"));
    
    ConditionFactoryBooleanState( final IConditionFactoryContext contextCallback ) {
    	super(contextCallback);
    }
    
	@Override
	public void isTrue() {
		contextCallback.addCondition(
			new Condition()
				.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(TRUE));
	}

	@Override
	public void isFalse() {
		contextCallback.addCondition(
			new Condition()
				.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(FALSE));
	}

	@Override
	public void is(boolean truthiness) {
		contextCallback.addCondition(
			new Condition()
				.withComparisonOperator(ComparisonOperator.EQ)
				.withAttributeValueList(truthiness ? TRUE : FALSE));
	}

	@Override
	public void isNot(boolean falsiness) {
		contextCallback.addCondition(
			new Condition()
				.withComparisonOperator(ComparisonOperator.NE)
				.withAttributeValueList(falsiness ? TRUE : FALSE));
	}
}
