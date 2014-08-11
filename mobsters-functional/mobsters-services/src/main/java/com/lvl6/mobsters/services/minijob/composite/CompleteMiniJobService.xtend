package com.lvl6.mobsters.services.minijob.composite

import com.lvl6.mobsters.dynamo.User
import java.util.Date

interface CompleteMiniJobService {
	
	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	def User completeMiniJob( String userId, String userMiniJobId,
			Date nowDate, int gemsSpent );
	
}