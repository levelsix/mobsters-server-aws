//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.StructureForUser;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureRequestProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureRequestProto.StructRetrieval;
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto.RetrieveCurrencyFromNormStructureStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.RetrieveCurrencyFromNormStructureRequestEvent;
//import com.lvl6.mobsters.events.response.RetrieveCurrencyFromNormStructureResponseEvent;
//import com.lvl6.mobsters.events.response.UpdateClientUserResponseEvent;
//import com.lvl6.mobsters.info.StructureResourceGenerator;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProtoWithMaxResources;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class RetrieveCurrencyFromNormStructureController extends EventController
//{
//
//	private static Logger LOG =
//	    LoggerFactory.getLogger(RetrieveCurrencyFromNormStructureController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public RetrieveCurrencyFromNormStructureController()
//	{
//		numAllocatedThreads = 14;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new RetrieveCurrencyFromNormStructureRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_RETRIEVE_CURRENCY_FROM_NORM_STRUCTURE_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final RetrieveCurrencyFromNormStructureRequestProto reqProto =
//		    ((RetrieveCurrencyFromNormStructureRequestEvent) event).getRetrieveCurrencyFromNormStructureRequestProto();
//		// LOG.info("reqProto=" + reqProto);
//		// get stuff client sent
//		final MinimumUserProtoWithMaxResources senderResourcesProto = reqProto.getSender();
//		final MinimumUserProto senderProto = senderResourcesProto.getMinUserProto();
//		final String userUuid = senderProto.getUserUuid();
//		final List<StructRetrieval> structRetrievals = reqProto.getStructRetrievalsList();
//		final Timestamp curTime = new Timestamp((new Date()).getTime());
//		final int maxCash = senderResourcesProto.getMaxCash();
//		final int maxOil = senderResourcesProto.getMaxOil();
//
//		final Map<Integer, Timestamp> userStructUuidsToTimesOfRetrieval =
//		    new HashMap<Integer, Timestamp>();
//		final Map<Integer, Integer> userStructUuidsToAmountCollected =
//		    new HashMap<Integer, Integer>();
//		final List<Integer> duplicates = new ArrayList<Integer>();
//		// create map from ids to times and check for duplicates
//		getUuidsAndTimes(structRetrievals, duplicates, userStructUuidsToTimesOfRetrieval,
//		    userStructUuidsToAmountCollected);
//
//		final RetrieveCurrencyFromNormStructureResponseProto.Builder resBuilder =
//		    RetrieveCurrencyFromNormStructureResponseProto.newBuilder();
//		resBuilder.setStatus(RetrieveCurrencyFromNormStructureStatus.FAIL_OTHER);
//		resBuilder.setSender(senderResourcesProto);
//
//		svcTxManager.beginTransaction();
//		try {
//			final User user = RetrieveUtils.userRetrieveUtils()
//			    .getUserById(senderProto.getUserUuid());
//			int previousCash = 0;
//			int previousOil = 0;
//			final List<Integer> userStructUuids =
//			    new ArrayList<Integer>(userStructUuidsToTimesOfRetrieval.keySet());
//
//			final Map<Integer, StructureForUser> userStructUuidsToUserStructs =
//			    getUserStructUuidsToUserStructs(userUuid, userStructUuids);
//			final Map<Integer, StructureResourceGenerator> userStructUuidsToGenerators =
//			    getUserStructUuidsToResourceGenerators(userStructUuidsToUserStructs.values());
//
//			// this will contain the amount user collects
//			final Map<String, Integer> resourcesGained = new HashMap<String, Integer>();
//			// userStructUuidsToTimesOfRetrieval and
//			// userStructUuidsToUserStructs will be
//			// modified to contain only the valid user structs user can retrieve
//			// currency from
//			final boolean legitRetrieval =
//			    checkLegitRetrieval(resBuilder, user, userStructUuids,
//			        userStructUuidsToUserStructs, userStructUuidsToGenerators, duplicates,
//			        userStructUuidsToTimesOfRetrieval, userStructUuidsToAmountCollected,
//			        resourcesGained);
//
//			int cashGain = 0;
//			int oilGain = 0;
//			final Map<String, Integer> currencyChange = new HashMap<String, Integer>();
//			boolean successful = false;
//			if (legitRetrieval) {
//				cashGain = resourcesGained.get(MiscMethods.cash);
//				previousCash = user.getCash();
//				oilGain = resourcesGained.get(MiscMethods.oil);
//				previousOil = user.getOil();
//
//				successful =
//				    writeChangesToDb(user, cashGain, oilGain, userStructUuidsToUserStructs,
//				        userStructUuidsToTimesOfRetrieval, userStructUuidsToAmountCollected,
//				        maxCash, maxOil, currencyChange);
//			}
//			if (successful) {
//				resBuilder.setStatus(RetrieveCurrencyFromNormStructureStatus.SUCCESS);
//			}
//
//			final RetrieveCurrencyFromNormStructureResponseEvent resEvent =
//			    new RetrieveCurrencyFromNormStructureResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setRetrieveCurrencyFromNormStructureResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in RetrieveCurrencyFromNormStructureController.processRequestEvent",
//				    e);
//			}
//
//			if (legitRetrieval) {
//				// null PvpLeagueFromUser means will pull from hazelcast instead
//				final UpdateClientUserResponseEvent resEventUpdate =
//				    MiscMethods.createUpdateClientUserResponseEventAndUpdateLeaderboard(user,
//				        null);
//				resEventUpdate.setTag(event.getTag());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEventUpdate);
//				try {
//					eventWriter.writeEvent(resEventUpdate);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in RetrieveCurrencyFromNormStructureController.processRequestEvent",
//					    e);
//				}
//
//				writeToUserCurrencyHistory(user, previousCash, previousOil, curTime,
//				    userStructUuidsToUserStructs, userStructUuidsToGenerators,
//				    userStructUuidsToTimesOfRetrieval, userStructUuidsToAmountCollected,
//				    currencyChange);
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in RetrieveCurrencyFromNormStructureController processEvent",
//			    e);
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	// separate the duplicate ids from the unique ones
//	private void getUuidsAndTimes( final List<StructRetrieval> srList,
//	    final List<Integer> duplicates,
//	    final Map<Integer, Timestamp> structUuidsToTimesOfRetrieval,
//	    final Map<Integer, Integer> structUuidsToAmountCollected )
//	{
//		if (srList.isEmpty()) {
//			LOG.error("RetrieveCurrencyFromNormStruct request did not send any user struct ids.");
//			return;
//		}
//
//		for (final StructRetrieval sr : srList) {
//			final String key = sr.getUserStructUuid();
//			final Timestamp value = new Timestamp(sr.getTimeOfRetrieval());
//			final int amount = sr.getAmountCollected();
//
//			if (structUuidsToTimesOfRetrieval.containsKey(key)) {
//				duplicates.add(key);
//			} else {
//				structUuidsToTimesOfRetrieval.put(key, value);
//				structUuidsToAmountCollected.put(key, amount);
//			}
//		}
//	}
//
//	// retrieve these user structs from the db and put them in a map
//	private Map<Integer, StructureForUser> getUserStructUuidsToUserStructs(
//	    final String userUuid, final List<Integer> userStructUuids )
//	{
//		final Map<Integer, StructureForUser> returnValue =
//		    new HashMap<Integer, StructureForUser>();
//		if ((null == userStructUuids)
//		    || userStructUuids.isEmpty()) {
//			LOG.error("no user struct ids!");
//			return returnValue;
//		}
//
//		final List<StructureForUser> userStructList = RetrieveUtils.userStructRetrieveUtils()
//		    .getSpecificOrAllUserStructsForUser(userUuid, userStructUuids);
//		for (final StructureForUser us : userStructList) {
//			if (null != us) {
//				returnValue.put(us.getId(), us);
//			} else {
//				LOG.error("could not retrieve one of the user structs. userStructUuids to retrieve="
//				    + userStructUuids
//				    + ". user structs retrieved="
//				    + userStructList
//				    + ". Continuing with processing.");
//			}
//		}
//		return returnValue;
//	}
//
//	// link up a user struct id with the structure object
//	private Map<Integer, StructureResourceGenerator> getUserStructUuidsToResourceGenerators(
//	    final Collection<StructureForUser> userStructs )
//	{
//		final Map<Integer, StructureResourceGenerator> returnValue =
//		    new HashMap<Integer, StructureResourceGenerator>();
//		final Map<Integer, StructureResourceGenerator> structUuidsToStructs =
//		    StructureResourceGeneratorRetrieveUtils.getStructUuidsToResourceGenerators();
//
//		if ((null == userStructs)
//		    || userStructs.isEmpty()) {
//			LOG.error("There are no user structs.");
//		}
//
//		for (final StructureForUser us : userStructs) {
//			final int structId = us.getStructId();
//			final int userStructId = us.getId();
//
//			final StructureResourceGenerator s = structUuidsToStructs.get(structId);
//			if (null != s) {
//				returnValue.put(userStructId, s);
//			} else {
//				LOG.error("structure with id "
//				    + structId
//				    + " does not exist, therefore UserStruct is invalid:"
//				    + us);
//			}
//		}
//
//		return returnValue;
//	}
//
//	private boolean checkLegitRetrieval( final Builder resBuilder, final User user,
//	    final List<Integer> userStructUuids,
//	    final Map<Integer, StructureForUser> userStructUuidsToUserStructs,
//	    final Map<Integer, StructureResourceGenerator> userStructUuidsToGenerators,
//	    final List<Integer> duplicates,
//	    final Map<Integer, Timestamp> userStructUuidsToTimesOfRetrieval,
//	    final Map<Integer, Integer> userStructUuidsToAmountCollected,
//	    final Map<String, Integer> resourcesGained )
//	{
//
//		final String userUuid = user.getId();
//
//		if ((user == null)
//		    || userStructUuids.isEmpty()
//		    || userStructUuidsToUserStructs.isEmpty()
//		    || userStructUuidsToGenerators.isEmpty()
//		    || userStructUuidsToTimesOfRetrieval.isEmpty()) {
//			LOG.error("user is null, or no struct ids, user structs, structures, or retrieval times . user="
//			    + user
//			    + "\t userStructUuids="
//			    + userStructUuids
//			    + "\t structUuidsToUserStructs="
//			    + userStructUuidsToUserStructs
//			    + "\t userStructUuidsToGenerators="
//			    + userStructUuidsToGenerators
//			    + "\t userStructUuidsToRetrievalTimes="
//			    + userStructUuidsToTimesOfRetrieval);
//			return false;
//		}
//		if (!duplicates.isEmpty()) {
//			resBuilder.setStatus(RetrieveCurrencyFromNormStructureStatus.FAIL_OTHER);
//			LOG.warn("duplicate struct ids in request. ids="
//			    + duplicates);
//		}
//
//		// go through the userStructUuids the user sent, checking which structs
//		// can be
//		// retrieved
//		int cash = 0;
//		int oil = 0;
//		for (final Integer id : userStructUuids) {
//			final StructureForUser userStruct = userStructUuidsToUserStructs.get(id);
//			final StructureResourceGenerator struct = userStructUuidsToGenerators.get(id);
//
//			if ((null == userStruct)
//			    || (userUuid != userStruct.getUserUuid())
//			    || !userStruct.isComplete()) {
//				resBuilder.setStatus(RetrieveCurrencyFromNormStructureStatus.FAIL_OTHER);
//				LOG.error("(will continue processing) struct owner is not user, or struct"
//				    + " is not complete yet. userStruct="
//				    + userStruct);
//				// remove invalid user structure
//				userStructUuidsToUserStructs.remove(id);
//				userStructUuidsToTimesOfRetrieval.remove(id);
//				userStructUuidsToAmountCollected.remove(id);
//				continue;
//			}
//
//			final String type = struct.getResourceTypeGenerated();
//			final ResourceType rt = ResourceType.valueOf(type);
//			if (ResourceType.CASH.equals(rt)) {
//				cash += userStructUuidsToAmountCollected.get(id);
//			} else if (ResourceType.OIL.equals(rt)) {
//				oil += userStructUuidsToAmountCollected.get(id);
//			} else {
//				LOG.error("(will continue processing) unknown resource type: "
//				    + rt);
//				// remove invalid user structure
//				userStructUuidsToUserStructs.remove(id);
//				userStructUuidsToTimesOfRetrieval.remove(id);
//				userStructUuidsToAmountCollected.remove(id);
//			}
//		}
//		// return to the caller the amount of money the user can collect
//		resourcesGained.put(MiscMethods.cash, cash);
//		resourcesGained.put(MiscMethods.oil, oil);
//
//		return true;
//	}
//
//	private boolean writeChangesToDb( final User user, int cashGain, int oilGain,
//	    final Map<Integer, StructureForUser> userStructUuidsToUserStructs,
//	    final Map<Integer, Timestamp> userStructUuidsToTimesOfRetrieval,
//	    final Map<Integer, Integer> userStructUuidsToAmountCollected, final int maxCash,
//	    final int maxOil, final Map<String, Integer> currencyChange )
//	{
//		// capping how much the user can gain of a certain resource
//		final int curCash = Math.min(user.getCash(), maxCash); // in case user's
//															   // cash
//		// is more than maxCash
//		final int maxCashUserCanGain = maxCash
//		    - curCash; // this is the max cash the user can gain
//		cashGain = Math.min(maxCashUserCanGain, cashGain);
//
//		final int curOil = Math.min(user.getOil(), maxOil); // in case user's
//															// oil is
//		// more than maxOil
//		final int maxOilUserCanGain = maxOil
//		    - curOil;
//		oilGain = Math.min(maxOilUserCanGain, oilGain);
//
//		if (!user.updateRelativeCoinsOilRetrievedFromStructs(cashGain, oilGain)) {
//			LOG.error("problem with updating user stats after retrieving "
//			    + cashGain
//			    + " cash"
//			    + "\t"
//			    + oilGain
//			    + " oil.");
//			return false;
//
//		} else {
//			if (0 != oilGain) {
//				currencyChange.put(MiscMethods.oil, oilGain);
//			}
//			if (0 != cashGain) {
//				currencyChange.put(MiscMethods.cash, cashGain);
//			}
//		}
//
//		if (!UpdateUtils.get()
//		    .updateUserStructsLastretrieved(userStructUuidsToTimesOfRetrieval,
//		        userStructUuidsToUserStructs)) {
//			LOG.error("problem with updating user structs last retrieved for userStructUuids "
//			    + userStructUuidsToTimesOfRetrieval);
//			return false;
//		}
//		return true;
//	}
//
//	public void writeToUserCurrencyHistory( final User aUser, final int previousCash,
//	    final int previousOil, final Timestamp curTime,
//	    final Map<Integer, StructureForUser> userStructUuidsToUserStructs,
//	    final Map<Integer, StructureResourceGenerator> userStructUuidsToGenerators,
//	    final Map<Integer, Timestamp> userStructUuidsToTimesOfRetrieval,
//	    final Map<Integer, Integer> userStructUuidsToAmountCollected,
//	    final Map<String, Integer> currencyChange )
//	{
//
//		final String userUuid = aUser.getId();
//		final Map<String, Integer> previousCurrencies = new HashMap<String, Integer>();
//		final Map<String, Integer> currentCurrencies = new HashMap<String, Integer>();
//		final Map<String, String> reasonsForChanges = new HashMap<String, String>();
//		final Map<String, String> details = new HashMap<String, String>();
//		final String cash = MiscMethods.cash;
//		final String oil = MiscMethods.oil;
//		final String reasonForChange =
//		    ControllerConstants.UCHRFC__RETRIEVE_CURRENCY_FROM_NORM_STRUCT;
//		final StringBuilder cashDetailSb = new StringBuilder();
//		cashDetailSb.append("(userStructId,time,amount)=");
//		final StringBuilder oilDetailSb = new StringBuilder();
//		oilDetailSb.append("(userStructId,time,amount)=");
//
//		// being descriptive, separating cash stuff from oil stuff
//		for (final Integer id : userStructUuidsToAmountCollected.keySet()) {
//			final StructureResourceGenerator struct = userStructUuidsToGenerators.get(id);
//			final Timestamp t = userStructUuidsToTimesOfRetrieval.get(id);
//			final int amount = userStructUuidsToAmountCollected.get(id);
//
//			final String type = struct.getResourceTypeGenerated();
//			final ResourceType rt = ResourceType.valueOf(type);
//			if (ResourceType.CASH.equals(rt)) {
//				cashDetailSb.append("(");
//				cashDetailSb.append(id);
//				cashDetailSb.append(",");
//				cashDetailSb.append(t);
//				cashDetailSb.append(",");
//				cashDetailSb.append(amount);
//				cashDetailSb.append(")");
//
//			} else if (ResourceType.OIL.equals(rt)) {
//				oilDetailSb.append("(");
//				oilDetailSb.append(id);
//				oilDetailSb.append(",");
//				oilDetailSb.append(t);
//				oilDetailSb.append(",");
//				oilDetailSb.append(amount);
//				oilDetailSb.append(")");
//			}
//		}
//
//		previousCurrencies.put(cash, previousCash);
//		previousCurrencies.put(oil, previousOil);
//		currentCurrencies.put(cash, aUser.getCash());
//		currentCurrencies.put(oil, aUser.getOil());
//		reasonsForChanges.put(cash, reasonForChange);
//		reasonsForChanges.put(oil, reasonForChange);
//		details.put(cash, cashDetailSb.toString());
//		details.put(oil, oilDetailSb.toString());
//
//		MiscMethods.writeToUserCurrencyOneUser(userUuid, curTime, currencyChange,
//		    previousCurrencies, currentCurrencies, reasonsForChanges, details);
//	}
//
//}
