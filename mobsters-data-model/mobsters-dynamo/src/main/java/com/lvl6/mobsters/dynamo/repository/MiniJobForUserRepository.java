package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;

import com.lvl6.mobsters.dynamo.MiniJobForUser;

@Component
public interface MiniJobForUserRepository extends
		BaseDynamoCollectionRepository<MiniJobForUser,String> {

	public List<MiniJobForUser> findByUserIdAndId(
		String userId,
		Collection<String> userMiniJobIds );

	public List<MiniJobForUser> findByUserId( String userId );

}