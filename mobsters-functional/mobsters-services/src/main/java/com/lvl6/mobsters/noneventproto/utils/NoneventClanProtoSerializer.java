package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.dynamo.ClanForUser;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.FullUserClanProto;


public interface NoneventClanProtoSerializer
{

	public FullUserClanProto createFullUserClanProtoFromUserClan( ClanForUser cfu );

}
