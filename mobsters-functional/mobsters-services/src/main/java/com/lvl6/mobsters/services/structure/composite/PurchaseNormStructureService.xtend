package com.lvl6.mobsters.services.structure.composite

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.utility.values.CoordinatePair
import java.util.Date

interface PurchaseNormStructureService {
	
	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	def Pair<User, String> purchaseStructure( String userId, int structId,
			CoordinatePair cp, Date nowDate, int gemsSpent, String resourceType,
			int resourceChange );
	
}