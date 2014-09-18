package com.lvl6.mobsters.domain.config

import com.googlecode.cqengine.CQEngine
import com.googlecode.cqengine.IndexedCollection
import com.googlecode.cqengine.attribute.Attribute
import com.googlecode.cqengine.index.hash.HashIndex
import com.googlecode.cqengine.index.unique.UniqueIndex
import com.lvl6.mobsters.info.CQAttrs
import com.lvl6.mobsters.info.Item
import com.lvl6.mobsters.info.Monster
import com.lvl6.mobsters.info.Quest
import com.lvl6.mobsters.info.QuestJobMonsterItem
import com.lvl6.mobsters.info.Task
import com.lvl6.mobsters.info.TaskStage
import com.lvl6.mobsters.info.TaskStageMonster
import java.util.ArrayList
import java.util.List
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

// A class designed for populating all the IndexedCollections needed to serve configuration data,
// but as mutable data structures by necessity.  The @Transactional annotation must be on an object
// instance, not a static method, and the method cannot be a @PostConstruct, because Spring is not 
// yer prepared to satisfy the needs of AOP at that time and @Transactional requires AOP.
//
// The Intialization on Holder Classloading design pattern is how we bridge these requirements.
// Spring instantiates both this class and a Facade class.  This class is injected into the Facade,
// which copies to a static field where the Holder has been coded to find it when it is loaded.
//
// None of the registry lookup routines are used by Spring, so the first caller is a genuine consumer
// and the one to cause the JVM to classload the Holder.  The Holder populates its state in final
// fields and those objects never undergo a state change after its constructor returns, so what the
// IndexLoader constructs as mutable becomes immutable and visible once transfered to the 
// static Holder class.
@Component
package class IndexLoader
{
	val EntityManager entityManager
	
	@Property
	val IndexedCollection<Monster> cqMonsters = 
		CQEngine.newInstance();
	
	@Property
	val IndexedCollection<Item> cqItems = 
		CQEngine.newInstance();
	
	@Property
	val IndexedCollection<Task> cqTasks = 
		CQEngine.newInstance();
	
	@Property
	val IndexedCollection<TaskStage> cqTaskStages = 
		CQEngine.newInstance();
	
	@Property
	val IndexedCollection<TaskStageMonster> cqTaskStageMonsters = 
		CQEngine.newInstance();
		
	@Property
	val IndexedCollection<Quest> cqQuests =
		CQEngine.newInstance()
		
	@Property
	val IndexedCollection<QuestJobMonsterItem> cqQuestJobMonsterItems =
		CQEngine.newInstance()
		
	val ArrayList<IndexLoader.ConfigClassDef<?>> bootstrapData =
		new ArrayList<IndexLoader.ConfigClassDef<?>>(3) => [
			it.add(
				new IndexLoader.ConfigClassDef<Task>(
					"SELECT t FROM Task AS t", CQAttrs.TASK_ID, cqTasks
				)[
					it.prerequisiteTask?.name;
					it.taskMapElements?.forEach[tme| tme.element]
					it.taskStages?.forEach[ts|ts.stageNum]
				]
			)
			
			it.add(
				new IndexLoader.ConfigClassDef<TaskStage>(
					"SELECT ts FROM TaskStage AS ts", CQAttrs.TASKSTAGE_ID, cqTaskStages
				)[it.stageMonsters?.forEach[tsm|tsm.chanceToAppear]]
			)
			
			it.add(
				new IndexLoader.ConfigClassDef<TaskStageMonster>(
					"SELECT tsm FROM TaskStageMonster AS tsm", CQAttrs.TASKSTAGEMONSTER_ID, cqTaskStageMonsters
				)[it.monster?.imagePrefix]
			)
			
			it.add(
				new IndexLoader.ConfigClassDef<Item>(
					"SELECT i FROM Item AS i", CQAttrs.ITEM_ID, cqItems
				)[return]
			)		
			it.add(
				new IndexLoader.ConfigClassDef<Monster>(
					"SELECT m FROM Monster AS m", CQAttrs.MONSTER_ID, cqMonsters
				)[
					it.battleDialogue?.forEach[mbd|mbd.dialogue]
					it.evolutionCatalystMonster?.imagePrefix
					it.evolutionMonster?.imagePrefix
					it.lvlInfo?.forEach[mli|mli.curLvlRequiredExp]
				]
			)

			it.add(
				new IndexLoader.ConfigClassDef<Quest>(
					"SELECT q FROM Quest AS q", CQAttrs.QUEST_ID, cqQuests
				)[
					it.questJobs?.forEach[qj|qj.priority]
					it.questsRequiredForThis?.forEach[qj|qj.cashReward]
				]
			)
			
		]
	
	package static class ConfigClassDef<ConfigClass>
	{
		val String selectQuery
		val Attribute<ConfigClass, Integer> idAttribute
		val IndexedCollection<ConfigClass> indexedCollection
		val (ConfigClass)=>void dehydrationLambda
		
		new(
			String selectQuery, 
			Attribute<ConfigClass,Integer> idAttribute, 
			IndexedCollection<ConfigClass> indexedCollection,
			(ConfigClass)=>void dehydrationLambda )
		{
			this.selectQuery = selectQuery
			this.idAttribute = idAttribute
			this.indexedCollection = indexedCollection
			this.dehydrationLambda = dehydrationLambda
		}
		
		def initialize(EntityManager em) 
		{
			val List<ConfigClass> configObjects = 
				em.createQuery(this.selectQuery)
				.getResultList();
			for (ConfigClass nextObject : configObjects) {
				this.dehydrationLambda.apply(nextObject)
			}
			this.indexedCollection.addIndex(
				UniqueIndex.onAttribute(this.idAttribute))
			this.indexedCollection.addAll(configObjects);
		}
	}
	
	@Autowired
	new( EntityManagerFactory emFactory ) 
	{
		this.entityManager = emFactory.createEntityManager();
	}
		
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	def void buildConfig() {
		for (IndexLoader.ConfigClassDef<?> nextConfigType : bootstrapData) {
			nextConfigType.initialize(entityManager)
		}
		
		// Special Case: QuestJobMonsterItem -- it's a relationship table
		buildSpecialCaseIndices();
	}
	
	private def buildSpecialCaseIndices()
	{
		val List<QuestJobMonsterItem> cfgObjList =
			this.entityManager.createQuery("SELECT qjmi FROM QuestJobMonsterItem AS qjmi")
			.resultList
		for (QuestJobMonsterItem nextQjmi : cfgObjList) {
			nextQjmi.questJob?.description
			nextQjmi.monster?.imagePrefix
			nextQjmi.item?.name
		}
		
		cqQuestJobMonsterItems.addIndex(
			HashIndex.onAttribute(CQAttrs.QUESTJOBMONSTERITEM_MONSTER));
		cqQuestJobMonsterItems.addIndex(
			HashIndex.onAttribute(CQAttrs.QUESTJOBMONSTERITEM_QUEST_JOB));
		cqQuestJobMonsterItems.addAll(cfgObjList)
	}
}