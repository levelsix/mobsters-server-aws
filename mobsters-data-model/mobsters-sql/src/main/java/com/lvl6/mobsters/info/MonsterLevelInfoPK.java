package com.lvl6.mobsters.info;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MonsterLevelInfoPK implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + level;
		result = prime * result + monsterId;
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
		if (monsterId != other.monsterId) {
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7333608374903575813L;

//	@JoinColumn(
//		name = "monster_id",
//		nullable = false,
//		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT),
//		referencedColumnName = "id",
//		insertable = true,
//		updatable = false,
//		unique = false
//	)
	@Column(nullable=false, name="monster_id")
    int monsterId;

	@Column(name = "level", nullable = false)	
	int level;

	public int getMonsterId() {
		return monsterId;
	}

	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
