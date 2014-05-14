package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanBossReward;
@Component public class ClanBossRewardRepository extends BaseDynamoRepository<ClanBossReward>{
	public ClanBossRewardRepository(){
		super(ClanBossReward.class);
	}

}