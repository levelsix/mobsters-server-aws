package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterEvolvingForUser;
@Component public abstract class MonsterEvolvingForUserRepository extends BaseDynamoItemRepositoryImpl<MonsterEvolvingForUser>{
	public MonsterEvolvingForUserRepository(){
		super(MonsterEvolvingForUser.class);
	}

}