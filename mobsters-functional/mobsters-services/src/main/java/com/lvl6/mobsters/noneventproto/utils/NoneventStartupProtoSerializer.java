package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.StartupConstants.AnimatedSpriteOffsetProto;
import com.lvl6.mobsters.info.AnimatedSpriteOffset;

public interface NoneventStartupProtoSerializer
{
	public AnimatedSpriteOffsetProto createAnimatedSpriteOffsetProtoFromAnimatedSpriteOffset(AnimatedSpriteOffset aso);
	
}
