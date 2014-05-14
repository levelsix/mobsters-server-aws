package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.StructureResourceGenerator;
@Component public class StructureResourceGeneratorRepository extends BaseDynamoRepository<StructureResourceGenerator>{
	public StructureResourceGeneratorRepository(){
		super(StructureResourceGenerator.class);
	}

}