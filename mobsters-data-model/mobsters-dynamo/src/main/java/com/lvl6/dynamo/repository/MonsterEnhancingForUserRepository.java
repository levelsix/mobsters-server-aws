package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.MonsterEnhancingForUser;
@Component public class MonsterEnhancingForUserRepository extends BaseDynamoRepository<MonsterEnhancingForUser>{
	public MonsterEnhancingForUserRepository(){
		super(MonsterEnhancingForUser.class);
	}

}