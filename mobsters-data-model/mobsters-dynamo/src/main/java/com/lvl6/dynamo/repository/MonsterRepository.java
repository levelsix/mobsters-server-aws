package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Monster;
public class MonsterRepository extends BaseDynamoRepository<Monster>{
	public MonsterRepository(){
		super(Monster.class);
	}

}