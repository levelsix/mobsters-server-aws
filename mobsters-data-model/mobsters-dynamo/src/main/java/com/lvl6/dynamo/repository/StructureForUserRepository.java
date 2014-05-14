package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.StructureForUser;
@Component public class StructureForUserRepository extends BaseDynamoRepository<StructureForUser>{
	public StructureForUserRepository(){
		super(StructureForUser.class);
	}

}