package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.CoordinatePair;
@Component public class CoordinatePairRepository extends BaseDynamoRepositoryImpl<CoordinatePair>{
	public CoordinatePairRepository(){
		super(CoordinatePair.class);
	}

}