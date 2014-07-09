package com.lvl6.mobsters.dynamo.repository.filter;

import com.amazonaws.services.dynamodbv2.model.Condition;

interface IConditionFactoryContext {
    void addCondition( Condition newCondition );
    
    void noCondition();
}
