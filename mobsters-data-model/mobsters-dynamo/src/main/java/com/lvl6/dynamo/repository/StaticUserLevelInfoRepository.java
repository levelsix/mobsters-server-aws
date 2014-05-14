package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.StaticUserLevelInfo;
@Component public class StaticUserLevelInfoRepository extends BaseDynamoRepository<StaticUserLevelInfo>{
	public StaticUserLevelInfoRepository(){
		super(StaticUserLevelInfo.class);
	}

}