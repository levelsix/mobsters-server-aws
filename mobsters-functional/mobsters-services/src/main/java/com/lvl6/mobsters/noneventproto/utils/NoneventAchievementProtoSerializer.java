package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.noneventproto.NoneventAchievementProto.AchievementProto;

public interface NoneventAchievementProtoSerializer
{
	public AchievementProto createAchievementProto(Achievement a);
	
}
