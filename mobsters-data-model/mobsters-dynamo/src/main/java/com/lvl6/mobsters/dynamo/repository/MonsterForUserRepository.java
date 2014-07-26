package com.lvl6.mobsters.dynamo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.conditions.Director;
import com.lvl6.mobsters.conditions.IBooleanConditionBuilder;
import com.lvl6.mobsters.conditions.IIntConditionBuilder;
import com.lvl6.mobsters.conditions.IStringConditionBuilder;
import com.lvl6.mobsters.dynamo.MonsterForUser;

@Component
public interface MonsterForUserRepository extends BaseDynamoCollectionRepository<MonsterForUser, String>
{
	List<MonsterForUser> findByUserId( String userId );
	
	List<MonsterForUser> findByUserIdAndAll( String userId, Director<MonsterForUserConditionBuilder> filterDirector );
	
	List<MonsterForUser> findByUserIdAndAny( String userId, Director<MonsterForUserConditionBuilder> filterDirector );
	
	List<MonsterForUser> findByUserIdAndMonsterForUserIdIn( String userId, Iterable<String> monsterForUserIds );

	List<MonsterForUser> findByUserIdAndMonsterForUserIdInOrTeamSlotNumAndUserId(
		String userId,
		Collection<String> monsterForUserIds,
		Integer teamSlotNum );

	public interface MonsterForUserConditionBuilder {
		MonsterForUserConditionBuilder monsterForUserUuid( Director<IStringConditionBuilder> director );
		
		MonsterForUserConditionBuilder monsterId( Director<IIntConditionBuilder> director );
				
		MonsterForUserConditionBuilder currentExp( Director<IIntConditionBuilder> director );
		
		MonsterForUserConditionBuilder currentLvl( Director<IIntConditionBuilder> director );
		
		MonsterForUserConditionBuilder currentHealth( Director<IIntConditionBuilder> director );
		
		MonsterForUserConditionBuilder numPieces( Director<IIntConditionBuilder> director );
		
		MonsterForUserConditionBuilder complete( Director<IBooleanConditionBuilder> director );
		
		// TODO: Add Date support--define and implement IDateConditionBuilder
		// MonsterForUserConditionBuilder combineStartTime( Director<IDateConditionBuilder> director );
		
		MonsterForUserConditionBuilder teamSlotNum( Director<IIntConditionBuilder> director );
	}
}
