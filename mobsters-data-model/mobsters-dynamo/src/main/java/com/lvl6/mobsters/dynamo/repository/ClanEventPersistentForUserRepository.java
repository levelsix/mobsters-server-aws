package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanEventPersistentForUser;
@Component public class ClanEventPersistentForUserRepository extends BaseDynamoRepositoryImpl<ClanEventPersistentForUser>{
	public ClanEventPersistentForUserRepository(){
		super(ClanEventPersistentForUser.class);
	}

}