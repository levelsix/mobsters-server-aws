//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
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
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteRequestProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto;
//import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto.NormStructWaitCompleteStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.NormStructWaitCompleteRequestEvent;
//import com.lvl6.mobsters.events.response.NormStructWaitCompleteResponseEvent;
//import com.lvl6.mobsters.info.Structure;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class NormStructWaitCompleteController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(NormStructWaitCompleteController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public NormStructWaitCompleteController()
//	{
//		numAllocatedThreads = 5;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new NormStructWaitCompleteRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_NORM_STRUCT_WAIT_COMPLETE_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final NormStructWaitCompleteRequestProto reqProto =
//		    ((NormStructWaitCompleteRequestEvent) event).getNormStructWaitCompleteRequestProto();
//
//		// stuff client sent
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		List<Integer> userStructUuids = reqProto.getUserStructUuidList();
//		userStructUuids = new ArrayList<Integer>(userStructUuids);
//		final Timestamp clientTime = new Timestamp(reqProto.getCurTime());
//
//		// stuff to send to client
//		final NormStructWaitCompleteResponseProto.Builder resBuilder =
//		    NormStructWaitCompleteResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(NormStructWaitCompleteStatus.FAIL_OTHER);
//
//		svcTxManager.beginTransaction();
//		try {
//			final List<StructureForUser> userStructs = RetrieveUtils.userStructRetrieveUtils()
//			    .getSpecificOrAllUserStructsForUser(userUuid, userStructUuids);
//
//			final List<Timestamp> newRetrievedTimes = new ArrayList<Timestamp>();
//			final boolean legitWaitComplete =
//			    checkLegitWaitComplete(resBuilder, userStructs, userStructUuids,
//			        senderProto.getUserUuid(), clientTime, newRetrievedTimes);
//
//			boolean success = false;
//			if (legitWaitComplete) {
//				// upgrading and building a building is the same thing
//				success = writeChangesToDB(userUuid, userStructs, newRetrievedTimes);
//			}
//
//			if (success) {
//				resBuilder.setStatus(NormStructWaitCompleteStatus.SUCCESS);
//				final List<StructureForUser> newUserStructs =
//				    RetrieveUtils.userStructRetrieveUtils()
//				        .getSpecificOrAllUserStructsForUser(userUuid, userStructUuids);
//				for (final StructureForUser userStruct : newUserStructs) {
//					resBuilder.addUserStruct(CreateInfoProtoUtils.createFullUserStructureProtoFromUserstruct(userStruct));
//				}
//			}
//
//			final NormStructWaitCompleteResponseEvent resEvent =
//			    new NormStructWaitCompleteResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setNormStructWaitCompleteResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in NormStructWaitCompleteController.processRequestEvent",
//				    e);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in NormStructWaitCompleteController processEvent", e);
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private boolean checkLegitWaitComplete( final Builder resBuilder,
//	    final List<StructureForUser> userStructs, final List<Integer> userStructUuids,
//	    final String userUuid, final Timestamp clientTime,
//	    final List<Timestamp> newRetrievedTimes )
//	{
//
//		if ((userStructs == null)
//		    || (userStructUuids == null)
//		    || (clientTime == null)
//		    || (userStructUuids.size() != userStructs.size())) {
//			resBuilder.setStatus(NormStructWaitCompleteStatus.FAIL_OTHER);
//			LOG.error("userStructs is null, userStructUuids is null, clientTime is null, or array lengths different. userStructs="
//			    + userStructs
//			    + ", userStructUuids="
//			    + userStructUuids
//			    + ", clientTime="
//			    + clientTime);
//			return false;
//		}
//
//		// for each user structure complete the ones the client said are done.
//		// replace what client sent with the ones that are actually done
//		final List<StructureForUser> validUserStructs = new ArrayList<StructureForUser>();
//		final List<Integer> validUserStructUuids = new ArrayList<Integer>();
//
//		final List<Timestamp> timesBuildsFinished =
//		    calculateValidUserStructs(userUuid, clientTime, userStructs, validUserStructUuids,
//		        validUserStructs);
//
//		if (userStructs.size() != validUserStructs.size()) {
//			LOG.warn("some of what the client sent is invalid. idsClientSent="
//			    + userStructUuids
//			    + "\t validUuids="
//			    + validUserStructUuids);
//			userStructs.clear();
//			userStructs.addAll(validUserStructs);
//
//			userStructUuids.clear();
//			userStructUuids.addAll(validUserStructUuids);
//		}
//
//		newRetrievedTimes.addAll(timesBuildsFinished);
//		return true;
//
//	}
//
//	// "validUserStructUuids" and "validUserStructs" WILL BE POPULATED
//	private List<Timestamp> calculateValidUserStructs( final String userUuid,
//	    final Timestamp clientTime, final List<StructureForUser> userStructs,
//	    final List<Integer> validUserStructUuids, final List<StructureForUser> validUserStructs )
//	{
//		final List<Timestamp> timesBuildsFinished = new ArrayList<Timestamp>();
//		final Map<Integer, Structure> structures =
//		    StructureRetrieveUtils.getStructUuidsToStructs();
//
//		for (final StructureForUser us : userStructs) {
//			if (us.getUserUuid() != userUuid) {
//				LOG.warn("user struct's owner's id is "
//				    + us.getUserUuid()
//				    + ", and user id is "
//				    + userUuid);
//				continue;
//			}
//			final Structure struct = structures.get(us.getStructId());
//			if (struct == null) {
//				LOG.warn("no struct in db exists with id "
//				    + us.getStructId());
//				continue;
//			}
//
//			final Date purchaseDate = us.getPurchaseTime();
//			final long buildTimeMillis = 60000 * struct.getMinutesToBuild();
//
//			if (null != purchaseDate) {
//				final long timeBuildFinished = purchaseDate.getTime()
//				    + buildTimeMillis;
//				if (timeBuildFinished > clientTime.getTime()) {
//					LOG.warn("the building is not done yet. userstruct="
//					    + ", client time is "
//					    + clientTime
//					    + ", purchase time was "
//					    + purchaseDate);
//					continue;
//				}// else this building is done now
//
//				validUserStructUuids.add(us.getId());
//				validUserStructs.add(us);
//				timesBuildsFinished.add(new Timestamp(timeBuildFinished));
//
//			} else {
//				LOG.warn("user struct has never been bought or purchased according to db. "
//				    + us);
//			}
//		}
//		return timesBuildsFinished;
//	}
//
//	private boolean writeChangesToDB( final String userUuid,
//	    final List<StructureForUser> buildsDone, final List<Timestamp> newRetrievedTimes )
//	{
//		if (!UpdateUtils.get()
//		    .updateUserStructsBuildingIscomplete(userUuid, buildsDone, newRetrievedTimes)) {
//			LOG.error("problem with marking norm struct builds as complete for one of these structs: "
//			    + buildsDone);
//			return false;
//		}
//		return true;
//	}
//
//}
