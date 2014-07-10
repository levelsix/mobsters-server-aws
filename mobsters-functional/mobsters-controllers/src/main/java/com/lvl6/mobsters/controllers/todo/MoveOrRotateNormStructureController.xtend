package com.lvl6.mobsters.controllers.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.CoordinatePair;
import com.lvl6.mobsters.dynamo.StructureForUser;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureRequestProto.MoveOrRotateNormStructType;
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureResponseProto.MoveOrRotateNormStructureStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.MoveOrRotateNormStructureRequestEvent;
import com.lvl6.mobsters.events.response.MoveOrRotateNormStructureResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventStructureProto.StructOrientation;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class MoveOrRotateNormStructureController extends EventController
{

	private static Logger LOG =
	    LoggerFactory.getLogger(MoveOrRotateNormStructureController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public MoveOrRotateNormStructureController()
	{
		numAllocatedThreads = 3;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new MoveOrRotateNormStructureRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_MOVE_OR_ROTATE_NORM_STRUCTURE_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final MoveOrRotateNormStructureRequestProto reqProto =
		    ((MoveOrRotateNormStructureRequestEvent) event).getMoveOrRotateNormStructureRequestProto();

		// get stuff client sent
		final MinimumUserProto senderProto = reqProto.getSender();
		final int userStructId = reqProto.getUserStructUuid();
		final MoveOrRotateNormStructType type = reqProto.getType();

		CoordinatePair newCoords = null;
		final StructOrientation orientation = null;
		if (type == MoveOrRotateNormStructType.MOVE) {
			newCoords = new CoordinatePair(reqProto.getCurStructCoordinates()
			    .getX(), reqProto.getCurStructCoordinates()
			    .getY());
		}

		final MoveOrRotateNormStructureResponseProto.Builder resBuilder =
		    MoveOrRotateNormStructureResponseProto.newBuilder();
		resBuilder.setSender(senderProto);

		// only locking so you cant moveOrRotate it hella times
		svcTxManager.beginTransaction();
		try {
			boolean legit = true;
			resBuilder.setStatus(MoveOrRotateNormStructureStatus.SUCCESS);

			final StructureForUser userStruct = RetrieveUtils.userStructRetrieveUtils()
			    .getSpecificUserStruct(userStructId);
			if (userStruct == null) {
				legit = false;
				resBuilder.setStatus(MoveOrRotateNormStructureStatus.SUCCESS);
			}

			if ((type == MoveOrRotateNormStructType.MOVE)
			    && (newCoords == null)) {
				legit = false;
				resBuilder.setStatus(MoveOrRotateNormStructureStatus.OTHER_FAIL);
				LOG.error("asked to move, but the coordinates supplied in are null. reqProto's newStructCoordinates="
				    + reqProto.getCurStructCoordinates());
			}

			if (legit) {
				if (type == MoveOrRotateNormStructType.MOVE) {
					if (!UpdateUtils.get()
					    .updateUserStructCoord(userStructId, newCoords)) {
						resBuilder.setStatus(MoveOrRotateNormStructureStatus.OTHER_FAIL);
						LOG.error("problem with updating coordinates to "
						    + newCoords
						    + " for user struct "
						    + userStructId);
					} else {
						resBuilder.setStatus(MoveOrRotateNormStructureStatus.SUCCESS);
					}
				} else {
					if (!UpdateUtils.get()
					    .updateUserStructOrientation(userStructId, orientation)) {
						resBuilder.setStatus(MoveOrRotateNormStructureStatus.OTHER_FAIL);
						LOG.error("problem with updating orientation to "
						    + orientation
						    + " for user struct "
						    + userStructId);
					} else {
						resBuilder.setStatus(MoveOrRotateNormStructureStatus.SUCCESS);
					}
				}
			}
			final MoveOrRotateNormStructureResponseEvent resEvent =
			    new MoveOrRotateNormStructureResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setMoveOrRotateNormStructureResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in MoveOrRotateNormStructureController.processRequestEvent",
				    e);
			}

		} catch (final Exception e) {
			LOG.error("exception in MoveOrRotateNormStructure processEvent", e);
		} finally {
			svcTxManager.commit();
		}
	}

}
