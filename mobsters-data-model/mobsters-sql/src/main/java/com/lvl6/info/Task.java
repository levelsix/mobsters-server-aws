package com.lvl6.info;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Task extends BasePersistentObject{

	@Column(name = "final")
	private static final long serialVersionUID = 4039380828851189212L;
	@Column(name = "good_name")
	private String goodName;
	@Column(name = "description")
	private String description;
	@Column(name = "city_id")
	private int cityId;
	//@Column(name = "energy_cost")
//	private int energyCost;
	@Column(name = "asset_number_within_city")
	private int assetNumberWithinCity;
	@Column(name = "prerequisite_task_id")
	private int prerequisiteTaskId;
	@Column(name = "prerequisite_quest_id")
	private int prerequisiteQuestId;	
	public Task(){}
	public Task(int id, String goodName, String description, int cityId,
			int assetNumberWithinCity, int prerequisiteTaskId, int prerequisiteQuestId) {
		super();
		this.goodName = goodName;
		this.description = description;
		this.cityId = cityId;
		this.assetNumberWithinCity = assetNumberWithinCity;
		this.prerequisiteTaskId = prerequisiteTaskId;
		this.prerequisiteQuestId = prerequisiteQuestId;
	}
	
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getAssetNumberWithinCity() {
		return assetNumberWithinCity;
	}
	public void setAssetNumberWithinCity(int assetNumberWithinCity) {
		this.assetNumberWithinCity = assetNumberWithinCity;
	}
	public int getPrerequisiteTaskId() {
		return prerequisiteTaskId;
	}
	public void setPrerequisiteTaskId(int prerequisiteTaskId) {
		this.prerequisiteTaskId = prerequisiteTaskId;
	}
	public int getPrerequisiteQuestId() {
		return prerequisiteQuestId;
	}
	public void setPrerequisiteQuestId(int prerequisiteQuestId) {
		this.prerequisiteQuestId = prerequisiteQuestId;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", goodName=" + goodName + ", description="
				+ description + ", cityId=" + cityId + ", assetNumberWithinCity="
				+ assetNumberWithinCity + ", prerequisiteTaskId=" + prerequisiteTaskId
				+ ", prerequisiteQuestId=" + prerequisiteQuestId + "]";
	}
	
}
