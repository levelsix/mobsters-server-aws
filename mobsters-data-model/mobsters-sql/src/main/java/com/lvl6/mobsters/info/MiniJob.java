package com.lvl6.mobsters.info;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MiniJob extends BaseIntPersistentObject{

    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "required_struct_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
    private Structure requiredStruct;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "cash_reward")
	private int cashReward;
	
	@Column(name = "oil_reward")
	private int oilReward;
	
	@Column(name = "gem_reward")
	private int gemReward;
	
	@ManyToOne
	@JoinColumn(
		name = "monster_id_reward",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Monster monsterReward;
	
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
	
    private Random rand;
    
	public MiniJob(){}

    public MiniJob(
        Structure requiredStruct,
        String name,
        int cashReward,
        int oilReward,
        int gemReward,
        Monster monsterReward,
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



    public String getName()
    {
        return name;
    }

    public Structure getRequiredStruct()
	{
		return requiredStruct;
	}

	public void setRequiredStruct( Structure requiredStruct )
	{
		this.requiredStruct = requiredStruct;
	}

	public void setName( String name )
    {
        this.name = name;
    }

    public int getCashReward()
    {
        return cashReward;
    }

    public void setCashReward( int cashReward )
    {
        this.cashReward = cashReward;
    }

    public int getOilReward()
    {
        return oilReward;
    }

    public void setOilReward( int oilReward )
    {
        this.oilReward = oilReward;
    }

    public int getGemReward()
    {
        return gemReward;
    }

    public void setGemReward( int gemReward )
    {
        this.gemReward = gemReward;
    }

	public Monster getMonsterReward()
	{
		return monsterReward;
	}

	public void setMonsterReward( Monster monsterReward )
	{
		this.monsterReward = monsterReward;
	}

	public String getQuality()
    {
        return quality;
    }

    public void setQuality( String quality )
    {
        this.quality = quality;
    }

    public int getMaxNumMonstersAllowed()
    {
        return maxNumMonstersAllowed;
    }

    public void setMaxNumMonstersAllowed( int maxNumMonstersAllowed )
    {
        this.maxNumMonstersAllowed = maxNumMonstersAllowed;
    }

    public float getChanceToAppear()
    {
        return chanceToAppear;
    }

    public void setChanceToAppear( float chanceToAppear )
    {
        this.chanceToAppear = chanceToAppear;
    }

    public int getHpRequired()
    {
        return hpRequired;
    }

    public void setHpRequired( int hpRequired )
    {
        this.hpRequired = hpRequired;
    }

    public int getAtkRequired()
    {
        return atkRequired;
    }

    public void setAtkRequired( int atkRequired )
    {
        this.atkRequired = atkRequired;
    }

    public int getMinDmgDealt()
    {
        return minDmgDealt;
    }

    public void setMinDmgDealt( int minDmgDealt )
    {
        this.minDmgDealt = minDmgDealt;
    }

    public int getMaxDmgDealt()
    {
        return maxDmgDealt;
    }

    public void setMaxDmgDealt( int maxDmgDealt )
    {
        this.maxDmgDealt = maxDmgDealt;
    }

    public int getDurationMinMinutes()
    {
        return durationMinMinutes;
    }

    public void setDurationMinMinutes( int durationMinMinutes )
    {
        this.durationMinMinutes = durationMinMinutes;
    }

    public int getDurationMaxMinutes()
    {
        return durationMaxMinutes;
    }

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
