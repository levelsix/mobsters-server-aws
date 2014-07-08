package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.lvl6.mobsters.dynamo.AchievementForUser;

@Component
public class AchievementForUserRepositoryImpl extends
		BaseDynamoCollectionRepositoryImpl<AchievementForUser, Integer> 
	implements AchievementForUserRepository {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(AchievementForUserRepositoryImpl.class);

	public AchievementForUserRepositoryImpl() {
		super(AchievementForUser.class, "achievementId", Integer.class);
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.AchievementForUserRepository#findByUserIdAndId(java.lang.String, java.util.Collection)
	 */
	@Override
	public List<AchievementForUser> findByUserIdAndAchievementId(final String userId,
	    final Iterable<Integer> achievementIds) {
	    return loadEach(userId, achievementIds);
	}

	@Override
	public List<AchievementForUser> findByUserId( final String userId ) {
		return loadAll(userId);
	}

	@Override
	public List<LocalSecondaryIndex> getLocalIndexes() {
		return null;
	}

	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes() {
		return null;
	}

}