package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.BossEvent;
@Component public class BossEventRepository extends BaseDynamoRepository<BossEvent>{
	public BossEventRepository(){
		super(BossEvent.class);
	}

}