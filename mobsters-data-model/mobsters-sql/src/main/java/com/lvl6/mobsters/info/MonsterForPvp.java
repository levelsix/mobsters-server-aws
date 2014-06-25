package com.lvl6.mobsters.info;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="MonsterForPvp")
@Table(name="monster_for_pvp")
@Proxy(lazy=true, proxyClass=IMonsterForPvp.class)
public class MonsterForPvp extends BaseIntPersistentObject implements IMonsterForPvp{

	private static final long serialVersionUID = -3692062543893586730L;
	

	@ManyToOne
	@JoinColumn(
		name = "monster_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IMonster monster;
	
	@Column(name = "monster_lvl")
	private int monsterLvl;
	@Column(name = "elo")
	private int elo;
	@Column(name = "min_cash_reward")
	private int minCashReward;
	@Column(name = "max_cash_reward")
	private int maxCashReward;
	@Column(name = "min_oil_reward")
	private int minOilReward;
	@Column(name = "max_oil_reward")
	private int maxOilReward;	
	
	//not part of the table, just for convenience
	private Random rand = new Random();	
	
	public MonsterForPvp(){}
	public MonsterForPvp(int id, IMonster monster, int monsterLvl, int elo,
			int minCashReward, int maxCashReward, int minOilReward, int maxOilReward) {
		super(id);
		this.monster = monster;
		this.monsterLvl = monsterLvl;
		this.elo = elo;
		this.minCashReward = minCashReward;
		this.maxCashReward = maxCashReward;
		this.minOilReward = minOilReward;
		this.maxOilReward = maxOilReward;
	}
	
  //covenience methods--------------------------------------------------------
  public Random getRand() {
    return rand;
  }

  public void setRand(Random rand) {
    this.rand = rand;
  }
  
  public int getCashDrop() {
    //example goal: [min,max]=[5, 10], transform range to start at 0.
    //[min-min, max-min] = [0,max-min] = [0,10-5] = [0,5]
    //this means there are (10-5)+1 possible numbers
    
    int minMaxDiff = getMaxCashReward() - getMinCashReward();
    int randCash = rand.nextInt(minMaxDiff + 1); 

    //number generated in [0, max-min] range, but need to transform
    //back to original range [min, max]. so add min. [0+min, max-min+min]
    return randCash + getMinCashReward();
  }
  
  public int getOilDrop() {
    //example goal: [min,max]=[5, 10], transform range to start at 0.
    //[min-min, max-min] = [0,max-min] = [0,10-5] = [0,5]
    //this means there are (10-5)+1 possible numbers
    
    int minMaxDiff = getMaxOilReward() - getMinOilReward();
    int randCash = rand.nextInt(minMaxDiff + 1); 

    //number generated in [0, max-min] range, but need to transform
    //back to original range [min, max]. so add min. [0+min, max-min+min]
    return randCash + getMinOilReward();
  }

  //end covenience methods--------------------------------------------------------


	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#getMonster()
	 */
	@Override
	public IMonster getMonster()
	{
		return monster;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#setMonster(com.lvl6.mobsters.info.Monster)
	 */
	@Override
	public void setMonster( IMonster monster )
	{
		this.monster = monster;
	}
	
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#getMonsterLvl()
	 */
	@Override
	public int getMonsterLvl() {
		return monsterLvl;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#setMonsterLvl(int)
	 */
	@Override
	public void setMonsterLvl(int monsterLvl) {
		this.monsterLvl = monsterLvl;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#getElo()
	 */
	@Override
	public int getElo() {
		return elo;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#setElo(int)
	 */
	@Override
	public void setElo(int elo) {
		this.elo = elo;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#getMinCashReward()
	 */
	@Override
	public int getMinCashReward() {
		return minCashReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#setMinCashReward(int)
	 */
	@Override
	public void setMinCashReward(int minCashReward) {
		this.minCashReward = minCashReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#getMaxCashReward()
	 */
	@Override
	public int getMaxCashReward() {
		return maxCashReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#setMaxCashReward(int)
	 */
	@Override
	public void setMaxCashReward(int maxCashReward) {
		this.maxCashReward = maxCashReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#getMinOilReward()
	 */
	@Override
	public int getMinOilReward() {
		return minOilReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#setMinOilReward(int)
	 */
	@Override
	public void setMinOilReward(int minOilReward) {
		this.minOilReward = minOilReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#getMaxOilReward()
	 */
	@Override
	public int getMaxOilReward() {
		return maxOilReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMonsterForPvp#setMaxOilReward(int)
	 */
	@Override
	public void setMaxOilReward(int maxOilReward) {
		this.maxOilReward = maxOilReward;
	}

	@Override
	public String toString() {
		return "MonsterForPvp [id=" + id + ", monsterId=" + monster
				+ ", monsterLvl=" + monsterLvl + ", elo=" + elo + ", minCashReward="
				+ minCashReward + ", maxCashReward=" + maxCashReward
				+ ", minOilReward=" + minOilReward + ", maxOilReward=" + maxOilReward
				+ "]";
	}

}
