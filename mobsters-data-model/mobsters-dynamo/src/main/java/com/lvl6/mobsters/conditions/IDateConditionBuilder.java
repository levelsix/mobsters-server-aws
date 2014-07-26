package com.lvl6.mobsters.conditions;

import java.util.Collection;
import java.util.Date;

public interface IDateConditionBuilder {
    void isNull();
    
    void isNotNull();
    
    void isDate(Date value);
    
    void isNotDate(Date value);
    
    void inDates(Date...matchValues);
    
    void earlierThan(Date value);
    
    void laterThan(Date value);
    
    void earlierOrEqual(Date value);
    
    void laterOrEqual(Date value);
    
    void betweenDates(Date lower, Date upper);
    
    void inDateCollection(Collection<Date> matchValues);
}
