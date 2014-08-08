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

/*
 was duple (quest_job_id, monster_id) unique? but a quest_job_id can have multiple monster_ids?
 akamath:  uhh, ya so multiple monsters can drop a duckie for example
 me:  will that duckie be dropped in another quest_job_id?
 akamath:  ya it could
 */

@Entity(name="QuestJobMonsterItem")
@Table(name="quest_job_monster_item")
@Proxy(lazy=true, proxyClass=IQuestJobMonsterItem.class)
public class QuestJobMonsterItem 
	extends BaseIntPersistentObject 
	implements IQuestJobMonsterItem
{

	private static final long serialVersionUID = 2792766000315300575L;


	@ManyToOne(fetch=FetchType.EAGER, targetEntity=QuestJob.class)
	@JoinColumn(
		name = "quest_job_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IQuestJob questJob;
	
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=Monster.class)
	@JoinColumn(
		name = "monster_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IMonster monster;
	
	@ManyToOne(fetch=FetchType.EAGER, targetEntity=Item.class)
	@JoinColumn(
		name = "item_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IItem item;
	
	@Column(name = "item_drop_rate")
	private float itemDropRate;	
	
	public QuestJobMonsterItem()
	{ }
	
	public QuestJobMonsterItem(
		IQuestJob questJob, IMonster monster, IItem item, float itemDropRate) 
	{
		super();
		this.questJob = questJob;
		this.monster = monster;
		this.item = item;
		this.itemDropRate = itemDropRate;
	}
	
	//end covenience methods--------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#getQuestJob()
	 */
	@Override
	public IQuestJob getQuestJob() {
		return questJob;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#setQuestJob(int)
	 */
	@Override
	public void setQuestJob(IQuestJob questJob) {
		this.questJob = questJob;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#getMonster()
	 */
	@Override
	public IMonster getMonster() {
		return monster;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#setMonster(int)
	 */
	@Override
	public void setMonster(IMonster monster) {
		this.monster = monster;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#getItem()
	 */
	@Override
	public IItem getItem() {
		return item;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#setItem(int)
	 */
	@Override
	public void setItem(IItem item) {
		this.item = item;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#getItemDropRate()
	 */
	@Override
	public float getItemDropRate() {
		return itemDropRate;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#setItemDropRate(float)
	 */
	@Override
	public void setItemDropRate(float itemDropRate) {
		this.itemDropRate = itemDropRate;
	}

	@Override
	public String toString() {
		return 
			String.format(
				"QuestJobMonsterItem [questJobId=%d, monsterId=%d, itemId=%d, itemDropRate=%f",
				questJob.getId(), monster.getId(), item.getId(), itemDropRate);
	}
	
}
