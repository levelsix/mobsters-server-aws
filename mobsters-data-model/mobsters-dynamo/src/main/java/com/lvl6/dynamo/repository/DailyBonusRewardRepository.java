package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.DailyBonusReward;
@Component public class DailyBonusRewardRepository extends BaseDynamoRepository<DailyBonusReward>{
	public DailyBonusRewardRepository(){
		super(DailyBonusReward.class);
	}

}