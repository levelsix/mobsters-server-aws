package com.lvl6.mobsters.services.structure.composite

import com.lvl6.mobsters.dynamo.User
import java.util.Date
import java.util.Map

interface CollectCurrencyFromNormStructureService {
	
	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	// TODO: Figure out another way to encapsulate data 
	def User collectResources( String userId, int maxCash,
			int maxOil, Date nowDate,
			Map<String, Pair<Date, Integer>> sfuIdToCollectTimeAndAmount );
	
}