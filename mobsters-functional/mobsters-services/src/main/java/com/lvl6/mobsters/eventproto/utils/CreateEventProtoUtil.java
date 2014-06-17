package com.lvl6.mobsters.eventproto.utils;

import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;

public interface CreateEventProtoUtil
{
	public UpdateClientUserResponseEvent createUpdateClientUserResponseEvent(
		User u, UserCredential uc, UserDataRarelyAccessed udra, Clan clan,
		PvpLeagueForUser plfu);
}
