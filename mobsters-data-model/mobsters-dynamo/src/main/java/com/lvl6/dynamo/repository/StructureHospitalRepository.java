package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.StructureHospital;
@Component public class StructureHospitalRepository extends BaseDynamoRepository<StructureHospital>{
	public StructureHospitalRepository(){
		super(StructureHospital.class);
	}

}