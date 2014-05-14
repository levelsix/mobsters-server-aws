package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.PvpLeague;
@Component public class PvpLeagueRepository extends BaseDynamoRepository<PvpLeague>{
	public PvpLeagueRepository(){
		super(PvpLeague.class);
	}

}