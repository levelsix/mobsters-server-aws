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

//monster_id, gems, and cash are all mutually exclusive

@Entity(name="BoosterItem") //set so the cache can refer to this class/table by name 
@Table(name="booster_item")
// created proxy interface because don't want hibernate to subclass the concrete implementation
// under any circumstances where it would return a proxy of this entity.
// "lazy=true" means any references to this entity type that are not explicitly eager will be populated
// by a proxy object to enact laziness (deferred retrieval).  Lazy loading is the default behavior,
// so this is merely making the default explicit.
@Proxy(lazy=true, proxyClass=IBoosterItem.class) 
public class BoosterItem extends BaseIntPersistentObject implements IBoosterItem{	

	private static final long serialVersionUID = 967478067223025160L;
	

	// "targetEntity=BoosterPack.class" is specified because the property is IBoosterPack.
	// IBoosterPack enables the class to store a proxy if necessary, but does not provide 
	// BoosterPack is the entity type and Hibernate needs a hint to link the entity type with
	// that proxy-capable interface.
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=BoosterPack.class)
	@JoinColumn(
		name = "booster_pack_id", //exact column name (verbatim) in the booster_item table 
		nullable = false, //(foreign key must be valid) a booster item must have a booster pack
		//name=none means that hibernate won't create a foreign key constraint in the db
		//NO_CONSTRAINT flag is not used, but is included for more human clarity than "name=none"
		foreignKey = @ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT)) 
	private IBoosterPack boosterPack;
	
	//"optional=true" means monster object/reference can be nonexistent/null
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Monster.class, optional=true)
	@JoinColumn(
		name = "monster_id",
		nullable = true,
		foreignKey = @ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IMonster monster;
	
	@Column(name = "num_pieces")
	private int numPieces;
	
	@Column(name = "is_complete")
	private boolean isComplete;
	
	@Column(name = "is_special")
	private boolean isSpecial;
	
	@Column(name = "gem_reward")
	private int gemReward;
	
	@Column(name = "cash_reward")
	private int cashReward;
	
	@Column(name = "chance_to_appear")
	private float chanceToAppear;  
	
	public BoosterItem(){}
	public BoosterItem(int id, IBoosterPack boosterPack, IMonster monster, int numPieces,
			boolean isComplete, boolean isSpecial, int gemReward, int cashReward,
			float chanceToAppear) {
		super(id);
		this.boosterPack = boosterPack;
		this.monster = monster;
		this.numPieces = numPieces;
		this.isComplete = isComplete;
		this.isSpecial = isSpecial;
		this.gemReward = gemReward;
		this.cashReward = cashReward;
		this.chanceToAppear = chanceToAppear;
	}



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#getBoosterPack()
	 */
	@Override
	public IBoosterPack getBoosterPack() {
		return boosterPack;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#setBoosterPack(com.lvl6.mobsters.info.BoosterPack)
	 */
	@Override
	public void setBoosterPack(IBoosterPack boosterPack) {
		this.boosterPack = boosterPack;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#getMonster()
	 */
	@Override
	public IMonster getMonster() {
		return monster;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#setMonster(com.lvl6.mobsters.info.Monster)
	 */
	@Override
	public void setMonster(IMonster monster) {
		this.monster = monster;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#getNumPieces()
	 */
	@Override
	public int getNumPieces() {
		return numPieces;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#setNumPieces(int)
	 */
	@Override
	public void setNumPieces(int numPieces) {
		this.numPieces = numPieces;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#isComplete()
	 */
	@Override
	public boolean isComplete() {
		return isComplete;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#setComplete(boolean)
	 */
	@Override
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#isSpecial()
	 */
	@Override
	public boolean isSpecial() {
		return isSpecial;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#setSpecial(boolean)
	 */
	@Override
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#getGemReward()
	 */
	@Override
	public int getGemReward() {
		return gemReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#setGemReward(int)
	 */
	@Override
	public void setGemReward(int gemReward) {
		this.gemReward = gemReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#getCashReward()
	 */
	@Override
	public int getCashReward() {
		return cashReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#setCashReward(int)
	 */
	@Override
	public void setCashReward(int cashReward) {
		this.cashReward = cashReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#getChanceToAppear()
	 */
	@Override
	public float getChanceToAppear() {
		return chanceToAppear;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IBoosterItem#setChanceToAppear(float)
	 */
	@Override
	public void setChanceToAppear(float chanceToAppear) {
		this.chanceToAppear = chanceToAppear;
	}

	@Override
	public String toString() {
		return "BoosterItem [id=" + id + ", boosterPack=" + boosterPack
				+ ", monster=" + monster + ", numPieces=" + numPieces
				+ ", isComplete=" + isComplete + ", isSpecial=" + isSpecial
				+ ", gemReward=" + gemReward + ", cashReward=" + cashReward
				+ ", chanceToAppear=" + chanceToAppear + "]";
	}
  
}
