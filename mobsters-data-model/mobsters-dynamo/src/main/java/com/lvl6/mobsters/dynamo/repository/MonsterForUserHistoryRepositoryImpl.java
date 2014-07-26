package com.lvl6.mobsters.dynamo.repository;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUserHistory;

@Component
public class MonsterForUserHistoryRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<MonsterForUserHistory, Date>
	implements
		MonsterForUserHistoryRepository
{
	/*
	private static final Logger LOG =
		LoggerFactory.getLogger(MonsterForUserHistoryRepositoryImpl.class);
	*/
	protected MonsterForUserHistoryRepositoryImpl()
	{
		super(MonsterForUserHistory.class, "date", Date.class);
	}

}
