package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanRaid;
@Component public class ClanRaidRepository extends BaseDynamoRepository<ClanRaid>{
	public ClanRaidRepository(){
		super(ClanRaid.class);
	}

}