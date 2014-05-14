package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.GoldSale;
@Component public class GoldSaleRepository extends BaseDynamoRepository<GoldSale>{
	public GoldSaleRepository(){
		super(GoldSale.class);
	}

}