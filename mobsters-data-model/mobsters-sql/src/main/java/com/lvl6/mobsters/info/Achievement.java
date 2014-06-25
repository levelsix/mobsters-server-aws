package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Achievement extends BaseIntPersistentObject{

	
	@Column(name = "achievement_name")
	private String achievementName;
	@Column(name = "description")
	private String description;
	@Column(name = "gem_reward")
	private int gemReward;
	@Column(name = "lvl")
	private int lvl; 				//max value most likely 3
	@Column(name = "achievement_type")
	private String achievementType;
	@Column(name = "resource_type")
	private String resourceType; 	//could be null
	@Column(name = "monster_element")
	private String monsterElement; 	//could be null
	@Column(name = "monster_quality")
	private String monsterQuality;	//could be null
	@Column(name = "static_data_id")
	private int staticDataId;		//could be 0, as in not set
	@Column(name = "quantity")
	private int quantity;
	@Column(name = "priority")
	private int priority;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "prerequisite_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Achievement prerequisiteAchievement;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "successor_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Achievement successorAchievement;	
	
	public Achievement(){}
	public Achievement(int id, String achievementName, String description,
			int gemReward, int lvl, String achievementType,
			String resourceType, String monsterElement, String monsterQuality,
			int staticDataId, int quantity, int priority, Achievement prerequisiteAchievement,
			Achievement successorAchievement) {
		super(id);
		this.achievementName = achievementName;
		this.description = description;
		this.gemReward = gemReward;
		this.lvl = lvl;
		this.achievementType = achievementType;
		this.resourceType = resourceType;
		this.monsterElement = monsterElement;
		this.monsterQuality = monsterQuality;
		this.staticDataId = staticDataId;
		this.quantity = quantity;
		this.priority = priority;
		this.prerequisiteAchievement = prerequisiteAchievement;
		this.successorAchievement = successorAchievement;
	}



	public String getAchievementName() {
		return achievementName;
	}

	public void setAchievementName(String achievementName) {
		this.achievementName = achievementName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getGemReward() {
		return gemReward;
	}

	public void setGemReward(int gemReward) {
		this.gemReward = gemReward;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public String getAchievementType() {
		return achievementType;
	}

	public void setAchievementType(String achievementType) {
		this.achievementType = achievementType;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getMonsterElement() {
		return monsterElement;
	}

	public void setMonsterElement(String monsterElement) {
		this.monsterElement = monsterElement;
	}

	public String getMonsterQuality() {
		return monsterQuality;
	}

	public void setMonsterQuality(String monsterQuality) {
		this.monsterQuality = monsterQuality;
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

	public Achievement getPrerequisiteAchievement()
	{
		return prerequisiteAchievement;
	}
	public void setPrerequisiteAchievement( Achievement prerequisiteAchievement )
	{
		this.prerequisiteAchievement = prerequisiteAchievement;
	}
	public Achievement getSuccessorAchievement()
	{
		return successorAchievement;
	}
	public void setSuccessorAchievement( Achievement successorAchievement )
	{
		this.successorAchievement = successorAchievement;
	}
	
	@Override
	public String toString() {
		return "Achievement [id=" + id + ", achievementName=" + achievementName
				+ ", description=" + description + ", gemReward=" + gemReward
				+ ", lvl=" + lvl + ", achievementType=" + achievementType
				+ ", resourceType=" + resourceType + ", monsterElement="
				+ monsterElement + ", monsterQuality=" + monsterQuality
				+ ", staticDataId=" + staticDataId + ", quantity=" + quantity
				+ ", priority=" + priority + ", prerequisiteId="
				+ prerequisiteAchievement + ", successorId=" + successorAchievement + "]";
	}
	
}
