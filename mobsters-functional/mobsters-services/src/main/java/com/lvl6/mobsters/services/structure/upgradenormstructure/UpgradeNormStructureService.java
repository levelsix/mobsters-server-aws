package com.lvl6.mobsters.services.structure.upgradenormstructure;

import java.util.Date;

import com.lvl6.mobsters.dynamo.User;

public interface UpgradeNormStructureService {

	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	public User upgradeNormStructure( String userId, String userStructId,
			int gemsSpent, String rt, int resourceChange, Date timeOfUpgrade );
	
	public User speedUpConstructingUserStruct( String userId, String userStructId, int gemCost, Date now );
}
