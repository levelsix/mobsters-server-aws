package com.lvl6.mobsters.info;

import java.io.Serializable;

public interface IBaseIntPersistentObject extends Serializable
{
    public int getId();
    
    // Even before AbstractIntComparable inheritance, configuration objects implemented equals() and
    // could validly be used as hash keys.  Equality is largely based on id equivalence, so there should
    // be no convenient public means of changing ids.
    // public void setId(int id);
}
