package com.lvl6.mobsters.binaryproto;

import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_ACCEPT_AND_REJECT_FB_INVITE_FOR_SLOTS_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_ACHIEVEMENT_PROGRESS_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_ACHIEVEMENT_REDEEM_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_ADD_MONSTER_TO_BATTLE_TEAM_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_APPROVE_OR_REJECT_REQUEST_TO_JOIN_CLAN_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_ATTACK_CLAN_RAID_MONSTER_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_BEGIN_CLAN_RAID_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_BEGIN_DUNGEON_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_BEGIN_MINI_JOB_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_BEGIN_OBSTACLE_REMOVAL_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_BEGIN_PVP_BATTLE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_BOOT_PLAYER_FROM_CLAN_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_CHANGE_CLAN_SETTINGS_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_COMBINE_USER_MONSTER_PIECES_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_COMPLETE_MINI_JOB_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_CREATE_CLAN_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_ENABLE_APNS_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_END_DUNGEON_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_END_PVP_BATTLE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_ENHANCEMENT_WAIT_TIME_COMPLETE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_EVOLUTION_FINISHED_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_EVOLVE_MONSTER_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_EXCHANGE_GEMS_FOR_RESOURCES_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_FINISH_NORM_STRUCT_WAITTIME_WITH_DIAMONDS_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_HEAL_MONSTER_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_INCREASE_MONSTER_INVENTORY_SLOT_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_INVITE_FB_FRIENDS_FOR_SLOTS_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_IN_APP_PURCHASE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_LEAVE_CLAN_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_LEVEL_UP_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_LOAD_PLAYER_CITY_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_LOGOUT_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_MOVE_OR_ROTATE_NORM_STRUCTURE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_NORM_STRUCT_WAIT_COMPLETE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_OBSTACLE_REMOVAL_COMPLETE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_PRIVATE_CHAT_POST_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_PROMOTE_DEMOTE_CLAN_MEMBER_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_PURCHASE_BOOSTER_PACK_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_PURCHASE_NORM_STRUCTURE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_QUEST_ACCEPT_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_QUEST_PROGRESS_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_QUEST_REDEEM_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_QUEUE_UP_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_RECORD_CLAN_RAID_STATS_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_REDEEM_MINI_JOB_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_REMOVE_MONSTER_FROM_BATTLE_TEAM_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_REQUEST_JOIN_CLAN_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_RESTRICT_USER_MONSTER_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_RETRACT_REQUEST_JOIN_CLAN_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_RETRIEVE_CLAN_INFO_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_RETRIEVE_CURRENCY_FROM_NORM_STRUCTURE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_REVIVE_IN_DUNGEON_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_SELL_USER_MONSTER_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_SEND_GROUP_CHAT_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_SET_AVATAR_MONSTER_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_SET_FACEBOOK_ID_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_SET_GAME_CENTER_ID_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_SPAWN_MINI_JOB_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_SPAWN_OBSTACLE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_STARTUP_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_SUBMIT_MONSTER_ENHANCEMENT_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_TRANSFER_CLAN_OWNERSHIP_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_UPDATE_MONSTER_HEALTH_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_UPDATE_USER_CURRENCY_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_UPGRADE_NORM_STRUCTURE_EVENT_VALUE;
import static com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest.C_USER_CREATE_EVENT_VALUE;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

import com.google.common.base.Preconditions;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementProgressResponseProto;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemResponseProto;
import com.lvl6.mobsters.eventproto.EventApnsProto.EnableAPNSResponseProto;
import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackResponseProto;
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostResponseProto;
import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatResponseProto;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipResponseProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonResponseProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonResponseProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonResponseProto;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesResponseProto;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.InAppPurchaseResponseProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.BeginMiniJobResponseProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobResponseProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobResponseProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.SpawnMiniJobResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AddMonsterToBattleTeamResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.HealMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RestrictUserMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementResponseProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.UpdateMonsterHealthResponseProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleResponseProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleResponseProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpResponseProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptResponseProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressResponseProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemResponseProto;
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleResponseProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetAvatarMonsterResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyResponseProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateResponseProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.websockets.MobstersHeaderAccessor;
/**
 * This class has two conversion responsibilities with regard to REPLY messages.
 * 
 * 1)  Given a Spring Message encapsulation with payload and a Spring-standardized header interface, convert the
 *     Payload to a Java Object that conforms to a given Class.  (In our case, the Class will always be GeneratedMessage,
 *     the abstract root of all Protobuf representations).  (Inbound use case)
 * 2)  Given a Java Object and a copy of the Spring Headers, return a byte array that Spring will later combine with 
 *     the Headers it shared to create a Message object.  (Outbound use case)
 * 
 * @author John
 *
 */
