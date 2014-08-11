package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.info.BoosterPack;
import com.lvl6.mobsters.info.IBoosterDisplayItem;
import com.lvl6.mobsters.info.IBoosterItem;
import com.lvl6.mobsters.noneventproto.NoneventBoosterPackProto.BoosterDisplayItemProto;
import com.lvl6.mobsters.noneventproto.NoneventBoosterPackProto.BoosterItemProto;
import com.lvl6.mobsters.noneventproto.NoneventBoosterPackProto.BoosterPackProto;

public interface NoneventBoosterPackProtoSerializer
{

	public BoosterPackProto createBoosterPackProto(BoosterPack bp);

	BoosterItemProto createBoosterItemProto( IBoosterItem bi );

	BoosterDisplayItemProto createBoosterDisplayItemProto( IBoosterDisplayItem bdi );
}
