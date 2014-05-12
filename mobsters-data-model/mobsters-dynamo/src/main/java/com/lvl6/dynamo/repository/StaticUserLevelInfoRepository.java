package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.StaticUserLevelInfo;
public class StaticUserLevelInfoRepository extends BaseDynamoRepository<StaticUserLevelInfo>{
	public StaticUserLevelInfoRepository(){
		super(StaticUserLevelInfo.class);
	}

}