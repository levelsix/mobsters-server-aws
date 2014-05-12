package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.CityElement;
public class CityElementRepository extends BaseDynamoRepository<CityElement>{
	public CityElementRepository(){
		super(CityElement.class);
	}

}