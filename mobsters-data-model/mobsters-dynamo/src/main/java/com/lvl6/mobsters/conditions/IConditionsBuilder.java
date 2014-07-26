package com.lvl6.mobsters.conditions;


public interface IConditionsBuilder {
    void filterIntField(String fieldName, Director<IIntConditionBuilder> director);
    
    void filterBooleanField(String fieldName, Director<IBooleanConditionBuilder> director);
    
    void filterStringField(String fieldName, Director<IStringConditionBuilder> director);

    // TODO: Among others...
    // void filterDateField(String fieldName, Director<IDateConditionBuilder> director);
}
