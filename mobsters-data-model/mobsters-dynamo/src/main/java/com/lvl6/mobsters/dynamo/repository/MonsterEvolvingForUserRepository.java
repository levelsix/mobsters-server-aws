package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
@Component public class MonsterEvolvingForUserRepository extends BaseDynamoRepository<MonsterEvolvingForUser>{
	public MonsterEvolvingForUserRepository(){
		super(MonsterEvolvingForUser.class);
	}

}