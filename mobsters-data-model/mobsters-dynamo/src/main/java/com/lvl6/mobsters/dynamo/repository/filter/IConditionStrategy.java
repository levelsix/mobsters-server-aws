package com.lvl6.mobsters.dynamo.repository.filter;

public interface IConditionStrategy {
    void filterIntField(String fieldName, Director<IIntConditionBuilder> director);
    
    void filterBooleanField(String fieldName, Director<IBooleanConditionBuilder> director);
    
    // TODO: Among others...
    // void filterStringField(String fieldName, Director<IDynamoStringPredicateBuilder> director);
}
