package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ObstacleForUser;
@Component public interface ObstacleForUserRepository extends
	BaseDynamoCollectionRepository<ObstacleForUser, String>{

}