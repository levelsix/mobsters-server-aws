package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.TournamentEvent;
public class TournamentEventRepository extends BaseDynamoRepository<TournamentEvent>{
	public TournamentEventRepository(){
		super(TournamentEvent.class);
	}

}