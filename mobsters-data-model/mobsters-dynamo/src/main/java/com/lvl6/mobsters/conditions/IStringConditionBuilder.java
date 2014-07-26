package com.lvl6.mobsters.conditions;

import java.util.Collection;

public interface IStringConditionBuilder {
    void isNull();
    
    void isNotNull();
    
    void isString(String value);
    
    void isNotString(String value);

    void inStringCollection(Collection<String> matchValues);
    
    void in(String...matchValues);
    
    void after(String value);
    
    void afterOrEquals(String value);
    
    void before(String value);
    
    void beforeOrEquals(String value);
    
    void betweenStrings(String lower, String upper);

    void hasSubstring(String substring);
    
    void lacksSubstring(String substring);
    
    void hasPrefix(String prefix);
}
