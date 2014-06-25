package com.lvl6.mobsters.info;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="Task")
@Table(name="task")
@Proxy(lazy=true, proxyClass=ITask.class)
public class Task extends BaseIntPersistentObject implements ITask{

	private static final long serialVersionUID = -520057120226567292L;

	@Column(name = "good_name")
	private String goodName;

	@Column(name = "description")
	private String description;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "city_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private City city;

	@Column(name = "asset_number_within_city")
	private int assetNumberWithinCity;

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Task.class)
	@JoinColumn(
		name = "prerequisite_task_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private ITask prerequisiteTask;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(
		name = "prerequisite_quest_id",
		nullable = true,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private Quest prerequisiteQuest;	

	public Task() {
		super();
	}


	public Task(final int id, final String goodName, final String description, final City city,
			final int assetNumberWithinCity, final ITask prerequisiteTask, final Quest prerequisiteQuest) {
		super(id);
		this.goodName = goodName;
		this.description = description;
		this.city = city;
		this.assetNumberWithinCity = assetNumberWithinCity;
		this.prerequisiteTask = prerequisiteTask;
		this.prerequisiteQuest = prerequisiteQuest;
	}
	
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getGoodName()
	 */
	@Override
	public String getGoodName() {
		return goodName;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setGoodName(java.lang.String)
	 */
	@Override
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getCity()
	 */
	@Override
	public City getCity() {
		return city;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setCity(com.lvl6.mobsters.info.City)
	 */
	@Override
	public void setCity(City city) {
		this.city = city;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getAssetNumberWithinCity()
	 */
	@Override
	public int getAssetNumberWithinCity() {
		return assetNumberWithinCity;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setAssetNumberWithinCity(int)
	 */
	@Override
	public void setAssetNumberWithinCity(int assetNumberWithinCity) {
		this.assetNumberWithinCity = assetNumberWithinCity;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getPrerequisiteTask()
	 */
	@Override
	public ITask getPrerequisiteTask() {
		return prerequisiteTask;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setPrerequisiteTask(com.lvl6.mobsters.info.ITask)
	 */
	@Override
	public void setPrerequisiteTask(ITask prerequisiteTask) {
		this.prerequisiteTask = prerequisiteTask;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#getPrerequisiteQuest()
	 */
	@Override
	public Quest getPrerequisiteQuest() {
		return prerequisiteQuest;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.ITask#setPrerequisiteQuest(com.lvl6.mobsters.info.Quest)
	 */
	@Override
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
