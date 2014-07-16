package com.lvl6.mobsters.services.structure;

import java.util.Date;
import java.util.Map;

import com.lvl6.mobsters.common.utils.Director;
import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.StructureForUser;

public interface StructureService {

	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC
//	public StructureForUser getStructureForUserIdAndId(String userId, String structureForUserId);

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	
	public void createObstaclesForUser(
		CreateObstaclesReplyBuilder replyBuilder,
		String userId,
		Director<CreateObstacleCollectionBuilder> director);
	
	public interface CreateObstaclesReplyBuilder {
		public CreateObstaclesReplyBuilder resultOk();
	}
	
	public interface CreateObstacleCollectionBuilder {
		public CreateObstacleCollectionBuilder addStructure(
			int obstacleId, float xCoord, float yCoord, String orientation);
	}

	/**************************************************************************/

	public void createStructuresForUser(
		CreateStructuresReplyBuilder replyBuilder,
		String userId,
		Director<CreateStructureCollectionBuilder> director);

	public interface CreateStructuresReplyBuilder {	
		public CreateStructuresReplyBuilder resultOk();
	}

	public enum ConstructionStatusKind {
		INCOMPLETE,
		COMPLETE_AS_EMPTY,
		COMPLETE_AS_FULL
	}

	public interface CreateStructureOptionsBuilder {
		/**
		 * Purchase timestamp.  Defaults to TimeUtils.createNow().
		 * 
		 * @param purchaseTime
		 */
		CreateStructureOptionsBuilder purchaseTime(Date purchaseTime);

		/**
		 * Construction progress.  Defaults to INCOMPLETE.
		 * 
		 * @param status
		 */
		CreateStructureOptionsBuilder constructionStatus(ConstructionStatusKind status);

		/**
		 * ???
		 * 
		 * @param fbInviteStructLvl
		 */
		CreateStructureOptionsBuilder fbInviteStructLvl(int fbInviteStructLvl);
	}

	public interface CreateStructureCollectionBuilder {
		public CreateStructureCollectionBuilder addStructure(
			int structureId, float xCoord, float yCoord);

		public CreateStructureCollectionBuilder addStructure(
			int structureId, float xCoord, float yCoord,
			Director<CreateStructureOptionsBuilder> director);
	}
	/**************************************************************************/

	public void beginUpgradingUserStruct( StructureForUser sfu, Date upgradeTime );

}
