package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.ClanEventPersistentForClan;
public class ClanEventPersistentForClanRepository extends BaseDynamoRepository<ClanEventPersistentForClan>{
	public ClanEventPersistentForClanRepository(){
		super(ClanEventPersistentForClan.class);
	}

}