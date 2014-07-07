package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot;
@Component public abstract class UserFacebookInviteForSlotRepository extends BaseDynamoItemRepositoryImpl<UserFacebookInviteForSlot>{
	public UserFacebookInviteForSlotRepository(){
		super(UserFacebookInviteForSlot.class);
	}

}