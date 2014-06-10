package com.lvl6.mobsters.controllers.todo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.PrivateChatPostRequestEvent;
import com.lvl6.mobsters.events.response.PrivateChatPostResponseEvent;
import com.lvl6.mobsters.dynamo.AdminChatPost;
import com.lvl6.mobsters.dynamo.PrivateChatPost;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.misc.MiscMethods;
import com.lvl6.properties.ControllerConstants;
import com.lvl6.mobsters.noneventproto.NoneventChatProto.PrivateChatPostProto;
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostRequestProto;
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostResponseProto;
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostResponseProto.PrivateChatPostStatus;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.retrieveutils.rarechange.BannedUserRetrieveUtils;
import com.lvl6.utils.AdminChatUtil;
import com.lvl6.utils.CreateInfoProtoUtils;
import com.lvl6.utils.RetrieveUtils;
import com.lvl6.utils.utilmethods.InsertUtil;

@Component
@DependsOn("gameServer")
public class PrivateChatPostController extends EventController {

	private static Logger LOG = LoggerFactory.getLogger(PrivateChatPostController.class);
	}.getClass().getEnclosingClass());

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected AdminChatUtil adminChatUtil;

	@Autowired
	protected InsertUtil insertUtils;
	public PrivateChatPostController() {
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent() {
		return new PrivateChatPostRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType() {
		return EventProtocolRequest.C_PRIVATE_CHAT_POST_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event, final EventsToDispatch eventWriter ) throws Exception {
		final PrivateChatPostRequestProto reqProto = ((PrivateChatPostRequestEvent) event)
				.getPrivateChatPostRequestProto();

		// from client
		final MinimumUserProto senderProto = reqProto.getSender();
		final int posterId = senderProto.getUserUuid();
		final int recipientId = reqProto.getRecipientUuid();
		final String content = (reqProto.hasContent()) ? reqProto.getContent() : "";

		// to client
		final PrivateChatPostResponseProto.Builder resBuilder = PrivateChatPostResponseProto.newBuilder();
		resBuilder.setSender(senderProto);

		final List<Integer> userUuids = new ArrayList<Integer>();
		userUuids.add(posterId);
		userUuids.add(recipientId);
		
		try {
			final Map<Integer, User> users = RetrieveUtils.userRetrieveUtils().getUsersByUuids(userUuids);
			boolean legitPost = checkLegitPost(resBuilder, posterId, recipientId, content, users);

			final PrivateChatPostResponseEvent resEvent = new PrivateChatPostResponseEvent(posterId);
			resEvent.setTag(event.getTag());

			if (legitPost) {
				// record in db
				final Timestamp timeOfPost = new Timestamp(new Date().getTime());
				final String censoredContent = MiscMethods.censorUserInput(content);
				final int privateChatPostId = insertUtils.insertIntoPrivateChatPosts(posterId, recipientId,
						censoredContent, timeOfPost);
				if (privateChatPostId <= 0) {
					legitPost = false;
					resBuilder.setStatus(PrivateChatPostStatus.OTHER_FAIL);
					LOG.error("problem with inserting private chat post into db. posterId=" + posterId
							+ ", recipientId=" + recipientId + ", content=" + content + ", censoredContent="
							+ censoredContent + ", timeOfPost=" + timeOfPost);
				} else {

					if(recipientId == ControllerConstants.STARTUP__ADMIN_CHAT_USER_ID) {
						final AdminChatPost acp = new AdminChatPost(privateChatPostId,posterId, recipientId,  new Date(),censoredContent);
						acp.setUsername(users.get(posterId).getName());
						adminChatUtil.sendAdminChatEmail(acp);
					}
					final PrivateChatPost pwp = new PrivateChatPost(privateChatPostId, posterId, recipientId,
							timeOfPost, censoredContent);
					final User poster = users.get(posterId);
					final User recipient = users.get(recipientId);
					final PrivateChatPostProto pcpp = CreateInfoProtoUtils
							.createPrivateChatPostProtoFromPrivateChatPost(pwp, poster, recipient);
					resBuilder.setPost(pcpp);

					// send to recipient of the private chat post
					final PrivateChatPostResponseEvent resEvent2 = new PrivateChatPostResponseEvent(recipientId);
					resEvent2.setPrivateChatPostResponseProto(resBuilder.build());
					server.writeAPNSNotificationOrEvent(resEvent2);
				}
			}
			// send to sender of the private chat post
			resEvent.setPrivateChatPostResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: " + resEvent);
			try {
			    eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
			    LOG.error("fatal exception in PrivateChatPostController.processRequestEvent", e);
			}

			// if (legitPost && recipientId != posterId) {
			// User wallOwner = users.get(recipientId);
			// User poster = users.get(posterId);
			// if (MiscMethods.checkIfGoodSide(wallOwner.getType()) ==
			// !MiscMethods.checkIfGoodSide(poster.getType())) {
			// QuestUtils.checkAndSendQuestsCompleteBasic(server, posterId,
			// senderProto, SpecialQuestAction.WRITE_ON_ENEMY_WALL, true);
			// }
			// }
		} catch (final Exception e) {
			LOG.error("exception in PrivateChatPostController processEvent", e);
			try {
				resBuilder.setStatus(PrivateChatPostStatus.OTHER_FAIL);
				final PrivateChatPostResponseEvent resEvent =
						new PrivateChatPostResponseEvent(posterId);
				resEvent.setTag(event.getTag());
				resEvent.setPrivateChatPostResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: " + resEvent);
				try {
				    eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
				    LOG.error("fatal exception in PrivateChatPostController.processRequestEvent", e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in PrivateChatPostController processEvent", e);
			}
		}

	}

	private boolean checkLegitPost(final Builder resBuilder, final int posterId, final int recipientId, final String content,
			final Map<Integer, User> users) {
		if (users == null) {
			resBuilder.setStatus(PrivateChatPostStatus.OTHER_FAIL);
			LOG.error("users are null- posterId=" + posterId + ", recipientId=" + recipientId);
			return false;
		}
		if ((users.size() != 2) && (posterId != recipientId)) {
			resBuilder.setStatus(PrivateChatPostStatus.OTHER_FAIL);
			LOG.error("error retrieving one of the users. posterId=" + posterId + ", recipientId="
					+ recipientId);
			return false;
		}
		if ((users.size() != 1) && (posterId == recipientId)) {
			resBuilder.setStatus(PrivateChatPostStatus.OTHER_FAIL);
			LOG.error("error retrieving one of the users. posterId=" + posterId + ", recipientId="
					+ recipientId);
			return false;
		}
		if ((content == null) || (content.length() == 0)) {
			resBuilder.setStatus(PrivateChatPostStatus.NO_CONTENT_SENT);
			LOG.error("no content when posterId " + posterId + " tries to post on wall with owner "
					+ recipientId);
			return false;
		}
		// maybe use different controller constants...
		if (content.length() >= ControllerConstants.SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING) {
			resBuilder.setStatus(PrivateChatPostStatus.POST_TOO_LARGE);
			LOG.error("wall post is too long. content length is " + content.length() + ", max post length="
					+ ControllerConstants.SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING + ", posterId " + posterId
					+ " tries to post on wall with owner " + recipientId);
			return false;
		}
		final Set<Integer> banned = BannedUserRetrieveUtils.getAllBannedUsers();
		if ((null != banned) && banned.contains(posterId)) {
			resBuilder.setStatus(PrivateChatPostStatus.BANNED);
			LOG.warn("banned user tried to send a post. posterId=" + posterId);
			return false;
		}
		resBuilder.setStatus(PrivateChatPostStatus.SUCCESS);
		return true;
	}
	
	public AdminChatUtil getAdminChatUtil() {
		return adminChatUtil;
	}
	
	public void setAdminChatUtil(final AdminChatUtil adminChatUtil) {
		this.adminChatUtil = adminChatUtil;
	}
	
	public void setInsertUtils(final InsertUtil insertUtils) {
		this.insertUtils = insertUtils;
	}
	
}
