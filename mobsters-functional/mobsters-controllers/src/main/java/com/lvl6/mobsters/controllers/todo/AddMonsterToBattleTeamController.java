package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.MonsterEnhancingForUser;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.MonsterHealingForUser;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AddMonsterToBattleTeamRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AddMonsterToBattleTeamResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AddMonsterToBattleTeamResponseProto.AddMonsterToBattleTeamStatus;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AddMonsterToBattleTeamResponseProto.Builder;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.AddMonsterToBattleTeamRequestEvent;
import com.lvl6.mobsters.events.response.AddMonsterToBattleTeamResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class AddMonsterToBattleTeamController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(AddMonsterToBattleTeamController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public AddMonsterToBattleTeamController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new AddMonsterToBattleTeamRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_ADD_MONSTER_TO_BATTLE_TEAM_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final AddMonsterToBattleTeamRequestProto reqProto =
		    ((AddMonsterToBattleTeamRequestEvent) event).getAddMonsterToBattleTeamRequestProto();

		// get values sent from the client (the request proto)
		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int teamSlotNum = reqProto.getTeamSlotNum();
		final long userMonsterId = reqProto.getUserMonsterUuid();

		// set some values to send to the client (the response proto)
		final AddMonsterToBattleTeamResponseProto.Builder resBuilder =
		    AddMonsterToBattleTeamResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setStatus(AddMonsterToBattleTeamStatus.FAIL_OTHER); // default

		svcTxManager.beginTransaction();
		try {

			// make sure it exists
			final Map<Long, MonsterForUser> idsToMonsters =
			    RetrieveUtils.monsterForUserRetrieveUtils()
			        .getSpecificOrAllUserMonstersForUser(userUuid, null);
			// //get the ones that aren't in enhancing or healing
			// Map<Long, MonsterEnhancingForUser> inEnhancing =
			// MonsterEnhancingForUserRetrieveUtils.getMonstersForUser(userUuid);
			// Map<Long, MonsterHealingForUser> inHealing =
			// MonsterHealingForUserRetrieveUtils.getMonstersForUser(userUuid);
			final Map<Long, MonsterEnhancingForUser> inEnhancing =
			    new HashMap<Long, MonsterEnhancingForUser>();
			final Map<Long, MonsterHealingForUser> inHealing =
			    new HashMap<Long, MonsterHealingForUser>();

			final boolean legit =
			    checkLegit(resBuilder, userUuid, teamSlotNum, userMonsterId, idsToMonsters,
			        inEnhancing, inHealing);

			boolean successful = false;
			if (legit) {
				final MonsterForUser mfu = idsToMonsters.get(userMonsterId);
				successful = writeChangesToDb(userUuid, teamSlotNum, userMonsterId, mfu);
			}

			if (successful) {
				resBuilder.setStatus(AddMonsterToBattleTeamStatus.SUCCESS);
			}

			final AddMonsterToBattleTeamResponseEvent resEvent =
			    new AddMonsterToBattleTeamResponseEvent(userUuid);
			resEvent.setTag(event.getTag());
			resEvent.setAddMonsterToBattleTeamResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error(
				    "fatal exception in AddMonsterToBattleTeamController.processRequestEvent",
				    e);
			}
			//
			// UpdateClientUserResponseEvent resEventUpdate = MiscMethods
			// .createUpdateClientUserResponseEventAndUpdateLeaderboard(aUser);
			// resEventUpdate.setTag(event.getTag());
			// server.writeEvent(resEventUpdate);
		} catch (final Exception e) {
			LOG.error("exception in AddMonsterToBattleTeamController processEvent", e);
			// don't let the client hang
			try {
				resBuilder.setStatus(AddMonsterToBattleTeamStatus.FAIL_OTHER);
				final AddMonsterToBattleTeamResponseEvent resEvent =
				    new AddMonsterToBattleTeamResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setAddMonsterToBattleTeamResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in AddMonsterToBattleTeamController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in AddMonsterToBattleTeamController processEvent", e);
			}
		} finally {
			svcTxManager.commit();
		}
	}

	/*
	 * Return true if user request is valid; false otherwise and set the builder
	 * status to the appropriate value.
	 */
	private boolean checkLegit( final Builder resBuilder, final String userUuid,
	    final int teamSlotNum, final long userMonsterId,
	    final Map<Long, MonsterForUser> idsToMonsters,
	    final Map<Long, MonsterEnhancingForUser> inEnhancing,
	    final Map<Long, MonsterHealingForUser> inHealing )
	{

		if (!idsToMonsters.containsKey(userMonsterId)) {
			LOG.error("no monster_for_user exists with id="
			    + userMonsterId
			    + " and userUuid="
			    + userUuid);
			return false;
		}
		final MonsterForUser mfu = idsToMonsters.get(userMonsterId);

		// if a monster is already occupying the slot, replace it
		clearBattleTeamSlot(teamSlotNum, idsToMonsters);

		// CHECK TO MAKE SURE THE USER MONSTER IS COMPLETE
		if (!mfu.isComplete()) {
			LOG.error("user error: user trying to equip incomplete monster. userUuid="
			    + userUuid
			    + "\t monsterForUser="
			    + mfu);
			return false;
		}

		// inEnhancing has userMonsterId values for keys
		// NOT IN ENHANCING
		if (inEnhancing.containsKey(userMonsterId)) {
			LOG.error("user error: user is trying to \"equip\" a monster that is in"
			    + " enhancing."
			    + "\t userUuid="
			    + userUuid
			    + "\t monsterForUser="
			    + mfu
			    + " inEnhancing="
			    + inEnhancing);
			return false;
		}

		// inHealing has userMonsterId values for keys
		// NOT IN HEALING
		if (inHealing.containsKey(userMonsterId)) {
			LOG.error("user error: user is trying to \"equip\" a monster that is in"
			    + " healing."
			    + "\t userUuid="
			    + userUuid
			    + "\t monsterForUser="
			    + mfu
			    + " inHealing="
			    + inHealing);
			return false;
		}

		resBuilder.setStatus(AddMonsterToBattleTeamStatus.SUCCESS);
		return true;
	}

	// if there is a, or are monsters in the map with a teamSlotNum value that
	// equals
	// the argument passed in: teamSlotNum, then remove the existing guys in
	// said slot
	private void clearBattleTeamSlot( final int teamSlotNum,
	    final Map<Long, MonsterForUser> idsToMonsters )
	{
		final List<Long> userMonsterUuids = new ArrayList<Long>();

		// gather up the userMonsterUuids with a team slot value = teamSlotNum
		// (batch it)
		for (final MonsterForUser mfu : idsToMonsters.values()) {
			if (mfu.getTeamSlotNum() == teamSlotNum) {
				LOG.warn("more than one monster sharing team slot. userMonster="
				    + mfu);
				userMonsterUuids.add(mfu.getId());
			}
		}

		if (!userMonsterUuids.isEmpty()) {
			final int newTeamSlotNum = 0;
			// remove these monsters from the team slot with value = teamSlotNum
			final int num = UpdateUtils.get()
			    .nullifyMonstersTeamSlotNum(userMonsterUuids, newTeamSlotNum);
			LOG.warn("removed userMonsterUuids from teamSlot="
			    + teamSlotNum
			    + "\t userMonsterUuids="
			    + userMonsterUuids
			    + "\t numRemoved="
			    + num);
		}

	}

	private boolean writeChangesToDb( final int uId, final int teamSlotNum,
	    final long userMonsterId, final MonsterForUser mfu )
	{
		final int numUpdated = UpdateUtils.get()
		    .updateUserMonsterTeamSlotNum(userMonsterId, teamSlotNum);

		if (numUpdated == 1) {
			return true;
		}
		LOG.warn("unexpected error: user monster not updated. "
		    + "actual numUpdated="
		    + numUpdated
		    + "expected: 1 "
		    + "monsterForUser="
		    + mfu);
		return true;
	}

}
