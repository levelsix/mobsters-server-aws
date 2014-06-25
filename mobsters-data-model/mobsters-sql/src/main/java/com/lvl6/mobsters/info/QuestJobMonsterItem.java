package com.lvl6.mobsters.info;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class QuestJobMonsterItem extends BaseIntPersistentObject implements IQuestJobMonsterItem{

	private static final long serialVersionUID = 2792766000315300575L;

	
	@Column(name = "quest_job_id")
	private int questJobId;
	
	@Column(name = "monster_id")
	private int monsterId;
	
	@Column(name = "item_id")
	private int itemId;
	
	@Column(name = "item_drop_rate")
	private float itemDropRate;	
	//convenience object

	private Random rand = new Random();
	
	public QuestJobMonsterItem(){}
	public QuestJobMonsterItem(int questJobId, int monsterId, int itemId,
			float itemDropRate) {
		super();
		this.questJobId = questJobId;
		this.monsterId = monsterId;
		this.itemId = itemId;
		this.itemDropRate = itemDropRate;
	}

	//covenience methods--------------------------------------------------------
	public Random getRand() {
		return rand;
	}
	
	public void setRand(Random rand) {
		this.rand = rand;
	}
	
	public boolean didItemDrop() {
		float randFloat = getRand().nextFloat();

		if (randFloat < getItemDropRate()) {
			return true;
		} else {
			return false;
		}
	}
	
	//end covenience methods--------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#getQuestJobId()
	 */
	@Override
	public int getQuestJobId() {
		return questJobId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#setQuestJobId(int)
	 */
	@Override
	public void setQuestJobId(int questJobId) {
		this.questJobId = questJobId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#getMonsterId()
	 */
	@Override
	public int getMonsterId() {
		return monsterId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#setMonsterId(int)
	 */
	@Override
	public void setMonsterId(int monsterId) {
		this.monsterId = monsterId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#getItemId()
	 */
	@Override
	public int getItemId() {
		return itemId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJobMonsterItem#setItemId(int)
	 */
	@Override
	public void setItemId(int itemId) {
		this.itemId = itemId;
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
		return "QuestJobMonsterItem [questJobId=" + questJobId + ", monsterId="
				+ monsterId + ", itemId=" + itemId + ", itemDropRate="
				+ itemDropRate + "]";
	}
	
}
