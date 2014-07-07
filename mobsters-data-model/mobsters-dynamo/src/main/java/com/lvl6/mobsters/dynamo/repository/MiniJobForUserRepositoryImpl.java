package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.lvl6.mobsters.dynamo.MiniJobForUser;

@Component
public class MiniJobForUserRepositoryImpl extends
		BaseDynamoCollectionRepositoryImpl<MiniJobForUser, String> implements MiniJobForUserRepository {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(MiniJobForUserRepositoryImpl.class);

	public MiniJobForUserRepositoryImpl() {
		super(MiniJobForUser.class, "miniJobForUserId", String.class);
	}

	/* (non-Javadoc)
	 * @see com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository#findByUserIdAndId(java.lang.String, java.util.Iterable)
	 */
	@Override
	public List<MiniJobForUser> findByUserIdAndId(final String userId,
        final Iterable<String> userMiniJobIds) {
        return loadEach(userId, userMiniJobIds);
    }

	@Override
	public List<MiniJobForUser> findByUserId( final String userId ) {
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
