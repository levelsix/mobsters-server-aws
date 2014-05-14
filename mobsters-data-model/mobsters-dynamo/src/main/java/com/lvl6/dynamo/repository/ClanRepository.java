package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Clan;
@Component public class ClanRepository extends BaseDynamoRepository<Clan>{
	public ClanRepository(){
		super(Clan.class);
	}

}