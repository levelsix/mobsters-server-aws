package com.lvl6.mobsters.services.structure;

import java.util.Date;

import com.lvl6.mobsters.dynamo.User;


public interface BeginObstacleRemovalService
{

	// NON CRUD LOGIC

	/**************************************************************************/
	// CRUD LOGIC
	
	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/
	public User initiateRemoveObstacle(String userId, String userObstacleId,
		Date clientTime, int gemsSpent, String resourceType, int resourceChange);

	
}
