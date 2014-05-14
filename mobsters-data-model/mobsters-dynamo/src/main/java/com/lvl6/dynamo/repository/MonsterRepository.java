package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Monster;
@Component public class MonsterRepository extends BaseDynamoRepository<Monster>{
	public MonsterRepository(){
		super(Monster.class);
	}

}