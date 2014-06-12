package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
@Component public class ObstacleForUserRepository extends BaseDynamoRepositoryImpl<ObstacleForUser>{
	public ObstacleForUserRepository(){
		super(ObstacleForUser.class);
	}

}