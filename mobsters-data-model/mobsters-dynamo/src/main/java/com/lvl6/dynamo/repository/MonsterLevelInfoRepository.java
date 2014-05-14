package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.MonsterLevelInfo;
@Component public class MonsterLevelInfoRepository extends BaseDynamoRepository<MonsterLevelInfo>{
	public MonsterLevelInfoRepository(){
		super(MonsterLevelInfo.class);
	}

}