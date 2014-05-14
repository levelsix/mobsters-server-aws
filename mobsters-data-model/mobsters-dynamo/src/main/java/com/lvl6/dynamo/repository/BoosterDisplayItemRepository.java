package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.BoosterDisplayItem;
@Component public class BoosterDisplayItemRepository extends BaseDynamoRepository<BoosterDisplayItem>{
	public BoosterDisplayItemRepository(){
		super(BoosterDisplayItem.class);
	}

}