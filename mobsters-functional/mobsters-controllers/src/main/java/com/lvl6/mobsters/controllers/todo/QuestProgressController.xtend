//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.MonsterForUser
//import com.lvl6.mobsters.dynamo.QuestForUser
//import com.lvl6.mobsters.dynamo.QuestJobForUser
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressResponseProto
//import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressResponseProto.QuestProgressStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.QuestProgressRequestEvent
//import com.lvl6.mobsters.events.response.QuestProgressResponseEvent
//import com.lvl6.mobsters.info.Quest
//import com.lvl6.mobsters.info.QuestJob
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.noneventproto.NoneventQuestProto.QuestJobProto.QuestJobType
//import com.lvl6.mobsters.noneventproto.NoneventQuestProto.UserQuestJobProto
//import com.lvl6.mobsters.server.EventController
//import java.util.ArrayList
//import java.util.Date
//import java.util.HashMap
//import java.util.HashSet
//import java.util.List
//import java.util.Map
//import java.util.Set
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class QuestProgressController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(QuestProgressController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Autowired
//	protected var QuestJobForUserRetrieveUtil questJobForUserRetrieveUtil
//	@Autowired
//	protected var UpdateUtil updateUtil
//
//	new()
//	{
//		numAllocatedThreads = 5
//	}
//
//	override createRequestEvent()
//	{
//		new QuestProgressRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_QUEST_PROGRESS_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as QuestProgressRequestEvent)).questProgressRequestProto
//		LOG.info('reqProto=' + reqProto)
//		val senderProto = reqProto.sender
//		val userUuid = senderProto.userUuid
//		val questId = reqProto.questUuid
//		val isQuestComplete = reqProto.isComplete
//		var userQuestJobProtoList = reqProto.userQuestJobsList
//		userQuestJobProtoList = new ArrayList<UserQuestJobProto>(userQuestJobProtoList)
//		val questJobIdToUserQuestJobProto = QuestUtils::mapifyByQuestJobId(userQuestJobProtoList)
//		var deleteUserMonsterUuids = reqProto.deleteUserMonsterUuidsList
//		deleteUserMonsterUuids = new ArrayList<Long>(deleteUserMonsterUuids)
//		val deleteDate = new Date()
//		val resBuilder = QuestProgressResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = QuestProgressStatus.FAIL_OTHER
//		svcTxManager.beginTransaction
//		try
//		{
//			val qfu = RetrieveUtils::questForUserRetrieveUtils.
//				getSpecificUnredeemedUserQuest(userUuid, questId)
//			val questJobIdToUserQuestJob = questJobForUserRetrieveUtil.
//				getQuestJobUuidsToJobs(userUuid, questId)
//			var Map<Long, MonsterForUser> userMonstersInDb = null
//			if ((null !== deleteUserMonsterUuids) && !deleteUserMonsterUuids.empty)
//			{
//				userMonstersInDb = RetrieveUtils::monsterForUserRetrieveUtils.
//					getSpecificOrAllUserMonstersForUser(userUuid, deleteUserMonsterUuids)
//			}
//			val legitProgress = checkLegitProgress(resBuilder, userUuid, questId,
//				isQuestComplete, qfu, questJobIdToUserQuestJobProto, questJobIdToUserQuestJob,
//				deleteUserMonsterUuids, userMonstersInDb)
//			var success = false
//			if (legitProgress)
//			{
//				success = writeChangesToDB(userUuid, questId, isQuestComplete,
//					questJobIdToUserQuestJobProto, questJobIdToUserQuestJob,
//					deleteUserMonsterUuids)
//			}
//			if (success)
//			{
//				resBuilder.status = QuestProgressStatus.SUCCESS
//			}
//			val resEvent = new QuestProgressResponseEvent(senderProto.userUuid)
//			resEvent.tag = event.tag
//			resEvent.questProgressResponseProto = resBuilder.build
//			LOG.info('Writing event: ' + resEvent)
//			try
//			{
//				eventWriter.writeEvent(resEvent)
//			}
//			catch (Throwable e)
//			{
//				LOG.error('fatal exception in QuestProgressController.processRequestEvent', e)
//			}
//			if (success)
//			{
//				writeChangesToHistory(userUuid, questId, userMonstersInDb, deleteDate)
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in QuestProgress processEvent', e)
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def checkLegitProgress(Builder resBuilder, String userUuid, int questId,
//		boolean isQuestComplete, QuestForUser qfu,
//		Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto,
//		Map<Integer, QuestJobForUser> questJobUuidsToUserQuestJob,
//		List<Long> deleteUserMonsterUuids, Map<Long, MonsterForUser> userMonstersInDb)
//	{
//		val quest = QuestRetrieveUtils::getQuestForQuestId(questId)
//		if (null === quest)
//		{
//			LOG.error('no quest exists with id=' + questId)
//			resBuilder.status = QuestProgressStatus.FAIL_NO_QUEST_EXISTS
//			return false;
//		}
//		validateDeletingMonsterQuestJob(questJobIdToUserQuestJobProto, deleteUserMonsterUuids,
//			userMonstersInDb)
//		val copy = new HashMap<Integer, UserQuestJobProto>(questJobIdToUserQuestJobProto)
//		val completedQuestJobUuids = new HashSet<Integer>()
//		for (questJobId : copy.keySet)
//		{
//			val uqjp = copy.get(questJobId)
//			val isValidQuestJobProgress = checkIfQuestJobCanBeUpdated(quest, questJobId,
//				questJobUuidsToUserQuestJob)
//			if (!isValidQuestJobProgress)
//			{
//				LOG.warn(
//					'client sent invalid questJob: ' + uqjp +
//						' removing it from being persisted to db')
//				questJobIdToUserQuestJobProto.remove(questJobId)
//        continue;
//			}
//			val isQuestJobComplete = uqjp.isComplete
//			if (!isQuestJobComplete)
//			{
//				continue;
//			}
//			val qj = QuestJobRetrieveUtils::getQuestJobForQuestJobId(questJobId)
//			val isQuestJobReallyComplete = checkIfQuestJobIsComplete(uqjp, qj)
//			if (!isQuestJobReallyComplete)
//			{
//				LOG.warn(
//					'client incorreclty sent completed questJob: ' + uqjp +
//						' removing it from being persisted to db')
//				questJobIdToUserQuestJobProto.remove(questJobId)
//        continue;
//			}
//			completedQuestJobUuids.add(questJobId)
//		}
//		if (!isQuestComplete)
//		{
//			return true;
//		}
//		if (!checkEntireQuestComplete(questId, completedQuestJobUuids,
//			questJobUuidsToUserQuestJob))
//		{
//			LOG.error(
//				"client says user's quest is complete, but it isn't. " + 'userQuestJobs: ' +
//					questJobUuidsToUserQuestJob)
//			return false;
//		}
//		true
//	}
//
//	private def validateDeletingMonsterQuestJob(
//		Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto,
//		List<Long> deleteUserMonsterUuids, Map<Long, MonsterForUser> userMonstersInDb)
//	{
//		val copy = new HashMap<Integer, UserQuestJobProto>(questJobIdToUserQuestJobProto)
//		val donateMonsterQuestJobUuids = new HashSet<Integer>()
//		for (questJobId : copy.keySet)
//		{
//			val qj = QuestJobRetrieveUtils::getQuestJobForQuestJobId(questJobId)
//			val questJobType = qj.questJobType
//			if (questJobType != QuestJobType::DONATE_MONSTER)
//			{
//				continue;
//			}
//			donateMonsterQuestJobUuids.add(questJobId)
//		}
//		if (donateMonsterQuestJobUuids.empty)
//		{
//			if ((null !== deleteUserMonsterUuids) && !deleteUserMonsterUuids.empty)
//			{
//				LOG.warn(
//					'client wants to delete monsters but there are no' +
//						' DONATE_MONSTER quest jobs.')
//				deleteUserMonsterUuids.clear
//			}
//			return;
//		}
//		if (donateMonsterQuestJobUuids.size > 1)
//		{
//			LOG.warn(
//				'client wants to satisfy more than one DONATE_MONSTER ' +
//					'questJob. removing these questJobUuids:' + donateMonsterQuestJobUuids)
//			for (questJobId : donateMonsterQuestJobUuids)
//			{
//				questJobIdToUserQuestJobProto.remove(questJobId)
//			}
//			deleteUserMonsterUuids.clear
//			return;
//		}
//		LOG.info(
//			'exactly one DONATE_MONSTER quest job, checking if enough' + ' monster ids are sent')
//		val donateQuestJobId = new ArrayList<Integer>(donateMonsterQuestJobUuids).get(0)
//		val uqjp = questJobIdToUserQuestJobProto.get(donateQuestJobId)
//		if (!uqjp.isComplete)
//		{
//			LOG.error('client did not set DONATE_MONSTER quest job to complete')
//			questJobIdToUserQuestJobProto.remove(donateQuestJobId)
//			deleteUserMonsterUuids.clear
//			return;
//		}
//		validateMonstersBeingDeleted(donateQuestJobId, questJobIdToUserQuestJobProto,
//			deleteUserMonsterUuids, userMonstersInDb)
//	}
//
//	private def validateMonstersBeingDeleted(int donateQuestJobId,
//		Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto,
//		List<Long> deleteUserMonsterUuids, Map<Long, MonsterForUser> userMonstersInDb)
//	{
//		val deleteSize = deleteUserMonsterUuids.size
//		val existingSize = userMonstersInDb.size
//		if (deleteSize !== existingSize)
//		{
//			LOG.error(
//				'user trying to delete some nonexisting user_monsters.' + ' deleteUuids=' +
//					deleteUserMonsterUuids + '	 existing' + ' user_monsters=' + userMonstersInDb)
//			questJobIdToUserQuestJobProto.remove(donateQuestJobId)
//			deleteUserMonsterUuids.clear
//			return;
//		}
//		val qj = QuestJobRetrieveUtils::getQuestJobForQuestJobId(donateQuestJobId)
//		val requiredProgress = qj.quantity
//		val uqjp = questJobIdToUserQuestJobProto.get(donateQuestJobId)
//		val questProgress = uqjp.progress
//		if ((deleteSize !== requiredProgress) || (requiredProgress !== questProgress))
//		{
//			LOG.error(
//				'insufficient vespene gas, jk, userMonsterUuids to delete' + ' need: ' +
//					requiredProgress + ', sent ids: ' + deleteUserMonsterUuids)
//			questJobIdToUserQuestJobProto.remove(donateQuestJobId)
//			deleteUserMonsterUuids.clear
//			return;
//		}
//		if (!MonsterStuffUtils::checkAllUserMonstersAreComplete(deleteUserMonsterUuids,
//			userMonstersInDb))
//		{
//			LOG.error(
//				'user trying to delete an incomplete user monster.' + ' deletedUserMonsters=' +
//					userMonstersInDb + '	 QuestJob=' + qj)
//			questJobIdToUserQuestJobProto.remove(donateQuestJobId)
//			deleteUserMonsterUuids.clear
//		}
//	}
//
//	private def checkIfQuestJobCanBeUpdated(Quest quest, int questJobId,
//		Map<Integer, QuestJobForUser> questJobUuidsToUserQuestJob)
//	{
//		val qj = QuestJobRetrieveUtils::getQuestJobForQuestJobId(questJobId)
//		if (!questJobUuidsToUserQuestJob.containsKey(questJobId))
//		{
//			LOG.error(
//				'user trying to update progress for nonexisting' +
//					' QuestJobForUser with questJobId=' + questJobId + '	 quest=' + quest +
//					'	 questJob=' + qj + '	 userQuestJobs=' + questJobUuidsToUserQuestJob)
//			return false;
//		}
//		val qjfu = questJobUuidsToUserQuestJob.get(questJobId)
//		if (qjfu.complete)
//		{
//			LOG.error('quest job for user already complete. qjfu=' + qjfu)
//			return false;
//		}
//		true
//	}
//
//	private def checkIfQuestJobIsComplete(UserQuestJobProto uqjp, QuestJob qj)
//	{
//		val newProgress = uqjp.progress
//		val questJobMaxProgress = qj.quantity
//		if (newProgress > questJobMaxProgress)
//		{
//			LOG.warn(
//				'client is trying to set user_quest_job past the max' + ' progress. questJob=' +
//					qj + '	 newProgress=' + newProgress)
//		}
//		if (newProgress < questJobMaxProgress)
//		{
//			LOG.error(
//				"client says quest job is complete but it isn't. sent:" + newProgress +
//					'. progress should be questJob:' + qj)
//			return false;
//		}
//		true
//	}
//
//	private def checkEntireQuestComplete(int questId, Set<Integer> questJobUuidsJustCompleted,
//		Map<Integer, QuestJobForUser> questJobUuidsToUserQuestJob)
//	{
//		val questJobIdToQuestJob = QuestJobRetrieveUtils::getQuestJobsForQuestId(questId)
//		for (questJobId : questJobIdToQuestJob.keySet)
//		{
//			if (questJobUuidsJustCompleted.contains(questJobId))
//			{
//				continue;
//			}
//			if (!questJobUuidsToUserQuestJob.containsKey(questJobId))
//			{
//				LOG.info('questJobForUser does not exist for quest job id:' + questJobId)
//				return false;
//			}
//			val qjfu = questJobUuidsToUserQuestJob.get(questJobId)
//			if (!qjfu.complete)
//			{
//				LOG.info('questJobForUser is not complete: ' + qjfu)
//				return false;
//			}
//		}
//		true
//	}
//
//	private def writeChangesToDB(String userUuid, int questId, boolean questComplete,
//		Map<Integer, UserQuestJobProto> questJobIdToUserQuestJobProto,
//		Map<Integer, QuestJobForUser> questJobIdToUserQuestJob,
//		List<Long> deleteUserMonsterUuids)
//	{
//		if (questJobIdToUserQuestJobProto.empty)
//		{
//			return true;
//		}
//		val questJobIdToNewUserQuestJob = QuestUtils::
//			deserializeUserQuestJobProto(questJobIdToUserQuestJobProto)
//		var num = updateUtil.updateUserQuestJobs(userUuid, questJobIdToNewUserQuestJob)
//		LOG.error('num updated for unredeemd user quest job:' + num)
//		if (questComplete && !updateUtil.updateUserQuestIscomplete(userUuid, questId))
//		{
//			LOG.error('could not update user quest to complete. questId=' + questId)
//			return false;
//		}
//		if ((null !== deleteUserMonsterUuids) && !deleteUserMonsterUuids.empty)
//		{
//			num = DeleteUtils::get.deleteMonstersForUser(deleteUserMonsterUuids)
//			LOG.info(
//				'num user monsters deleted: ' + num + '	 ids deleted: ' + deleteUserMonsterUuids)
//		}
//		true
//	}
//
//	private def writeChangesToHistory(String userUuid, int questId,
//		Map<Long, MonsterForUser> deleteUserMonsters, Date deleteDate)
//	{
//	}
//
//	def getQuestJobForUserRetrieveUtil()
//	{
//		questJobForUserRetrieveUtil
//	}
//
//	def setQuestJobForUserRetrieveUtil(QuestJobForUserRetrieveUtil questJobForUserRetrieveUtil)
//	{
//		this.questJobForUserRetrieveUtil = questJobForUserRetrieveUtil
//	}
//
//	def getUpdateUtil()
//	{
//		updateUtil
//	}
//
//	def setUpdateUtil(UpdateUtil updateUtil)
//	{
//		this.updateUtil = updateUtil
//	}
//}
