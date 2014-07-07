package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import com.lvl6.mobsters.dynamo.AchievementForUser;

public interface AchievementForUserRepository extends BaseDynamoRepository<AchievementForUser>
{
	public List<AchievementForUser> findByUserIdAndAchievementId(
		String userId,
		Iterable<Integer> achievementIds );

	public List<AchievementForUser> findByUserId( String userId );
}