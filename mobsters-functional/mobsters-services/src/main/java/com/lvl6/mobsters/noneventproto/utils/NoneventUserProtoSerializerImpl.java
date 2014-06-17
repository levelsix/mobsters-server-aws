package com.lvl6.mobsters.noneventproto.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class NoneventUserProtoSerializerImpl {

	private static Logger log = LoggerFactory.getLogger(new Object() {
	}.getClass().getEnclosingClass());

	public static ClanProto createClanProto(Clan clan) {
		ClanProto.Builder cpb = ClanProto.newBuilder();
		
		cpb.setClanUuid(clan.getId());
		
		String str = clan.getName();
		if (null != str) {
			cpb.setName(str);
		} else {
			log.warn("clan name is null " + clan);
		}
		
		Date d = clan.getCreateTime();
		if (null != d) {
			cpb.setCreateTime(d.getTime());
		} else {
			log.warn("clan create time is null " + clan);
		}
		
		str = clan.getDescription();
		if (null != str) {
			cpb.setDescription(str);
		} else {
			log.warn("clan description is null " + clan);
		}
		
		str = clan.getTag();
		if (null != str) {
			cpb.setTag(str);
		} else {
			log.warn("clan tag is null " + str);
		}
		
		cpb.setRequestToJoinRequired(clan.isRequestToJoinRequired());
		cpb.setClanIconId(clan.getClanIconId());
		
		return cpb.build();
	}
	

	public static MinimumUserProto createMinimumUserProto(
			User user, Clan clan) {
		MinimumUserProto.Builder mupb = MinimumUserProto.newBuilder();
		
		mupb.setUserUuid(user.getId());

		String str = user.getName();
		if (null != str) {
			mupb.setName(str);
		} else {
			log.warn("user name is null " + str);
		}
		
		if (null != clan) {
			ClanProto cp = createClanProto(clan);
			mupb.setClan(cp);
		}
		
		mupb.setLevel(user.getLevel());
		
		return mupb.build();
	}
	
	public static MinimumUserProtoWithFacebookId createMinimumUserProtoWithFacebookId(
			String facebookId, User user, Clan clan) {
		MinimumUserProtoWithFacebookId.Builder mupwfib =
				MinimumUserProtoWithFacebookId.newBuilder();
		
		if (null != facebookId) {
			mupwfib.setFacebookId(facebookId);
		} else {
			log.warn("user facebook id is null " + user);
		}
		
		MinimumUserProto mup = createMinimumUserProto(user, clan);
		mupwfib.setMinUserProto(mup);
		
		return mupwfib.build();
	}
	
	public static UserProto createUserProto(User user, Clan clan) {
		UserProto.Builder upb = UserProto.newBuilder();
		
		upb.setUserUuid(user.getId());
		
		String str = user.getName();
		
		if (null != str) {
			upb.setName(str);
		} else {
			log.warn("user name is null " + str);
		}
		
		
		upb.setIsAdmin(user.isAdmin());
		upb.setLevel(user.getLevel());
		upb.setGems(user.getLevel());
		upb.setCash(user.getCash());
		upb.setOil(user.getOil());
		upb.setExperience(user.getExperience());
		
		if (null != clan) {
			ClanProto cp = createClanProto(clan);
			upb.setClan(cp);
		}
		
		return upb.build();
	}
	
	public static UserCredentialProto createUserCredentialProto(
			UserCredential uc) {
		UserCredentialProto.Builder ucpb = UserCredentialProto.newBuilder();
		
		ucpb.setUserUuid(uc.getUserId());
		
		String str = uc.getFacebookId();
		if (null == str) {
			ucpb.setFacebookId(str);
		} else {
			log.warn("UserCredential facebook id is null " + str);
		}
		
		str = uc.getUdid();
		if (null == str) {
			ucpb.setUdid(str);
		} else {
			log.warn("UserCredential udid is null " + str);
		}
		
		return ucpb.build();
	}
	
	public static UserDataRarelyUsedProto createUserDataRarelyUsedProto(
			UserDataRarelyAccessed udra) {
		UserDataRarelyUsedProto.Builder udrupb =
				UserDataRarelyUsedProto.newBuilder();
		
		udrupb.setUserUuid(udra.getUserId());
		
		String str = udra.getUdidForHistory();
		if (null != str) {
			udrupb.setUdidForHistory(str);
		} else {
			log.warn("UserDataRarelyAccessed udidForHistory is null " +
					udra);
		}
			
		str = udra.getGameCenterId();
		if (null != str) {
			udrupb.setGameCenterId(str);
		}
		
		str = udra.getDeviceToken();
		if (null != str) {
			udrupb.setDeviceToken(str);
		}
		
		udrupb.setFbIdSetOnUserCreate(udra.isFbIdSetOnUserCreate());
		
		Date d = udra.getLastLogin();
		if (null != d) {
			udrupb.setLastLogin(d.getTime());
		} else {
			log.warn("UserDataRarelyAccessed lastLogin is null " + d);
		}
		
		d = udra.getLastLogout();
		if (null != d) {
			udrupb.setLastLogout(d.getTime());
		}
		
		d = udra.getCreateTime();
		if (null != d) {
			udrupb.setCreateTime(d.getTime());
		} else {
			log.warn("UserDataRarelyAccessed createTime is null " + d);
		}
		
		d = udra.getLastObstacleSpawnTime();
		if (null != d) {
			udrupb.setLastObstacleSpawnTime(d.getTime());
		} else {
			log.warn("UserDataRarelyAccessed lastObstacleSpawnTime is null " +
					udra);
		} 
		
		d = udra.getLastMiniJobGeneratedTime();
		if (null != d) {
			udrupb.setLastMiniJobGeneratedTime(d.getTime());
		} else {
			log.warn("UserDataRarelyAccessed lastMiniJobGeneratedTime is null " +
					udra);
		}
		
		return udrupb.build();
	}
	
	public static FullUserProto createFullUserProto(User user,
			UserCredential uc, UserDataRarelyAccessed udra, Clan clan,
			PvpLeagueForUser plfu) {
		FullUserProto.Builder fupb = FullUserProto.newBuilder();
		
		if (null != user) {
			UserProto up = createUserProto(user, clan);
			fupb.setUp(up);
		}
		
		if (null != uc) {
			UserCredentialProto ucp = createUserCredentialProto(uc);
			fupb.setUcp(ucp);
		}
		
		if (null != udra) {
			UserDataRarelyUsedProto udrup = createUserDataRarelyUsedProto(udra);
			fupb.setUdrup(udrup);
		}
		
		if (null != plfu) {
			//TODO: finish implementing this
//			UserPvpLeagueProto uplp = createUserPvpLeagueProto(plfu);
		}
		
		return fupb.build();
	}
	
	public static StaticUserLevelInfoProto createStaticUserLevelInfoProto(
			StaticUserLevelInfo suli) {
		StaticUserLevelInfoProto.Builder sulipb = StaticUserLevelInfoProto
				.newBuilder();
		
		sulipb.setLevel(suli.getLvl());
		sulipb.setRequiredExperience(suli.getRequiredExp());
		
		return sulipb.build();
	}
	
}
