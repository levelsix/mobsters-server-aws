package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanEventPersistentUserReward;
@Component public abstract class ClanEventPersistentUserRewardRepository extends BaseDynamoItemRepositoryImpl<ClanEventPersistentUserReward>{
	public ClanEventPersistentUserRewardRepository(){
		super(ClanEventPersistentUserReward.class);
	}

}