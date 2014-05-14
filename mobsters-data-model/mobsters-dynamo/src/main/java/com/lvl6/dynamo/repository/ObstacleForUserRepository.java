package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ObstacleForUser;
@Component public class ObstacleForUserRepository extends BaseDynamoRepository<ObstacleForUser>{
	public ObstacleForUserRepository(){
		super(ObstacleForUser.class);
	}

}