public class ResponseProtobufConverter extends AbstractMessageConverter {
	
	static final MimeType PROTO_BUF_MIME_TYPE = new MimeType( "application", "protobuf" );

	private final GeneratedMessage.Builder<?>[] mobstersEventType;

	public ResponseProtobufConverter() {
		super(PROTO_BUF_MIME_TYPE);

		mobstersEventType = new GeneratedMessage.Builder<?>[102];

		mobstersEventType[C_STARTUP_EVENT_VALUE] =
			StartupResponseProto.newBuilder();
		mobstersEventType[C_IN_APP_PURCHASE_EVENT_VALUE] =
			InAppPurchaseResponseProto.newBuilder();
		mobstersEventType[C_PURCHASE_NORM_STRUCTURE_EVENT_VALUE] =
			PurchaseNormStructureResponseProto.newBuilder();
		mobstersEventType[C_MOVE_OR_ROTATE_NORM_STRUCTURE_EVENT_VALUE] =
			MoveOrRotateNormStructureResponseProto.newBuilder();
		mobstersEventType[C_SET_FACEBOOK_ID_EVENT_VALUE] =
			SetFacebookIdResponseProto.newBuilder();
		mobstersEventType[C_UPGRADE_NORM_STRUCTURE_EVENT_VALUE] =
			UpgradeNormStructureResponseProto.newBuilder();
		mobstersEventType[C_RETRIEVE_CURRENCY_FROM_NORM_STRUCTURE_EVENT_VALUE] =
			RetrieveCurrencyFromNormStructureResponseProto.newBuilder();
		mobstersEventType[C_FINISH_NORM_STRUCT_WAITTIME_WITH_DIAMONDS_EVENT_VALUE] =
			FinishNormStructWaittimeWithDiamondsResponseProto.newBuilder();
		mobstersEventType[C_NORM_STRUCT_WAIT_COMPLETE_EVENT_VALUE] =
			NormStructWaitCompleteResponseProto.newBuilder();
		mobstersEventType[C_LOAD_PLAYER_CITY_EVENT_VALUE] =
			LoadPlayerCityResponseProto.newBuilder();
		mobstersEventType[C_EXCHANGE_GEMS_FOR_RESOURCES_EVENT_VALUE] =
			ExchangeGemsForResourcesResponseProto.newBuilder();
		mobstersEventType[C_QUEST_ACCEPT_EVENT_VALUE] =
			QuestAcceptResponseProto.newBuilder();
		mobstersEventType[C_QUEST_PROGRESS_EVENT_VALUE] =
			QuestProgressResponseProto.newBuilder();
		mobstersEventType[C_QUEST_REDEEM_EVENT_VALUE] =
			QuestRedeemResponseProto.newBuilder();
		mobstersEventType[C_LEVEL_UP_EVENT_VALUE] =
			LevelUpResponseProto.newBuilder();
		mobstersEventType[C_ENABLE_APNS_EVENT_VALUE] =
			EnableAPNSResponseProto.newBuilder();
		mobstersEventType[C_USER_CREATE_EVENT_VALUE] =
			UserCreateResponseProto.newBuilder();
		mobstersEventType[C_SEND_GROUP_CHAT_EVENT_VALUE] =
			SendGroupChatResponseProto.newBuilder();
		mobstersEventType[C_CREATE_CLAN_EVENT_VALUE] =
			CreateClanResponseProto.newBuilder();
		mobstersEventType[C_LEAVE_CLAN_EVENT_VALUE] =
			LeaveClanResponseProto.newBuilder();
		mobstersEventType[C_REQUEST_JOIN_CLAN_EVENT_VALUE] =
			RequestJoinClanResponseProto.newBuilder();
		mobstersEventType[C_RETRACT_REQUEST_JOIN_CLAN_EVENT_VALUE] =
			RetractRequestJoinClanResponseProto.newBuilder();
		mobstersEventType[C_APPROVE_OR_REJECT_REQUEST_TO_JOIN_CLAN_EVENT_VALUE] =
			ApproveOrRejectRequestToJoinClanResponseProto.newBuilder();
		mobstersEventType[C_TRANSFER_CLAN_OWNERSHIP_VALUE] = 
			TransferClanOwnershipResponseProto.newBuilder();
		mobstersEventType[C_RETRIEVE_CLAN_INFO_EVENT_VALUE] =
			RetrieveClanInfoResponseProto.newBuilder();
		mobstersEventType[C_CHANGE_CLAN_SETTINGS_EVENT_VALUE] =
			ChangeClanSettingsResponseProto.newBuilder();
		mobstersEventType[C_BOOT_PLAYER_FROM_CLAN_EVENT_VALUE] =
			BootPlayerFromClanResponseProto.newBuilder();
		mobstersEventType[C_SUBMIT_MONSTER_ENHANCEMENT_EVENT_VALUE] =
			SubmitMonsterEnhancementResponseProto.newBuilder();
		mobstersEventType[C_EVOLVE_MONSTER_EVENT_VALUE] =
			EvolveMonsterResponseProto.newBuilder();
		mobstersEventType[C_PURCHASE_BOOSTER_PACK_EVENT_VALUE] =
			PurchaseBoosterPackResponseProto.newBuilder();
		mobstersEventType[C_EVOLUTION_FINISHED_EVENT_VALUE] =
			EvolutionFinishedResponseProto.newBuilder();
		mobstersEventType[C_ACHIEVEMENT_PROGRESS_EVENT_VALUE] =
			AchievementProgressResponseProto.newBuilder();
		mobstersEventType[C_PRIVATE_CHAT_POST_EVENT_VALUE] =
			PrivateChatPostResponseProto.newBuilder();
		mobstersEventType[C_BEGIN_DUNGEON_EVENT_VALUE] =
			BeginDungeonResponseProto.newBuilder();
		mobstersEventType[C_END_DUNGEON_EVENT_VALUE] =
			EndDungeonResponseProto.newBuilder();
		mobstersEventType[C_REVIVE_IN_DUNGEON_EVENT_VALUE] =
			ReviveInDungeonResponseProto.newBuilder();
		mobstersEventType[C_QUEUE_UP_EVENT_VALUE] =
			QueueUpResponseProto.newBuilder();
		mobstersEventType[C_UPDATE_MONSTER_HEALTH_EVENT_VALUE] =
			UpdateMonsterHealthResponseProto.newBuilder();
		mobstersEventType[C_HEAL_MONSTER_EVENT_VALUE] =
			HealMonsterResponseProto.newBuilder();
		mobstersEventType[C_ACHIEVEMENT_REDEEM_EVENT_VALUE] =
			AchievementRedeemResponseProto.newBuilder();
		mobstersEventType[C_ADD_MONSTER_TO_BATTLE_TEAM_EVENT_VALUE] =
			AddMonsterToBattleTeamResponseProto.newBuilder();
		mobstersEventType[C_REMOVE_MONSTER_FROM_BATTLE_TEAM_EVENT_VALUE] =
			RemoveMonsterFromBattleTeamResponseProto.newBuilder();
		mobstersEventType[C_INCREASE_MONSTER_INVENTORY_SLOT_EVENT_VALUE] =
			IncreaseMonsterInventorySlotResponseProto.newBuilder();
		mobstersEventType[C_ENHANCEMENT_WAIT_TIME_COMPLETE_EVENT_VALUE] =
			EnhancementWaitTimeCompleteResponseProto.newBuilder();
		mobstersEventType[C_COMBINE_USER_MONSTER_PIECES_EVENT_VALUE] =
			CombineUserMonsterPiecesResponseProto.newBuilder();
		mobstersEventType[C_SELL_USER_MONSTER_EVENT_VALUE] =
			SellUserMonsterResponseProto.newBuilder();
		mobstersEventType[C_INVITE_FB_FRIENDS_FOR_SLOTS_EVENT_VALUE] =
			InviteFbFriendsForSlotsResponseProto.newBuilder();
		mobstersEventType[C_ACCEPT_AND_REJECT_FB_INVITE_FOR_SLOTS_EVENT_VALUE] =
			AcceptAndRejectFbInviteForSlotsResponseProto.newBuilder();
		mobstersEventType[C_UPDATE_USER_CURRENCY_EVENT_VALUE] =
			UpdateUserCurrencyResponseProto.newBuilder();
		mobstersEventType[C_BEGIN_PVP_BATTLE_EVENT_VALUE] =
			BeginPvpBattleResponseProto.newBuilder();
		mobstersEventType[C_END_PVP_BATTLE_EVENT_VALUE] =
			EndPvpBattleResponseProto.newBuilder();
		mobstersEventType[C_BEGIN_CLAN_RAID_EVENT_VALUE] =
			BeginClanRaidResponseProto.newBuilder();
		mobstersEventType[C_ATTACK_CLAN_RAID_MONSTER_EVENT_VALUE] =
			AttackClanRaidMonsterResponseProto.newBuilder();
		mobstersEventType[C_RECORD_CLAN_RAID_STATS_EVENT_VALUE] =
			RecordClanRaidStatsResponseProto.newBuilder();
		mobstersEventType[C_PROMOTE_DEMOTE_CLAN_MEMBER_EVENT_VALUE] =
			PromoteDemoteClanMemberResponseProto.newBuilder();
		mobstersEventType[C_SET_GAME_CENTER_ID_EVENT_VALUE] =
			SetGameCenterIdResponseProto.newBuilder();
		mobstersEventType[C_SPAWN_OBSTACLE_EVENT_VALUE] =
			SpawnObstacleResponseProto.newBuilder();
		mobstersEventType[C_BEGIN_OBSTACLE_REMOVAL_EVENT_VALUE] =
			BeginObstacleRemovalResponseProto.newBuilder();
		mobstersEventType[C_OBSTACLE_REMOVAL_COMPLETE_EVENT_VALUE] =
			ObstacleRemovalCompleteResponseProto.newBuilder();
		mobstersEventType[C_SPAWN_MINI_JOB_EVENT_VALUE] =
			SpawnMiniJobResponseProto.newBuilder();
		mobstersEventType[C_BEGIN_MINI_JOB_EVENT_VALUE] =
			BeginMiniJobResponseProto.newBuilder();
		mobstersEventType[C_COMPLETE_MINI_JOB_EVENT_VALUE] =
			CompleteMiniJobResponseProto.newBuilder();
		mobstersEventType[C_REDEEM_MINI_JOB_EVENT_VALUE] =
			RedeemMiniJobResponseProto.newBuilder();
		mobstersEventType[C_SET_AVATAR_MONSTER_EVENT_VALUE] =
			SetAvatarMonsterResponseProto.newBuilder();
		mobstersEventType[C_RESTRICT_USER_MONSTER_EVENT_VALUE] =
			RestrictUserMonsterResponseProto.newBuilder();
		mobstersEventType[C_LOGOUT_EVENT_VALUE] = null;
	}

