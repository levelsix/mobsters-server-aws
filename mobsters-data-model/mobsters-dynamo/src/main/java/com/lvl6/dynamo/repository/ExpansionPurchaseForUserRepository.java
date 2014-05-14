package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ExpansionPurchaseForUser;
@Component public class ExpansionPurchaseForUserRepository extends BaseDynamoRepository<ExpansionPurchaseForUser>{
	public ExpansionPurchaseForUserRepository(){
		super(ExpansionPurchaseForUser.class);
	}

}