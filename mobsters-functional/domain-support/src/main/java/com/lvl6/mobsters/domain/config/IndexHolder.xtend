package com.lvl6.mobsters.domain.config

import com.googlecode.cqengine.IndexedCollection
import com.lvl6.mobsters.info.Item
import com.lvl6.mobsters.info.Monster
import com.lvl6.mobsters.info.Quest
import com.lvl6.mobsters.info.QuestJobMonsterItem
import com.lvl6.mobsters.info.Task
import com.lvl6.mobsters.info.TaskStage
import com.lvl6.mobsters.info.TaskStageMonster

package class IndexHolder
{
	static val IndexLoader indexLoader =
		ConfigurationRegistry.CONTENT_FOR_HOLDER_TO_INITIALIZE => [
			it.buildConfig()
		]
	
	static val IndexedCollection<Item> cqItems = indexLoader.cqItems
	
	static val IndexedCollection<Task> cqTasks = indexLoader.cqTasks
			
	static val IndexedCollection<Quest> cqQuests = indexLoader.cqQuests
	
	static val IndexedCollection<Monster> cqMonsters = indexLoader.cqMonsters
	
	static val IndexedCollection<TaskStage> cqTaskStages = 
		indexLoader.cqTaskStages
	
	static val IndexedCollection<TaskStageMonster> cqTaskStageMonsters = 
		indexLoader.cqTaskStageMonsters
		
	static val IndexedCollection<QuestJobMonsterItem> cqQuestJobMonsterItems =
		indexLoader.cqQuestJobMonsterItems

	static def IndexedCollection<Item> getCqItems() 
	{
		return IndexHolder::cqItems
	}

	static def IndexedCollection<Task> getCqTasks() 
	{
		return IndexHolder::cqTasks
	}

	static def IndexedCollection<Quest> getCqQuests() 
	{
		return IndexHolder::cqQuests
	}
  
    static def IndexedCollection<Monster> getCqMonsters() 
    {
    	return IndexHolder::cqMonsters
    }
  
    static def IndexedCollection<TaskStage> getCqTaskStages() 
    {
    	return IndexHolder::cqTaskStages
    }
  
    static def IndexedCollection<TaskStageMonster> getCqTaskStageMonsters() 
    {
    	return IndexHolder::cqTaskStageMonsters
    }

	static def IndexedCollection<QuestJobMonsterItem> getCqQuestJobMonsterItems() 
	{
    	return IndexHolder::cqQuestJobMonsterItems
    }
}