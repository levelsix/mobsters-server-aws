package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanEventPersistentForUser;
@Component public class ClanEventPersistentForUserRepository extends BaseDynamoRepository<ClanEventPersistentForUser>{
	public ClanEventPersistentForUserRepository(){
		super(ClanEventPersistentForUser.class);
	}

}