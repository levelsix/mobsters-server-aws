package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Structure;
@Component public class StructureRepository extends BaseDynamoRepository<Structure>{
	public StructureRepository(){
		super(Structure.class);
	}

}