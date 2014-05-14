package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanRaidStage;
@Component public class ClanRaidStageRepository extends BaseDynamoRepository<ClanRaidStage>{
	public ClanRaidStageRepository(){
		super(ClanRaidStage.class);
	}

}