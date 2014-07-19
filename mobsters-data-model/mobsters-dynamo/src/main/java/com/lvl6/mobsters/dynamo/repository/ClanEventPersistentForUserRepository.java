package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser;
@Component public abstract class ClanEventPersistentForUserRepository extends BaseDynamoItemRepositoryImpl<ClanEventPersistentForUser>{
	public ClanEventPersistentForUserRepository(){
		super(ClanEventPersistentForUser.class);
	}

}