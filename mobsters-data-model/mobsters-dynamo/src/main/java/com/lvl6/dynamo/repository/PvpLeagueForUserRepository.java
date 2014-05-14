package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.PvpLeagueForUser;
@Component public class PvpLeagueForUserRepository extends BaseDynamoRepository<PvpLeagueForUser>{
	public PvpLeagueForUserRepository(){
		super(PvpLeagueForUser.class);
	}

}