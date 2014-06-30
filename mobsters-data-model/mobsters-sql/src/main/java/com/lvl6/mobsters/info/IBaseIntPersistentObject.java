package com.lvl6.mobsters.info;

import java.io.Serializable;

public interface IBaseIntPersistentObject extends Serializable
{
    public int getId();
    
    public void setId(int id);
    
}
