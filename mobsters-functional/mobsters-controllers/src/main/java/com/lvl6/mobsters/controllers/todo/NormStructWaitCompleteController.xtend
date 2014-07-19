package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.StructureForUser
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto.NormStructWaitCompleteStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.NormStructWaitCompleteRequestEvent
import com.lvl6.mobsters.events.response.NormStructWaitCompleteResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.EventController
import java.sql.Timestamp
import java.util.ArrayList
import java.util.List
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class NormStructWaitCompleteController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(NormStructWaitCompleteController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 5
	}

	override createRequestEvent()
	{
		new NormStructWaitCompleteRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_NORM_STRUCT_WAIT_COMPLETE_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as NormStructWaitCompleteRequestEvent)).
			normStructWaitCompleteRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		var userStructUuids = reqProto.userStructUuidList
		userStructUuids = new ArrayList<Integer>(userStructUuids)
		val clientTime = new Timestamp(reqProto.curTime)
		val resBuilder = NormStructWaitCompleteResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = NormStructWaitCompleteStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			val userStructs = RetrieveUtils::userStructRetrieveUtils.
				getSpecificOrAllUserStructsForUser(userUuid, userStructUuids)
			val newRetrievedTimes = new ArrayList<Timestamp>()
			val legitWaitComplete = checkLegitWaitComplete(resBuilder, userStructs,
				userStructUuids, senderProto.userUuid, clientTime, newRetrievedTimes)
			var success = false
			if (legitWaitComplete)
			{
				success = writeChangesToDB(userUuid, userStructs, newRetrievedTimes)
			}
			if (success)
			{
				resBuilder.status = NormStructWaitCompleteStatus.SUCCESS
				val newUserStructs = RetrieveUtils::userStructRetrieveUtils.
					getSpecificOrAllUserStructsForUser(userUuid, userStructUuids)
				for (userStruct : newUserStructs)
				{
					resBuilder.addUserStruct(
						CreateInfoProtoUtils::
							createFullUserStructureProtoFromUserstruct(userStruct))
				}
			}
			val resEvent = new NormStructWaitCompleteResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.normStructWaitCompleteResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in NormStructWaitCompleteController.processRequestEvent', e)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in NormStructWaitCompleteController processEvent', e)
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegitWaitComplete(Builder resBuilder, List<StructureForUser> userStructs,
		List<Integer> userStructUuids, String userUuid, Timestamp clientTime,
		List<Timestamp> newRetrievedTimes)
	{
		if ((userStructs === null) || (userStructUuids === null) || (clientTime === null) ||
			(userStructUuids.size !== userStructs.size))
		{
			resBuilder.status = NormStructWaitCompleteStatus.FAIL_OTHER
			LOG.error(
				'userStructs is null, userStructUuids is null, clientTime is null, or array lengths different. userStructs=' +
					userStructs + ', userStructUuids=' + userStructUuids + ', clientTime=' +
					clientTime)
			return false;
		}
		val validUserStructs = new ArrayList<StructureForUser>()
		val validUserStructUuids = new ArrayList<Integer>()
		val timesBuildsFinished = calculateValidUserStructs(userUuid, clientTime, userStructs,
			validUserStructUuids, validUserStructs)
		if (userStructs.size !== validUserStructs.size)
		{
			LOG.warn(
				'some of what the client sent is invalid. idsClientSent=' + userStructUuids +
					'	 validUuids=' + validUserStructUuids)
			userStructs.clear
			userStructs.addAll(validUserStructs)
			userStructUuids.clear
			userStructUuids.addAll(validUserStructUuids)
		}
		newRetrievedTimes.addAll(timesBuildsFinished)
		true
	}

	private def calculateValidUserStructs(String userUuid, Timestamp clientTime,
		List<StructureForUser> userStructs, List<Integer> validUserStructUuids,
		List<StructureForUser> validUserStructs)
	{
		val timesBuildsFinished = new ArrayList<Timestamp>()
		val structures = StructureRetrieveUtils::structUuidsToStructs
		for (us : userStructs)
		{
			if (us.userUuid !== userUuid)
			{
				LOG.warn(
					"user struct's owner's id is " + us.userUuid + ', and user id is ' +
						userUuid)
        continue;
			}
			val struct = structures.get(us.structId)
			if (struct === null)
			{
				LOG.warn('no struct in db exists with id ' + us.structId)
        continue;
			}
			val purchaseDate = us.purchaseTime
			val buildTimeMillis = 60000 * struct.minutesToBuild
			if (null !== purchaseDate)
			{
				val timeBuildFinished = purchaseDate.time + buildTimeMillis
				if (timeBuildFinished > clientTime.time)
				{
					LOG.warn(
						'the building is not done yet. userstruct=' + ', client time is ' +
							clientTime + ', purchase time was ' + purchaseDate)
          continue;
				}
				validUserStructUuids.add(us.id)
				validUserStructs.add(us)
				timesBuildsFinished.add(new Timestamp(timeBuildFinished))
			}
			else
			{
				LOG.warn('user struct has never been bought or purchased according to db. ' + us)
			}
		}
		timesBuildsFinished
	}

	private def writeChangesToDB(String userUuid, List<StructureForUser> buildsDone,
		List<Timestamp> newRetrievedTimes)
	{
		if (!UpdateUtils::get.
			updateUserStructsBuildingIscomplete(userUuid, buildsDone, newRetrievedTimes))
		{
			LOG.error(
				'problem with marking norm struct builds as complete for one of these structs: ' +
					buildsDone)
			return false;
		}
		true
	}
}
