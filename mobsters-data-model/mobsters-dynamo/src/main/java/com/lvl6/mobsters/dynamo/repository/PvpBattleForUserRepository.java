package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.PvpBattleForUser;
@Component public abstract class PvpBattleForUserRepository extends BaseDynamoItemRepositoryImpl<PvpBattleForUser>{
	public PvpBattleForUserRepository(){
		super(PvpBattleForUser.class);
	}

}