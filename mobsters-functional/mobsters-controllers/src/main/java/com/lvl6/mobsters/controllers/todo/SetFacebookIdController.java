//package com.lvl6.mobsters.controllers.todo;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdRequestProto;
//import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdResponseProto;
//import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdResponseProto.SetFacebookIdStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.SetFacebookIdRequestEvent;
//import com.lvl6.mobsters.events.response.SetFacebookIdResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class SetFacebookIdController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(SetFacebookIdController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public SetFacebookIdController()
//	{
//		numAllocatedThreads = 1;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new SetFacebookIdRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_SET_FACEBOOK_ID_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final SetFacebookIdRequestProto reqProto =
//		    ((SetFacebookIdRequestEvent) event).getSetFacebookIdRequestProto();
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		String fbId = reqProto.getFbUuid();
//		final boolean isUserCreate = reqProto.getIsUserCreate();
//
//		// basically, if fbId is empty make it null
//		if ((null != fbId)
//		    && fbId.isEmpty()) {
//			fbId = null;
//		}
//
//		// prepping the arguments to query the db
//		List<String> facebookUuids = null;
//		if (null != fbId) {
//			facebookUuids = new ArrayList<String>();
//			facebookUuids.add(fbId);
//		}
//		final List<Integer> userUuids = new ArrayList<Integer>();
//		userUuids.add(userUuid);
//
//		final Builder resBuilder = SetFacebookIdResponseProto.newBuilder();
//		resBuilder.setStatus(SetFacebookIdStatus.FAIL_OTHER);
//		resBuilder.setSender(senderProto);
//		svcTxManager.beginTransaction();
//		try {
//			// User user =
//			// RetrieveUtils.userRetrieveUtils().getUserById(senderProto.getUserUuid());
//			final Map<Integer, User> userMap = RetrieveUtils.userRetrieveUtils()
//			    .getUsersForFacebookIdsOrUserUuids(facebookUuids, userUuids);
//			final User user = userMap.get(userUuid);
//
//			boolean legit = checkLegitRequest(resBuilder, user, fbId, userMap);
//
//			if (legit) {
//				legit = writeChangesToDb(user, fbId, isUserCreate);
//			}
//
//			if (legit) {
//				resBuilder.setStatus(SetFacebookIdStatus.SUCCESS);
//			}
//
//			final SetFacebookIdResponseProto resProto = resBuilder.build();
//			final SetFacebookIdResponseEvent resEvent =
//			    new SetFacebookIdResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setSetFacebookIdResponseProto(resProto);
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error("fatal exception in SetFacebookIdController.processRequestEvent", e);
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in SetFacebookIdController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(SetFacebookIdStatus.FAIL_OTHER);
//				final SetFacebookIdResponseEvent resEvent =
//				    new SetFacebookIdResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setSetFacebookIdResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error("fatal exception in SetFacebookIdController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in SetFacebookIdController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	private boolean checkLegitRequest( final Builder resBuilder, final User user,
//	    final String newFbId, final Map<Integer, User> userMap )
//	{
//		if ((newFbId == null)
//		    || newFbId.isEmpty()
//		    || (user == null)) {
//			LOG.error("fbId not set or user is null. fbId='"
//			    + newFbId
//			    + "'\t user="
//			    + user);
//			return false;
//		}
//
//		final String existingFbId = user.getFacebookId();
//		final boolean existingFbIdSet = (existingFbId != null)
//		    && !existingFbId.isEmpty();
//
//		if (existingFbIdSet) {
//			LOG.error("fbId already set for user. existingFbId='"
//			    + existingFbId
//			    + "'\t user="
//			    + user
//			    + "\t newFbId="
//			    + newFbId);
//			resBuilder.setStatus(SetFacebookIdStatus.FAIL_USER_FB_ID_ALREADY_SET);
//			return false;
//		}
//
//		// fbId is something and user doesn't have fbId
//		// now check if other users have the newFbId
//
//		if (userMap.size() > 1) {
//			// queried for a userUuid and a facebook id
//			LOG.error("fbId already taken. fbId='"
//			    + newFbId
//			    + "'\t usersInDb="
//			    + userMap);
//			resBuilder.setStatus(SetFacebookIdStatus.FAIL_FB_ID_EXISTS);
//
//			// client wants the user who has the facebook id
//			for (final User u : userMap.values()) {
//
//				if (!newFbId.equals(u.getFacebookId())) {
//					continue;
//				}
//
//				final MinimumUserProto existingProto =
//				    CreateInfoProtoUtils.createMinimumUserProtoFromUser(u);
//				resBuilder.setExisting(existingProto);
//				break;
//			}
//
//			return false;
//		}
//
//		return true;
//	}
//
//	private boolean writeChangesToDb( final User user, final String fbId,
//	    final boolean isUserCreate )
//	{
//
//		if (!user.updateSetFacebookId(fbId, isUserCreate)) {
//			LOG.error("problem with setting user's facebook id to "
//			    + fbId);
//			return false;
//		}
//
//		return true;
//	}
//
//}
