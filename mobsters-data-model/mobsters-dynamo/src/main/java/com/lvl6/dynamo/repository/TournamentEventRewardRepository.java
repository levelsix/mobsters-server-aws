package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TournamentEventReward;
@Component public class TournamentEventRewardRepository extends BaseDynamoRepository<TournamentEventReward>{
	public TournamentEventRewardRepository(){
		super(TournamentEventReward.class);
	}

}