package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;

import com.lvl6.mobsters.dynamo.MiniJobForUser;

public interface MiniJobForUserRepository extends BaseDynamoRepository<MiniJobForUser>
{

	public List<MiniJobForUser> findByUserIdAndMiniJobForUserIdIn(
		String userId,
		Collection<String> userMiniJobIds );

	public List<MiniJobForUser> findByUserId( String userId );

}