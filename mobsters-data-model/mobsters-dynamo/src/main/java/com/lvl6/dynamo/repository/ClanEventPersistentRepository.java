package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.ClanEventPersistent;
public class ClanEventPersistentRepository extends BaseDynamoRepository<ClanEventPersistent>{
	public ClanEventPersistentRepository(){
		super(ClanEventPersistent.class);
	}

}