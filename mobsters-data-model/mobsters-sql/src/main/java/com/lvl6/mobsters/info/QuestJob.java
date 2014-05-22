package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class QuestJob extends BasePersistentObject{

	
	private static final long serialVersionUID = 4809910755711898087L;	

	@Column(name = "quest_id")
	private int questId;
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
	public QuestJob(int id, int questId, String questJobType,
			String description, int staticDataId, int quantity, int priority,
			int cityId, int cityAssetNum) {
		super();
		this.questId = questId;
		this.questJobType = questJobType;
		this.description = description;
		this.staticDataId = staticDataId;
		this.quantity = quantity;
		this.priority = priority;
		this.cityId = cityId;
		this.cityAssetNum = cityAssetNum;
	}



	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
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
		return "QuestJob [id=" + id + ", questId=" + questId
				+ ", questJobType=" + questJobType + ", description="
				+ description + ", staticDataId=" + staticDataId
				+ ", quantity=" + quantity + ", priority=" + priority
				+ ", cityId=" + cityId + ", cityAssetNum=" + cityAssetNum + "]";
	}
	
}
