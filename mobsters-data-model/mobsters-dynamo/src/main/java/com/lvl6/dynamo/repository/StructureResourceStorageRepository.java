package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.StructureResourceStorage;
@Component public class StructureResourceStorageRepository extends BaseDynamoRepository<StructureResourceStorage>{
	public StructureResourceStorageRepository(){
		super(StructureResourceStorage.class);
	}

}