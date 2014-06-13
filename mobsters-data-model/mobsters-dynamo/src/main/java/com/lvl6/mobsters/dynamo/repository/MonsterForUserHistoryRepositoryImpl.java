package com.lvl6.mobsters.dynamo.repository;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUserHistory;

@Component
public class MonsterForUserHistoryRepositoryImpl extends BaseDynamoRepositoryImpl<MonsterForUserHistory>
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
		isActive = true;// for unit test
	}

}
