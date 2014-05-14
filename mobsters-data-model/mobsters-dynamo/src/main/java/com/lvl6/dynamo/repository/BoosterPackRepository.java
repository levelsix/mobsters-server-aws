package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.BoosterPack;
@Component public class BoosterPackRepository extends BaseDynamoRepository<BoosterPack>{
	public BoosterPackRepository(){
		super(BoosterPack.class);
	}

}