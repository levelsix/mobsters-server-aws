package com.lvl6.mobsters.info;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.lvl6.mobsters.utility.indexing.by_int.AbstractIntComparable;

@MappedSuperclass
public abstract class BaseIntPersistentObject
	extends AbstractIntComparable
	implements IBaseIntPersistentObject
{
    
	private static final long serialVersionUID = 3540073633234904633L;

	@Id 
	protected int id;

	protected BaseIntPersistentObject() { }
	
	protected BaseIntPersistentObject(final int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	protected int getOrderingInt() {
		return id;
	}

	/**
	 * Frameworks like Hibernate need a mutator, but its a unsafe thing to expose here due to equality being
	 * affected.  Fortunately, Hibernate understands this risk and manages it accordingly.
	 * 
	 * @param id
	 */
	void setId(final int id) {
		this.id = id;
	}
}
