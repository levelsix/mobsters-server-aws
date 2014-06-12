package com.lvl6.mobsters.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot;
@Component public class UserFacebookInviteForSlotRepository extends BaseDynamoRepositoryImpl<UserFacebookInviteForSlot>{
	public UserFacebookInviteForSlotRepository(){
		super(UserFacebookInviteForSlot.class);
	}

}