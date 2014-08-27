package com.lvl6.mobsters.noneventproto.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.QuestJobForUser;
import com.lvl6.mobsters.info.BaseIntPersistentObject;
import com.lvl6.mobsters.info.Item;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.noneventproto.NoneventChatProto.ColorProto;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.FullUserQuestProto;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.ItemProto;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.UserQuestJobProto;

@Component
public class NoneventQuestProtoSerializerImpl implements NoneventQuestProtoSerializer 
{
	private static final Logger LOG =
		LoggerFactory.getLogger(NoneventQuestProtoSerializerImpl.class);

	@Override
	public List<FullUserQuestProto> createFullUserQuestDataLarges(
		List<QuestForUser> userQuests,
		Map<Integer, BaseIntPersistentObject> questIdsToQuests,
		Map<Integer, Collection<QuestJobForUser>> questIdToUserQuestJobs )
	{
		List<FullUserQuestProto> fullUserQuestDataLargeProtos =
			new ArrayList<FullUserQuestProto>();

		for (QuestForUser userQuest : userQuests) {
			int questId = userQuest.getQuestId();
			Quest quest = (Quest) questIdsToQuests.get(questId);
			FullUserQuestProto.Builder builder = FullUserQuestProto.newBuilder();

			if (null == quest) {
				LOG.error(
					"no quest with id=%d, userQuest=%s",
					userQuest.getQuestId(), userQuest
				);
			} else {
				builder.setUserUuid(userQuest.getUserId());
				builder.setQuestId(quest.getId());
				builder.setIsRedeemed(userQuest.isRedeemed());
				builder.setIsComplete(userQuest.isComplete());

				// protofy the userQuestJobs
				List<UserQuestJobProto> userQuestJobProtoList =
					createUserQuestJobProto(questId, questIdToUserQuestJobs);

				builder.addAllUserQuestJobs(userQuestJobProtoList);
				fullUserQuestDataLargeProtos.add(builder.build());

			}
		}
		return fullUserQuestDataLargeProtos;
	}

	@Override
	public List<UserQuestJobProto> createUserQuestJobProto(
		int questId,
		Map<Integer, Collection<QuestJobForUser>> questIdToUserQuestJobs )
	{
		List<UserQuestJobProto> userQuestJobProtoList = new ArrayList<UserQuestJobProto>();

		if (!questIdToUserQuestJobs.containsKey(questId)) {
			// should never go in here!
			LOG.error(
				"user has quest but no quest_jobs for said quest; questId=%s, user's jobs=%s",
				questId, questIdToUserQuestJobs);
			return userQuestJobProtoList;
		}

		for (QuestJobForUser qjfu : questIdToUserQuestJobs.get(questId)) {
			UserQuestJobProto uqjp = createUserJobProto(qjfu);
			userQuestJobProtoList.add(uqjp);
		}
		return userQuestJobProtoList;
	}

	@Override
	public UserQuestJobProto createUserJobProto( QuestJobForUser qjfu )
	{
		UserQuestJobProto.Builder uqjpb = UserQuestJobProto.newBuilder();

		uqjpb.setQuestId(qjfu.getQuestId());
		uqjpb.setQuestJobId(qjfu.getQuestJobId());
		uqjpb.setIsComplete(qjfu.isComplete());
		uqjpb.setProgress(qjfu.getProgress());

		return uqjpb.build();
	}


	@Override
	public ItemProto createItemProtoFromItem(Item item) {
		ItemProto.Builder ipb = ItemProto.newBuilder();
	  	
		ipb.setItemId(item.getId());
	  	
	  	String str = item.getName();
	  	if (null != str) {
	  		ipb.setName(str);
	  	}
	  	
	  	str = item.getImgName();
	  	if (null != str) {
	  		ipb.setImgName(str);
	  	}
	  	
	  	str = item.getBorderImgName();
	  	if (null != str) {
	  		ipb.setBorderImgName(str);
	  	}
	  	
	  	ColorProto.Builder clrB = ColorProto.newBuilder();
	  	clrB.setBlue(item.getBlue());
	  	clrB.setGreen(item.getGreen());
	  	clrB.setRed(item.getRed());
	  	ipb.setColor(clrB.build());
	  	
	  	return ipb.build();
	}
}
