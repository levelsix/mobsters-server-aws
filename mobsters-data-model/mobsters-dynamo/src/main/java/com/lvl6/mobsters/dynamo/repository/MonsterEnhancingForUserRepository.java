package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
@Component public class MonsterEnhancingForUserRepository extends BaseDynamoRepositoryImpl<MonsterEnhancingForUser>{
	public MonsterEnhancingForUserRepository(){
		super(MonsterEnhancingForUser.class);
	}

}