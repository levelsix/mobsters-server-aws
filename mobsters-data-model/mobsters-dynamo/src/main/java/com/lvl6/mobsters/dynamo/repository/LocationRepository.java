package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.Location;
@Component public class LocationRepository extends BaseDynamoRepository<Location>{
	public LocationRepository(){
		super(Location.class);
	}

}