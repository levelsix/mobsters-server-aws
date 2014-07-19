package com.lvl6.mobsters.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptRequestProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptResponseProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptResponseProto.QuestAcceptStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.QuestAcceptRequestEvent;
import com.lvl6.mobsters.events.response.QuestAcceptResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;
import com.lvl6.mobsters.services.quest.QuestService;

@Component
public class QuestAcceptController extends EventController {

    private static Logger LOG = LoggerFactory.getLogger(QuestAcceptController.class);

    @Autowired
    protected QuestService questService;

    /*
     * @Autowired protected EventWriter eventWriter;
     */

    public QuestAcceptController() {}

    @Override
    public RequestEvent createRequestEvent() {
        return new QuestAcceptRequestEvent();
    }

    @Override
    public EventProtocolRequest getEventType() {
        return EventProtocolRequest.C_QUEST_ACCEPT_EVENT;
    }

    @Override
    protected void processRequestEvent( RequestEvent event, EventsToDispatch eventWriter ) throws Exception
    {
        final QuestAcceptRequestProto reqProto =
            ((QuestAcceptRequestEvent) event).getQuestAcceptRequestProto();
        final MinimumUserProto senderProto = reqProto.getSender();
        final String userIdString = senderProto.getUserUuid();
        final int questId = reqProto.getQuestId();

        // prepare to send response back to client
        QuestAcceptResponseProto.Builder responseBuilder =
            QuestAcceptResponseProto.newBuilder();
        responseBuilder.setStatus(QuestAcceptStatus.FAIL_OTHER);
        responseBuilder.setSender(senderProto);
        
        QuestAcceptResponseEvent resEvent = new QuestAcceptResponseEvent(userIdString);
        resEvent.setTag(event.getTag());

        // Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service
        if (!reqProto.hasQuestId() || questId < 0) {
            responseBuilder.setStatus(QuestAcceptStatus.SUCCESS);
        }

        // call service if syntax is ok
        if (QuestAcceptStatus.SUCCESS.equals(responseBuilder.getStatus())) {
            try {
                questService.createQuestForUser(userIdString, questId);
            } catch (Exception e) {
                LOG.error(
                    "exception in QuestAcceptController processEvent when calling questService",
                    e);
                responseBuilder.setStatus(QuestAcceptStatus.FAIL_OTHER);
            }
        }

        resEvent.setQuestAcceptResponseProto(responseBuilder.build());

        // write to client
        LOG.info("Writing event: " + resEvent);
        eventWriter.writeEvent(resEvent);
        
    }

	public QuestService getQuestService()
	{
		return questService;
	}

	public void setQuestService( QuestService questService )
	{
		this.questService = questService;
	}

}
