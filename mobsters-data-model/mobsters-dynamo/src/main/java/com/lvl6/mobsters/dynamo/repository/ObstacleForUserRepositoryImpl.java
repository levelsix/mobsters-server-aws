package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;

@Component 
public class ObstacleForUserRepositoryImpl extends BaseDynamoCollectionRepositoryImpl<ObstacleForUser, String>
	implements
		ObstacleForUserRepository {
	public ObstacleForUserRepositoryImpl(){
		super(ObstacleForUser.class, "obstacleForUserId", String.class);
	}
}