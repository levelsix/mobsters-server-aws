package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.MonsterEvolvingForUser;
@Component public class MonsterEvolvingForUserRepository extends BaseDynamoRepository<MonsterEvolvingForUser>{
	public MonsterEvolvingForUserRepository(){
		super(MonsterEvolvingForUser.class);
	}

}