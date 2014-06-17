package com.lvl6.mobsters.eventproto.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.eventproto.EventUserProto.UpdateClientUserResponseProto;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.FullUserProto;
import com.lvl6.mobsters.noneventproto.utils.NoneventUserProtoSerializer;

public class CreateEventProtoUtilImpl implements CreateEventProtoUtil {

	private static Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());
	
	@Autowired
	protected NoneventUserProtoSerializer noneventUserProtoSerializer;

	@Override
	public UpdateClientUserResponseEvent createUpdateClientUserResponseEvent(
			User u, UserCredential uc, UserDataRarelyAccessed udra, Clan clan,
			PvpLeagueForUser plfu) {
	    UpdateClientUserResponseProto.Builder ucurpb =
	    		UpdateClientUserResponseProto.newBuilder();
	    FullUserProto fup = noneventUserProtoSerializer.createFullUserProto(
	    		u, uc, udra, clan, plfu); 
	    ucurpb.setSender(fup);
	    ucurpb.setTimeOfUserUpdate(new Date().getTime()).build();
	    
	    
	    String userIdStr = u.getId().toString();
	    UpdateClientUserResponseEvent resEvent =
	    		new UpdateClientUserResponseEvent(userIdStr);
	    resEvent.setUpdateClientUserResponseProto(ucurpb.build());
	    
	    return resEvent;
	}

	public NoneventUserProtoSerializer getNoneventUserProtoSerializer()
	{
		return noneventUserProtoSerializer;
	}

	public void setNoneventUserProtoSerializer(
		NoneventUserProtoSerializer noneventUserProtoSerializer )
	{
		this.noneventUserProtoSerializer = noneventUserProtoSerializer;
	}
	
	
}
