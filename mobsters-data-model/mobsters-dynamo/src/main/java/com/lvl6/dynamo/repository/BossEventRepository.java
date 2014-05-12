package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.BossEvent;
public class BossEventRepository extends BaseDynamoRepository<BossEvent>{
	public BossEventRepository(){
		super(BossEvent.class);
	}

}