package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.CityElement;
@Component public class CityElementRepository extends BaseDynamoRepository<CityElement>{
	public CityElementRepository(){
		super(CityElement.class);
	}

}