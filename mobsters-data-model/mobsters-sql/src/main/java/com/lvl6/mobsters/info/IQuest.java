package com.lvl6.mobsters.info;

import java.util.List;

public interface IQuest extends IBaseIntPersistentObject {

	public String getQuestName();

	public void setQuestName(String questName);

	public String getDescription();

	public void setDescription(String description);

	public String getDoneResponse();

	public void setDoneResponse(String doneResponse);

	public String getAcceptDialogue();

	public void setAcceptDialogue(String acceptDialogue);

	public int getCashReward();

	public void setCashReward(int cashReward);

	public int getOilReward();

	public void setOilReward(int oilReward);

	public int getGemReward();

	public void setGemReward(int gemReward);

	public int getExpReward();

	public void setExpReward(int expReward);

	public int getMonsterIdReward();

	public void setMonsterIdReward(int monsterIdReward);

	public boolean isCompleteMonster();

	public void setCompleteMonster(boolean isCompleteMonster);

	public List<IQuest> getQuestsRequiredForThis();

	public void setQuestsRequiredForThis(List<IQuest> questsRequiredForThis);

	public String getQuestGiverName();

	public void setQuestGiverName(String questGiverName);

	public String getQuestGiverImagePrefix();

	public void setQuestGiverImagePrefix(String questGiverImagePrefix);

	public int getPriority();

	public void setPriority(int priority);

	public String getCarrotId();

	public void setCarrotId(String carrotId);

	public String getMonsterElement();

	public void setMonsterElement(String monsterElement);

	public List<IQuestJob> getQuestJobs();

	public void setQuestJobs(List<IQuestJob> questJobs);
}