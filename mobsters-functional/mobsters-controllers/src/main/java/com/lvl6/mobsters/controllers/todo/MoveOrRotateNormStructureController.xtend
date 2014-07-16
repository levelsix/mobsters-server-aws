package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.CoordinatePair
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureRequestProto.MoveOrRotateNormStructType
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureResponseProto.MoveOrRotateNormStructureStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.MoveOrRotateNormStructureRequestEvent
import com.lvl6.mobsters.events.response.MoveOrRotateNormStructureResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructOrientation
import com.lvl6.mobsters.server.EventController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class MoveOrRotateNormStructureController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(MoveOrRotateNormStructureController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 3
	}

	override createRequestEvent()
	{
		new MoveOrRotateNormStructureRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_MOVE_OR_ROTATE_NORM_STRUCTURE_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as MoveOrRotateNormStructureRequestEvent)).
			moveOrRotateNormStructureRequestProto
		val senderProto = reqProto.sender
		val userStructId = reqProto.userStructUuid
		val type = reqProto.type
		var CoordinatePair newCoords = null
		val StructOrientation orientation = null
		if (type === MoveOrRotateNormStructType::MOVE)
		{
			newCoords = new CoordinatePair(reqProto.curStructCoordinates.x,
				reqProto.curStructCoordinates.y)
		}
		val resBuilder = MoveOrRotateNormStructureResponseProto::newBuilder
		resBuilder.sender = senderProto
		svcTxManager.beginTransaction
		try
		{
			var legit = true
			resBuilder.status = MoveOrRotateNormStructureStatus.SUCCESS
			val userStruct = RetrieveUtils::userStructRetrieveUtils.
				getSpecificUserStruct(userStructId)
			if (userStruct === null)
			{
				legit = false
				resBuilder.status = MoveOrRotateNormStructureStatus.SUCCESS
			}
			if ((type === MoveOrRotateNormStructType::MOVE) && (newCoords === null))
			{
				legit = false
				resBuilder.status = MoveOrRotateNormStructureStatus.OTHER_FAIL
				LOG.error(
					"asked to move, but the coordinates supplied in are null. reqProto's newStructCoordinates=" +
						reqProto.curStructCoordinates)
			}
			if (legit)
			{
				if (type === MoveOrRotateNormStructType::MOVE)
				{
					if (!UpdateUtils::get.updateUserStructCoord(userStructId, newCoords))
					{
						resBuilder.status = MoveOrRotateNormStructureStatus.OTHER_FAIL
						LOG.error(
							'problem with updating coordinates to ' + newCoords +
								' for user struct ' + userStructId)
					}
					else
					{
						resBuilder.status = MoveOrRotateNormStructureStatus.SUCCESS
					}
				}
				else
				{
					if (!UpdateUtils::get.updateUserStructOrientation(userStructId, orientation))
					{
						resBuilder.status = MoveOrRotateNormStructureStatus.OTHER_FAIL
						LOG.error(
							'problem with updating orientation to ' + orientation +
								' for user struct ' + userStructId)
					}
					else
					{
						resBuilder.status = MoveOrRotateNormStructureStatus.SUCCESS
					}
				}
			}
			val resEvent = new MoveOrRotateNormStructureResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.moveOrRotateNormStructureResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in MoveOrRotateNormStructureController.processRequestEvent',
					e)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in MoveOrRotateNormStructure processEvent', e)
		}
		finally
		{
			svcTxManager.commit
		}
	}
}
