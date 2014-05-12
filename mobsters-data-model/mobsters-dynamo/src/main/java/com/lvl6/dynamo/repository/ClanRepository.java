package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Clan;
public class ClanRepository extends BaseDynamoRepository<Clan>{
	public ClanRepository(){
		super(Clan.class);
	}

}