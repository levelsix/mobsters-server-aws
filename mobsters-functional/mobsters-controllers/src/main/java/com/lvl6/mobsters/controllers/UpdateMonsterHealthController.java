package com.lvl6.mobsters.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.lvl6.mobsters.common.utils.CollectionUtils;
import com.lvl6.mobsters.common.utils.StringUtils;
import com.lvl6.mobsters.eventproto.EventMonsterProto.UpdateMonsterHealthRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.UpdateMonsterHealthResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.UpdateMonsterHealthResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventMonsterProto.UpdateMonsterHealthResponseProto.UpdateMonsterHealthStatus;
import com.lvl6.mobsters.events.ControllerResponseEvents;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.UpdateMonsterHealthRequestEvent;
import com.lvl6.mobsters.events.response.UpdateMonsterHealthResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserMonsterCurrentHealthProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.monster.MonsterService;


@Component
//@Lvl6Controller(reqProto=EventProtocolRequest.C_UPDATE_MONSTER_HEALTH_EVENT, respProto=EventProtocolResponse.S_UPDATE_MONSTER_HEALTH_EVENT)
public class UpdateMonsterHealthController extends EventController {
	private static final Logger LOG = LoggerFactory.getLogger(UpdateMonsterHealthController.class);
	
    @Autowired
    protected MonsterService monsterService;

	public UpdateMonsterHealthController() {
		numAllocatedThreads = 4;
	}
	
	@Override
	public RequestEvent createRequestEvent() {
		return new UpdateMonsterHealthRequestEvent();
	}


	@Override
	public EventProtocolRequest getEventType() {
		return EventProtocolRequest.C_UPDATE_MONSTER_HEALTH_EVENT;
	}

	@Override
	protected void processRequestEvent(RequestEvent event, ControllerResponseEvents eventWriter) {
		// identify client request.
		final UpdateMonsterHealthRequestProto reqProto = 
			((UpdateMonsterHealthRequestEvent) event).getUpdateMonsterHealthRequestProto();
		final MinimumUserProto sender = reqProto.getSender();
		final String userIdString = sender.getUserUuid();

		// prepare to send response back to client
		Builder responseBuilder = UpdateMonsterHealthResponseProto.newBuilder();
		UpdateMonsterHealthResponseEvent resEvent =
			new UpdateMonsterHealthResponseEvent(userIdString, event.getTag());

		// Check values client sent for syntax errors.  Call service only if syntax checks out ok
		final List<UserMonsterCurrentHealthProto> umchpList = reqProto.getUmchpList();
        if (StringUtils.hasContent(userIdString) || CollectionUtils.lacksSubstance(umchpList)) {
			responseBuilder.setStatus(UpdateMonsterHealthStatus.FAIL_OTHER);
		} else {
			//extract the ids so it's easier to get userMonsters from db
			com.google.common.collect.ImmutableMap.Builder<String, Integer> mapBuilder = ImmutableMap.builder();
			for (final UserMonsterCurrentHealthProto nextMonsterUnit : umchpList) {
				mapBuilder.put(
					nextMonsterUnit.getUserMonsterUuid(),
					Integer.valueOf(
						nextMonsterUnit.getCurrentHealth()
					)
				);
			}
			ImmutableMap<String,Integer> idToHealthMap = mapBuilder.build();
			
			if (CollectionUtils.lacksSubstance(idToHealthMap)) {
				responseBuilder.setStatus(UpdateMonsterHealthStatus.FAIL_OTHER);
			} else {
				try {
					monsterService.updateUserMonsterHealth(userIdString, idToHealthMap);
					resEvent.setUpdateMonsterHealthResponseProto(responseBuilder.build());
					responseBuilder.setStatus(UpdateMonsterHealthStatus.SUCCESS);

					//write to client
					LOG.info("Writing event: " + resEvent);
					eventWriter.writeEvent(resEvent);
				} catch (Exception e) {
					LOG.error("exception in UpdateMonsterHealthController processRequestEvent when calling MonsterService", e);
					try {
						//try to tell client that something failed
						responseBuilder.setStatus(UpdateMonsterHealthStatus.FAIL_OTHER);
						resEvent.setUpdateMonsterHealthResponseProto(responseBuilder.build());
						eventWriter.writeEvent(resEvent);
					} catch (Exception e2) {
						LOG.error("fatal exception in UpdateMonsterHealthController processRequestEvent", e2);
					}
				}
			}
		}
	}
	
	/*
	private boolean isValidRequest(Builder resBuilder, Map<UUID, MonsterForUser> mfuList,
			List<UUID> userMonsterIds, List<UserMonsterCurrentHealthProto> umchpList) {
		if (null == umchpList || umchpList.isEmpty()) {
	  		LOG.error("client error: no user monsters sent.");
	  		return false;
	  	}

		if (null == mfuList || mfuList.isEmpty()) {
			LOG.error("unexpected error: userMonsterIds don't exist. ids=" + userMonsterIds);
			return false;
		}

		//see if the user has the equips
	  	if (mfuList.size() != umchpList.size()) {
	  		LOG.error("unexpected error: mismatch between user equips client sent and " +
	  				"what is in the db. clientUserMonsterIds=" + userMonsterIds + "\t inDb=" +
	  				mfuList + "\t continuing the processing");
	  	}
	  	
		return true;
	}
	*/

	public MonsterService getMonsterService() {
		return monsterService;
	}

	public void setMonsterService(final MonsterService monsterService) {
		this.monsterService = monsterService;
	}
}
