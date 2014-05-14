package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.StructureLab;
@Component public class StructureLabRepository extends BaseDynamoRepository<StructureLab>{
	public StructureLabRepository(){
		super(StructureLab.class);
	}

}