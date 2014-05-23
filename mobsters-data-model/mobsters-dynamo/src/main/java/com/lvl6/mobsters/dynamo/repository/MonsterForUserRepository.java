package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterForUser;
@Component public class MonsterForUserRepository extends BaseDynamoRepository<MonsterForUser>{
	public MonsterForUserRepository(){
		super(MonsterForUser.class);
	}

}