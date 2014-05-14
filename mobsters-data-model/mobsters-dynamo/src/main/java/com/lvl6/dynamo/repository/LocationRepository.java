package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.Location;
@Component public class LocationRepository extends BaseDynamoRepository<Location>{
	public LocationRepository(){
		super(Location.class);
	}

}