package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.CoordinatePair;
@Component public class CoordinatePairRepository extends BaseDynamoRepository<CoordinatePair>{
	public CoordinatePairRepository(){
		super(CoordinatePair.class);
	}

}