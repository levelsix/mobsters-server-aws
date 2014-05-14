package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.StructureResidence;
@Component public class StructureResidenceRepository extends BaseDynamoRepository<StructureResidence>{
	public StructureResidenceRepository(){
		super(StructureResidence.class);
	}

}