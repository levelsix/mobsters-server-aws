package com.lvl6.mobsters.dynamo.repository;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUserHistory;

@Component
public interface MonsterForUserHistoryRepository extends BaseDynamoCollectionRepository<MonsterForUserHistory, Date>
{
}
