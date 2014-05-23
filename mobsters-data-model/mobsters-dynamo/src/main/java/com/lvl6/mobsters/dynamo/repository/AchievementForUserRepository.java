package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.AchievementForUser;
@Component public class AchievementForUserRepository extends BaseDynamoRepository<AchievementForUser>{
	public AchievementForUserRepository(){
		super(AchievementForUser.class);
		isActive = true;//for unit test
	}

}