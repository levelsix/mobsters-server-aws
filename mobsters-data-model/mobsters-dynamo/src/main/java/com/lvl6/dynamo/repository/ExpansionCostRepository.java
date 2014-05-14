package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.ExpansionCost;
@Component public class ExpansionCostRepository extends BaseDynamoRepository<ExpansionCost>{
	public ExpansionCostRepository(){
		super(ExpansionCost.class);
	}

}