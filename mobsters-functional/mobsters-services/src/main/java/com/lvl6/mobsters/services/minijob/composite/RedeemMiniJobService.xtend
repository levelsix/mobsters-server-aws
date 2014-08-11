package com.lvl6.mobsters.services.minijob.composite

import com.lvl6.mobsters.dynamo.User
import java.util.Date
import java.util.Map

interface RedeemMiniJobService {
	
	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	def User redeemMiniJob( String userId, String userMiniJobId, Date nowDate,
		int maxCash, int maxOil, Map<String, Integer> mfuIdToHealth
	);
	
}