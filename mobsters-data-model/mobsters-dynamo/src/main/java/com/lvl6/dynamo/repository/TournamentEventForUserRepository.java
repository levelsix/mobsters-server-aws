package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.TournamentEventForUser;
@Component public class TournamentEventForUserRepository extends BaseDynamoRepository<TournamentEventForUser>{
	public TournamentEventForUserRepository(){
		super(TournamentEventForUser.class);
	}

}