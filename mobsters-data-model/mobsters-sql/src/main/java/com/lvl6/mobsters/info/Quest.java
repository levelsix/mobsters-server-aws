package com.lvl6.mobsters.info;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="quest")
public class Quest extends BasePersistentObject{

	
	private static final long serialVersionUID = 943974595064267438L;	

	//@Column(name = "city_id")
	//	private int cityId;
	@Column(name = "quest_name")
	private String questName;
	@Column(name = "description")
	private String description;
	@Column(name = "done_response")
	private String doneResponse;
	@Column(name = "accept_dialogue")
	private Dialogue acceptDialogue;
	//@Column(name = "quest_type")
	//	private String questType;
	//@Column(name = "job_description")
	//	private String jobDescription;
	//@Column(name = "static_data_id")
	//	private int staticDataId;
	//@Column(name = "quantity")
	//	private int quantity;
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
	@ManyToMany(fetch=FetchType.LAZY)
	@Column(name = "quests_required_for_this")
	private List<Quest> questsRequiredForThis;
	@Column(name = "quest_giver_name")
	private String questGiverName;
	@Column(name = "quest_giver_image_prefix")
	private String questGiverImagePrefix;
	@Column(name = "priority")
	private int priority;
	@Column(name = "carrot_id")
	private String carrotId;
	//@Column(name = "is_achievement")
	//	private boolean isAchievement;
	@Column(name = "monster_element")
	private String monsterElement;	
	public Quest(){}
	public Quest(String id, String questName, String description, String doneResponse,
			Dialogue acceptDialogue, int cashReward, int oilReward, int gemReward,
			int expReward, int monsterIdReward, boolean isCompleteMonster,
			List<Quest> questsRequiredForThis, String questGiverName,
			String questGiverImagePrefix, int priority, String carrotId,
			String monsterElement) {
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
	}



	public String getQuestName() {
		return questName;
	}

	public void setQuestName(String questName) {
		this.questName = questName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDoneResponse() {
		return doneResponse;
	}

	public void setDoneResponse(String doneResponse) {
		this.doneResponse = doneResponse;
	}

	public Dialogue getAcceptDialogue() {
		return acceptDialogue;
	}

	public void setAcceptDialogue(Dialogue acceptDialogue) {
		this.acceptDialogue = acceptDialogue;
	}

	public int getCashReward() {
		return cashReward;
	}

	public void setCashReward(int cashReward) {
		this.cashReward = cashReward;
	}

	public int getOilReward() {
		return oilReward;
	}

	public void setOilReward(int oilReward) {
		this.oilReward = oilReward;
	}

	public int getGemReward() {
		return gemReward;
	}

	public void setGemReward(int gemReward) {
		this.gemReward = gemReward;
	}

	public int getExpReward() {
		return expReward;
	}

	public void setExpReward(int expReward) {
		this.expReward = expReward;
	}

	public int getMonsterIdReward() {
		return monsterIdReward;
	}

	public void setMonsterIdReward(int monsterIdReward) {
		this.monsterIdReward = monsterIdReward;
	}

	public boolean isCompleteMonster() {
		return isCompleteMonster;
	}

	public void setCompleteMonster(boolean isCompleteMonster) {
		this.isCompleteMonster = isCompleteMonster;
	}

	public List<Quest> getQuestsRequiredForThis() {
		return questsRequiredForThis;
	}

	public void setQuestsRequiredForThis(List<Quest> questsRequiredForThis) {
		this.questsRequiredForThis = questsRequiredForThis;
	}

	public String getQuestGiverName() {
		return questGiverName;
	}

	public void setQuestGiverName(String questGiverName) {
		this.questGiverName = questGiverName;
	}

	public String getQuestGiverImagePrefix() {
		return questGiverImagePrefix;
	}

	public void setQuestGiverImagePrefix(String questGiverImagePrefix) {
		this.questGiverImagePrefix = questGiverImagePrefix;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getCarrotId() {
		return carrotId;
	}

	public void setCarrotId(String carrotId) {
		this.carrotId = carrotId;
	}

	public String getMonsterElement() {
		return monsterElement;
	}

	public void setMonsterElement(String monsterElement) {
		this.monsterElement = monsterElement;
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
