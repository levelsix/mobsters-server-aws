package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.City;
@Component public class CityRepository extends BaseDynamoRepository<City>{
	public CityRepository(){
		super(City.class);
	}

}