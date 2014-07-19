package com.lvl6.mobsters.controllers;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto.CombineUserMonsterPiecesStatus;
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.CombineUserMonsterPiecesRequestEvent;
import com.lvl6.mobsters.events.response.CombineUserMonsterPiecesResponseEvent;
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.monster.MonsterService;

@Component
public class CombineUserMonsterPiecesController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(CombineUserMonsterPiecesController.class);

	@Autowired
	protected MonsterService monsterService;

    @Autowired
    protected CreateEventProtoUtil createEventProtoUtil; 

    /*
	 * @Autowired protected EventWriter eventWriter;
	 */

	public CombineUserMonsterPiecesController()
	{}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new CombineUserMonsterPiecesRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_COMBINE_USER_MONSTER_PIECES_EVENT;
	}

	@Override
	protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
	{
		final CombineUserMonsterPiecesRequestProto reqProto =
			((CombineUserMonsterPiecesRequestEvent) event).getCombineUserMonsterPiecesRequestProto();
		final MinimumUserProto senderProto = reqProto.getSender();
		final String userIdString = senderProto.getUserUuid();
		final List<String> userMonsterUuids = reqProto.getUserMonsterUuidsList();
//		userMonsterUuids = new ArrayList<String>(userMonsterUuids);
		final int gemCost = reqProto.getGemCost();
		final Date curDate = new Date();
		
		// prepare to send response back to client
		CombineUserMonsterPiecesResponseProto.Builder responseBuilder =
			CombineUserMonsterPiecesResponseProto.newBuilder();
		responseBuilder.setStatus(CombineUserMonsterPiecesStatus.SUCCESS);
		responseBuilder.setSender(senderProto);
		CombineUserMonsterPiecesResponseEvent resEvent =
			new CombineUserMonsterPiecesResponseEvent(userIdString);
		resEvent.setTag(event.getTag());

		// Check values client sent for syntax errors. Call service only if
		// syntax checks out ok; prepare arguments for service
		if (CollectionUtils.lacksSubstance(userMonsterUuids)) {
			LOG.error("the user didn't send any userMonsters to complete!.");
			responseBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_OTHER);
		}
		
		if (gemCost < 0) {
			LOG.error("gemCost is negative! Can only be positive. gemCost=" + gemCost);
			responseBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_OTHER);
		}
		
		//can only speed up combine one monster at a time
		if (gemCost > 0 && userMonsterUuids.size() > 1) {
			LOG.error("user speeding up combining pieces for multiple monsters can only "
			    + "speed up one monster. gemCost="
			    + gemCost
			    + "\t userMonsterUuids="
			    + userMonsterUuids);
			responseBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_MORE_THAN_ONE_MONSTER_FOR_COMBINE_SPEEDUP);
		}
		
		// call service if syntax is ok
		boolean success = false;
		User u = null;
		if (responseBuilder.getStatus() == CombineUserMonsterPiecesStatus.SUCCESS) {
			try {
				u = monsterService.combineMonsterForUser(userIdString, userMonsterUuids,
					gemCost, curDate);
				
				success = true;
			} catch (Exception e) {
				LOG.error(
					"exception in CombineUserMonsterPiecesController processEvent when calling userService",
					e);
				responseBuilder.setStatus(CombineUserMonsterPiecesStatus.FAIL_OTHER);
			}
		}

		resEvent.setCombineUserMonsterPiecesResponseProto(responseBuilder.build());

		// write to client
		LOG.info("Writing event: "
			+ resEvent);
		eventWriter.writeEvent(resEvent);

		if (success && gemCost > 0) {
			UpdateClientUserResponseEvent resEventUpdate =
            	createEventProtoUtil.createUpdateClientUserResponseEvent(u, null, null, null, null);
            eventWriter.writeEvent(resEventUpdate);
		}
	}

	public MonsterService getMonsterService()
	{
		return monsterService;
	}

	public void setMonsterService( MonsterService monsterService )
	{
		this.monsterService = monsterService;
	}

}
