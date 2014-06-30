package com.lvl6.mobsters.info;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseIntPersistentObject implements IBaseIntPersistentObject {

    private static final long serialVersionUID = 970907783140746344L;

    
    @Id 
	protected int id;

	protected BaseIntPersistentObject() { }
	
	protected BaseIntPersistentObject(final int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseIntPersistentObject other = (BaseIntPersistentObject) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
