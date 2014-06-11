package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUser;

@Component
public interface MonsterForUserRepository
{
	List<MonsterForUser> findByUserIdAndId( String userId, Collection<String> monsterForUserIds );

	Map<String, MonsterForUser> findByUserIdAndIdOrTeamSlotNumAndUserId(
		String userId,
		Collection<String> monsterForUserIds,
		Integer teamSlotNum );
}
