package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class QuestJob extends BaseIntPersistentObject{

	
	private static final long serialVersionUID = 8512045755153249399L;	

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




	public Quest getQuest()
	{
		return quest;
	}
	public void setQuest( Quest quest )
	{
		this.quest = quest;
	}
	public String getQuestJobType() {
		return questJobType;
	}

	public void setQuestJobType(String questJobType) {
		this.questJobType = questJobType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStaticDataId() {
		return staticDataId;
	}

	public void setStaticDataId(int staticDataId) {
		this.staticDataId = staticDataId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getCityAssetNum() {
		return cityAssetNum;
	}

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
