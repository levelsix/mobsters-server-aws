//package com.lvl6.mobsters.controllers.todo;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.Clan;
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.ClanForUser;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipRequestProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipResponseProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipResponseProto.TransferClanOwnershipStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.TransferClanOwnershipRequestEvent;
//import com.lvl6.mobsters.events.response.TransferClanOwnershipResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class TransferClanOwnershipController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(TransferClanOwnershipController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public TransferClanOwnershipController()
//	{
//		numAllocatedThreads = 2;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new TransferClanOwnershipRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_TRANSFER_CLAN_OWNERSHIP;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final TransferClanOwnershipRequestProto reqProto =
//		    ((TransferClanOwnershipRequestEvent) event).getTransferClanOwnershipRequestProto();
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final int newClanOwnerId = reqProto.getClanOwnerUuidNew();
//		final List<Integer> userUuids = new ArrayList<Integer>();
//		userUuids.add(userUuid);
//		userUuids.add(newClanOwnerId);
//
//		final TransferClanOwnershipResponseProto.Builder resBuilder =
//		    TransferClanOwnershipResponseProto.newBuilder();
//		resBuilder.setStatus(TransferClanOwnershipStatus.FAIL_OTHER);
//		resBuilder.setSender(senderProto);
//
//		int clanId = 0;
//		if (senderProto.hasClan()
//		    && (null != senderProto.getClan())) {
//			clanId = senderProto.getClan()
//			    .getClanId();
//		}
//
//		boolean lockedClan = false;
//		if (0 != clanId) {
//			lockedClan = getLocker().lockClan(clanId);
//		}
//		try {
//			final Map<Integer, User> users = RetrieveUtils.userRetrieveUtils()
//			    .getUsersByUuids(userUuids);
//			final Map<Integer, ClanForUser> userClans = RetrieveUtils.userClanRetrieveUtils()
//			    .getUserClanForUsers(clanId, userUuids);
//
//			final User user = users.get(userUuid);
//			final User newClanOwner = users.get(newClanOwnerId);
//
//			final boolean legitTransfer =
//			    checkLegitTransfer(resBuilder, lockedClan, userUuid, user, newClanOwnerId,
//			        newClanOwner, userClans);
//
//			if (legitTransfer) {
//				final List<UserClanStatus> statuses = new ArrayList<UserClanStatus>();
//				statuses.add(UserClanStatus.JUNIOR_LEADER);
//				statuses.add(UserClanStatus.LEADER);
//				writeChangesToDB(clanId, userUuids, statuses);
//				setResponseBuilderStuff(resBuilder, clanId, newClanOwner);
//			}
//
//			if (!legitTransfer) {
//				// if not successful write to guy
//				final TransferClanOwnershipResponseEvent resEvent =
//				    new TransferClanOwnershipResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setTransferClanOwnershipResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in TransferClanOwnershipController.processRequestEvent",
//					    e);
//				}
//			}
//
//			if (legitTransfer) {
//				final TransferClanOwnershipResponseEvent resEvent =
//				    new TransferClanOwnershipResponseEvent(senderProto.getUserUuid());
//				resEvent.setTag(event.getTag());
//				resEvent.setTransferClanOwnershipResponseProto(resBuilder.build());
//				server.writeClanEvent(resEvent, clanId);
//
//			}
//
//		} catch (final Exception e) {
//			LOG.error("exception in TransferClanOwnership processEvent", e);
//			try {
//				resBuilder.setStatus(TransferClanOwnershipStatus.FAIL_OTHER);
//				final TransferClanOwnershipResponseEvent resEvent =
//				    new TransferClanOwnershipResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setTransferClanOwnershipResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in TransferClanOwnershipController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in TransferClanOwnership processEvent", e);
//			}
//		} finally {
//			if ((0 != clanId)
//			    && lockedClan) {
//				getLocker().unlockClan(clanId);
//			}
//		}
//	}
//
//	private boolean checkLegitTransfer( final Builder resBuilder, final boolean lockedClan,
//	    final String userUuid, final User user, final int newClanOwnerId,
//	    final User newClanOwner, final Map<Integer, ClanForUser> userClans )
//	{
//
//		if (!lockedClan) {
//			LOG.error("couldn't obtain clan lock");
//			return false;
//		}
//
//		if ((user == null)
//		    || (newClanOwner == null)) {
//			resBuilder.setStatus(TransferClanOwnershipStatus.FAIL_OTHER);
//			LOG.error("user is "
//			    + user
//			    + ", new clan owner is "
//			    + newClanOwner);
//			return false;
//		}
//		if (user.getClanId() <= 0) {
//			resBuilder.setStatus(TransferClanOwnershipStatus.FAIL_NOT_AUTHORIZED);
//			LOG.error("user not in clan. user="
//			    + user);
//			return false;
//		}
//
//		if (newClanOwner.getClanId() != user.getClanId()) {
//			resBuilder.setStatus(TransferClanOwnershipStatus.FAIL_NEW_OWNER_NOT_IN_CLAN);
//			LOG.error("new owner not in same clan as user. new owner= "
//			    + newClanOwner
//			    + ", user is "
//			    + user);
//			return false;
//		}
//
//		if (!userClans.containsKey(userUuid)
//		    || !userClans.containsKey(newClanOwnerId)) {
//			LOG.error("a UserClan does not exist userUuid="
//			    + userUuid
//			    + ", newClanOwner="
//			    + newClanOwnerId
//			    + "\t userClans="
//			    + userClans);
//		}
//		final ClanForUser userClan = userClans.get(user.getId());
//
//		if (!UserClanStatus.LEADER.equals(userClan.getStatus())) {
//			resBuilder.setStatus(TransferClanOwnershipStatus.FAIL_NOT_AUTHORIZED);
//			LOG.error("user is "
//			    + user
//			    + ", and user isn't owner. user is:"
//			    + userClan);
//			return false;
//		}
//		resBuilder.setStatus(TransferClanOwnershipStatus.SUCCESS);
//		return true;
//	}
//
//	private void writeChangesToDB( final int clanId, final List<Integer> userIdList,
//	    final List<UserClanStatus> statuses )
//	{
//		// update clan for user table
//
//		final int numUpdated = UpdateUtils.get()
//		    .updateUserClanStatuses(clanId, userIdList, statuses);
//		LOG.info("num clan_for_user updated="
//		    + numUpdated
//		    + " userIdList="
//		    + userIdList
//		    + " statuses="
//		    + statuses);
//	}
//
//	private void setResponseBuilderStuff( final Builder resBuilder, final int clanId,
//	    final User newClanOwner )
//	{
//		final Clan clan = ClanRetrieveUtils.getClanWithId(clanId);
//		final List<Integer> clanIdList = Collections.singletonList(clanId);
//
//		final List<String> statuses = new ArrayList<String>();
//		statuses.add(UserClanStatus.LEADER.name());
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name());
//		statuses.add(UserClanStatus.CAPTAIN.name());
//		statuses.add(UserClanStatus.MEMBER.name());
//		final Map<Integer, Integer> clanIdToSize = RetrieveUtils.userClanRetrieveUtils()
//		    .getClanSizeForClanUuidsAndStatuses(clanIdList, statuses);
//
//		resBuilder.setMinClan(CreateInfoProtoUtils.createMinimumClanProtoFromClan(clan));
//
//		final int size = clanIdToSize.get(clanId);
//		resBuilder.setFullClan(CreateInfoProtoUtils.createFullClanProtoWithClanSize(clan, size));
//
//		final MinimumUserProto mup =
//		    CreateInfoProtoUtils.createMinimumUserProtoFromUser(newClanOwner);
//		resBuilder.setClanOwnerNew(mup);
//	}
//}
