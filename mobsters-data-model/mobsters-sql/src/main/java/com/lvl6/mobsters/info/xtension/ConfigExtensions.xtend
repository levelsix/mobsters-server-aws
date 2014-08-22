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
import com.lvl6.mobsters.info.Obstacle
import com.lvl6.mobsters.info.Quest
import com.lvl6.mobsters.info.QuestJobMonsterItem
import com.lvl6.mobsters.info.Structure
import com.lvl6.mobsters.info.Task
import com.lvl6.mobsters.info.TaskStageMonster
import com.lvl6.mobsters.info.repository.MonsterRepository
import com.lvl6.mobsters.info.repository.ObstacleRepository
import com.lvl6.mobsters.info.repository.QuestJobMonsterItemRepository
import com.lvl6.mobsters.info.repository.QuestRepository
import com.lvl6.mobsters.info.repository.StructureRepository
import com.lvl6.mobsters.info.repository.TaskRepository
import com.lvl6.mobsters.utility.indexing.by_int.AbstractIntComparable
import com.lvl6.mobsters.utility.indexing.by_int.IntKeyIndex
import com.lvl6.mobsters.utility.probability.ProbabilityExtensionLib
import java.util.List
import java.util.Set
import javax.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
public class ConfigExtensions {
	private static final Logger LOG = LoggerFactory.getLogger(ConfigExtensions)

	val extension ProbabilityExtensionLib probExtension
	val RepoDependencies repoDependencies;

	@Autowired
	new(ProbabilityExtensionLib probExtension,
		TaskRepository taskRepo,
		QuestRepository questRepo,
		MonsterRepository monsterRepo,
		QuestJobMonsterItemRepository qjmiRepo,
		StructureRepository structureRepo,
		ObstacleRepository obstacleRepo)
	{
		this.probExtension = probExtension
		this.repoDependencies =
			new RepoDependencies(
				taskRepo, questRepo, monsterRepo, qjmiRepo, structureRepo, obstacleRepo)
	}
		
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
		List<ITaskStageMonster> candidateList, String elementName)
	{
		return candidateList.findFirst[return it.monster.element == elementName]
	}
	
	public def IQuestJobMonsterItem rollForDroppedItem(
		IMonster m, Set<IQuestJob> qJobs)
	{
		return
			LoadMapHolderIdiom.MAPS.qjmiLookupMap
				.values
				.filter[it.monster === m && qJobs.contains(it.questJob)]
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

	
	/** Configuration Lookup Indices ***********/
	
	public def ITask getTaskMeta(int taskId)
	{
		return LoadMapHolderIdiom.MAPS.taskLookupMap.get(taskId)
	}

	public def ITask getTaskMeta(Integer taskId)
	{
		return LoadMapHolderIdiom.MAPS.taskLookupMap.get(taskId)
	}

	public def ITask getTaskMeta(AbstractIntComparable taskId)
	{
		return LoadMapHolderIdiom.MAPS.taskLookupMap.get(taskId)
	}

	public def Iterable<ITask> getTaskMeta(int[] taskIds)
	{
		return taskIds.map[LoadMapHolderIdiom.MAPS.taskLookupMap.get(it)]
	}

	public def Iterable<ITask> getTaskMeta(List<Integer> taskIds)
	{
		return taskIds.map[LoadMapHolderIdiom.MAPS.taskLookupMap.get(it)]
	}

	public def Iterable<ITask> getTaskMeta(Iterable<AbstractIntComparable> taskIds)
	{
		return taskIds.map[LoadMapHolderIdiom.MAPS.taskLookupMap.get(it)]
	}
	
	public def Set<? extends ITask> getAllTaskMeta() {
		return LoadMapHolderIdiom.MAPS.taskLookupMap.values();
	}


	// TODO: Expand all 6 method variants
	public def IQuest getQuestMeta(int questId)
	{
		return LoadMapHolderIdiom.MAPS.questLookupMap.get(questId)
	}

	public def Iterable<Quest> getQuestMeta(int[] questIds)
	{
		return questIds.map[LoadMapHolderIdiom.MAPS.questLookupMap.get(it)]
	}


	// TODO: Expand all 6 method variants
	public def IMonster getMonsterMeta(int monsterId)
	{
		return LoadMapHolderIdiom.MAPS.monsterLookupMap.get(monsterId)
	}

	public def Iterable<Monster> getMonsterMeta(int[] monsterIds)
	{
		return monsterIds.map[LoadMapHolderIdiom.MAPS.monsterLookupMap.get(it)]
	}


	// TODO: Expand all 6 method variants
	public def IQuestJobMonsterItem getQjmiMeta(int qjmiId)
	{
		return LoadMapHolderIdiom.MAPS.qjmiLookupMap.get(qjmiId)
	}


	// TODO: Expand all 6 method variants
	public def IStructure getStructreMeta(int structureId)
	{
		return LoadMapHolderIdiom.MAPS.structureLookupMap.get(structureId)
	}


	// TODO: Expand all 6 method variants
	public def IObstacle getObstacleMeta(int obstacleId)
	{
		return LoadMapHolderIdiom.MAPS.obstacleLookupMap.get(obstacleId)
	}
	
	package static var RepoDependencies REPO_DEPS_FOR_LOOKUP_MAPS = null

	// To use @PostConstruct, a kind of lazy initialization, we'd need to safeguard the thread visibility
	// of each map and its contents, requiring application of either the "Initialization On Demand Holder"
	// idiom, volatile fields and double-checked locking, or synchronization.
	@PostConstruct
	package def void populateLookupMaps() {
		com.lvl6.mobsters.info.xtension.ConfigExtensions.REPO_DEPS_FOR_LOOKUP_MAPS = this.repoDependencies
		
		// Trigger classLoading of LookupMaps here, so it can populate its static final RepoDependencies
		// reference while we're on a thread that has guaranteed visibility on the value just set above.
		// 
		// This code is executing while Spring is evaluating bean post-processors, so we cannot go so
		// far as to access LoadMapHolderIdiom, since the aspects required for transactions have not
		// yet been attached.  Loading LookupMaps here, however, at least establishes the preconditions
		// for any other thread to initialize LoadMapHolderIdiom correctly once this method returns.
		if (typeof(LookupMaps) == null) {
			LOG.error("Could not load LookupMaps.  This should never happen.")
		}
		
		return
	}
}
	
