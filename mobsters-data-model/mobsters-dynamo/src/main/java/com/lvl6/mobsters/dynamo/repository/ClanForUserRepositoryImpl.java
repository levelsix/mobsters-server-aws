package com.lvl6.mobsters.dynamo.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.lvl6.mobsters.dynamo.ClanForUser;

@Component
public class ClanForUserRepositoryImpl extends
		BaseDynamoCollectionRepositoryImpl<ClanForUser, String> implements
		ClanForUserRepository {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ClanForUserRepositoryImpl.class);

	public ClanForUserRepositoryImpl() {
		super(ClanForUser.class, "clanId", String.class);
	}

	@Override
	public List<ClanForUser> findByUserId(String userId) {
		return loadAll(userId);
	}

}