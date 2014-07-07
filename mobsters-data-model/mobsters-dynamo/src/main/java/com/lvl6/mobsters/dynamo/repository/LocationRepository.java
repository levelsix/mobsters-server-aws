package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.Location;
@Component public abstract class LocationRepository extends BaseDynamoItemRepositoryImpl<Location>{
	public LocationRepository(){
		super(Location.class);
	}

}