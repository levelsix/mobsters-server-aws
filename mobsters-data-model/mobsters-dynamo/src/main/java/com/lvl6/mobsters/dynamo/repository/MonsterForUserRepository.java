package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUser;

@Component
public interface MonsterForUserRepository extends BaseDynamoRepository<MonsterForUser>
{
	public List<MonsterForUser> findByUserId( String userId );
	
	public List<MonsterForUser> findByUserIdAndId( String userId, Collection<String> monsterForUserIds );

	public List<MonsterForUser> findByUserIdAndIdOrTeamSlotNumAndUserId(
		String userId,
		Collection<String> monsterForUserIds,
		Integer teamSlotNum );
}
