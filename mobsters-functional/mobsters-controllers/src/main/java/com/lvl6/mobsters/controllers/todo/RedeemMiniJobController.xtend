package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.MiniJobForUser
import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto.RedeemMiniJobStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.RedeemMiniJobRequestEvent
import com.lvl6.mobsters.events.response.RedeemMiniJobResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.ControllerConstants
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.HashMap
import java.util.List
import java.util.Map
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RedeemMiniJobController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(RedeemMiniJobController))
	@Autowired
	protected var DataServiceTxManager svcTxManager
	@Autowired
	protected var MonsterForUserRetrieveUtils monsterForUserRetrieveUtils
	@Autowired
	protected var MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new RedeemMiniJobRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_REDEEM_MINI_JOB_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as RedeemMiniJobRequestEvent)).redeemMiniJobRequestProto
		LOG.info('reqProto=' + reqProto)
		val senderResourcesProto = reqProto.sender
		val senderProto = senderResourcesProto.minUserProto
		val userUuid = senderProto.userUuid
		val now = new Date(reqProto.clientTime)
		val clientTime = new Timestamp(reqProto.clientTime)
		val userMiniJobId = reqProto.userMiniJobUuid
		val resBuilder = RedeemMiniJobResponseProto::newBuilder
		resBuilder.sender = senderResourcesProto
		resBuilder.status = RedeemMiniJobStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			val user = RetrieveUtils::userRetrieveUtils.getUserById(senderProto.userUuid)
			val mjfuList = new ArrayList<MiniJobForUser>()
			val legit = checkLegit(resBuilder, userUuid, user, userMiniJobId, mjfuList)
			var success = false
			val currencyChange = new HashMap<String, Integer>()
			val previousCurrency = new HashMap<String, Integer>()
			if (legit)
			{
				val mjfu = mjfuList.get(0)
				success = writeChangesToDB(resBuilder, userUuid, user, userMiniJobId, mjfu, now,
					clientTime, currencyChange, previousCurrency)
			}
			if (success)
			{
				resBuilder.status = RedeemMiniJobStatus.SUCCESS
			}
			val resEvent = new RedeemMiniJobResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.redeemMiniJobResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in RedeemMiniJobController.processRequestEvent', e)
			}
			if (success)
			{
				val resEventUpdate = MiscMethods::
					createUpdateClientUserResponseEventAndUpdateLeaderboard(user, null)
				resEventUpdate.tag = event.tag
				LOG.info('Writing event: ' + resEventUpdate)
				try
				{
					eventWriter.writeEvent(resEventUpdate)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in RedeemMiniJobController.processRequestEvent',
						e)
				}
				writeToUserCurrencyHistory(user, userMiniJobId, currencyChange, clientTime,
					previousCurrency)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in RedeemMiniJobController processEvent', e)
			try
			{
				resBuilder.status = RedeemMiniJobStatus.FAIL_OTHER
				val resEvent = new RedeemMiniJobResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.redeemMiniJobResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error('fatal exception in RedeemMiniJobController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in RedeemMiniJobController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegit(Builder resBuilder, String userUuid, User user, long userMiniJobId,
		List<MiniJobForUser> mjfuList)
	{
		val userMiniJobUuids = Collections::singleton(userMiniJobId)
		val idToUserMiniJob = miniJobForUserRetrieveUtil.
			getSpecificOrAllIdToMiniJobForUser(userUuid, userMiniJobUuids)
		if (idToUserMiniJob.empty)
		{
			LOG.error('no UserMiniJob exists with id=' + userMiniJobId)
			resBuilder.status = RedeemMiniJobStatus.FAIL_NO_MINI_JOB_EXISTS
			return false;
		}
		val mjfu = idToUserMiniJob.get(userMiniJobId)
		if (null === mjfu.timeCompleted)
		{
			LOG.error('MiniJobForUser incomplete: ' + mjfu)
			return false;
		}
		val miniJobId = mjfu.miniJobId
		val mj = MiniJobRetrieveUtils::getMiniJobForMiniJobId(miniJobId)
		if (null === mj)
		{
			LOG.error(
				'no MiniJob exists with id=' + miniJobId + '	 invalid MiniJobForUser=' + mjfu)
			resBuilder.status = RedeemMiniJobStatus.FAIL_NO_MINI_JOB_EXISTS
			return false;
		}
		mjfuList.add(mjfu)
		true
	}

	private def writeChangesToDB(Builder resBuilder, String userUuid, User user,
		long userMiniJobId, MiniJobForUser mjfu, Date now, Timestamp clientTime,
		Map<String, Integer> currencyChange, Map<String, Integer> previousCurrency)
	{
		val miniJobId = mjfu.miniJobId
		val mj = MiniJobRetrieveUtils::getMiniJobForMiniJobId(miniJobId)
		val prevGems = user.gems
		val prevCash = user.cash
		val prevOil = user.oil
		val gemsChange = mj.gemReward
		val cashChange = mj.cashReward
		val oilChange = mj.oilReward
		val monsterIdReward = mj.monsterIdReward
		if (!updateUser(user, gemsChange, cashChange, oilChange))
		{
			LOG.error(
				'unexpected error: could not decrement user gems by ' + gemsChange +
					', cash by ' + cashChange + ', and oil by ' + oilChange)
			return false;
		}
		else
		{
			if (0 !== gemsChange)
			{
				currencyChange.put(MiscMethods::gems, gemsChange)
				previousCurrency.put(MiscMethods::gems, prevGems)
			}
			if (0 !== cashChange)
			{
				currencyChange.put(MiscMethods::cash, cashChange)
				previousCurrency.put(MiscMethods::cash, prevCash)
			}
			if (0 !== oilChange)
			{
				currencyChange.put(MiscMethods::oil, oilChange)
				previousCurrency.put(MiscMethods::oil, prevOil)
			}
		}
		if (0 !== monsterIdReward)
		{
			val mfusopB = new StringBuilder()
			mfusopB.append(ControllerConstants::MFUSOP__MINI_JOB)
			mfusopB.append(' ')
			mfusopB.append(miniJobId)
			val mfusop = mfusopB.toString
			val monsterIdToNumPieces = new HashMap<Integer, Integer>()
			monsterIdToNumPieces.put(monsterIdReward, 1)
			val newOrUpdated = MonsterStuffUtils::updateUserMonsters(userUuid,
				monsterIdToNumPieces, mfusop, now)
			val fump = newOrUpdated.get(0)
			resBuilder.fump = fump
		}
		val numDeleted = DeleteUtils::get.deleteMiniJobForUser(userMiniJobId)
		LOG.info('userMiniJob numDeleted=' + numDeleted)
		true
	}

	private def updateUser(User u, int gemsChange, int cashChange, int oilChange)
	{
		val numChange = u.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemsChange)
		if (numChange <= 0)
		{
			LOG.error(
				'unexpected error: problem with updating user gems,' +
					' cash, and oil. gemChange=' + gemsChange + ', cash= ' + cashChange +
					', oil=' + oilChange + ' user=' + u)
			return false;
		}
		true
	}

	private def writeToUserCurrencyHistory(User aUser, long userMiniJobId,
		Map<String, Integer> currencyChange, Timestamp curTime,
		Map<String, Integer> previousCurrency)
	{
		val userUuid = aUser.id
		val reason = ControllerConstants.UCHRFC__SPED_UP_COMPLETE_MINI_JOB
		val detailsSb = new StringBuilder()
		detailsSb.append('userMiniJobId=')
		detailsSb.append(userMiniJobId)
		val currentCurrency = new HashMap<String, Integer>()
		val reasonsForChanges = new HashMap<String, String>()
		val detailsMap = new HashMap<String, String>()
		val gems = MiscMethods::gems
		currentCurrency.put(gems, aUser.gems)
		reasonsForChanges.put(gems, reason)
		detailsMap.put(gems, detailsSb.toString)
		MiscMethods::writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
			previousCurrency, currentCurrency, reasonsForChanges, detailsMap)
	}

	def getMonsterForUserRetrieveUtils()
	{
		monsterForUserRetrieveUtils
	}

	def setMonsterForUserRetrieveUtils(MonsterForUserRetrieveUtils monsterForUserRetrieveUtils)
	{
		this.monsterForUserRetrieveUtils = monsterForUserRetrieveUtils
	}

	def getMiniJobForUserRetrieveUtil()
	{
		miniJobForUserRetrieveUtil
	}

	def setMiniJobForUserRetrieveUtil(MiniJobForUserRetrieveUtil miniJobForUserRetrieveUtil)
	{
		this.miniJobForUserRetrieveUtil = miniJobForUserRetrieveUtil
	}
}
