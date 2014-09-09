package com.lvl6.mobsters.domain.config;

import java.util.Set;

import com.lvl6.mobsters.info.IItem;
import com.lvl6.mobsters.info.IMonster;
import com.lvl6.mobsters.info.IQuest;
import com.lvl6.mobsters.info.IQuestJob;
import com.lvl6.mobsters.info.IQuestJobMonsterItem;
import com.lvl6.mobsters.info.ITask;
import com.lvl6.mobsters.info.ITaskStage;
import com.lvl6.mobsters.info.ITaskStageMonster;

public interface IConfigurationRegistry 
{
	// Item
	public IItem getItemMeta(int itemMetaId);

	// Monster
	public IMonster getMonsterMeta(int monsterMetaId);

	// MonsterLevelInfo

	// Task
	public ITask getTaskMeta(int taskMetaId);
	
	// TaskStage
	public ITaskStage getTaskStageMeta(int taskStageMetaId);
	public ITaskStage getTaskStageMeta(Integer taskStageMetaId);
	
	// TaskStageMonster
	public ITaskStageMonster getTaskStageMonsterMeta(int taskStageMonsterMetaId);
	
	// Quest
	public IQuest getQuestMeta( int questId );
	public IQuest getQuestMeta( Integer questId );
	public Iterable<IQuest> getQuestMeta(int[] questMetaIds);
	public Iterable<IQuest> getQuestMeta(Iterable<Integer> questMetaIds);
	public Set<? extends IQuest> getAllQuestMeta();

	// QuestJobMonsterForItem
	public IQuestJobMonsterItem findQjmiByMonsterAndJob(IMonster monster, Set<IQuestJob> jobs);
}
