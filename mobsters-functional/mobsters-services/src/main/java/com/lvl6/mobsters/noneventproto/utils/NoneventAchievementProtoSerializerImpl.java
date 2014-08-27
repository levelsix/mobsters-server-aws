package com.lvl6.mobsters.noneventproto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.AchievementForUser;
import com.lvl6.mobsters.info.Achievement;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Element;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.Quality;
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.ResourceType;
import com.lvl6.mobsters.noneventproto.NoneventAchievementProto.AchievementProto;
import com.lvl6.mobsters.noneventproto.NoneventAchievementProto.AchievementProto.AchievementType;
import com.lvl6.mobsters.noneventproto.NoneventAchievementProto.UserAchievementProto;

@Component
public class NoneventAchievementProtoSerializerImpl implements NoneventAchievementProtoSerializer 
{
	private static final Logger LOG =
		LoggerFactory.getLogger(NoneventAchievementProtoSerializerImpl.class);

	@Override
	public AchievementProto createAchievementProto( Achievement a )
	{
		AchievementProto.Builder ab = AchievementProto.newBuilder();

		ab.setAchievementId(a.getId());

		String str = a.getAchievementName();
		if (null != str) {
			ab.setName(str);
		}

		str = a.getDescription();
		if (null != str) {
			ab.setDescription(str);
		}

		ab.setGemReward(a.getGemReward());
		ab.setLvl(a.getLvl());

		str = a.getAchievementType();
		if (null != str) {
			try {
				AchievementType at = AchievementType.valueOf(str);
				ab.setAchievementType(at);
			} catch(Throwable e) {
				LOG.error(
					String.format(
						"Could not lookup AchievementType enum value by name. achievementType=%s; achievement=%s",
						str, a),
					e);
			}
		}

		str = a.getResourceType();
		if (null != str) {
			try {
				ResourceType rt = ResourceType.valueOf(str);
				ab.setResourceType(rt);
			} catch(Throwable e) {
				LOG.error(
					String.format(
						"Could not lookup ResourceType enum value by name. resourceType=%s; achievement=%s",
						str, a),
					e);
			}
		}

		str = a.getMonsterElement();
		if (null != str) {
			try {
				Element me = Element.valueOf(str);
				ab.setElement(me);
			} catch(Throwable e) {
				LOG.error(
					String.format(
						"Could not lookup Element enum value by name. element=%s; achievement=%s",
						str, a),
					e);
			}
		}

		str = a.getMonsterQuality();
		if (null != str) {
			try {
				Quality mq = Quality.valueOf(str);
				ab.setQuality(mq);
			} catch(Throwable e) {
				LOG.error(
					String.format(
						"Could not lookup Quality enum value by name. quality=%s; achievement=%s",
						str, a),
					e);
			}
		}

		ab.setStaticDataId(a.getStaticDataId());
		ab.setQuantity(a.getQuantity());
		ab.setPriority(a.getPriority());
		ab.setPrerequisiteId(a.getPrerequisiteAchievement().getId());
		ab.setSuccessorId(a.getSuccessorAchievement().getId());

		return ab.build();
	}

	
	//BEGIN USER DATA SERIALIZATION
	
	
	@Override
	public UserAchievementProto createUserAchievementProto(
		AchievementForUser afu)
	{
		UserAchievementProto.Builder uapb = UserAchievementProto.newBuilder();

		uapb.setAchievementId(afu.getAchievementId());
		uapb.setProgress(afu.getProgress());
		uapb.setIsComplete(afu.isComplete());
		uapb.setIsRedeemed(afu.isRedeemed());

		return uapb.build();
	}
}
