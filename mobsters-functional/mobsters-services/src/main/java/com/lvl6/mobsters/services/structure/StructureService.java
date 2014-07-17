package com.lvl6.mobsters.services.structure;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.info.CoordinatePair;
import com.lvl6.mobsters.services.structure.StructureServiceImpl.CreateUserObstaclesSpecBuilderImpl;
import com.lvl6.mobsters.services.structure.StructureServiceImpl.CreateUserStructuresSpecBuilderImpl;

public interface StructureService
{

	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC
//	public StructureForUser getStructureForUserIdAndId(String userId, String structureForUserId);

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	
	public abstract void createObstaclesForUser(
		String userId,
		CreateUserObstaclesSpec createSpec );

	public interface CreateUserObstaclesSpecBuilder
	{
		public CreateUserObstaclesSpec build();

		public CreateUserObstaclesSpecBuilder setObstacleId(
			String userObstacleId,
			int obstacleId );

		public CreateUserObstaclesSpecBuilder setXCoord( String userObstacleId, int xCoord );

		public CreateUserObstaclesSpecBuilder setYCoord( String userObstacleId, int yCoord );

		public CreateUserObstaclesSpecBuilder setOrientation(
			String userObstacleId,
			String orientation );

	}

	public class CreateUserObstaclesSpec
	{
		// the end state: objects to be saved to db
		final private Map<String, ObstacleForUser> userObstacleIdToOfu;

		CreateUserObstaclesSpec( Map<String, ObstacleForUser> userObstacleIdToOfu )
		{
			this.userObstacleIdToOfu = userObstacleIdToOfu;
		}

		Map<String, ObstacleForUser> getUserObstacleIdToOfu()
		{
			return userObstacleIdToOfu;
		}

		public static CreateUserObstaclesSpecBuilder builder()
		{
			return new CreateUserObstaclesSpecBuilderImpl();
		}
	}

	/**************************************************************************/

	public abstract void createStructuresForUser(
		String userId,
		CreateUserStructuresSpec createSpec );

	public interface CreateUserStructuresSpecBuilder
	{
		public CreateUserStructuresSpec build();

		public CreateUserStructuresSpecBuilder setStructureId(
			String userStructureId,
			int obstacleId );

		public CreateUserStructuresSpecBuilder setXCoord( String userStructureId, float xCoord );

		public CreateUserStructuresSpecBuilder setYCoord( String userStructureId, float yCoord );

		public CreateUserStructuresSpecBuilder setLastRetrievedTime(
			String userStructureId,
			Date lastRetrieved );

		public CreateUserStructuresSpecBuilder setPurchaseTime(
			String userStructureId,
			Date purchaseTime );

		public CreateUserStructuresSpecBuilder setComplete(
			String userStructureId,
			boolean isComplete );

		public CreateUserStructuresSpecBuilder setFbInviteStructLvl(
			String userStructureId,
			int fbInviteStructLvl );

	}

	public class CreateUserStructuresSpec
	{
		// the end state: objects to be saved to db
		final private Map<String, StructureForUser> userStructureIdToOfu;

		CreateUserStructuresSpec( Map<String, StructureForUser> userStructureIdToOfu )
		{
			this.userStructureIdToOfu = userStructureIdToOfu;
		}

		Map<String, StructureForUser> getUserStructureIdToOfu()
		{
			return userStructureIdToOfu;
		}

		public static CreateUserStructuresSpecBuilder builder()
		{
			return new CreateUserStructuresSpecBuilderImpl();
		}
	}

	/**************************************************************************/

	public void beginUpgradingUserStruct( StructureForUser sfu, Date upgradeTime );

	public void moveUserStructure( String userId, String userStructId, CoordinatePair cp );
	
	public void finishConstructingUserStructures( String userId, List<String> userStructId, Date now );

}
