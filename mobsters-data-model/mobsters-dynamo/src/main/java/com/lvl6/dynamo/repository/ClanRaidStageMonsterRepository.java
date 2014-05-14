package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanRaidStageMonster;
@Component public class ClanRaidStageMonsterRepository extends BaseDynamoRepository<ClanRaidStageMonster>{
	public ClanRaidStageMonsterRepository(){
		super(ClanRaidStageMonster.class);
	}

}