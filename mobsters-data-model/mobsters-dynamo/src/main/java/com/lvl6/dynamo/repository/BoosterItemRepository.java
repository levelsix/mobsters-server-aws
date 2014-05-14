package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.BoosterItem;
@Component public class BoosterItemRepository extends BaseDynamoRepository<BoosterItem>{
	public BoosterItemRepository(){
		super(BoosterItem.class);
	}

}