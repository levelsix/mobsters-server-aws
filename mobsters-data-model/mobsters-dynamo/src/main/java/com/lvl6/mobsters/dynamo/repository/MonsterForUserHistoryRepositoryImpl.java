package com.lvl6.mobsters.dynamo.repository;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUserHistory;

@Component
public abstract class MonsterForUserHistoryRepositoryImpl extends BaseDynamoItemRepositoryImpl<MonsterForUserHistory>
	implements
		MonsterForUserHistoryRepository
{
	/*
	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterForUserHistoryRepositoryImpl.class);
	*/
	protected MonsterForUserHistoryRepositoryImpl()
	{
		super(MonsterForUserHistory.class);
	}

}
