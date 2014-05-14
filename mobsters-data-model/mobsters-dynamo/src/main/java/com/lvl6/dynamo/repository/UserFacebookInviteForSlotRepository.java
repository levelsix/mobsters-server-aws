package com.lvl6.dynamo.repository;
import org.springframework.stereotype.Component;

import com.lvl6.dynamo.UserFacebookInviteForSlot;
@Component public class UserFacebookInviteForSlotRepository extends BaseDynamoRepository<UserFacebookInviteForSlot>{
	public UserFacebookInviteForSlotRepository(){
		super(UserFacebookInviteForSlot.class);
	}

}