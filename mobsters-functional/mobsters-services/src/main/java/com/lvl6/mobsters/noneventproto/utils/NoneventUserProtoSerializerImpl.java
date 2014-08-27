package com.lvl6.mobsters.noneventproto.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

@Component
public class NoneventUserProtoSerializerImpl implements NoneventUserProtoSerializer
{
	private static final Logger LOG =
		LoggerFactory.getLogger(NoneventUserProtoSerializerImpl.class);

	@Override
	public ClanProto createClanProto(Clan clan) {
		ClanProto.Builder cpb = ClanProto.newBuilder();
		
		String clanUuid = clan.getId();
		cpb.setClanUuid(clanUuid);
		
		String str = clan.getName();
		if (null != str) {
			cpb.setName(str);
		} else {
			LOG.warn("clan name is null. clanUuid=%s", clanUuid);
		}
		
		Date d = clan.getCreateTime();
		if (null != d) {
			cpb.setCreateTime(d.getTime());
		} else {
			LOG.warn("clan create time is null. clanUuid=%s", clanUuid);
		}
		
		str = clan.getDescription();
		if (null != str) {
			cpb.setDescription(str);
		} else {
			LOG.warn("clan description is null. clanUuid=%s", clanUuid);
		}
		
		str = clan.getTag();
		if (null != str) {
			cpb.setTag(str);
		} else {
			LOG.warn("clan tag is null. clanUuid=%s", clanUuid);
		}
		
		cpb.setRequestToJoinRequired(clan.isRequestToJoinRequired());
		cpb.setClanIconId(clan.getClanIconId());
		
		return cpb.build();
	}
	
	@Override
	public MinimumUserProto createMinimumUserProto(
			User user, Clan clan) {
		MinimumUserProto.Builder mupb = MinimumUserProto.newBuilder();
		
		final String userUuid = user.getId();
		mupb.setUserUuid(userUuid);

		String str = user.getName();
		if (null != str) {
			mupb.setName(str);
		} else {
			LOG.warn("user name is null. userUuid=%s", userUuid);
		}
		
		if (null != clan) {
			ClanProto cp = createClanProto(clan);
			mupb.setClan(cp);
		}
		
		mupb.setLevel(user.getLevel());
		
		return mupb.build();
	}
	
	@Override
	public MinimumUserProtoWithFacebookId createMinimumUserProtoWithFacebookId(
			String facebookId, User user, Clan clan) {
		MinimumUserProtoWithFacebookId.Builder mupwfib =
				MinimumUserProtoWithFacebookId.newBuilder();
		
		if (null != facebookId) {
			mupwfib.setFacebookId(facebookId);
		} else {
			LOG.warn("user facebook id is null: %s", user.getId());
		}
		
		MinimumUserProto mup = createMinimumUserProto(user, clan);
		mupwfib.setMinUserProto(mup);
		
		return mupwfib.build();
	}
	
	@Override
	public UserProto createUserProto(User user, Clan clan) {
		UserProto.Builder upb = UserProto.newBuilder();
		
		upb.setUserUuid(user.getId());
		
		String str = user.getName();
		
		if (null != str) {
			upb.setName(str);
		} else {
			LOG.warn("user name is null");
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
	
	@Override
	public UserCredentialProto createUserCredentialProto(
			UserCredential uc) {
		UserCredentialProto.Builder ucpb = UserCredentialProto.newBuilder();
		
		final String userUuid = uc.getUserId();
		ucpb.setUserUuid(userUuid);
		
		String str = uc.getFacebookId();
		if (null == str) {
			ucpb.setFacebookId(str);
		} else {
			LOG.warn("UserCredential facebook id is null. userUuid=%s", userUuid);
		}
		
		str = uc.getUdid();
		if (null == str) {
			ucpb.setUdid(str);
		} else {
			LOG.warn("UserCredential udid is null. userUuid=%s", userUuid);
		}
		
		return ucpb.build();
	}
	
	@Override
	public UserDataRarelyUsedProto createUserDataRarelyUsedProto(
			UserDataRarelyAccessed udra) {
		UserDataRarelyUsedProto.Builder udrupb =
				UserDataRarelyUsedProto.newBuilder();
		
		final String userUuid = udra.getUserId();
		udrupb.setUserUuid(userUuid);
		
		String str = udra.getUdidForHistory();
		if (null != str) {
			udrupb.setUdidForHistory(str);
		} else {
			LOG.warn(
				"UserDataRarelyAccessed udidForHistory is null. userUuid=%s", userUuid);
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
			LOG.warn("UserDataRarelyAccessed lastLogin is null. userUuid=%s", userUuid);
		}
		
		d = udra.getLastLogout();
		if (null != d) {
			udrupb.setLastLogout(d.getTime());
		}
		
		d = udra.getCreateTime();
		if (null != d) {
			udrupb.setCreateTime(d.getTime());
		} else {
			LOG.warn(
				"UserDataRarelyAccessed createTime is null. userUuid=%s", userUuid);
		}
		
		d = udra.getLastObstacleSpawnTime();
		if (null != d) {
			udrupb.setLastObstacleSpawnTime(d.getTime());
		} else {
			LOG.warn(
				"UserDataRarelyAccessed lastObstacleSpawnTime is null. userUuid=%s",
				userUuid);
		} 
		
		d = udra.getLastMiniJobGeneratedTime();
		if (null != d) {
			udrupb.setLastMiniJobGeneratedTime(d.getTime());
		} else {
			LOG.warn(
				"UserDataRarelyAccessed lastMiniJobGeneratedTime is null. userUuid=%s",
				userUuid);
		}
		
		return udrupb.build();
	}
	
	@Override
	public FullUserProto createFullUserProto(User user,
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
	
	@Override
	public StaticUserLevelInfoProto createStaticUserLevelInfoProto(
			StaticUserLevelInfo suli) {
		StaticUserLevelInfoProto.Builder sulipb = StaticUserLevelInfoProto
				.newBuilder();
		
		sulipb.setLevel(suli.getLvl());
		sulipb.setRequiredExperience(suli.getRequiredExp());
		
		return sulipb.build();
	}
	
}
