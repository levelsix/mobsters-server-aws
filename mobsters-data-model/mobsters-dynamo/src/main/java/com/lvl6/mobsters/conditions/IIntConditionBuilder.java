package com.lvl6.mobsters.conditions;

import java.util.Collection;

public interface IIntConditionBuilder {
    void inNumberCollection(Collection<? extends Number> matchValues);
    
    void inNumbers(Number...matchValues);
    
    void in(int...matchValues);
    
    void is(int value);
    
    void isNot(int value);
    
    void greaterThan(int value);
    
    void greaterThanOrEqualTo(int value);
    
    void lessThan(int value);
    
    void lessThanOrEqualTo(int value);
    
    void between(int lower, int upper);
}
