package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.MonsterReward;
@Component public class MonsterRewardRepository extends BaseDynamoRepository<MonsterReward>{
	public MonsterRewardRepository(){
		super(MonsterReward.class);
	}

}