package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanRaidStageReward;
@Component public class ClanRaidStageRewardRepository extends BaseDynamoRepository<ClanRaidStageReward>{
	public ClanRaidStageRewardRepository(){
		super(ClanRaidStageReward.class);
	}

}