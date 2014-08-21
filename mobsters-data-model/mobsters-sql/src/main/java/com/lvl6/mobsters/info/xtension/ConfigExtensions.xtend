package com.lvl6.mobsters.info.xtension

import com.lvl6.mobsters.info.IMonster
import com.lvl6.mobsters.info.IMonsterLevelInfo
import com.lvl6.mobsters.info.IObstacle
import com.lvl6.mobsters.info.IQuest
import com.lvl6.mobsters.info.IQuestJob
import com.lvl6.mobsters.info.IQuestJobMonsterItem
import com.lvl6.mobsters.info.IStructure
import com.lvl6.mobsters.info.ITask
import com.lvl6.mobsters.info.ITaskStageMonster
import com.lvl6.mobsters.info.Monster
import com.lvl6.mobsters.info.Quest
import com.lvl6.mobsters.info.repository.MonsterRepository
import com.lvl6.mobsters.info.repository.ObstacleRepository
import com.lvl6.mobsters.info.repository.QuestJobMonsterItemRepository
import com.lvl6.mobsters.info.repository.QuestRepository
import com.lvl6.mobsters.info.repository.StructureRepository
import com.lvl6.mobsters.info.repository.TaskRepository
import com.lvl6.mobsters.utility.probability.ProbabilityExtensionLib
import java.util.List
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
public class ConfigExtensions {
	@Autowired
	var extension ProbabilityExtensionLib probExtension
	
	@Autowired
	var TaskRepository taskRepo
	
	@Autowired
	var QuestRepository questRepo
	
	@Autowired
	var MonsterRepository monsterRepo
	
	@Autowired
	var StructureRepository structureRepo
	
	@Autowired
	var ObstacleRepository obstacleRepo
	
	@Autowired
	var QuestJobMonsterItemRepository qjmiRepo
		
	/** Monster Extensions *********************/
	public def IMonsterLevelInfo getFirstLevelInfo( IMonster m ) {
		// TODO: This should be as simple as it.lvlInfo.get(0).  
		//        If that didn't work, lets figure out why.
		return
			m.lvlInfo.reduce[min, next|
				if (next.level < min.level)
					return next
				else
					return min
			]
	}
		
	public def ITaskStageMonster selectElementMonster(
		List<ITaskStageMonster> candidateList, String elementName
	)
	{
		return candidateList.findFirst[return it.monster.element == elementName]
	}
	
	public def IQuestJobMonsterItem rollForDroppedItem(
		IMonster m, Iterable<IQuestJob> qJobs)
	{
		return qjmiRepo.findByMonsterAndQuestJobIn(m, qJobs)
			.selectFirstIndependentEvent[return it.itemDropRate]		
	}
	
	/** Task Extensions ************************/
	  
	public def int rollCashDrop(TaskStageMonster stageMonster)
	{
		return rollValueInRange(stageMonster.minCashDrop, stageMonster.maxCashDrop)
	}

	public def int rollOilDrop(TaskStageMonster stageMonster)
	{
		return rollValueInRange(stageMonster.minOilDrop, stageMonster.maxOilDrop)
	}

	public def boolean didPuzzlePieceDrop(TaskStageMonster stageMonster)
	{
		return (nextFloat() < stageMonster.puzzlePieceDropRate)
	}

	
	/** Configuration Lookup Indices */

	public def ITask getTaskMeta(int taskId) {
		return taskRepo.findById(taskId)
	}

	public def IQuest getQuestMeta(int questId) {
		return questRepo.findById(questId)
	}

	public def List<Quest> getQuestMeta(int[] questIds) {
		return questRepo.findByIdIn(questIds)
	}

	public def IMonster getMonsterMeta(int monsterId) {
		return monsterRepo.findById(monsterId)
	}

	public def List<Monster> getMonsterMeta(int[] monsterIds) {
		return monsterRepo.findByIdIn(monsterIds)
	}

	public def IStructure getStructreMeta(int structureId) {
		return structureRepo.findById(structureId)
	}

	public def IObstacle getObstacleMeta(int obstacleId) {
		return obstacleRepo.findById(obstacleId)
	}

//	@PostConstruct
//	def void doInitExtension() {
//		// Read Task config and construct taskMap contents.
//		taskDefRepo.findAll().forEach[Task s|taskContextMap.put(s)]
//		return
//	}
}