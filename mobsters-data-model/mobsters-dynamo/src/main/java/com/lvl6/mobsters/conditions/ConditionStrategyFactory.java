package com.lvl6.mobsters.conditions;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.lvl6.mobsters.conditions.dynamo.QueryFilterConditionStrategy;
import com.lvl6.mobsters.conditions.dynamo.ScanFilterConditionStrategy;

public final class ConditionStrategyFactory {
    private ConditionStrategyFactory() 
    { }
    
    public static final QueryFilterConditionStrategy getQueryFilterBuilder(
    	final DynamoDBQueryExpression<?> unfilteredQuery) 
    {
    	return new QueryFilterConditionStrategy(unfilteredQuery);
    }
    
    public static final ScanFilterConditionStrategy getScanFilterConditionStrategy(
    	final DynamoDBScanExpression unfilteredScan)
    {
    	return new ScanFilterConditionStrategy(unfilteredScan);
    }
    
    /**
     * TODO For portability--Predicates are lambdas that apply the requested conditional checks to an
     * input argument object of matching type and then return a boolean result.
     * @return
    public static final PredicateBuilderStrategy getPredicateConditionStrategy()
     */
}
