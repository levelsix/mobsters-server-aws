package com.lvl6.mobsters.services.structure

import com.lvl6.mobsters.common.utils.Director
import com.lvl6.mobsters.dynamo.StructureForUser
import java.util.Date

interface StructureService
{
	enum OrientationKind {
		POSITION_ONE,
		POSITION_TWO
	}
	
	/** 
 	*/
	def void createObstaclesForUser(CreateObstaclesReplyBuilder replyBuilder,
		String userId, Director<CreateObstacleCollectionBuilder> director)
	
	interface CreateObstaclesReplyBuilder
	{
		def CreateObstaclesReplyBuilder resultOk()
	}

	interface CreateObstacleCollectionBuilder
	{
		def CreateObstacleCollectionBuilder addObstacle(
			int obstacleId, float xCoord, float yCoord, OrientationKind orientation
		)
	}

	/** 
 	*/
	def void createStructuresForUser(CreateStructuresReplyBuilder replyBuilder,
		String userId, Director<CreateStructureCollectionBuilder> director)

	interface CreateStructuresReplyBuilder
	{
		def CreateStructuresReplyBuilder resultOk()
	}

	enum ConstructionStatusKind
	{
		INCOMPLETE,
		COMPLETE_AS_EMPTY,
		COMPLETE_AS_FULL
	}

	interface CreateStructureOptionsBuilder
	{
		/** 
		 * Purchase timestamp.  Defaults to TimeUtils.createNow().
		 */
		def CreateStructureOptionsBuilder purchaseTime(Date purchaseTime)

		/** 
		 * Construction progress.  Defaults to INCOMPLETE.
		 */
		def CreateStructureOptionsBuilder constructionStatus(ConstructionStatusKind status)
		
		// def CreateStructureOptionsBuilder orientation(OrientationKind orientation)
		
		/** 
		 * ???
		 */
		def CreateStructureOptionsBuilder fbInviteStructLvl(int fbInviteStructLvl)
	}

	interface CreateStructureCollectionBuilder
	{
		def CreateStructureCollectionBuilder addStructure(
			int structureId, float xCoord, float yCoord)

		def CreateStructureCollectionBuilder addStructure(
			int structureId, float xCoord, float yCoord,
			Director<CreateStructureOptionsBuilder> director)
	}

	/** 
 	*/
	def void beginUpgradingUserStruct(StructureForUser sfu, Date upgradeTime)
}
