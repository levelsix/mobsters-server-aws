package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Proxy;

@Entity(name="Achievement")
@Table(name="achievement")
@Proxy(lazy=false, proxyClass=IAchievement.class)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Achievement extends BaseIntPersistentObject implements IAchievement{

	private static final long serialVersionUID = 873467535443000372L;
	
	
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
	
	@OneToOne(fetch=FetchType.LAZY, targetEntity=Achievement.class, optional=true)
	@JoinColumn(
		name = "prerequisite_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IAchievement prerequisiteAchievement;
	
	@OneToOne(fetch=FetchType.LAZY, targetEntity=Achievement.class, optional=true)
	@JoinColumn(
		name = "successor_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IAchievement successorAchievement;	
	
	public Achievement(){}
	public Achievement(int id, String achievementName, String description,
			int gemReward, int lvl, String achievementType,
			String resourceType, String monsterElement, String monsterQuality,
			int staticDataId, int quantity, int priority, IAchievement prerequisiteAchievement,
			IAchievement successorAchievement) {
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



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getAchievementName()
	 */
	@Override
	public String getAchievementName() {
		return achievementName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setAchievementName(java.lang.String)
	 */
	@Override
	public void setAchievementName(String achievementName) {
		this.achievementName = achievementName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getGemReward()
	 */
	@Override
	public int getGemReward() {
		return gemReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setGemReward(int)
	 */
	@Override
	public void setGemReward(int gemReward) {
		this.gemReward = gemReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getLvl()
	 */
	@Override
	public int getLvl() {
		return lvl;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setLvl(int)
	 */
	@Override
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getAchievementType()
	 */
	@Override
	public String getAchievementType() {
		return achievementType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setAchievementType(java.lang.String)
	 */
	@Override
	public void setAchievementType(String achievementType) {
		this.achievementType = achievementType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getResourceType()
	 */
	@Override
	public String getResourceType() {
		return resourceType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setResourceType(java.lang.String)
	 */
	@Override
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getMonsterElement()
	 */
	@Override
	public String getMonsterElement() {
		return monsterElement;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setMonsterElement(java.lang.String)
	 */
	@Override
	public void setMonsterElement(String monsterElement) {
		this.monsterElement = monsterElement;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getMonsterQuality()
	 */
	@Override
	public String getMonsterQuality() {
		return monsterQuality;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setMonsterQuality(java.lang.String)
	 */
	@Override
	public void setMonsterQuality(String monsterQuality) {
		this.monsterQuality = monsterQuality;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getStaticDataId()
	 */
	@Override
	public int getStaticDataId() {
		return staticDataId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setStaticDataId(int)
	 */
	@Override
	public void setStaticDataId(int staticDataId) {
		this.staticDataId = staticDataId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getQuantity()
	 */
	@Override
	public int getQuantity() {
		return quantity;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setQuantity(int)
	 */
	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getPriority()
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setPriority(int)
	 */
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getPrerequisiteAchievement()
	 */
	@Override
	public IAchievement getPrerequisiteAchievement()
	{
		return prerequisiteAchievement;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setPrerequisiteAchievement(com.lvl6.mobsters.info.IAchievement)
	 */
	@Override
	public void setPrerequisiteAchievement( IAchievement prerequisiteAchievement )
	{
		this.prerequisiteAchievement = prerequisiteAchievement;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#getSuccessorAchievement()
	 */
	@Override
	public IAchievement getSuccessorAchievement()
	{
		return successorAchievement;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IAchievement#setSuccessorAchievement(com.lvl6.mobsters.info.IAchievement)
	 */
	@Override
	public void setSuccessorAchievement( IAchievement successorAchievement )
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
