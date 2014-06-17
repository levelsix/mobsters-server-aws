package com.lvl6.mobsters.noneventproto.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.QuestJobForUser;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.FullUserQuestProto;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.UserQuestJobProto;

public class CreateNoneventQuestProtoUtil
{

	private static Logger log = LoggerFactory.getLogger(new Object() {}.getClass()
		.getEnclosingClass());

	public static List<FullUserQuestProto> createFullUserQuestDataLarges(
		List<QuestForUser> userQuests,
		Map<Integer, Quest> questIdsToQuests,
		Map<Integer, Collection<QuestJobForUser>> questIdToUserQuestJobs )
	{
		List<FullUserQuestProto> fullUserQuestDataLargeProtos =
			new ArrayList<FullUserQuestProto>();

		for (QuestForUser userQuest : userQuests) {
			int questId = userQuest.getQuestId();
			Quest quest = questIdsToQuests.get(questId);
			FullUserQuestProto.Builder builder = FullUserQuestProto.newBuilder();

			if (null == quest) {
				log.error("no quest with id "
					+ userQuest.getQuestId()
					+ ", userQuest="
					+ userQuest);
			}

			if (null != quest) {
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

	public static List<UserQuestJobProto> createUserQuestJobProto(
		int questId,
		Map<Integer, Collection<QuestJobForUser>> questIdToUserQuestJobs )
	{
		List<UserQuestJobProto> userQuestJobProtoList = new ArrayList<UserQuestJobProto>();

		if (!questIdToUserQuestJobs.containsKey(questId)) {
			// should never go in here!
			log.error("user has quest but no quest_jobs for said quest."
				+ " questId="
				+ questId
				+ "\t user's quest jobs are:"
				+ questIdToUserQuestJobs);
			return userQuestJobProtoList;
		}

		for (QuestJobForUser qjfu : questIdToUserQuestJobs.get(questId)) {
			UserQuestJobProto uqjp = createUserJobProto(qjfu);
			userQuestJobProtoList.add(uqjp);
		}
		return userQuestJobProtoList;
	}

	public static UserQuestJobProto createUserJobProto( QuestJobForUser qjfu )
	{
		UserQuestJobProto.Builder uqjpb = UserQuestJobProto.newBuilder();

		uqjpb.setQuestId(qjfu.getQuestId());
		uqjpb.setQuestJobId(qjfu.getQuestJobId());
		uqjpb.setIsComplete(qjfu.isComplete());
		uqjpb.setProgress(qjfu.getProgress());

		return uqjpb.build();
	}

}
