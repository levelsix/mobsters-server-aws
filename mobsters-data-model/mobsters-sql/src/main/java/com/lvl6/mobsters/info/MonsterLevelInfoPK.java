package com.lvl6.mobsters.info;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class MonsterLevelInfoPK implements Serializable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + level;
		result = prime * result + ((monster == null) ? 0 : monster.getId());
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
		MonsterLevelInfoPK other = (MonsterLevelInfoPK) obj;
		if (level != other.level) {
			return false;
		}
		if (monster == null) {
			if (other.monster != null) {
				return false;
			}
		} else if (other.monster == null) {
			return false;
		} else if (monster.getId() != other.monster.getId()) {
			return false;
		}
		
		return true;
	}

	/**
	 */
	private static final long serialVersionUID = -3560950509517997610L;

//	@JoinColumn(
//		name = "monster_id",
//		nullable = false,
//		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT),
//		referencedColumnName = "id",
//		insertable = true,
//		updatable = false,
//		unique = false
//	)
//    int monster_id;
    
	@ManyToOne(
		fetch = FetchType.LAZY, 
		targetEntity = Monster.class, 
		optional = false, 
		cascade = CascadeType.REFRESH
	)
	@JoinColumn(
		name = "monster_id", 
		referencedColumnName = "id", 
		nullable = false, 
		foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT), 
		insertable = true, 
		updatable = false, 
		unique = false
	)
	IMonster monster;

	@Column(
		name = "level",
		nullable = false
	)	
	int level;

	public IMonster getMonster() {
		return monster;
	}

	public void setMonster(IMonster monster) {
		this.monster = monster;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
