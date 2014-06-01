package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="task")
public class Task extends BasePersistentObject{

	private static final long serialVersionUID = -520057120226567292L;

	@Column(name = "good_name")
	private String goodName;

	@Column(name = "description")
	private String description;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "city_id", foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private City city;

	// @Column(name = "energy_cost")
	// private int energyCost;

	@Column(name = "asset_number_within_city")
	private int assetNumberWithinCity;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "prerequisite_task_id", foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	Task prerequisiteTask;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "prerequisite_quest_id", foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Quest prerequisiteQuest;	

	public Task() {
		super();
	}

	public Task(final int id, final String goodName, final String description, final City city,
			final int assetNumberWithinCity, final Task prerequisiteTask, final Quest prerequisiteQuest) {
		// super(id);
		this.goodName = goodName;
		this.description = description;
		this.city = city;
		this.assetNumberWithinCity = assetNumberWithinCity;
		this.prerequisiteTask = prerequisiteTask;
		this.prerequisiteQuest = prerequisiteQuest;
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
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public int getAssetNumberWithinCity() {
		return assetNumberWithinCity;
	}
	public void setAssetNumberWithinCity(int assetNumberWithinCity) {
		this.assetNumberWithinCity = assetNumberWithinCity;
	}
	public Task getPrerequisiteTask() {
		return prerequisiteTask;
	}
	public void setPrerequisiteTask(Task prerequisiteTask) {
		this.prerequisiteTask = prerequisiteTask;
	}
	public Quest getPrerequisiteQuest() {
		return prerequisiteQuest;
	}
	public void setPrerequisiteQuest(Quest prerequisiteQuest) {
		this.prerequisiteQuest = prerequisiteQuest;
	}
	@Override
	public String toString() {
		return "Task [goodName=" + goodName + ", description=" + description
				+ ", city=" + city + ", assetNumberWithinCity="
				+ assetNumberWithinCity + ", prerequisiteTask="
				+ prerequisiteTask + ", prerequisiteQuest="
				+ prerequisiteQuest + "]";
	}
	
}
