package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUser;

@Component
public interface MonsterForUserRepository extends BaseDynamoCollectionRepository<MonsterForUser, String>
{
	List<MonsterForUser> findByUserIdAndId( String userId, Iterable<String> monsterForUserIds );

	List<MonsterForUser> findByUserIdAndIdOrTeamSlotNumAndUserId(
		String userId,
		Collection<String> monsterForUserIds,
		Integer teamSlotNum );
}
