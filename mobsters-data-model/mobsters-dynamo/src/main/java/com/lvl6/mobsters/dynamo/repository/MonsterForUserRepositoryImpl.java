package com.lvl6.mobsters.dynamo.repository;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUser;
@Component
public class MonsterForUserRepositoryImpl extends BaseDynamoRepository<MonsterForUser> implements MonsterForUserRepository {
	public MonsterForUserRepositoryImpl(){
		super(MonsterForUser.class);
	}

	@Override
	public List<MonsterForUser> findByUserIdAndId(
		String userId, Iterable<String> monsterIds) {
		final ArrayList<MonsterForUser> retVal = new ArrayList<MonsterForUser>();
		// final Builder retVal = ImmutableList.builder();
		for (final String monsterForUserId : monsterIds ) {
			final MonsterForUser nextMonster = load(monsterForUserId);
			if (userId.equals(nextMonster.getUserId())) {
				retVal.add(nextMonster);
			}
		}
		return retVal;
	}

}