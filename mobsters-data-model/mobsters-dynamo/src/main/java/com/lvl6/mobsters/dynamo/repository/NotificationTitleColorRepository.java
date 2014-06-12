package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.NotificationTitleColor;
@Component public class NotificationTitleColorRepository extends BaseDynamoRepositoryImpl<NotificationTitleColor>{
	public NotificationTitleColorRepository(){
		super(NotificationTitleColor.class);
	}

}