package com.lvl6.mobsters.info;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class BasePersistentObject implements Serializable{

	private static final long serialVersionUID = 6615796821793149044L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	// @GeneratedValue(generator="system-uuid")
	// @GenericGenerator(name="system-uuid", strategy = "uuid")
	protected Integer id;

	protected BasePersistentObject() { }
	
	protected BasePersistentObject(final Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
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
