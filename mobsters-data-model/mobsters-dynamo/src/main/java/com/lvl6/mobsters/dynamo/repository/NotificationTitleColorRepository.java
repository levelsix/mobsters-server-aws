package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.NotificationTitleColor;
@Component public abstract class NotificationTitleColorRepository extends BaseDynamoItemRepositoryImpl<NotificationTitleColor>{
	public NotificationTitleColorRepository(){
		super(NotificationTitleColor.class);
	}

}