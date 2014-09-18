package com.lvl6.mobsters.domain.config

import com.lvl6.mobsters.info.IMonster
import com.lvl6.mobsters.info.IMonsterLevelInfo
import com.lvl6.mobsters.info.IQuestJob
import com.lvl6.mobsters.info.IQuestJobMonsterItem
import com.lvl6.mobsters.info.ITaskStageMonster
import com.lvl6.mobsters.info.TaskStageMonster
import com.lvl6.mobsters.utility.probability.ProbabilityExtensionLib
import java.util.List
import java.util.Set
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
public class ConfigExtensions {
	val extension ProbabilityExtensionLib probExtension
	
	val extension IConfigurationRegistry configRegistry

	@Autowired
	new( ProbabilityExtensionLib probExtension, IConfigurationRegistry configRegistry )
	{
		this.probExtension = probExtension
		this.configRegistry = configRegistry
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
		return configRegistry.findQjmiByMonsterAndJob(m, qJobs)
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
}
