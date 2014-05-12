package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.TournamentEventForUser;
public class TournamentEventForUserRepository extends BaseDynamoRepository<TournamentEventForUser>{
	public TournamentEventForUserRepository(){
		super(TournamentEventForUser.class);
	}

}