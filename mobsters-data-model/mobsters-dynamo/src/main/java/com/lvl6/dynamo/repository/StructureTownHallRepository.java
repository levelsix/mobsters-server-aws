package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.StructureTownHall;
@Component public class StructureTownHallRepository extends BaseDynamoRepository<StructureTownHall>{
	public StructureTownHallRepository(){
		super(StructureTownHall.class);
	}

}