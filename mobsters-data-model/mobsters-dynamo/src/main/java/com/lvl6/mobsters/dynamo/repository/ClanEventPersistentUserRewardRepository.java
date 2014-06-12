package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanEventPersistentUserReward;
@Component public class ClanEventPersistentUserRewardRepository extends BaseDynamoRepositoryImpl<ClanEventPersistentUserReward>{
	public ClanEventPersistentUserRewardRepository(){
		super(ClanEventPersistentUserReward.class);
	}

}