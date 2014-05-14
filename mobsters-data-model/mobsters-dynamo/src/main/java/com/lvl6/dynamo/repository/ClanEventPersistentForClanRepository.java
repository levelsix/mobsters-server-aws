package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanEventPersistentForClan;
@Component public class ClanEventPersistentForClanRepository extends BaseDynamoRepository<ClanEventPersistentForClan>{
	public ClanEventPersistentForClanRepository(){
		super(ClanEventPersistentForClan.class);
	}

}