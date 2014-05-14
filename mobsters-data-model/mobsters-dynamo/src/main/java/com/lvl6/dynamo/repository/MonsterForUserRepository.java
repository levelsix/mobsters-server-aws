package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.MonsterForUser;
@Component public class MonsterForUserRepository extends BaseDynamoRepository<MonsterForUser>{
	public MonsterForUserRepository(){
		super(MonsterForUser.class);
	}

}