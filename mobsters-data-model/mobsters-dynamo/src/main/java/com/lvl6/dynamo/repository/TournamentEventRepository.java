package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TournamentEvent;
@Component public class TournamentEventRepository extends BaseDynamoRepository<TournamentEvent>{
	public TournamentEventRepository(){
		super(TournamentEvent.class);
	}

}