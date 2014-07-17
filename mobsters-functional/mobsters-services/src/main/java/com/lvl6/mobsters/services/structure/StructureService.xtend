package com.lvl6.mobsters.services.structure

import com.lvl6.mobsters.common.utils.Director
import com.lvl6.mobsters.dynamo.StructureForUser
import com.lvl6.mobsters.info.CoordinatePair
import java.util.Date
import java.util.List

interface StructureService
{
	enum OrientationKind {
		POSITION_ONE,
		POSITION_TWO
	}
	
	/** 
 	*/
	def void createObstaclesForUser(StructureService.CreateObstaclesReplyBuilder replyBuilder,
		String userId, Director<StructureService.CreateObstacleCollectionBuilder> director)
	
	interface CreateObstaclesReplyBuilder
	{
		def StructureService.CreateObstaclesReplyBuilder resultOk()
	}

	interface CreateObstacleCollectionBuilder
	{
		def StructureService.CreateObstacleCollectionBuilder addObstacle(
			int obstacleId, float xCoord, float yCoord, StructureService.OrientationKind orientation
		)
	}

	/** 
 	*/
	def void createStructuresForUser(StructureService.CreateStructuresReplyBuilder replyBuilder,
		String userId, Director<StructureService.CreateStructureCollectionBuilder> director)

	interface CreateStructuresReplyBuilder
	{
		def StructureService.CreateStructuresReplyBuilder resultOk()
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
		def StructureService.CreateStructureOptionsBuilder purchaseTime(Date purchaseTime)

		/** 
		 * Construction progress.  Defaults to INCOMPLETE.
		 */
		def StructureService.CreateStructureOptionsBuilder constructionStatus(StructureService.ConstructionStatusKind status)
		
		// def CreateStructureOptionsBuilder orientation(OrientationKind orientation)
		
		/** 
		 * ???
		 */
		def StructureService.CreateStructureOptionsBuilder fbInviteStructLvl(int fbInviteStructLvl)
	}

	interface CreateStructureCollectionBuilder
	{
		def StructureService.CreateStructureCollectionBuilder addStructure(
			int structureId, float xCoord, float yCoord)

		def StructureService.CreateStructureCollectionBuilder addStructure(
			int structureId, float xCoord, float yCoord,
			Director<StructureService.CreateStructureOptionsBuilder> director)
	}

	/** 
 	*/
	def void beginUpgradingUserStruct(StructureForUser sfu, Date upgradeTime)

	def void moveUserStructure( String userId, String userStructId, CoordinatePair cp );
	
	def void finishConstructingCompletedUserStructures( String userId, List<String> userStructId, Date now );

	/**
	 * TODO: This is NOT a valid service API method--StrucutreForUser cannot appear in the
	 * call signature!
	 */
	def void speedUpConstructingUserStructures( StructureForUser sfu, Date upgradeTime );
}
