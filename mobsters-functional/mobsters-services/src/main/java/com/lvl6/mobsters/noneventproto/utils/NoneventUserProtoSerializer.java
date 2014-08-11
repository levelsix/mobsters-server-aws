package com.lvl6.mobsters.noneventproto.utils;

import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.UserDataRarelyAccessed;
import com.lvl6.mobsters.info.StaticUserLevelInfo;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.ClanProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.FullUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithFacebookId;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.StaticUserLevelInfoProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.UserCredentialProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.UserDataRarelyUsedProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.UserProto;

public interface NoneventUserProtoSerializer {

	public ClanProto createClanProto(Clan clan);
	

	public MinimumUserProto createMinimumUserProto(
			User user, Clan clan);
	
	public MinimumUserProtoWithFacebookId createMinimumUserProtoWithFacebookId(
			String facebookId, User user, Clan clan);
	
	public UserProto createUserProto(User user, Clan clan);
	
	public UserCredentialProto createUserCredentialProto(
			UserCredential uc);
	
	public UserDataRarelyUsedProto createUserDataRarelyUsedProto(
			UserDataRarelyAccessed udra);
	
	public FullUserProto createFullUserProto(User user,
			UserCredential uc, UserDataRarelyAccessed udra, Clan clan,
			PvpLeagueForUser plfu);

	public StaticUserLevelInfoProto createStaticUserLevelInfoProto(
			StaticUserLevelInfo suli);
	
}
