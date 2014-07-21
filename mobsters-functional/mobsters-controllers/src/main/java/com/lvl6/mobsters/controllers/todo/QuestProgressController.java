//package com.lvl6.mobsters.controllers.todo;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.MonsterForUser;
//import com.lvl6.mobsters.dynamo.QuestForUser;
//import com.lvl6.mobsters.dynamo.QuestJobForUser;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressRequestProto;
//import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressResponseProto;
//import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressResponseProto.QuestProgressStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.QuestProgressRequestEvent;
//import com.lvl6.mobsters.events.response.QuestProgressResponseEvent;
//import com.lvl6.mobsters.info.Quest;
//import com.lvl6.mobsters.info.QuestJob;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventQuestProto.QuestJobProto.QuestJobType;
//import com.lvl6.mobsters.noneventproto.NoneventQuestProto.UserQuestJobProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class QuestProgressController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(QuestProgressController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	@Autowired
//	protected QuestJobForUserRetrieveUtil questJobForUserRetrieveUtil;
//
//	@Autowired
//	protected UpdateUtil updateUtil;
//
//	public QuestProgressController()
//	{
//		numAllocatedThreads = 5;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new QuestProgressRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_QUEST_PROGRESS_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final QuestProgressRequestProto reqProto =
//		    ((QuestProgressRequestEvent) event).getQuestProgressRequestProto();
//
//		LOG.info("reqProto="
//		    + reqProto);
//
//		// get stuff client sent
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final int questId = reqProto.getQuestUuid();
//		final boolean isQuestComplete = reqProto.getIsComplete();
//
//		List<UserQuestJobProto> userQuestJobProtoList = reqProto.getUserQuestJobsList();
//		userQuestJobProtoList = new ArrayList<UserQuestJobProto>(userQuestJobProtoList);
//
//		final Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto =
//		    QuestUtils.mapifyByQuestJobId(userQuestJobProtoList);
//
//		// use this value when updating user quest, don't check this
//		// at the moment used for donate monster quests
//		List<Long> deleteUserMonsterUuids = reqProto.getDeleteUserMonsterUuidsList();
//		deleteUserMonsterUuids = new ArrayList<Long>(deleteUserMonsterUuids);
//		final Date deleteDate = new Date();
//
//		// set stuff to send to the client
//		final QuestProgressResponseProto.Builder resBuilder =
//		    QuestProgressResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(QuestProgressStatus.FAIL_OTHER);
//
//		svcTxManager.beginTransaction();
//		try {
//			// retrieve whatever is necessary from the db
//			final QuestForUser qfu = RetrieveUtils.questForUserRetrieveUtils()
//			    .getSpecificUnredeemedUserQuest(userUuid, questId);
//
//			final Map<Integer, QuestJobForUser> questJobIdToUserQuestJob =
//			    getQuestJobForUserRetrieveUtil().getQuestJobUuidsToJobs(userUuid, questId);
//
//			// only retrieve user monsters if client sent ids
//			Map<Long, MonsterForUser> userMonstersInDb = null;
//			if ((null != deleteUserMonsterUuids)
//			    && !deleteUserMonsterUuids.isEmpty()) {
//				userMonstersInDb = RetrieveUtils.monsterForUserRetrieveUtils()
//				    .getSpecificOrAllUserMonstersForUser(userUuid, deleteUserMonsterUuids);
//			}
//
//			final boolean legitProgress =
//			    checkLegitProgress(resBuilder, userUuid, questId, isQuestComplete, qfu,
//			        questJobIdToUserQuestJobProto, questJobIdToUserQuestJob,
//			        deleteUserMonsterUuids, userMonstersInDb);
//
//			boolean success = false;
//			if (legitProgress) {
//				success =
//				    writeChangesToDB(userUuid, questId, isQuestComplete,
//				        questJobIdToUserQuestJobProto, questJobIdToUserQuestJob,
//				        deleteUserMonsterUuids);
//			}
//
//			if (success) {
//				resBuilder.setStatus(QuestProgressStatus.SUCCESS);
//			}
//
//			final QuestProgressResponseEvent resEvent =
//			    new QuestProgressResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setQuestProgressResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error("fatal exception in QuestProgressController.processRequestEvent", e);
//			}
//
//			if (success) {
//				// TODO: RECORD THAT THE USER DELETED THESE MONSERS AND THE
//				// REASON
//				writeChangesToHistory(userUuid, questId, userMonstersInDb, deleteDate);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in QuestProgress processEvent", e);
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private boolean checkLegitProgress( final Builder resBuilder, final String userUuid,
//	    final int questId, final boolean isQuestComplete, final QuestForUser qfu,
//	    final Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto,
//	    final Map<Integer, QuestJobForUser> questJobUuidsToUserQuestJob,
//	    final List<Long> deleteUserMonsterUuids,
//	    final Map<Long, MonsterForUser> userMonstersInDb )
//	{
//
//		final Quest quest = QuestRetrieveUtils.getQuestForQuestId(questId);
//
//		// make sure the quest, relating to the user_quest being updated,
//		// exists
//		if (null == quest) {
//			LOG.error("no quest exists with id="
//			    + questId);
//			resBuilder.setStatus(QuestProgressStatus.FAIL_NO_QUEST_EXISTS);
//			return false;
//		}
//
//		validateDeletingMonsterQuestJob(questJobIdToUserQuestJobProto, deleteUserMonsterUuids,
//		    userMonstersInDb);
//
//		// Loop through each client UserQuestJobProto,
//		// if it isn't valid, remove it from being persisted to db
//		final Map<Integer, UserQuestJobProto> copy =
//		    new HashMap<Integer, UserQuestJobProto>(questJobIdToUserQuestJobProto);
//		final Set<Integer> completedQuestJobUuids = new HashSet<Integer>();
//
//		for (final Integer questJobId : copy.keySet()) {
//			final UserQuestJobProto uqjp = copy.get(questJobId);
//
//			// check if this is valid quest job
//			final boolean isValidQuestJobProgress =
//			    checkIfQuestJobCanBeUpdated(quest, questJobId, questJobUuidsToUserQuestJob);
//			if (!isValidQuestJobProgress) {
//				LOG.warn("client sent invalid questJob: "
//				    + uqjp
//				    + " removing it from being persisted to db");
//				questJobIdToUserQuestJobProto.remove(questJobId);
//				continue;
//			}
//
//			// check if this quest job can be declared as completed
//			final boolean isQuestJobComplete = uqjp.getIsComplete();
//			if (!isQuestJobComplete) {
//				continue;
//			}
//
//			final QuestJob qj = QuestJobRetrieveUtils.getQuestJobForQuestJobId(questJobId);
//			final boolean isQuestJobReallyComplete = checkIfQuestJobIsComplete(uqjp, qj);
//			if (!isQuestJobReallyComplete) {
//				LOG.warn("client incorreclty sent completed questJob: "
//				    + uqjp
//				    + " removing it from being persisted to db");
//				questJobIdToUserQuestJobProto.remove(questJobId);
//				continue;
//			}
//
//			// quest job is really complete
//			completedQuestJobUuids.add(questJobId);
//		}
//
//		// quest job is indeed complete
//		// since quest job complete, check if quest isComplete is set,
//		// if not then return true. otherwise, check to see that the other
//		// quest jobs for this quest are complete
//		if (!isQuestComplete) {
//			return true;
//		}
//
//		if (!checkEntireQuestComplete(questId, completedQuestJobUuids,
//		    questJobUuidsToUserQuestJob)) {
//			LOG.error("client says user's quest is complete, but it isn't. "
//			    + "userQuestJobs: "
//			    + questJobUuidsToUserQuestJob);
//			return false;
//		}
//
//		return true;
//	}
//
//	// go through each UserQuestJobProto, looking for the donate monster quest
//	private void validateDeletingMonsterQuestJob(
//	    final Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto,
//	    final List<Long> deleteUserMonsterUuids,
//	    final Map<Long, MonsterForUser> userMonstersInDb )
//	{
//
//		final Map<Integer, UserQuestJobProto> copy =
//		    new HashMap<Integer, UserQuestJobProto>(questJobIdToUserQuestJobProto);
//
//		// verify there's only one donate monster job, if deleting monsters
//		final Set<Integer> donateMonsterQuestJobUuids = new HashSet<Integer>();
//		for (final Integer questJobId : copy.keySet()) {
//
//			final QuestJob qj = QuestJobRetrieveUtils.getQuestJobForQuestJobId(questJobId);
//
//			final String questJobType = qj.getQuestJobType();
//			if (!questJobType.equals(QuestJobType.DONATE_MONSTER)) {
//				continue;
//			}
//			donateMonsterQuestJobUuids.add(questJobId);
//		}
//
//		// if there are no donate monster jobs,
//		// deleteUserMonsterUuids should be empty or null
//		if (donateMonsterQuestJobUuids.isEmpty()) {
//			if ((null != deleteUserMonsterUuids)
//			    && !deleteUserMonsterUuids.isEmpty()) {
//				LOG.warn("client wants to delete monsters but there are no"
//				    + " DONATE_MONSTER quest jobs.");
//				deleteUserMonsterUuids.clear();
//			}
//			return;
//		}
//
//		// if there is more than one DONATE_MONSTER quest job, delete them
//		// and empty out deleteUserMonsterUuids
//		if (donateMonsterQuestJobUuids.size() > 1) {
//			LOG.warn("client wants to satisfy more than one DONATE_MONSTER "
//			    + "questJob. removing these questJobUuids:"
//			    + donateMonsterQuestJobUuids);
//			for (final Integer questJobId : donateMonsterQuestJobUuids) {
//				questJobIdToUserQuestJobProto.remove(questJobId);
//			}
//			deleteUserMonsterUuids.clear();
//			return;
//		}
//
//		LOG.info("exactly one DONATE_MONSTER quest job, checking if enough"
//		    + " monster ids are sent");
//
//		final int donateQuestJobId = new ArrayList<Integer>(donateMonsterQuestJobUuids).get(0);
//		final UserQuestJobProto uqjp = questJobIdToUserQuestJobProto.get(donateQuestJobId);
//		if (!uqjp.getIsComplete()) {
//			LOG.error("client did not set DONATE_MONSTER quest job to complete");
//			questJobIdToUserQuestJobProto.remove(donateQuestJobId);
//			deleteUserMonsterUuids.clear();
//			return;
//		}
//
//		validateMonstersBeingDeleted(donateQuestJobId, questJobIdToUserQuestJobProto,
//		    deleteUserMonsterUuids, userMonstersInDb);
//	}
//
//	private void validateMonstersBeingDeleted( final int donateQuestJobId,
//	    final Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto,
//	    final List<Long> deleteUserMonsterUuids,
//	    final Map<Long, MonsterForUser> userMonstersInDb )
//	{
//		final int deleteSize = deleteUserMonsterUuids.size();
//		// make sure the deleted user monster ids exist
//		final int existingSize = userMonstersInDb.size();
//		if (deleteSize != existingSize) {
//			LOG.error("user trying to delete some nonexisting user_monsters."
//			    + " deleteUuids="
//			    + deleteUserMonsterUuids
//			    + "\t existing"
//			    + " user_monsters="
//			    + userMonstersInDb);
//			questJobIdToUserQuestJobProto.remove(donateQuestJobId);
//			deleteUserMonsterUuids.clear();
//			return;
//		}
//
//		// user wants to delete some monsters, make sure it's the right amount
//		final QuestJob qj = QuestJobRetrieveUtils.getQuestJobForQuestJobId(donateQuestJobId);
//
//		final int requiredProgress = qj.getQuantity();
//		final UserQuestJobProto uqjp = questJobIdToUserQuestJobProto.get(donateQuestJobId);
//		final int questProgress = uqjp.getProgress();
//
//		if ((deleteSize != requiredProgress)
//		    || (requiredProgress != questProgress)) {
//			LOG.error("insufficient vespene gas, jk, userMonsterUuids to delete"
//			    + " need: "
//			    + requiredProgress
//			    + ", sent ids: "
//			    + deleteUserMonsterUuids);
//			questJobIdToUserQuestJobProto.remove(donateQuestJobId);
//			deleteUserMonsterUuids.clear();
//			return;
//		}
//
//		if (!MonsterStuffUtils.checkAllUserMonstersAreComplete(deleteUserMonsterUuids,
//		    userMonstersInDb)) {
//			// user trying to delete incomplete user monster
//			LOG.error("user trying to delete an incomplete user monster."
//			    + " deletedUserMonsters="
//			    + userMonstersInDb
//			    + "\t QuestJob="
//			    + qj);
//			questJobIdToUserQuestJobProto.remove(donateQuestJobId);
//			deleteUserMonsterUuids.clear();
//		}
//	}
//
//	private boolean checkIfQuestJobCanBeUpdated( final Quest quest, final int questJobId,
//	    final Map<Integer, QuestJobForUser> questJobUuidsToUserQuestJob )
//	{
//
//		final QuestJob qj = QuestJobRetrieveUtils.getQuestJobForQuestJobId(questJobId);
//		// check to make sure that the user has this quest job
//		if (!questJobUuidsToUserQuestJob.containsKey(questJobId)) {
//			// expected to never go in here
//			LOG.error("user trying to update progress for nonexisting"
//			    + " QuestJobForUser with questJobId="
//			    + questJobId
//			    + "\t quest="
//			    + quest
//			    + "\t questJob="
//			    + qj
//			    + "\t userQuestJobs="
//			    + questJobUuidsToUserQuestJob);
//			return false;
//		}
//
//		// check if already complete
//		final QuestJobForUser qjfu = questJobUuidsToUserQuestJob.get(questJobId);
//		if (qjfu.isComplete()) {
//			LOG.error("quest job for user already complete. qjfu="
//			    + qjfu);
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean checkIfQuestJobIsComplete( final UserQuestJobProto uqjp, final QuestJob qj )
//	{
//		final int newProgress = uqjp.getProgress();
//
//		// client is saying quest job is complete
//		// now check if the quest job is actually complete
//		final int questJobMaxProgress = qj.getQuantity();
//		if (newProgress > questJobMaxProgress) {
//			LOG.warn("client is trying to set user_quest_job past the max"
//			    + " progress. questJob="
//			    + qj
//			    + "\t newProgress="
//			    + newProgress);
//		}
//
//		if (newProgress < questJobMaxProgress) {
//			LOG.error("client says quest job is complete but it isn't. sent:"
//			    + newProgress
//			    + ". progress should be questJob:"
//			    + qj);
//			return false;
//		}
//
//		return true;
//	}
//
//	// go through each questJob for quest and see if user finished questJob or
//	// has now just finished it
//	private boolean checkEntireQuestComplete( final int questId,
//	    final Set<Integer> questJobUuidsJustCompleted,
//	    final Map<Integer, QuestJobForUser> questJobUuidsToUserQuestJob )
//	{
//
//		// get all the quest's quest job ids
//		final Map<Integer, QuestJob> questJobIdToQuestJob =
//		    QuestJobRetrieveUtils.getQuestJobsForQuestId(questId);
//
//		// go through all the quest's job ids and see if user completed it
//		for (final Integer questJobId : questJobIdToQuestJob.keySet()) {
//			if (questJobUuidsJustCompleted.contains(questJobId)) {
//				continue;
//			}
//
//			if (!questJobUuidsToUserQuestJob.containsKey(questJobId)) {
//				LOG.info("questJobForUser does not exist for quest job id:"
//				    + questJobId);
//				return false;
//			}
//
//			final QuestJobForUser qjfu = questJobUuidsToUserQuestJob.get(questJobId);
//			if (!qjfu.isComplete()) {
//				LOG.info("questJobForUser is not complete: "
//				    + qjfu);
//				return false;
//			}
//		}
//
//		return true;
//	}
//
//	private boolean writeChangesToDB( final String userUuid, final int questId,
//	    final boolean questComplete,
//	    final Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto,
//	    final Map<Integer, QuestJobForUser> questJobIdToUserQuestJob,
//	    final List<Long> deleteUserMonsterUuids )
//	{
//
//		if (questJobIdToUserQuestJobProto.isEmpty()) {
//			return true;
//		}
//
//		final Map<Integer, QuestJobForUser> questJobIdToNewUserQuestJob =
//		    QuestUtils.deserializeUserQuestJobProto(questJobIdToUserQuestJobProto);
//
//		// update user quest jobs
//		int num = getUpdateUtil().updateUserQuestJobs(userUuid, questJobIdToNewUserQuestJob);
//		LOG.error("num updated for unredeemd user quest job:"
//		    + num);
//
//		// update user quest
//		if (questComplete
//		    && !getUpdateUtil().updateUserQuestIscomplete(userUuid, questId)) {
//			LOG.error("could not update user quest to complete. questId="
//			    + questId);
//			return false;
//		}
//
//		// delete the user monster ids
//		if ((null != deleteUserMonsterUuids)
//		    && !deleteUserMonsterUuids.isEmpty()) {
//			num = DeleteUtils.get()
//			    .deleteMonstersForUser(deleteUserMonsterUuids);
//			LOG.info("num user monsters deleted: "
//			    + num
//			    + "\t ids deleted: "
//			    + deleteUserMonsterUuids);
//		}
//		return true;
//	}
//
//	// TODO: FIX THIS
//	private void writeChangesToHistory( final String userUuid, final int questId,
//	    final Map<Long, MonsterForUser> deleteUserMonsters, final Date deleteDate )
//	{
//		//
//		// if (null == deleteUserMonsters || deleteUserMonsters.isEmpty()) {
//		// return;
//		// }
//		// String deleteReason = ControllerConstants.MFUDR__QUEST + questId;
//		//
//		// int size = deleteUserMonsters.size();
//		// List<String> deleteReasons = Collections.nCopies(size, deleteReason);
//		// Collection<MonsterForUser> userMonsters =
//		// deleteUserMonsters.values();
//		// List<MonsterForUser> userMonstersList = new
//		// ArrayList<MonsterForUser>(userMonsters);
//		// int num = InsertUtils.get().insertIntoMonsterForUserDeleted(userUuid,
//		// deleteReasons, userMonstersList, deleteDate);
//		//
//		// LOG.info("user monsters deleted for questId=" + questId + ". num=" +
//		// num);
//	}
//
//	public QuestJobForUserRetrieveUtil getQuestJobForUserRetrieveUtil()
//	{
//		return questJobForUserRetrieveUtil;
//	}
//
//	public void setQuestJobForUserRetrieveUtil(
//	    final QuestJobForUserRetrieveUtil questJobForUserRetrieveUtil )
//	{
//		this.questJobForUserRetrieveUtil = questJobForUserRetrieveUtil;
//	}
//
//	public UpdateUtil getUpdateUtil()
//	{
//		return updateUtil;
//	}
//
//	public void setUpdateUtil( final UpdateUtil updateUtil )
//	{
//		this.updateUtil = updateUtil;
//	}
//
//}
