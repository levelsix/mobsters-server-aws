package com.lvl6.mobsters.eventproto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.eventproto.EventUserProto.UpdateClientUserResponseProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.FullUserProto;
import com.lvl6.mobsters.noneventproto.utils.CreateNoneventUserProtoUtil;

public class CreateEventProtoUtil {

	private static Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());

	public static UpdateClientUserResponseEvent createUpdateClientUserResponseEvent(
			User u, UserCredential uc, UserDataRarelyAccessed udra, Clan clan,
			PvpLeagueForUser plfu) {
	    UpdateClientUserResponseProto.Builder ucurpb =
	    		UpdateClientUserResponseProto.newBuilder();
	    FullUserProto fup = CreateNoneventUserProtoUtil.createFullUserProto(
	    		u, uc, udra, clan, plfu); 
	    ucurpb.setSender(fup);
	    ucurpb.setTimeOfUserUpdate(new Date().getTime()).build();
	    
	    
	    String userIdStr = u.getId().toString();
	    UpdateClientUserResponseEvent resEvent =
	    		new UpdateClientUserResponseEvent(userIdStr);
	    resEvent.setUpdateClientUserResponseProto(ucurpb.build());
	    
	    return resEvent;
	}
}
