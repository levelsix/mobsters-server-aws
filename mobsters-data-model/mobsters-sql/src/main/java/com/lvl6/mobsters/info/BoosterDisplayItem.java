package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="BoosterDisplayItem")
@Table(name="BoosterDisplayItem")
@Proxy(lazy=true, proxyClass=IBoosterDisplayItem.class) 
public class BoosterDisplayItem extends BaseIntPersistentObject implements IBoosterDisplayItem{	

	private static final long serialVersionUID = -2934977312069500949L;
	
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=BoosterPack.class)
	@JoinColumn(
		name = "booster_pack_id",
		nullable=false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IBoosterPack boosterPack;
	
	@Column(name = "is_monster")
	private boolean isMonster;
	@Column(name = "is_complete")
	private boolean isComplete;
	@Column(name = "monster_quality")
	private String monsterQuality;
	@Column(name = "gem_reward")
	private int gemReward;
	@Column(name = "quantity")
	private int quantity;  
	public BoosterDisplayItem(){}
	public BoosterDisplayItem(int id, IBoosterPack boosterPack, boolean isMonster,
			boolean isComplete, String monsterQuality, int gemReward,
			int quantity) {
		super(id);
		this.boosterPack = boosterPack;
		this.isMonster = isMonster;
		this.isComplete = isComplete;
		this.monsterQuality = monsterQuality;
		this.gemReward = gemReward;
		this.quantity = quantity;
	}



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#getBoosterPack()
	 */
	@Override
	public IBoosterPack getBoosterPack()
	{
		return boosterPack;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#setBoosterPack(com.lvl6.mobsters.info.IBoosterPack)
	 */
	@Override
	public void setBoosterPack( IBoosterPack boosterPack )
	{
		this.boosterPack = boosterPack;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#isMonster()
	 */
	@Override
	public boolean isMonster() {
		return isMonster;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#setMonster(boolean)
	 */
	@Override
	public void setMonster(boolean isMonster) {
		this.isMonster = isMonster;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#isComplete()
	 */
	@Override
	public boolean isComplete() {
		return isComplete;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#setComplete(boolean)
	 */
	@Override
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#getMonsterQuality()
	 */
	@Override
	public String getMonsterQuality() {
		return monsterQuality;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#setMonsterQuality(java.lang.String)
	 */
	@Override
	public void setMonsterQuality(String monsterQuality) {
		this.monsterQuality = monsterQuality;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#getGemReward()
	 */
	@Override
	public int getGemReward() {
		return gemReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#setGemReward(int)
	 */
	@Override
	public void setGemReward(int gemReward) {
		this.gemReward = gemReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#getQuantity()
	 */
	@Override
	public int getQuantity() {
		return quantity;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterDisplayItem#setQuantity(int)
	 */
	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "BoosterDisplayItem [id=" + id + ", boosterPackId=" + boosterPack
				+ ", isMonster=" + isMonster + ", isComplete=" + isComplete
				+ ", monsterQuality=" + monsterQuality + ", gemReward=" + gemReward
				+ ", quantity=" + quantity + "]";
	}
	
}