	@Override
	protected boolean supports( Class<?> clazz ) {
		Preconditions.checkNotNull(clazz);
		return Message.class.isAssignableFrom(clazz);
	}
	
	@Override
	public Object convertFromInternal(
		org.springframework.messaging.Message<?> message, 
		Class<?> outputClass ) 
	{
		// NOTE: Use of an Accessor class couples this to a particular protocol.  It yields more concise code, but
		//       it bypasses Spring's effort to generalize header access in a cross-protocol fashion.
		final EventProtocolRequest eventType = 
			MobstersHeaderAccessor.wrap(message)
				.getRequestType();
		final byte [] binarySource = 
			(byte []) message.getPayload();
	
		try {
			return 
				new ParsedProtoRequest<Message>(
					mobstersEventType[eventType.getNumber()]
						.clone()
						.mergeFrom(binarySource)
						.build(),
					binarySource,
					eventType
				);
		} catch (InvalidProtocolBufferException | NullPointerException e) {
			return
				new ParsedProtoRequest<Message>(null, binarySource, eventType);
			// throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_EMPTY_MSG, e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public byte[] convertToInternal( 
		Object payload, MessageHeaders headers ) 
	{
		final ParsedProtoRequest<Message> protoWrapper = 
			(ParsedProtoRequest<Message>) payload;
		return protoWrapper.getBinarySource();
	}
} 