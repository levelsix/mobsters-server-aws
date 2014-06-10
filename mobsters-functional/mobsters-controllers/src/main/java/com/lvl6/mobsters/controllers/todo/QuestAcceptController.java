package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptRequestProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptResponseProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptResponseProto.QuestAcceptStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.QuestAcceptRequestEvent;
import com.lvl6.mobsters.events.response.QuestAcceptResponseEvent;
import com.lvl6.mobsters.info.Quest;
import com.lvl6.mobsters.info.QuestJob;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class QuestAcceptController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(QuestAcceptController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected InsertUtil insertUtils;

	public void setInsertUtils( final InsertUtil insertUtils )
	{
		this.insertUtils = insertUtils;
	}

	public QuestAcceptController()
	{
		numAllocatedThreads = 5;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new QuestAcceptRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_QUEST_ACCEPT_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final QuestAcceptRequestProto reqProto =
		    ((QuestAcceptRequestEvent) event).getQuestAcceptRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int questId = reqProto.getQuestUuid();

		final QuestAcceptResponseProto.Builder resBuilder =
		    QuestAcceptResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(QuestAcceptStatus.FAIL_OTHER);

		svcTxManager.beginTransaction();
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			final Quest quest = QuestRetrieveUtils.getQuestForQuestId(questId);

			final boolean legitAccept =
			    checkLegitAccept(resBuilder, user, userUuid, quest, questId);

			boolean success = false;
			if (legitAccept) {
				success = writeChangesToDB(userUuid, questId, quest);
			}

			if (success) {
				resBuilder.setStatus(QuestAcceptStatus.SUCCESS);
			}

			final QuestAcceptResponseEvent resEvent =
			    new QuestAcceptResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setQuestAcceptResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in QuestAcceptController.processRequestEvent", e);
			}

		} catch (final Exception e) {
			LOG.error("exception in QuestAccept processEvent", e);
		} finally {
			svcTxManager.commit();
		}
	}

	private boolean checkLegitAccept( final Builder resBuilder, final User user,
	    final String userUuid, final Quest quest, final int questId )
	{
		if ((user == null)
		    || (quest == null)) {
			LOG.error("parameter passed in is null. user="
			    + user
			    + ", quest="
			    + quest);
			return false;
		}
		final List<QuestForUser> inProgressAndRedeemedUserQuests =
		    RetrieveUtils.questForUserRetrieveUtils()
		        .getUserQuestsForUser(user.getId());
		final List<Integer> inProgressQuestUuids = new ArrayList<Integer>();
		final List<Integer> redeemedQuestUuids = new ArrayList<Integer>();

		if (inProgressAndRedeemedUserQuests != null) {
			for (final QuestForUser uq : inProgressAndRedeemedUserQuests) {
				if (uq.isRedeemed()) {
					redeemedQuestUuids.add(uq.getQuestId());
				} else {
					inProgressQuestUuids.add(uq.getQuestId());
				}
			}
			final List<Integer> availableQuestUuids =
			    QuestUtils.getAvailableQuestsForUser(redeemedQuestUuids, inProgressQuestUuids);
			if ((availableQuestUuids != null)
			    && availableQuestUuids.contains(quest.getId())) {
				resBuilder.setStatus(QuestAcceptStatus.SUCCESS);
				return true;
			} else {
				resBuilder.setStatus(QuestAcceptStatus.FAIL_NOT_AVAIL_TO_USER);
				LOG.error("quest with id "
				    + quest.getId()
				    + " is not available to user");
				return false;
			}
		}

		if (inProgressQuestUuids.contains(questId)) {
			LOG.error("db says user already accepted this quest. quest="
			    + quest);
			resBuilder.setStatus(QuestAcceptStatus.FAIL_ALREADY_ACCEPTED);
			return false;
		}

		return true;
	}

	private boolean writeChangesToDB( final String userUuid, final int questId,
	    final Quest quest )
	{
		// insert the quest for the user
		int num = InsertUtils.get()
		    .insertUserQuest(userUuid, questId);

		LOG.info("num quests inserted into quest_for_user: "
		    + num
		    + "\t quest inserted: "
		    + quest);

		// insert the quest job for user
		final Map<Integer, QuestJob> questJobUuidsToJobs =
		    QuestJobRetrieveUtils.getQuestJobsForQuestId(questId);
		final List<Integer> questJobUuids =
		    new ArrayList<Integer>(questJobUuidsToJobs.keySet());

		num = InsertUtils.get()
		    .insertUserQuestJobs(userUuid, questId, questJobUuids);
		LOG.info("num quest jobs inserted into quest_job_for_user: "
		    + num);

		return true;
	}

}
