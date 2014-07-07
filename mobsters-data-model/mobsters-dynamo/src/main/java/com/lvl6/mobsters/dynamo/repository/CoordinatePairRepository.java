package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.CoordinatePair;
@Component public abstract class CoordinatePairRepository extends BaseDynamoItemRepositoryImpl<CoordinatePair>{
	public CoordinatePairRepository(){
		super(CoordinatePair.class);
	}

}