package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanBoss;
@Component public class ClanBossRepository extends BaseDynamoRepository<ClanBoss>{
	public ClanBossRepository(){
		super(ClanBoss.class);
	}

}