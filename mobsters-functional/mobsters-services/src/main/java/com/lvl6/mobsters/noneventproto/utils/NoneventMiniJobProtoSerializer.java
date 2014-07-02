package com.lvl6.mobsters.noneventproto.utils;

import java.util.List;
import java.util.Map;

import com.lvl6.mobsters.dynamo.MiniJobForUser;
import com.lvl6.mobsters.info.MiniJob;
import com.lvl6.mobsters.noneventproto.NoneventMiniJobProto.MiniJobProto;
import com.lvl6.mobsters.noneventproto.NoneventMiniJobProto.UserMiniJobProto;

public interface NoneventMiniJobProtoSerializer
{
	public MiniJobProto createMiniJobProto( MiniJob mj );

	//BEGIN USER DATA SERIALIZATION
	
	public List<UserMiniJobProto> createUserMiniJobProtos(
		List<MiniJobForUser> mjfuList,
		Map<Integer, MiniJob> miniJobIdToMiniJob );

}
