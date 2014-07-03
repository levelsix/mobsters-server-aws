package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.ClanEventPersistentForClan;
@Component public abstract class ClanEventPersistentForClanRepository extends BaseDynamoItemRepositoryImpl<ClanEventPersistentForClan>{
	public ClanEventPersistentForClanRepository(){
		super(ClanEventPersistentForClan.class);
	}

}