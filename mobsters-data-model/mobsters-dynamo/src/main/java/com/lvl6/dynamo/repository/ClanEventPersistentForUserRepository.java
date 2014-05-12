package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.ClanEventPersistentForUser;
public class ClanEventPersistentForUserRepository extends BaseDynamoRepository<ClanEventPersistentForUser>{
	public ClanEventPersistentForUserRepository(){
		super(ClanEventPersistentForUser.class);
	}

}