package com.lvl6.mobsters.info;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity(name="Quest")
@Table(name="quest")
@Proxy(lazy=true, proxyClass=IQuest.class)
@Cacheable(true)
public class Quest extends BaseIntPersistentObject implements IQuest {

	
	private static final long serialVersionUID = -7293560537221688587L;	

	@Column(name = "quest_name")
	private String questName;
	@Column(name = "description")
	private String description;
	@Column(name = "done_response")
	private String doneResponse;
	
	@Column(name = "accept_dialogue")
	private String acceptDialogue;
	
	@Column(name = "cash_reward")
	private int cashReward;
	@Column(name = "oil_reward")
	private int oilReward;
	@Column(name = "gem_reward")
	private int gemReward;
	@Column(name = "exp_reward")
	private int expReward;
	@Column(name = "monster_id_reward")
	private int monsterIdReward;
	@Column(name = "is_complete_monster")
	private boolean isCompleteMonster;
	@ManyToMany(fetch=FetchType.LAZY, targetEntity=Quest.class)
	@Column(name = "quests_required_for_this")
	private List<IQuest> questsRequiredForThis;
	@Column(name = "quest_giver_name")
	private String questGiverName;
	@Column(name = "quest_giver_image_prefix")
	private String questGiverImagePrefix;
	@Column(name = "priority")
	private int priority;
	@Column(name = "carrot_id")
	private String carrotId;
	@Column(name = "monster_element")
	private String monsterElement;	
	
	@OneToMany(
		cascade={CascadeType.PERSIST, CascadeType.REFRESH},
		fetch=FetchType.EAGER,
		mappedBy="quest", 
		orphanRemoval=true,
		targetEntity=QuestJob.class)
	private List<IQuestJob> questJobs;  
	
