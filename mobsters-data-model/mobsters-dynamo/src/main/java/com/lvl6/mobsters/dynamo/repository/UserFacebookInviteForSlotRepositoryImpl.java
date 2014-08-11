package com.lvl6.mobsters.dynamo.repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.UserFacebookInviteForSlot;
@Component
public class UserFacebookInviteForSlotRepositoryImpl extends
	BaseDynamoCollectionRepositoryImpl<UserFacebookInviteForSlot, String>
		implements UserFacebookInviteForSlotRepository
{
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(UserFacebookInviteForSlotRepositoryImpl.class);

	
	public UserFacebookInviteForSlotRepositoryImpl(){
		super( UserFacebookInviteForSlot.class, "recipientFacebookId", String.class );
	}

}