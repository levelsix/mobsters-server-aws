package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.City;
public class CityRepository extends BaseDynamoRepository<City>{
	public CityRepository(){
		super(City.class);
	}

}