package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.TournamentEventReward;
public class TournamentEventRewardRepository extends BaseDynamoRepository<TournamentEventReward>{
	public TournamentEventRewardRepository(){
		super(TournamentEventReward.class);
	}

}