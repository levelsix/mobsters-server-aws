package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Achievement;
@Component public class AchievementRepository extends BaseDynamoRepository<Achievement>{
	public AchievementRepository(){
		super(Achievement.class);
	}

}