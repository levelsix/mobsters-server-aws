package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.AchievementForUser;
public class AchievementForUserRepository extends BaseDynamoRepository<AchievementForUser>{
	public AchievementForUserRepository(){
		super(AchievementForUser.class);
	}

}