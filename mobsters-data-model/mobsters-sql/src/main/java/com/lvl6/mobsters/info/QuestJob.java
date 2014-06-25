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

@Entity(name="QuestJob")
@Table(name="quest_job")
@Proxy(lazy=true, proxyClass=IQuestJob.class)
public class QuestJob extends BaseIntPersistentObject implements IQuestJob{

	private static final long serialVersionUID = -9216295516712129814L;
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "quest_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Quest quest;
	
	@Column(name = "quest_job_type")
	private String questJobType;
	@Column(name = "description")
	private String description;
	@Column(name = "static_data_id")
	private int staticDataId;
	@Column(name = "quantity")
	private int quantity;	
	//how this quest job is ordered among other quest jobs
	//with the same quest id

	@Column(name = "priority")
	private int priority;	
	//could be 0

	@Column(name = "city_id")
	private int cityId;	
	//could be 0

	@Column(name = "city_asset_num")
	private int cityAssetNum;	
	public QuestJob(){}
	public QuestJob(int id, Quest quest, String questJobType,
			String description, int staticDataId, int quantity, int priority,
			int cityId, int cityAssetNum) {
		super(id);
		this.quest = quest;
		this.questJobType = questJobType;
		this.description = description;
		this.staticDataId = staticDataId;
		this.quantity = quantity;
		this.priority = priority;
		this.cityId = cityId;
		this.cityAssetNum = cityAssetNum;
	}




	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getQuest()
	 */
	@Override
	public Quest getQuest()
	{
		return quest;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setQuest(com.lvl6.mobsters.info.Quest)
	 */
	@Override
	public void setQuest( Quest quest )
	{
		this.quest = quest;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getQuestJobType()
	 */
	@Override
	public String getQuestJobType() {
		return questJobType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setQuestJobType(java.lang.String)
	 */
	@Override
	public void setQuestJobType(String questJobType) {
		this.questJobType = questJobType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getStaticDataId()
	 */
	@Override
	public int getStaticDataId() {
		return staticDataId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setStaticDataId(int)
	 */
	@Override
	public void setStaticDataId(int staticDataId) {
		this.staticDataId = staticDataId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getQuantity()
	 */
	@Override
	public int getQuantity() {
		return quantity;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setQuantity(int)
	 */
	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getPriority()
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setPriority(int)
	 */
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getCityId()
	 */
	@Override
	public int getCityId() {
		return cityId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setCityId(int)
	 */
	@Override
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getCityAssetNum()
	 */
	@Override
	public int getCityAssetNum() {
		return cityAssetNum;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setCityAssetNum(int)
	 */
	@Override
	public void setCityAssetNum(int cityAssetNum) {
		this.cityAssetNum = cityAssetNum;
	}

	@Override
	public String toString() {
		return "QuestJob [id=" + id + ", questId=" + quest
				+ ", questJobType=" + questJobType + ", description="
				+ description + ", staticDataId=" + staticDataId
				+ ", quantity=" + quantity + ", priority=" + priority
				+ ", cityId=" + cityId + ", cityAssetNum=" + cityAssetNum + "]";
	}
	
}
