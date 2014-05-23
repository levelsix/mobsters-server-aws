package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.PvpBattleForUser;
@Component public class PvpBattleForUserRepository extends BaseDynamoRepository<PvpBattleForUser>{
	public PvpBattleForUserRepository(){
		super(PvpBattleForUser.class);
	}

}