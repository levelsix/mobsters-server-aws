package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.PrivateChatPost;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventChatProto.GroupChatMessageProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class RetrievePrivateChatPostsController extends EventController
{

	private static Logger LOG =
	    LoggerFactory.getLogger(RetrievePrivateChatPostsController.class);

	public RetrievePrivateChatPostsController()
	{
		numAllocatedThreads = 5;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new RetrievePrivateChatPostsRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_RETRIEVE_PRIVATE_CHAT_POST_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final RetrievePrivateChatPostsRequestProto reqProto =
		    ((RetrievePrivateChatPostsRequestEvent) event).getRetrievePrivateChatPostsRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int otherUserId = reqProto.getOtherUserUuid();
		final int beforePrivateChatId = reqProto.getBeforePrivateChatUuid();

		final RetrievePrivateChatPostsResponseProto.Builder resBuilder =
		    RetrievePrivateChatPostsResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		if (reqProto.hasBeforePrivateChatId()) {
			resBuilder.setBeforePrivateChatId(beforePrivateChatId);
		}
		resBuilder.setOtherUserId(otherUserId);

		try {
			resBuilder.setStatus(RetrievePrivateChatPostsStatus.SUCCESS);

			List<PrivateChatPost> recentPrivateChatPosts;
			if (beforePrivateChatId > 0) {
				// if client specified a private chat id
				recentPrivateChatPosts =
				    PrivateChatPostRetrieveUtils.getPrivateChatPostsBetweenUsersBeforePostId(
				        ControllerConstants.RETRIEVE_PLAYER_WALL_POSTS__NUM_POSTS_CAP,
				        beforePrivateChatId, userUuid, otherUserId);
			} else {
				// //if client didn't specify a private chat id
				recentPrivateChatPosts =
				    PrivateChatPostRetrieveUtils.getPrivateChatPostsBetweenUsersBeforePostId(
				        ControllerConstants.RETRIEVE_PLAYER_WALL_POSTS__NUM_POSTS_CAP,
				        ControllerConstants.NOT_SET, userUuid, otherUserId);
			}
			if (recentPrivateChatPosts != null) {
				if ((recentPrivateChatPosts != null)
				    && (recentPrivateChatPosts.size() > 0)) {
					final List<Integer> userUuids = new ArrayList<Integer>();
					userUuids.add(userUuid);
					userUuids.add(otherUserId);
					Map<Integer, User> usersByUuids = null;
					if (userUuids.size() > 0) {
						usersByUuids = RetrieveUtils.userRetrieveUtils()
						    .getUsersByUuids(userUuids);

						// for not hitting the db for every private chat post
						final Map<Integer, MinimumUserProtoWithLevel> userUuidsToMups =
						    generateUserUuidsToMupsWithLevel(usersByUuids, userUuid,
						        otherUserId);

						// convert private chat post to group chat message proto
						for (final PrivateChatPost pwp : recentPrivateChatPosts) {
							final int posterId = pwp.getPosterId();

							final long time = pwp.getTimeOfPost()
							    .getTime();
							final MinimumUserProtoWithLevel user =
							    userUuidsToMups.get(posterId);
							final String content = pwp.getContent();
							final boolean isAdmin = false;

							final GroupChatMessageProto gcmp =
							    CreateInfoProtoUtils.createGroupChatMessageProto(time, user,
							        content, isAdmin, pwp.getId());
							resBuilder.addPosts(gcmp);
						}
					}
				}
			} else {
				LOG.info("No private chat posts found for userUuid="
				    + userUuid
				    + " and otherUserId="
				    + otherUserId);
			}

			final RetrievePrivateChatPostsResponseProto resProto = resBuilder.build();

			final RetrievePrivateChatPostsResponseEvent resEvent =
			    new RetrievePrivateChatPostsResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setRetrievePrivateChatPostsResponseProto(resProto);

			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in RetrievePrivateChatPostsController.processRequestEvent",
				    e);
			}
		} catch (final Exception e) {
			LOG.error("exception in RetrievePrivateChatPostsController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(RetrievePrivateChatPostsStatus.FAIL);
				final RetrievePrivateChatPostsResponseEvent resEvent =
				    new RetrievePrivateChatPostsResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setRetrievePrivateChatPostsResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in RetrievePrivateChatPostsController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in RetrievePrivateChatPostsController processEvent", e);
			}
		}

	}

	private Map<Integer, MinimumUserProtoWithLevel> generateUserUuidsToMupsWithLevel(
	    final Map<Integer, User> usersByUuids, final String userUuid, final int otherUserId )
	{
		final Map<Integer, MinimumUserProtoWithLevel> userUuidsToMups =
		    new HashMap<Integer, MinimumUserProtoWithLevel>();

		final User aUser = usersByUuids.get(userUuid);
		final User otherUser = usersByUuids.get(otherUserId);

		final MinimumUserProtoWithLevel mup1 =
		    CreateInfoProtoUtils.createMinimumUserProtoWithLevelFromUser(aUser);
		userUuidsToMups.put(userUuid, mup1);

		final MinimumUserProtoWithLevel mup2 =
		    CreateInfoProtoUtils.createMinimumUserProtoWithLevelFromUser(otherUser);
		userUuidsToMups.put(otherUserId, mup2);

		return userUuidsToMups;
	}

}