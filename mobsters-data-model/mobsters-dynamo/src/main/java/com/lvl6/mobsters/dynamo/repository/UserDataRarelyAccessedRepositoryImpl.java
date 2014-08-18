package com.lvl6.mobsters.dynamo.repository;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;

@Component
public class UserDataRarelyAccessedRepositoryImpl
	extends BaseDynamoItemRepositoryImpl<UserDataRarelyAccessed>
	implements UserDataRarelyAccessedRepository
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(UserDataRarelyAccessedRepositoryImpl.class);

	public UserDataRarelyAccessedRepositoryImpl() {
		super(UserDataRarelyAccessed.class);
	}

	@Override
	protected UserDataRarelyAccessed getHashKeyObject(final String hashKey) {
		final UserDataRarelyAccessed retVal = new UserDataRarelyAccessed();
		retVal.setUserId(hashKey);
		return retVal;
	}

	@Override
	public List<LocalSecondaryIndex> getLocalIndexes() {
		return Collections.emptyList();
	}

	@Override
	public List<GlobalSecondaryIndex> getGlobalIndexes() {
		return Collections.emptyList();
	}
}