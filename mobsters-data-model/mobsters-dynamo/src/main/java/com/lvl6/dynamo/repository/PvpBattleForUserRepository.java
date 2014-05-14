package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.PvpBattleForUser;
@Component public class PvpBattleForUserRepository extends BaseDynamoRepository<PvpBattleForUser>{
	public PvpBattleForUserRepository(){
		super(PvpBattleForUser.class);
	}

}