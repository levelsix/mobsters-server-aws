package com.lvl6.mobsters.controllers;

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto
import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto.UpgradeNormStructureStatus
import com.lvl6.mobsters.eventproto.utils.CreateEventProtoUtil
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.UpgradeNormStructureRequestEvent
import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent
import com.lvl6.mobsters.events.response.UpgradeNormStructureResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.noneventproto.ConfigNoneventSharedEnumProto.ResourceType
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto
import com.lvl6.mobsters.server.EventController
import com.lvl6.mobsters.services.structure.upgradenormstructure.UpgradeNormStructureService
import java.util.Date
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
public class UpgradeNormStructureController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(typeof(UpgradeNormStructureController));
	
	@Autowired
	@Property package UpgradeNormStructureService upgradeNormStructureService;

	@Autowired
	@Property package CreateEventProtoUtil createEventProtoUtil;

	new () {
		super();
	}

	override public RequestEvent createRequestEvent()
	{
		return new UpgradeNormStructureRequestEvent();
	}

	override public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_UPGRADE_NORM_STRUCTURE_EVENT;
	}

	override protected void processRequestEvent( RequestEvent event,
	    EventsToDispatch eventWriter ) throws Exception
	{
		val reqProto = (event as UpgradeNormStructureRequestEvent)
		.upgradeNormStructureRequestProto;

		val MinimumUserProto senderProto = reqProto.sender;
		val String userUuid = senderProto.userUuid;
		val String userStructId = reqProto.userStructUuid;
		val Date timeOfUpgrade = new Date(reqProto.timeOfUpgrade);
		val int gemsSpent = reqProto.gemsSpent;
		// positive means refund, negative means charge user
		val int resourceChange = reqProto.resourceChange;
		val ResourceType rt = reqProto.resourceType;
		val String resourceType = rt.name;

		val UpgradeNormStructureResponseProto.Builder resBuilder =
		    UpgradeNormStructureResponseProto.newBuilder();
		resBuilder.sender = senderProto;
		resBuilder.status = UpgradeNormStructureStatus.SUCCESS;
		
		val UpgradeNormStructureResponseEvent resEvent =
			new UpgradeNormStructureResponseEvent(userUuid);
		resEvent.tag = event.tag;
		
		// Check values client sent for syntax errors. Call service only if
        // syntax checks out ok; prepare arguments for service

        // call service if syntax is ok
        var boolean successful = false;
        var User u = null;

		try {
			// TODO: Keep track of the user currency history somehow
			u = upgradeNormStructureService.upgradeNormStructure(
				userUuid, userStructId, gemsSpent, resourceType, resourceChange, timeOfUpgrade
			);
			
			successful = true;
		} catch (Exception e) {
			LOG.error(
        		"exception in UpgradeNormStructureController processEvent when calling userService",
        		e);
        	resBuilder.setStatus(UpgradeNormStructureStatus.FAIL_OTHER);
		}
		
        // write to client
        LOG.info("Writing event: " + resEvent);
        eventWriter.writeEvent(resEvent);
        
        if (successful) {
        	val UpdateClientUserResponseEvent resEventUpdate =
            	createEventProtoUtil.createUpdateClientUserResponseEvent(u, null, null, null, null);
            eventWriter.writeEvent(resEventUpdate);
        }
		
	}
	
//	private void writeChangesToDB( val User user, val StructureForUser userStruct,
//	    val Structure upgradedStruct, val int gemsSpent, val int resourceChange,
//	    val ResourceType rt, val Timestamp timeOfUpgrade, val Map<String, Integer> money )
//	{
//
//		val int newStructId = upgradedStruct.getId();
//		// upgrade the user's struct
//		if (!UpdateUtils.get()
//		    .updateBeginUpgradingUserStruct(userStruct.getId(), newStructId, timeOfUpgrade)) {
//			LOG.error("problem with changing time of upgrade to "
//			    + timeOfUpgrade
//			    + " and marking as incomplete, the user struct "
//			    + userStruct);
//		}
//
//		// charge the user
//		int cashChange = 0;
//		int oilChange = 0;
//		val int gemChange = -1
//		    * gemsSpent;
//
//		if (ResourceType.CASH.equals(rt)) {
//			cashChange = resourceChange;
//		} else if (ResourceType.OIL.equals(rt)) {
//			oilChange = resourceChange;
//		}
//
//		val int num = user.updateRelativeCashAndOilAndGems(cashChange, oilChange, gemChange);
//		if (1 != num) {
//			LOG.error("problem with updating user currency. gemChange="
//			    + gemChange
//			    + " cashChange="
//			    + cashChange
//			    + "\t oilChange="
//			    + oilChange
//			    + "\t numRowsUpdated="
//			    + num);
//		} else {// things went ok
//			if (0 != gemChange) {
//				money.put(MiscMethods.gems, gemChange);
//			}
//			if (0 != cashChange) {
//				money.put(MiscMethods.cash, cashChange);
//			}
//			if (0 != oilChange) {
//				money.put(MiscMethods.oil, oilChange);
//			}
//		}
//
//	}
//
//	private void writeToUserCurrencyHistory( val User aUser,
//	    val StructureForUser userStruct, val Structure curStruct,
//	    val Structure upgradedStruct, val Timestamp timeOfUpgrade,
//	    val Map<String, Integer> money, val int previousCash, val int previousOil,
//	    val int previousGems )
//	{
//
//		val String userUuid = aUser.getId();
//		val String userStructUuid = userStruct.getId();
//		val int prevStructId = curStruct.getId();
//		val int prevLevel = curStruct.getLevel();
//		val StringBuilder structDetailsSb = new StringBuilder();
//		structDetailsSb.append("uStructId:");
//		structDetailsSb.append(userStructUuid);
//		structDetailsSb.append(" preStructId:");
//		structDetailsSb.append(prevStructId);
//		structDetailsSb.append(" prevLevel:");
//		structDetailsSb.append(prevLevel);
//		val String structDetails = structDetailsSb.toString();
//
//		val Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
//		val Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
//		val String reasonForChange = ControllerConstants.UCHRFC__UPGRADE_NORM_STRUCT;
//		val Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		val Map<String, String> details = new HashMap<String, String>();
//		val String gems = MiscMethods.gems;
//		val String oil = MiscMethods.oil;
//		val String cash = MiscMethods.cash;
//
//		previousCurrencies.put(cash, previousCash);
//		previousCurrencies.put(oil, previousOil);
//		previousCurrencies.put(gems, previousGems);
//
//		currentCurrencies.put(cash, aUser.getCash());
//		currentCurrencies.put(oil, aUser.getOil());
//		currentCurrencies.put(gems, aUser.getGems());
//
//		reasonsForChanges.put(cash, reasonForChange);
//		reasonsForChanges.put(oil, reasonForChange);
//		reasonsForChanges.put(gems, reasonForChange);
//
//		details.put(cash, structDetails);
//		details.put(oil, structDetails);
//		details.put(gems, structDetails);
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, timeOfUpgrade, money,
//		    previousCurrencies, currentCurrencies, reasonsForChanges, details);
//	}


}
