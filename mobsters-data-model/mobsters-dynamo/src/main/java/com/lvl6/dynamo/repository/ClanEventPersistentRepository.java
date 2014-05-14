package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ClanEventPersistent;
@Component public class ClanEventPersistentRepository extends BaseDynamoRepository<ClanEventPersistent>{
	public ClanEventPersistentRepository(){
		super(ClanEventPersistent.class);
	}

}