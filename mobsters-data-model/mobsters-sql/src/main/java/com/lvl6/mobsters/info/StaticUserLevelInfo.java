package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="StaticUserLevelInfo")
@Table(name="static_user_level_info")
@Proxy(lazy=true, proxyClass=IStaticUserLevelInfo.class)
public class StaticUserLevelInfo extends BaseIntPersistentObject implements IStaticUserLevelInfo{	

	private static final long serialVersionUID = -6910912642407976813L;
	
	
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
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStaticUserLevelInfo#getLvl()
	 */
	@Override
	public int getLvl() {
		return lvl;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStaticUserLevelInfo#setLvl(int)
	 */
	@Override
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStaticUserLevelInfo#getRequiredExp()
	 */
	@Override
	public int getRequiredExp() {
		return requiredExp;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IStaticUserLevelInfo#setRequiredExp(int)
	 */
	@Override
	public void setRequiredExp(int requiredExp) {
		this.requiredExp = requiredExp;
	}

	@Override
	public String toString() {
		return "StaticUserLevelInfo [lvl=" + lvl + ", requiredExp=" + requiredExp
				+ "]";
	}
  
}
