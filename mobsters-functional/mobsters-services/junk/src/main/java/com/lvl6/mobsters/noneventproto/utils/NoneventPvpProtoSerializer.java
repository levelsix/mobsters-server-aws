package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.info.PvpLeague;
import com.lvl6.mobsters.noneventproto.NoneventPvpProto.PvpLeagueProto;

public interface NoneventPvpProtoSerializer
{
	public PvpLeagueProto createPvpLeagueProto(PvpLeague pl);
	
}
