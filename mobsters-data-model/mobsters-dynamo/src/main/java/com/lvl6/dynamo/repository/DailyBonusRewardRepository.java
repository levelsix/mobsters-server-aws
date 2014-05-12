package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.DailyBonusReward;
public class DailyBonusRewardRepository extends BaseDynamoRepository<DailyBonusReward>{
	public DailyBonusRewardRepository(){
		super(DailyBonusReward.class);
	}

}