package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.MonsterForUser
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto
import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto.Builder
import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto.RemoveMonsterFromBattleTeamStatus
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.events.request.RemoveMonsterFromBattleTeamRequestEvent
import com.lvl6.mobsters.events.response.RemoveMonsterFromBattleTeamResponseEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.EventController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class RemoveMonsterFromBattleTeamController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(RemoveMonsterFromBattleTeamController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 4
	}

	override createRequestEvent()
	{
		new RemoveMonsterFromBattleTeamRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_REMOVE_MONSTER_FROM_BATTLE_TEAM_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as RemoveMonsterFromBattleTeamRequestEvent)).
			removeMonsterFromBattleTeamRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val userMonsterId = reqProto.userMonsterUuid
		val resBuilder = RemoveMonsterFromBattleTeamResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.status = RemoveMonsterFromBattleTeamStatus.FAIL_OTHER
		svcTxManager.beginTransaction
		try
		{
			val mfu = RetrieveUtils::monsterForUserRetrieveUtils.
				getSpecificUserMonster(userMonsterId)
			val legit = checkLegit(resBuilder, userUuid, userMonsterId, mfu)
			var successful = false
			if (legit)
			{
				successful = writeChangesToDb(userUuid, userMonsterId, mfu)
			}
			if (successful)
			{
				resBuilder.status = RemoveMonsterFromBattleTeamStatus.SUCCESS
			}
			val resEvent = new RemoveMonsterFromBattleTeamResponseEvent(userUuid)
			resEvent.tag = event.tag
			resEvent.removeMonsterFromBattleTeamResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error(
					'fatal exception in RemoveMonsterFromBattleTeamController.processRequestEvent',
					e)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in RemoveMonsterFromBattleTeamController processEvent', e)
			try
			{
				resBuilder.status = RemoveMonsterFromBattleTeamStatus.FAIL_OTHER
				val resEvent = new RemoveMonsterFromBattleTeamResponseEvent(userUuid)
				resEvent.tag = event.tag
				resEvent.removeMonsterFromBattleTeamResponseProto = resBuilder.build
				LOG.info('Writing event: ' + resEvent)
				try
				{
					eventWriter.writeEvent(resEvent)
				}
				catch (Throwable e)
				{
					LOG.error(
						'fatal exception in RemoveMonsterFromBattleTeamController.processRequestEvent',
						e)
				}
			}
			catch (Exception e2)
			{
				LOG.error('exception2 in RemoveMonsterFromBattleTeamController processEvent', e)
			}
		}
		finally
		{
			svcTxManager.commit
		}
	}

	private def checkLegit(Builder resBuilder, String userUuid, long userMonsterId,
		MonsterForUser mfu)
	{
		if (null === mfu)
		{
			LOG.error('no monster_for_user exists with id=' + userMonsterId)
			return false;
		}
		val mfuUserUuid = mfu.userUuid
		if (mfuUserId !== userUuid)
		{
			LOG.error(
				'what is this I don\'t even...client trying to "unequip" ' +
					"another user's monster. userUuid=" + userUuid + '	 monsterForUser=' + mfu)
			return false;
		}
		resBuilder.status = RemoveMonsterFromBattleTeamStatus.SUCCESS
		true
	}

	private def writeChangesToDb(int uId, long userMonsterId, MonsterForUser mfu)
	{
		val teamSlotNum = 0
		val numUpdated = UpdateUtils::get.
			updateUserMonsterTeamSlotNum(userMonsterId, teamSlotNum)
		if (numUpdated === 1)
		{
			return true;
		}
		LOG.warn(
			'unexpected error: user monster not updated. ' + 'actual numUpdated=' + numUpdated +
				'expected: 1 ' + 'monsterForUser=' + mfu)
		true
	}
}
