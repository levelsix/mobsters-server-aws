package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.Clan;
@Component public abstract class ClanRepository extends BaseDynamoItemRepositoryImpl<Clan>{
	public ClanRepository(){
		super(Clan.class);
	}

}