package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
@Component public abstract class MonsterHealingForUserRepository extends BaseDynamoItemRepositoryImpl<MonsterHealingForUser>{
	public MonsterHealingForUserRepository(){
		super(MonsterHealingForUser.class);
	}

}