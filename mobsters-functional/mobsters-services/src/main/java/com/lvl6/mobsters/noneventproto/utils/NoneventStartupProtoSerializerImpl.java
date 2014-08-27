package com.lvl6.mobsters.noneventproto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto.StartupConstants.AnimatedSpriteOffsetProto;
import com.lvl6.mobsters.utility.values.AnimatedSpriteOffset;

@Component
public class NoneventStartupProtoSerializerImpl implements NoneventStartupProtoSerializer 
{
	@SuppressWarnings("unused")
	private static final Logger LOG =
		LoggerFactory.getLogger(NoneventStartupProtoSerializerImpl.class);
	
	@Autowired
	private NoneventStructureProtoSerializer noneventStructureProtoSerializer;

	@Override
	public AnimatedSpriteOffsetProto createAnimatedSpriteOffsetProtoFromAnimatedSpriteOffset(
		AnimatedSpriteOffset aso)
	{
		return AnimatedSpriteOffsetProto
			.newBuilder()
			.setImageName(
				aso.getImgName())
	        .setOffSet(
	        	noneventStructureProtoSerializer.createCoordinateProtoFromCoordinatePair(
	        		aso.getOffSet()))
	        .build();
	}



}
