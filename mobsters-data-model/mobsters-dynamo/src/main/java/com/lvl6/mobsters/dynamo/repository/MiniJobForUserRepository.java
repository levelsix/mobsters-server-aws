package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.MiniJobForUser;

public interface MiniJobForUserRepository extends
		BaseDynamoCollectionRepository<MiniJobForUser,String> {

	public List<MiniJobForUser> findByUserIdAndMiniJobForUserIdIn(
		String userId,
		Iterable<String> userMiniJobIds );

	public List<MiniJobForUser> findByUserId( String userId );

}