	public Quest(){}
	public Quest(int id, String questName, String description, String doneResponse,
			String acceptDialogue, int cashReward, int oilReward, int gemReward,
			int expReward, int monsterIdReward, boolean isCompleteMonster,
			List<IQuest> questsRequiredForThis, String questGiverName,
			String questGiverImagePrefix, int priority, String carrotId,
			String monsterElement, List<IQuestJob> questJobs) {
		super(id);
		this.questName = questName;
		this.description = description;
		this.doneResponse = doneResponse;
		this.acceptDialogue = acceptDialogue;
		this.cashReward = cashReward;
		this.oilReward = oilReward;
		this.gemReward = gemReward;
		this.expReward = expReward;
		this.monsterIdReward = monsterIdReward;
		this.isCompleteMonster = isCompleteMonster;
		this.questsRequiredForThis = questsRequiredForThis;
		this.questGiverName = questGiverName;
		this.questGiverImagePrefix = questGiverImagePrefix;
		this.priority = priority;
		this.carrotId = carrotId;
		this.monsterElement = monsterElement;
		this.questJobs = questJobs;
	}



	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getQuestName()
	 */
	@Override
	public String getQuestName() {
		return questName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setQuestName(java.lang.String)
	 */
	@Override
	public void setQuestName(String questName) {
		this.questName = questName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getDoneResponse()
	 */
	@Override
	public String getDoneResponse() {
		return doneResponse;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setDoneResponse(java.lang.String)
	 */
	@Override
	public void setDoneResponse(String doneResponse) {
		this.doneResponse = doneResponse;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getAcceptDialogue()
	 */
	@Override
	public String getAcceptDialogue() {
		return acceptDialogue;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setAcceptDialogue(java.lang.String)
	 */
	@Override
	public void setAcceptDialogue(String acceptDialogue) {
		this.acceptDialogue = acceptDialogue;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getCashReward()
	 */
	@Override
	public int getCashReward() {
		return cashReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setCashReward(int)
	 */
	@Override
	public void setCashReward(int cashReward) {
		this.cashReward = cashReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getOilReward()
	 */
	@Override
	public int getOilReward() {
		return oilReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setOilReward(int)
	 */
	@Override
	public void setOilReward(int oilReward) {
		this.oilReward = oilReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getGemReward()
	 */
	@Override
	public int getGemReward() {
		return gemReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setGemReward(int)
	 */
	@Override
	public void setGemReward(int gemReward) {
		this.gemReward = gemReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getExpReward()
	 */
	@Override
	public int getExpReward() {
		return expReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setExpReward(int)
	 */
	@Override
	public void setExpReward(int expReward) {
		this.expReward = expReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getMonsterIdReward()
	 */
	@Override
	public int getMonsterIdReward() {
		return monsterIdReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setMonsterIdReward(int)
	 */
	@Override
	public void setMonsterIdReward(int monsterIdReward) {
		this.monsterIdReward = monsterIdReward;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#isCompleteMonster()
	 */
	@Override
	public boolean isCompleteMonster() {
		return isCompleteMonster;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setCompleteMonster(boolean)
	 */
	@Override
	public void setCompleteMonster(boolean isCompleteMonster) {
		this.isCompleteMonster = isCompleteMonster;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getQuestsRequiredForThis()
	 */
	@Override
	public List<IQuest> getQuestsRequiredForThis() {
		 if (questsRequiredForThis == null) {
			 questsRequiredForThis = new ArrayList<IQuest>(2);
		 };
		 
		 return questsRequiredForThis;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setQuestsRequiredForThis(java.util.List)
	 */
	@Override
	public void setQuestsRequiredForThis(final List<IQuest> questsRequiredForThis) {
		this.questsRequiredForThis = questsRequiredForThis;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getQuestGiverName()
	 */
	@Override
	public String getQuestGiverName() {
		return questGiverName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setQuestGiverName(java.lang.String)
	 */
	@Override
	public void setQuestGiverName(String questGiverName) {
		this.questGiverName = questGiverName;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getQuestGiverImagePrefix()
	 */
	@Override
	public String getQuestGiverImagePrefix() {
		return questGiverImagePrefix;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setQuestGiverImagePrefix(java.lang.String)
	 */
	@Override
	public void setQuestGiverImagePrefix(String questGiverImagePrefix) {
		this.questGiverImagePrefix = questGiverImagePrefix;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getPriority()
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setPriority(int)
	 */
	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getCarrotId()
	 */
	@Override
	public String getCarrotId() {
		return carrotId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setCarrotId(java.lang.String)
	 */
	@Override
	public void setCarrotId(String carrotId) {
		this.carrotId = carrotId;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getMonsterElement()
	 */
	@Override
	public String getMonsterElement() {
		return monsterElement;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setMonsterElement(java.lang.String)
	 */
	@Override
	public void setMonsterElement(String monsterElement) {
		this.monsterElement = monsterElement;
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#getQuestJobs()
	 */
	@Override
	public List<IQuestJob> getQuestJobs()
	{
		if( questJobs == null ) {
			questJobs = new ArrayList<IQuestJob>(3);
		}
		
		return questJobs;
	}
	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.info.IQuest#setQuestJobs(java.util.List)
	 */
	@Override
	public void setQuestJobs( List<IQuestJob> questJobs )
	{
		this.questJobs = questJobs;
	}
	
	@Override
	public String toString() {
		return "Quest [id=" + id + ", questName=" + questName
				+ ", description=" + description + ", doneResponse="
				+ doneResponse + ", acceptDialogue=" + acceptDialogue
				+ ", cashReward=" + cashReward + ", oilReward=" + oilReward
				+ ", gemReward=" + gemReward + ", expReward=" + expReward
				+ ", monsterIdReward=" + monsterIdReward
				+ ", isCompleteMonster=" + isCompleteMonster
				+ ", questsRequiredForThis=" + questsRequiredForThis
				+ ", questGiverName=" + questGiverName
				+ ", questGiverImagePrefix=" + questGiverImagePrefix
				+ ", priority=" + priority + ", carrotId=" + carrotId
				+ ", monsterElement=" + monsterElement + "]";
	}

}
