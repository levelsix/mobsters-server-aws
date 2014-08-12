package com.lvl6.mobsters.services.facebookinvite

import java.util.Date
import java.util.Map

interface FacebookInviteService {
	
	// NON CRUD LOGIC

	/**************************************************************************/

	// BEGIN READ ONLY LOGIC

	// END READ ONLY LOGIC
	/**************************************************************************/

	// TRANSACTIONAL LOGIC
	def void inviteFbUsers( String userId,
 		Map<String, Pair<String, Integer>> fbIdToUserStruct,
		Date nowDate );
	
}