//package com.lvl6.mobsters.controllers.todo;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanRequestProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanResponseProto;
//import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanResponseProto.BootPlayerFromClanStatus;
//import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanResponseProto.Builder;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.BootPlayerFromClanRequestEvent;
//import com.lvl6.mobsters.events.response.BootPlayerFromClanResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class BootPlayerFromClanController extends EventController
//{
//
//	private static Logger LOG = LoggerFactory.getLogger(BootPlayerFromClanController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public BootPlayerFromClanController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new BootPlayerFromClanRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_BOOT_PLAYER_FROM_CLAN_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final BootPlayerFromClanRequestProto reqProto =
//		    ((BootPlayerFromClanRequestEvent) event).getBootPlayerFromClanRequestProto();
//
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final int playerToBootId = reqProto.getPlayerToBoot();
//		final List<Integer> userUuids = new ArrayList<Integer>();
//		userUuids.add(userUuid);
//		userUuids.add(playerToBootId);
//
//		final BootPlayerFromClanResponseProto.Builder resBuilder =
//		    BootPlayerFromClanResponseProto.newBuilder();
//		resBuilder.setStatus(BootPlayerFromClanStatus.FAIL_OTHER);
//		resBuilder.setSender(senderProto);
//
//		int clanId = 0;
//
//		if (senderProto.hasClan()
//		    && (null != senderProto.getClan())) {
//			clanId = senderProto.getClan()
//			    .getClanId();
//		}
//		boolean lockedClan = false;
//		if (0 != clanId) {
//			lockedClan = getLocker().lockClan(clanId);
//		}
//		try {
//			final Map<Integer, User> users = RetrieveUtils.userRetrieveUtils()
//			    .getUsersByUuids(userUuids);
//			final User user = users.get(userUuid);
//			final User playerToBoot = users.get(playerToBootId);
//
//			final boolean legitBoot =
//			    checkLegitBoot(resBuilder, lockedClan, user, playerToBoot);
//
//			boolean success = false;
//			if (legitBoot) {
//				success = writeChangesToDB(user, playerToBoot);
//			}
//
//			if (success) {
//				final MinimumUserProto mup =
//				    CreateInfoProtoUtils.createMinimumUserProtoFromUser(playerToBoot);
//				resBuilder.setPlayerToBoot(mup);
//			}
//
//			final BootPlayerFromClanResponseEvent resEvent =
//			    new BootPlayerFromClanResponseEvent(senderProto.getUserUuid());
//			resEvent.setTag(event.getTag());
//			resEvent.setBootPlayerFromClanResponseProto(resBuilder.build());
//
//			if (success) {
//				// if successful write to clan
//				server.writeClanEvent(resEvent, clanId);
//			} else {
//				// write to user if fail
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in BootPlayerFromClanController.processRequestEvent",
//					    e);
//				}
//			}
//		} catch (final Exception e) {
//			LOG.error("exception in BootPlayerFromClan processEvent", e);
//			try {
//				resBuilder.setStatus(BootPlayerFromClanStatus.FAIL_OTHER);
//				final BootPlayerFromClanResponseEvent resEvent =
//				    new BootPlayerFromClanResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setBootPlayerFromClanResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in BootPlayerFromClanController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in BootPlayerFromClan processEvent", e);
//			}
//		} finally {
//			if ((0 != clanId)
//			    && lockedClan) {
//				getLocker().unlockClan(clanId);
//			}
//		}
//	}
//
//	private boolean checkLegitBoot( final Builder resBuilder, final boolean lockedClan,
//	    final User user, final User playerToBoot )
//	{
//
//		if (!lockedClan) {
//			LOG.error("couldn't obtain clan lock");
//			return false;
//		}
//
//		if ((user == null)
//		    || (playerToBoot == null)) {
//			resBuilder.setStatus(BootPlayerFromClanStatus.FAIL_OTHER);
//			LOG.error("user is "
//			    + user
//			    + ", playerToBoot is "
//			    + playerToBoot);
//			return false;
//		}
//
//		final int clanId = user.getClanId();
//		final List<String> statuses = new ArrayList<String>();
//		statuses.add(UserClanStatus.LEADER.name());
//		statuses.add(UserClanStatus.JUNIOR_LEADER.name());
//		final List<Integer> userUuids = RetrieveUtils.userClanRetrieveUtils()
//		    .getUserUuidsWithStatuses(clanId, statuses);
//
//		final Set<Integer> uniqUserUuids = new HashSet<Integer>();
//		if ((null != userUuids)
//		    && !userUuids.isEmpty()) {
//			uniqUserUuids.addAll(userUuids);
//		}
//
//		final String userUuid = user.getId();
//		if (!uniqUserUuids.contains(userUuid)) {
//			resBuilder.setStatus(BootPlayerFromClanStatus.FAIL_NOT_AUTHORIZED);
//			LOG.error("user can't boot player. user="
//			    + user
//			    + "\t playerToBoot="
//			    + playerToBoot);
//			return false;
//		}
//		if (playerToBoot.getClanId() != user.getClanId()) {
//			resBuilder.setStatus(BootPlayerFromClanStatus.FAIL_BOOTED_NOT_IN_CLAN);
//			LOG.error("playerToBoot is not in user clan. playerToBoot is in "
//			    + playerToBoot.getClanId());
//			return false;
//		}
//		resBuilder.setStatus(BootPlayerFromClanStatus.SUCCESS);
//		return true;
//	}
//
//	private boolean writeChangesToDB( final User user, final User playerToBoot )
//	{
//		if (!DeleteUtils.get()
//		    .deleteUserClan(playerToBoot.getId(), playerToBoot.getClanId())) {
//			LOG.error("problem with deleting user clan info for playerToBoot with id "
//			    + playerToBoot.getId()
//			    + " and clan id "
//			    + playerToBoot.getClanId());
//		}
//		if (!playerToBoot.updateRelativeCoinsAbsoluteClan(0, null)) {
//			LOG.error("problem with change playerToBoot "
//			    + playerToBoot
//			    + " clan id to nothing");
//		}
//		return true;
//	}
//
//}
