package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.AchievementForUser;
@Component public class AchievementForUserRepository extends BaseDynamoRepository<AchievementForUser>{
	public AchievementForUserRepository(){
		super(AchievementForUser.class);
	}

}