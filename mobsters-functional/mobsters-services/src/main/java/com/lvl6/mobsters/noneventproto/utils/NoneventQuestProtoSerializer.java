package com.lvl6.mobsters.noneventproto.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.QuestJobForUser;
import com.lvl6.mobsters.info.Item;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.FullUserQuestProto;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.ItemProto;
import com.lvl6.mobsters.noneventproto.NoneventQuestProto.UserQuestJobProto;

public interface NoneventQuestProtoSerializer
{

	public List<FullUserQuestProto> createFullUserQuestDataLarges(
		List<QuestForUser> userQuests,
		Map<Integer, Quest> questIdsToQuests,
		Map<Integer, Collection<QuestJobForUser>> questIdToUserQuestJobs );

	public List<UserQuestJobProto> createUserQuestJobProto(
		int questId,
		Map<Integer, Collection<QuestJobForUser>> questIdToUserQuestJobs );

	public UserQuestJobProto createUserJobProto( QuestJobForUser qjfu );

	public abstract ItemProto createItemProtoFromItem( Item item );
}