package class RepoDependencies 
{
	@Property
	val TaskRepository taskRepo
	@Property
	val QuestRepository questRepo
	@Property
	val MonsterRepository monsterRepo
	@Property
	val QuestJobMonsterItemRepository qjmiRepo
	@Property
	val StructureRepository structureRepo
	@Property
	val ObstacleRepository obstacleRepo

	new(TaskRepository taskRepo,
		QuestRepository questRepo,
		MonsterRepository monsterRepo,
		QuestJobMonsterItemRepository qjmiRepo,
		StructureRepository structureRepo,
		ObstacleRepository obstacleRepo)
	{
		this._taskRepo = taskRepo
		this._questRepo = questRepo
		this._monsterRepo = monsterRepo
		this._qjmiRepo = qjmiRepo
		this._structureRepo = structureRepo
		this._obstacleRepo = obstacleRepo
	}
}

package class LookupMaps {
	@Property
	val IntKeyIndex<Task> taskLookupMap	
	@Property
	val IntKeyIndex<Quest> questLookupMap
	@Property
	val IntKeyIndex<Monster> monsterLookupMap
	@Property
	val IntKeyIndex<QuestJobMonsterItem> qjmiLookupMap
	@Property
	val IntKeyIndex<Structure> structureLookupMap
	@Property
	val IntKeyIndex<Obstacle> obstacleLookupMap

    package static val RepoDependencies REPO_DEPS_FOR_HOLDER = 
    	ConfigExtensions.REPO_DEPS_FOR_LOOKUP_MAPS

	new()
	{
		this._taskLookupMap = IntKeyIndex.createIndex()
		this._questLookupMap = IntKeyIndex.createIndex()
		this._monsterLookupMap = IntKeyIndex.createIndex()
		this._qjmiLookupMap = IntKeyIndex.createIndex()
		this._structureLookupMap = IntKeyIndex.createIndex()
		this._obstacleLookupMap = IntKeyIndex.createIndex()
	}
	
	@Transactional(
		propagation=Propagation::REQUIRED,
		isolation=Isolation::READ_COMMITTED,
		readOnly=true
	)
	def void initialize(RepoDependencies repoDeps)
	{
		// Read Task config and construct taskMap contents.
		//
		// Lazy-Loading Guidelines:
		// -- For each association:
		//    -- Path 1: Dead End
		//       -- If the association's type is either:
		//	        -- A root configuration class (possibly the same being handled, possibly not)
		//          -- Not a root configuration object, but it has no associations of its own
		//       -- Then pick a value property _other than id_ on the target object and read it.
		//    -- Path 2: Cascade   
		//       -- If the association's type is a non-configuration root with associations, cascade.
		//       -- Use a with block (=> [ ]) to access any associations of the referenced object
		//          by recursively applying this procedure.
		// -- For each collection:
		//    -- Path 1: Dead End
		//       -- If the association's type is either:
		//	        -- A root configuration class (possibly the same being handled, possibly not)
		//          -- Not a root configuration object, but it has no associations of its own
		//       -- Then access the collection and call '.iterator.hasNext'
		//    -- Path 2: Cascade   
		//       -- If the association's type is a non-configuration root with associations, cascade.
		//       -- Access the collection with a forEach lambda and recursively apply this procedure to
		//          each member object.
		//
		// ** NOTE ** Be careful about nulls!  A best-practice recommendation for dealing with
		//            instructions about accessing a value property--pick one with a non-primitive
		//            Object return type, such as String, so you can use '?.' conditional access
		//            notation.
		repoDeps.taskRepo.findAll().forEach[
			_taskLookupMap.put(
				it => [
					it.prerequisiteTask?.name
					it.taskMapElements?.iterator().hasNext()
					it.taskStages?.forEach[
						it.stageMonsters?.forEach[
							it.monster?.displayName
						]
					]
				]
			)
		]
		
		repoDeps.questRepo.findAll().forEach[
			_questLookupMap.put(
				it => [
				]
			)
		]

		return
	}
}

package final class LoadMapHolderIdiom {
	package static final LookupMaps MAPS =
		new LookupMaps() => [
			it.initialize(LookupMaps.REPO_DEPS_FOR_HOLDER)
		]
		
	public static def LookupMaps getInstance()
	{
		return MAPS
	}
}