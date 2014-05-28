package com.lvl6.mobsters.info;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class BasePersistentObject implements Serializable{

	private static final long serialVersionUID = 6615796821793149044L;

	
	/*
	 * IDs are String UUID for two reasons:
	 * 1. Impossible to reliably generate sequential Integers in distributed databases. Which this database will be if it gets big enough. If we were going to use numeric IDs they would be Longs.
	 * 2. To maintain consistency with Dynamo which will only auto generate String UUIDs. 
	 * 
	 */
	
	@Id 
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	protected String id;

	protected BasePersistentObject() { }
	
	protected BasePersistentObject(final String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id != null) ? id.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasePersistentObject other = (BasePersistentObject) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
