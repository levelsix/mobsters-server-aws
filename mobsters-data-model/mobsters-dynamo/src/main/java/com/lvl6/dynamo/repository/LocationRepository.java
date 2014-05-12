package com.lvl6.dynamo.repository;
import com.lvl6.dynamo.Location;
public class LocationRepository extends BaseDynamoRepository<Location>{
	public LocationRepository(){
		super(Location.class);
	}

}