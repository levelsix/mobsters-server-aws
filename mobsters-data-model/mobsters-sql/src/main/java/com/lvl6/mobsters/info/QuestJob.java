package com.lvl6.mobsters.info;

import javax.persistence.Cacheable;
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

//June 25, 2014
//At the moment, one quest_job per quest
//At the moment, one quest_job for a task, one-to-one

@Entity(name="QuestJob")
@Table(name="quest_job")
@Proxy(lazy=true, proxyClass=IQuestJob.class)
@Cacheable(true)
public class QuestJob extends BaseIntPersistentObject implements IQuestJob
{
	private static final long serialVersionUID = -7270821507727387958L;

	@ManyToOne(fetch=FetchType.LAZY, targetEntity=Quest.class)
	@JoinColumn(
		name = "quest_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private IQuest quest;
	
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

	@OneToOne(fetch=FetchType.LAZY, targetEntity=Task.class, optional=false)
	@JoinColumn(
		name = "task_id",
		nullable = false,
		foreignKey=@ForeignKey(name="none", value=ConstraintMode.NO_CONSTRAINT))
	private ITask task;
	
	public QuestJob(){}
	public QuestJob(int id, IQuest quest, String questJobType,
			String description, int staticDataId, int quantity, int priority,
			ITask task) {
		super(id);
		this.quest = quest;
		this.questJobType = questJobType;
		this.description = description;
		this.staticDataId = staticDataId;
		this.quantity = quantity;
		this.priority = priority;
		this.task = task;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#getQuest()
	 */
	@Override
	public IQuest getQuest()
	{
		return quest;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuestJob#setQuest(com.lvl6.mobsters.info.Quest)
	 */
	@Override
	public void setQuest( IQuest quest )
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

	@Override
	public ITask getTask()
	{
		return task;
	}
	@Override
	public void setTask( ITask task )
	{
		this.task = task;
	}
	
	@Override
	public String toString() {
		return "QuestJob [id=" + id + ", questId=" + quest
				+ ", questJobType=" + questJobType + ", description="
				+ description + ", staticDataId=" + staticDataId
				+ ", quantity=" + quantity + ", priority=" + priority + "]";
	}
	
}
