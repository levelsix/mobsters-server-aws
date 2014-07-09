package com.lvl6.mobsters.dynamo.repository.filter;

public interface IBooleanConditionBuilder {
    void isTrue();
    
    void isFalse();
    
    void is(boolean truthiness);
    
    void isNot(boolean falsiness);
}
