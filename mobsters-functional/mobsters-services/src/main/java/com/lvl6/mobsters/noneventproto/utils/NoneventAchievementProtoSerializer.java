package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.dynamo.AchievementForUser;
import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.noneventproto.NoneventAchievementProto.AchievementProto;
import com.lvl6.mobsters.noneventproto.NoneventAchievementProto.UserAchievementProto;

public interface NoneventAchievementProtoSerializer
{
	public AchievementProto createAchievementProto(Achievement a);

	//BEGIN USER DATA SERIALIZATION
	
	public UserAchievementProto createUserAchievementProto( AchievementForUser afu );
	
}
