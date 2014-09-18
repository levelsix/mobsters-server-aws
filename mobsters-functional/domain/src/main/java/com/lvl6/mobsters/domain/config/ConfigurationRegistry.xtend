package com.lvl6.mobsters.domain.config

import com.lvl6.mobsters.info.CQAttrs
import com.lvl6.mobsters.info.IItem
import com.lvl6.mobsters.info.IMonster
import com.lvl6.mobsters.info.IQuest
import com.lvl6.mobsters.info.IQuestJob
import com.lvl6.mobsters.info.ITask
import com.lvl6.mobsters.info.ITaskStage
import com.lvl6.mobsters.info.TaskStageMonster
import java.util.List
import java.util.Set
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.googlecode.cqengine.query.QueryFactory.*

@Component
package class ConfigurationRegistry 
	implements IConfigurationRegistry 
{
	@Property
	var IndexLoader uninitializedIndexLoader
	
	package static var IndexLoader CONTENT_FOR_HOLDER_TO_INITIALIZE
	
	@Autowired
	package new( IndexLoader uninitializedIndexLoader )
	{
		_uninitializedIndexLoader = uninitializedIndexLoader
		CONTENT_FOR_HOLDER_TO_INITIALIZE = _uninitializedIndexLoader
	}
	
	//
	// IMonster
	//
	
	public override IMonster getMonsterMeta( int monsterId )
	{
		return 
			IndexHolder.cqMonsters
			.retrieve(
				equal(CQAttrs.MONSTER_ID, monsterId)
			).findFirst[true]
	}

	public def IMonster getMonsterMeta( Integer monsterId )
	{
		return monsterId.intValue.getMonsterMeta
	}

	public def Iterable<IMonster> getMonsterMeta( int[] monsterIds )
	{
		return monsterIds.map[it.getMonsterMeta]
	}

	public def Iterable<IMonster> getMonsterMeta( List<Integer> monsterIds )
	{
		return monsterIds.map[it.intValue.getMonsterMeta]
	}
	
	public def Set<? extends IMonster> getAllMonsterMeta() {
		return IndexHolder.cqMonsters.toSet
	}

	//
	// IItem		
	//
	
	public def override IItem getItemMeta( int itemId )
	{
		return 
			IndexHolder.cqItems.retrieve(
				equal(CQAttrs.ITEM_ID, itemId)
			).findFirst[true]
	}

	public def IItem getItemMeta(
		Integer itemId )
	{
		return itemId.intValue.getItemMeta
	}

	public def Iterable<IItem> getItemMeta(
		int[] itemIds )
	{
		return itemIds.map[it.getItemMeta]
	}

	public def Iterable<IItem> getItemMeta(
		List<Integer> itemIds )
	{
		return itemIds.map[it.intValue.getItemMeta]
	}
	
	public def Set<? extends IItem> getAllItemMeta() {
		return IndexHolder.cqItems.toSet
	}

	//
	// ITask
	//
	
	public override ITask getTaskMeta(int itemId)
	{
		return 
			IndexHolder.cqTasks
			.retrieve(
				equal(CQAttrs.TASK_ID, itemId)
			).findFirst[true]
	}

	public def ITask getTaskMeta(Integer itemId)
	{
		return itemId.intValue.getTaskMeta
	}

	public def Iterable<ITask> getTaskMeta(int[] itemIds)
	{
		return itemIds.map[it.getTaskMeta]
	}

	public def Iterable<ITask> getTaskMeta(List<Integer> itemIds)
	{
		return itemIds.map[it.intValue.getTaskMeta]
	}
	
	public def Set<? extends ITask> getAllTaskMeta() {
		return IndexHolder.cqTasks.toSet
	}
		
	// 
	// TaskStage
	//
	
	public def override ITaskStage getTaskStageMeta(
		int itemStageId )
	{
		return 
			IndexHolder.cqTaskStages
			.retrieve(
				equal(CQAttrs.TASKSTAGE_ID, itemStageId)
			).findFirst[true]
	}

	public def override ITaskStage getTaskStageMeta(
		Integer itemStageId )
	{
		return itemStageId.intValue.getTaskStageMeta
	}

	public def Iterable<ITaskStage> getTaskStageMeta(
		int[] itemStageIds )
	{
		return itemStageIds.map[it.getTaskStageMeta]
	}

	public def Iterable<ITaskStage> getTaskStageMeta(
		List<Integer> itemStageIds )
	{
		return itemStageIds.map[it.intValue.getTaskStageMeta]
	}

	public def Set<? extends ITaskStage> getAllTaskStageMeta() {
		return IndexHolder.cqTaskStages.toSet
	}
	
	// 
	// TaskStageMonster
	//
	
	public def override TaskStageMonster getTaskStageMonsterMeta( int taskStageMonsterId )
	{
		return 
			IndexHolder.cqTaskStageMonsters
			.retrieve(
				equal(CQAttrs.TASKSTAGEMONSTER_ID, taskStageMonsterId)
			).findFirst[true]
	}

			public def TaskStageMonster getTaskStageMonsterMeta(
		Integer itemStageId )
	{
		return itemStageId.intValue.getTaskStageMonsterMeta
	}

	public def Iterable<TaskStageMonster> getTaskStageMonsterMeta(
		int[] itemStageIds )
	{
		return itemStageIds.map[it.getTaskStageMonsterMeta]
	}

	public def Iterable<TaskStageMonster> getTaskStageMonsterMeta(
		List<Integer> itemStageIds )
	{
		return itemStageIds.map[it.intValue.getTaskStageMonsterMeta]
	}

	public def Set<? extends TaskStageMonster> getAllTaskStageMonsterMeta() {
		return IndexHolder.cqTaskStageMonsters.toSet
	}
	
	//
	// IQuest
	//
	
	public override IQuest getQuestMeta( int questId )
	{
		return IndexHolder.cqQuests.retrieve(
			equal(CQAttrs.QUEST_ID, questId)
		).findFirst[true]
	}
	
	override IQuest getQuestMeta( Integer questId )
	{
		return questId.intValue.getQuestMeta
	}

	override Iterable<IQuest> getQuestMeta( int[] questIds )
	{
		return questIds.map[it.getQuestMeta]
	}

	override Iterable<IQuest> getQuestMeta( Iterable<Integer> questIds )
	{
		return questIds.map[it.intValue.getQuestMeta]
	}
	
	// TODO: Consider holding a second immutable set to answer queries for "all".
	override Set<? extends IQuest> getAllQuestMeta() {
		return IndexHolder.cqQuests.toSet
	}
	
	//
	// IQuestJobMonsterItem
	//
	
	override findQjmiByMonsterAndJob(IMonster monster, Set<IQuestJob> qJobs) {
		IndexHolder.cqQuestJobMonsterItems.retrieve(
			and(
				equal(CQAttrs.QUESTJOBMONSTERITEM_MONSTER, monster),
				in(CQAttrs.QUESTJOBMONSTERITEM_QUEST_JOB, qJobs)
			)
		).findFirst[true]
	}	
}