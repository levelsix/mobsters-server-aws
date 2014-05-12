package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.MonsterLevelInfo;
public class MonsterLevelInfoRepository extends BaseDynamoRepository<MonsterLevelInfo>{
	public MonsterLevelInfoRepository(){
		super(MonsterLevelInfo.class);
	}

}