package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class StaticUserLevelInfo extends BasePersistentObject{	

	@Column(name = "final")
	private static final long serialVersionUID = -258608581176466155L;
	@Column(name = "lvl")
	private int lvl;
	@Column(name = "required_exp")
	private int requiredExp;  
	public StaticUserLevelInfo(){}
	public StaticUserLevelInfo(int lvl, int requiredExp) {
		super();
		this.lvl = lvl;
		this.requiredExp = requiredExp;
	}
	
	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public int getRequiredExp() {
		return requiredExp;
	}

	public void setRequiredExp(int requiredExp) {
		this.requiredExp = requiredExp;
	}

	@Override
	public String toString() {
		return "StaticUserLevelInfo [lvl=" + lvl + ", requiredExp=" + requiredExp
				+ "]";
	}
  
}
