package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.MonsterForPvp;
@Component public class MonsterForPvpRepository extends BaseDynamoRepository<MonsterForPvp>{
	public MonsterForPvpRepository(){
		super(MonsterForPvp.class);
	}

}