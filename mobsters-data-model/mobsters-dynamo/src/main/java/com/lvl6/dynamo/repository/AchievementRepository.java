package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Achievement;
public class AchievementRepository extends BaseDynamoRepository<Achievement>{
	public AchievementRepository(){
		super(Achievement.class);
	}

}