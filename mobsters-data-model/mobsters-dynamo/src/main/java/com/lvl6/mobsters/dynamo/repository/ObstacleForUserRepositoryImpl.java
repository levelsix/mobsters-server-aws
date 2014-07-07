package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
@Component public class ObstacleForUserRepositoryImpl extends BaseDynamoRepositoryImpl<ObstacleForUser>
	implements
		ObstacleForUserRepository{
	public ObstacleForUserRepositoryImpl(){
		super(ObstacleForUser.class);
	}

}