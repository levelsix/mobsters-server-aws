package com.lvl6.mobsters.info;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="MiniJob")
@Table(name="mini_job")
@Proxy(lazy=true, proxyClass=IMiniJob.class)
public class MiniJob extends BaseIntPersistentObject implements IMiniJob{
	
    private static final long serialVersionUID = 4265353634469388410L;
    

	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Structure.class)
	@JoinColumn(
		name = "required_struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
    private IStructure requiredStruct;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "cash_reward")
	private int cashReward;
	
	@Column(name = "oil_reward")
	private int oilReward;
	
	@Column(name = "gem_reward")
	private int gemReward;
	
	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Monster.class)
	@JoinColumn(
		name = "monster_id_reward",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IMonster monsterReward;
	
	@Column(name = "quality")
	private String quality;
	
	@Column(name = "max_num_monsters_allowed")
	private int maxNumMonstersAllowed;
	
	@Column(name = "chance_to_appear")
	private float chanceToAppear;
	
	@Column(name = "hp_required")
	private int hpRequired;
	
	@Column(name = "atk_required")
	private int atkRequired;
	
	@Column(name = "min_dmg_dealt")
    private int minDmgDealt;
	
	@Column(name = "max_dmg_dealt")
	private int maxDmgDealt;
	
    @Column(name = "duration_min_minutes")
    private int durationMinMinutes;
    
    @Column(name = "duration_max_minutes")
    private int durationMaxMinutes;
	
    private Random rand = new Random();
    
	public MiniJob(){}

    public MiniJob(
        IStructure requiredStruct,
        String name,
        int cashReward,
        int oilReward,
        int gemReward,
        IMonster monsterReward,
        String quality,
        int maxNumMonstersAllowed,
        float chanceToAppear,
        int hpRequired,
        int atkRequired,
        int minDmgDealt,
        int maxDmgDealt,
        int durationMinMinutes,
        int durationMaxMinutes )
    {
        super();
        this.requiredStruct = requiredStruct;
        this.name = name;
        this.cashReward = cashReward;
        this.oilReward = oilReward;
        this.gemReward = gemReward;
        this.monsterReward = monsterReward;
        this.quality = quality;
        this.maxNumMonstersAllowed = maxNumMonstersAllowed;
        this.chanceToAppear = chanceToAppear;
        this.hpRequired = hpRequired;
        this.atkRequired = atkRequired;
        this.minDmgDealt = minDmgDealt;
        this.maxDmgDealt = maxDmgDealt;
        this.durationMinMinutes = durationMinMinutes;
        this.durationMaxMinutes = durationMaxMinutes;
    }
    
    //covenience methods--------------------------------------------------------
    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public int getDmgDealt() {
        //example goal: [min,max]=[5, 10], transform range to start at 0.
        //[min-min, max-min] = [0,max-min] = [0,10-5] = [0,5]
        //this means there are (10-5)+1 possible numbers

        int minMaxDiff = getMaxDmgDealt() - getMinDmgDealt();
        int randDmg = rand.nextInt(minMaxDiff + 1); 

        //number generated in [0, max-min] range, but need to transform
        //back to original range [min, max]. so add min. [0+min, max-min+min]
        return randDmg + getMinDmgDealt();
    }
    
    public int getDurationMinutes() {
        int minMaxDiff = getDurationMaxMinutes() - getDurationMinMinutes();
        int randMinutes = rand.nextInt(minMaxDiff + 1);
        
        return randMinutes + getDurationMinMinutes();
    }
    
    //end covenience methods--------------------------------------------------------



    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getName()
	 */
    @Override
	public String getName()
    {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getRequiredStruct()
	 */
    @Override
	public IStructure getRequiredStruct()
	{
		return requiredStruct;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setRequiredStruct(com.lvl6.mobsters.info.IStructure)
	 */
	@Override
	public void setRequiredStruct( IStructure requiredStruct )
	{
		this.requiredStruct = requiredStruct;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setName(java.lang.String)
	 */
	@Override
	public void setName( String name )
    {
        this.name = name;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getCashReward()
	 */
    @Override
	public int getCashReward()
    {
        return cashReward;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setCashReward(int)
	 */
    @Override
	public void setCashReward( int cashReward )
    {
        this.cashReward = cashReward;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getOilReward()
	 */
    @Override
	public int getOilReward()
    {
        return oilReward;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setOilReward(int)
	 */
    @Override
	public void setOilReward( int oilReward )
    {
        this.oilReward = oilReward;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getGemReward()
	 */
    @Override
	public int getGemReward()
    {
        return gemReward;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setGemReward(int)
	 */
    @Override
	public void setGemReward( int gemReward )
    {
        this.gemReward = gemReward;
    }

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getMonsterReward()
	 */
	@Override
	public IMonster getMonsterReward()
	{
		return monsterReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setMonsterReward(com.lvl6.mobsters.info.IMonster)
	 */
	@Override
	public void setMonsterReward( IMonster monsterReward )
	{
		this.monsterReward = monsterReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getQuality()
	 */
	@Override
	public String getQuality()
    {
        return quality;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setQuality(java.lang.String)
	 */
    @Override
	public void setQuality( String quality )
    {
        this.quality = quality;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getMaxNumMonstersAllowed()
	 */
    @Override
	public int getMaxNumMonstersAllowed()
    {
        return maxNumMonstersAllowed;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setMaxNumMonstersAllowed(int)
	 */
    @Override
	public void setMaxNumMonstersAllowed( int maxNumMonstersAllowed )
    {
        this.maxNumMonstersAllowed = maxNumMonstersAllowed;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getChanceToAppear()
	 */
    @Override
	public float getChanceToAppear()
    {
        return chanceToAppear;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setChanceToAppear(float)
	 */
    @Override
	public void setChanceToAppear( float chanceToAppear )
    {
        this.chanceToAppear = chanceToAppear;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getHpRequired()
	 */
    @Override
	public int getHpRequired()
    {
        return hpRequired;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setHpRequired(int)
	 */
    @Override
	public void setHpRequired( int hpRequired )
    {
        this.hpRequired = hpRequired;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getAtkRequired()
	 */
    @Override
	public int getAtkRequired()
    {
        return atkRequired;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setAtkRequired(int)
	 */
    @Override
	public void setAtkRequired( int atkRequired )
    {
        this.atkRequired = atkRequired;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getMinDmgDealt()
	 */
    @Override
	public int getMinDmgDealt()
    {
        return minDmgDealt;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setMinDmgDealt(int)
	 */
    @Override
	public void setMinDmgDealt( int minDmgDealt )
    {
        this.minDmgDealt = minDmgDealt;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getMaxDmgDealt()
	 */
    @Override
	public int getMaxDmgDealt()
    {
        return maxDmgDealt;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setMaxDmgDealt(int)
	 */
    @Override
	public void setMaxDmgDealt( int maxDmgDealt )
    {
        this.maxDmgDealt = maxDmgDealt;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getDurationMinMinutes()
	 */
    @Override
	public int getDurationMinMinutes()
    {
        return durationMinMinutes;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setDurationMinMinutes(int)
	 */
    @Override
	public void setDurationMinMinutes( int durationMinMinutes )
    {
        this.durationMinMinutes = durationMinMinutes;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#getDurationMaxMinutes()
	 */
    @Override
	public int getDurationMaxMinutes()
    {
        return durationMaxMinutes;
    }

    /* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IMiniJob#setDurationMaxMinutes(int)
	 */
    @Override
	public void setDurationMaxMinutes( int durationMaxMinutes )
    {
        this.durationMaxMinutes = durationMaxMinutes;
    }

    @Override
    public String toString()
    {
        return "MiniJob [requiredStructId=" +
            requiredStruct +
            ", name=" +
            name +
            ", cashReward=" +
            cashReward +
            ", oilReward=" +
            oilReward +
            ", gemReward=" +
            gemReward +
            ", monsterIdReward=" +
            monsterReward +
            ", quality=" +
            quality +
            ", maxNumMonstersAllowed=" +
            maxNumMonstersAllowed +
            ", chanceToAppear=" +
            chanceToAppear +
            ", hpRequired=" +
            hpRequired +
            ", atkRequired=" +
            atkRequired +
            ", minDmgDealt=" +
            minDmgDealt +
            ", maxDmgDealt=" +
            maxDmgDealt +
            ", durationMinMinutes=" +
            durationMinMinutes +
            ", durationMaxMinutes=" +
            durationMaxMinutes +
            "]";
    }
	
}
