//package com.lvl6.mobsters.controllers.todo;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.MonsterForUser;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamRequestProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto.Builder;
//import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto.RemoveMonsterFromBattleTeamStatus;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.RemoveMonsterFromBattleTeamRequestEvent;
//import com.lvl6.mobsters.events.response.RemoveMonsterFromBattleTeamResponseEvent;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class RemoveMonsterFromBattleTeamController extends EventController
//{
//
//	private static Logger LOG =
//	    LoggerFactory.getLogger(RemoveMonsterFromBattleTeamController.class);
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	public RemoveMonsterFromBattleTeamController()
//	{
//		numAllocatedThreads = 4;
//	}
//
//	@Override
//	public RequestEvent createRequestEvent()
//	{
//		return new RemoveMonsterFromBattleTeamRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType()
//	{
//		return EventProtocolRequest.C_REMOVE_MONSTER_FROM_BATTLE_TEAM_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event,
//	    final EventsToDispatch eventWriter ) throws Exception
//	{
//		final RemoveMonsterFromBattleTeamRequestProto reqProto =
//		    ((RemoveMonsterFromBattleTeamRequestEvent) event).getRemoveMonsterFromBattleTeamRequestProto();
//
//		// get values sent from the client (the request proto)
//		final MinimumUserProto senderProto = reqProto.getSender();
//		final String userUuid = senderProto.getUserUuid();
//		final long userMonsterId = reqProto.getUserMonsterUuid();
//
//		// set some values to send to the client (the response proto)
//		final RemoveMonsterFromBattleTeamResponseProto.Builder resBuilder =
//		    RemoveMonsterFromBattleTeamResponseProto.newBuilder();
//		resBuilder.setSender(senderProto);
//		resBuilder.setStatus(RemoveMonsterFromBattleTeamStatus.FAIL_OTHER); // default
//
//		svcTxManager.beginTransaction();
//		try {
//			// User aUser =
//			// RetrieveUtils.userRetrieveUtils().getUserById(userUuid);
//
//			// make sure it exists
//			final MonsterForUser mfu = RetrieveUtils.monsterForUserRetrieveUtils()
//			    .getSpecificUserMonster(userMonsterId);
//
//			final boolean legit = checkLegit(resBuilder, userUuid, userMonsterId, mfu);
//
//			boolean successful = false;
//			if (legit) {
//				successful = writeChangesToDb(userUuid, userMonsterId, mfu);
//			}
//
//			if (successful) {
//				resBuilder.setStatus(RemoveMonsterFromBattleTeamStatus.SUCCESS);
//			}
//
//			final RemoveMonsterFromBattleTeamResponseEvent resEvent =
//			    new RemoveMonsterFromBattleTeamResponseEvent(userUuid);
//			resEvent.setTag(event.getTag());
//			resEvent.setRemoveMonsterFromBattleTeamResponseProto(resBuilder.build());
//			// write to client
//			LOG.info("Writing event: "
//			    + resEvent);
//			try {
//				eventWriter.writeEvent(resEvent);
//			} catch (final Throwable e) {
//				LOG.error(
//				    "fatal exception in RemoveMonsterFromBattleTeamController.processRequestEvent",
//				    e);
//			}
//			//
//			// UpdateClientUserResponseEvent resEventUpdate = MiscMethods
//			// .createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser);
//			// resEventUpdate.setTag(event.getTag());
//			// server.writeEvent(resEventUpdate);
//		} catch (final Exception e) {
//			LOG.error("exception in RemoveMonsterFromBattleTeamController processEvent", e);
//			// don't let the client hang
//			try {
//				resBuilder.setStatus(RemoveMonsterFromBattleTeamStatus.FAIL_OTHER);
//				final RemoveMonsterFromBattleTeamResponseEvent resEvent =
//				    new RemoveMonsterFromBattleTeamResponseEvent(userUuid);
//				resEvent.setTag(event.getTag());
//				resEvent.setRemoveMonsterFromBattleTeamResponseProto(resBuilder.build());
//				// write to client
//				LOG.info("Writing event: "
//				    + resEvent);
//				try {
//					eventWriter.writeEvent(resEvent);
//				} catch (final Throwable e) {
//					LOG.error(
//					    "fatal exception in RemoveMonsterFromBattleTeamController.processRequestEvent",
//					    e);
//				}
//			} catch (final Exception e2) {
//				LOG.error("exception2 in RemoveMonsterFromBattleTeamController processEvent", e);
//			}
//		} finally {
//			svcTxManager.commit();
//		}
//	}
//
//	/*
//	 * Return true if user request is valid; false otherwise and set the builder
//	 * status to the appropriate value.
//	 */
//	private boolean checkLegit( final Builder resBuilder, final String userUuid,
//	    final long userMonsterId, final MonsterForUser mfu )
//	{
//
//		if (null == mfu) {
//			LOG.error("no monster_for_user exists with id="
//			    + userMonsterId);
//			return false;
//		}
//
//		// check to make sure this is indeed the user's monster
//		final String mfuUserUuid = mfu.getUserUuid();
//		if (mfuUserId != userUuid) {
//			LOG.error("what is this I don't even...client trying to \"unequip\" "
//			    + "another user's monster. userUuid="
//			    + userUuid
//			    + "\t monsterForUser="
//			    + mfu);
//			return false;
//		}
//
//		resBuilder.setStatus(RemoveMonsterFromBattleTeamStatus.SUCCESS);
//		return true;
//	}
//
//	private boolean writeChangesToDb( final int uId, final long userMonsterId,
//	    final MonsterForUser mfu )
//	{
//		// "unequip" monster
//		final int teamSlotNum = 0;
//
//		final int numUpdated = UpdateUtils.get()
//		    .updateUserMonsterTeamSlotNum(userMonsterId, teamSlotNum);
//
//		if (numUpdated == 1) {
//			return true;
//		}
//		LOG.warn("unexpected error: user monster not updated. "
//		    + "actual numUpdated="
//		    + numUpdated
//		    + "expected: 1 "
//		    + "monsterForUser="
//		    + mfu);
//		return true;
//	}
//
//}
