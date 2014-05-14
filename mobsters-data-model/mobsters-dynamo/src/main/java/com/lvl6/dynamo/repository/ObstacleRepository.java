package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Obstacle;
@Component public class ObstacleRepository extends BaseDynamoRepository<Obstacle>{
	public ObstacleRepository(){
		super(Obstacle.class);
	}

}