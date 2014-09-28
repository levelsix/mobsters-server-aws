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
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementProgressRequestProto;
import com.lvl6.mobsters.eventproto.EventAchievementProto.AchievementRedeemRequestProto;
import com.lvl6.mobsters.eventproto.EventApnsProto.EnableAPNSRequestProto;
import com.lvl6.mobsters.eventproto.EventBoosterPackProto.PurchaseBoosterPackRequestProto;
import com.lvl6.mobsters.eventproto.EventChatProto.PrivateChatPostRequestProto;
import com.lvl6.mobsters.eventproto.EventChatProto.SendGroupChatRequestProto;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.ApproveOrRejectRequestToJoinClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.AttackClanRaidMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.BeginClanRaidRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.BootPlayerFromClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.CreateClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.LeaveClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.PromoteDemoteClanMemberRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RecordClanRaidStatsRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RequestJoinClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RetractRequestJoinClanRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.TransferClanOwnershipRequestProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.BeginDungeonRequestProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.EndDungeonRequestProto;
import com.lvl6.mobsters.eventproto.EventDungeonProto.ReviveInDungeonRequestProto;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.ExchangeGemsForResourcesRequestProto;
import com.lvl6.mobsters.eventproto.EventInAppPurchaseProto.InAppPurchaseRequestProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.BeginMiniJobRequestProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.CompleteMiniJobRequestProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.RedeemMiniJobRequestProto;
import com.lvl6.mobsters.eventproto.EventMiniJobProto.SpawnMiniJobRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AcceptAndRejectFbInviteForSlotsRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.AddMonsterToBattleTeamRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.CombineUserMonsterPiecesRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EnhancementWaitTimeCompleteRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolutionFinishedRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.EvolveMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.HealMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.IncreaseMonsterInventorySlotRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.InviteFbFriendsForSlotsRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RemoveMonsterFromBattleTeamRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.RestrictUserMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SellUserMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.SubmitMonsterEnhancementRequestProto;
import com.lvl6.mobsters.eventproto.EventMonsterProto.UpdateMonsterHealthRequestProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.BeginPvpBattleRequestProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.EndPvpBattleRequestProto;
import com.lvl6.mobsters.eventproto.EventPvpProto.QueueUpRequestProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestAcceptRequestProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestProgressRequestProto;
import com.lvl6.mobsters.eventproto.EventQuestProto.QuestRedeemRequestProto;
import com.lvl6.mobsters.eventproto.EventStartupProto.StartupRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.BeginObstacleRemovalRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.FinishNormStructWaittimeWithDiamondsRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.MoveOrRotateNormStructureRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.NormStructWaitCompleteRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.ObstacleRemovalCompleteRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.PurchaseNormStructureRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.RetrieveCurrencyFromNormStructureRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.SpawnObstacleRequestProto;
import com.lvl6.mobsters.eventproto.EventStructureProto.UpgradeNormStructureRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.LevelUpRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.LogoutRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetAvatarMonsterRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetFacebookIdRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.SetGameCenterIdRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UpdateUserCurrencyRequestProto;
import com.lvl6.mobsters.eventproto.EventUserProto.UserCreateRequestProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersException;
import com.lvl6.mobsters.utility.exception.Lvl6MobstersStatusCode;
import com.lvl6.mobsters.websockets.MobstersHeaderAccessor;

/**
 * This class has two conversion responsibilities
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
public class RequestProtobufConverter extends AbstractMessageConverter {
	
	static final MimeType PROTO_BUF_MIME_TYPE = new MimeType( "application", "protobuf" );

	private final GeneratedMessage.Builder<?>[] mobstersEventType;

	public RequestProtobufConverter() {
		super(PROTO_BUF_MIME_TYPE);

		mobstersEventType = new GeneratedMessage.Builder<?>[102];

		mobstersEventType[C_STARTUP_EVENT_VALUE] =
				StartupRequestProto.newBuilder();
		mobstersEventType[C_IN_APP_PURCHASE_EVENT_VALUE] =
				InAppPurchaseRequestProto.newBuilder();
		mobstersEventType[C_PURCHASE_NORM_STRUCTURE_EVENT_VALUE] =
				PurchaseNormStructureRequestProto.newBuilder();
		mobstersEventType[C_MOVE_OR_ROTATE_NORM_STRUCTURE_EVENT_VALUE] =
				MoveOrRotateNormStructureRequestProto.newBuilder();
		mobstersEventType[C_SET_FACEBOOK_ID_EVENT_VALUE] =
				SetFacebookIdRequestProto.newBuilder();
		mobstersEventType[C_UPGRADE_NORM_STRUCTURE_EVENT_VALUE] =
				UpgradeNormStructureRequestProto.newBuilder();
		mobstersEventType[C_RETRIEVE_CURRENCY_FROM_NORM_STRUCTURE_EVENT_VALUE] =
				RetrieveCurrencyFromNormStructureRequestProto.newBuilder();
		mobstersEventType[C_FINISH_NORM_STRUCT_WAITTIME_WITH_DIAMONDS_EVENT_VALUE] =
				FinishNormStructWaittimeWithDiamondsRequestProto.newBuilder();
		mobstersEventType[C_NORM_STRUCT_WAIT_COMPLETE_EVENT_VALUE] =
				NormStructWaitCompleteRequestProto.newBuilder();
		mobstersEventType[C_LOAD_PLAYER_CITY_EVENT_VALUE] =
				LoadPlayerCityRequestProto.newBuilder();
		mobstersEventType[C_EXCHANGE_GEMS_FOR_RESOURCES_EVENT_VALUE] =
				ExchangeGemsForResourcesRequestProto.newBuilder();
		mobstersEventType[C_QUEST_ACCEPT_EVENT_VALUE] =
				QuestAcceptRequestProto.newBuilder();
		mobstersEventType[C_QUEST_PROGRESS_EVENT_VALUE] =
				QuestProgressRequestProto.newBuilder();
		mobstersEventType[C_QUEST_REDEEM_EVENT_VALUE] =
				QuestRedeemRequestProto.newBuilder();
		mobstersEventType[C_LEVEL_UP_EVENT_VALUE] =
				LevelUpRequestProto.newBuilder();
		mobstersEventType[C_ENABLE_APNS_EVENT_VALUE] =
				EnableAPNSRequestProto.newBuilder();
		mobstersEventType[C_USER_CREATE_EVENT_VALUE] =
				UserCreateRequestProto.newBuilder();
		mobstersEventType[C_SEND_GROUP_CHAT_EVENT_VALUE] =
				SendGroupChatRequestProto.newBuilder();
		mobstersEventType[C_CREATE_CLAN_EVENT_VALUE] =
				CreateClanRequestProto.newBuilder();
		mobstersEventType[C_LEAVE_CLAN_EVENT_VALUE] =
				LeaveClanRequestProto.newBuilder();
		mobstersEventType[C_REQUEST_JOIN_CLAN_EVENT_VALUE] =
				RequestJoinClanRequestProto.newBuilder();
		mobstersEventType[C_RETRACT_REQUEST_JOIN_CLAN_EVENT_VALUE] =
				RetractRequestJoinClanRequestProto.newBuilder();
		mobstersEventType[C_APPROVE_OR_REJECT_REQUEST_TO_JOIN_CLAN_EVENT_VALUE] =
				ApproveOrRejectRequestToJoinClanRequestProto.newBuilder();
		mobstersEventType[C_TRANSFER_CLAN_OWNERSHIP_VALUE] = 
				TransferClanOwnershipRequestProto.newBuilder();
		mobstersEventType[C_RETRIEVE_CLAN_INFO_EVENT_VALUE] =
				RetrieveClanInfoRequestProto.newBuilder();
		mobstersEventType[C_CHANGE_CLAN_SETTINGS_EVENT_VALUE] =
				ChangeClanSettingsRequestProto.newBuilder();
		mobstersEventType[C_BOOT_PLAYER_FROM_CLAN_EVENT_VALUE] =
				BootPlayerFromClanRequestProto.newBuilder();
		mobstersEventType[C_SUBMIT_MONSTER_ENHANCEMENT_EVENT_VALUE] =
				SubmitMonsterEnhancementRequestProto.newBuilder();
		mobstersEventType[C_EVOLVE_MONSTER_EVENT_VALUE] =
				EvolveMonsterRequestProto.newBuilder();
		mobstersEventType[C_PURCHASE_BOOSTER_PACK_EVENT_VALUE] =
				PurchaseBoosterPackRequestProto.newBuilder();
		mobstersEventType[C_EVOLUTION_FINISHED_EVENT_VALUE] =
				EvolutionFinishedRequestProto.newBuilder();
		mobstersEventType[C_ACHIEVEMENT_PROGRESS_EVENT_VALUE] =
				AchievementProgressRequestProto.newBuilder();
		mobstersEventType[C_PRIVATE_CHAT_POST_EVENT_VALUE] =
				PrivateChatPostRequestProto.newBuilder();
		mobstersEventType[C_BEGIN_DUNGEON_EVENT_VALUE] =
				BeginDungeonRequestProto.newBuilder();
		mobstersEventType[C_END_DUNGEON_EVENT_VALUE] =
				EndDungeonRequestProto.newBuilder();
		mobstersEventType[C_REVIVE_IN_DUNGEON_EVENT_VALUE] =
				ReviveInDungeonRequestProto.newBuilder();
		mobstersEventType[C_QUEUE_UP_EVENT_VALUE] =
				QueueUpRequestProto.newBuilder();
		mobstersEventType[C_UPDATE_MONSTER_HEALTH_EVENT_VALUE] =
				UpdateMonsterHealthRequestProto.newBuilder();
		mobstersEventType[C_HEAL_MONSTER_EVENT_VALUE] =
				HealMonsterRequestProto.newBuilder();
		mobstersEventType[C_ACHIEVEMENT_REDEEM_EVENT_VALUE] =
				AchievementRedeemRequestProto.newBuilder();
		mobstersEventType[C_ADD_MONSTER_TO_BATTLE_TEAM_EVENT_VALUE] =
				AddMonsterToBattleTeamRequestProto.newBuilder();
		mobstersEventType[C_REMOVE_MONSTER_FROM_BATTLE_TEAM_EVENT_VALUE] =
				RemoveMonsterFromBattleTeamRequestProto.newBuilder();
		mobstersEventType[C_INCREASE_MONSTER_INVENTORY_SLOT_EVENT_VALUE] =
				IncreaseMonsterInventorySlotRequestProto.newBuilder();
		mobstersEventType[C_ENHANCEMENT_WAIT_TIME_COMPLETE_EVENT_VALUE] =
				EnhancementWaitTimeCompleteRequestProto.newBuilder();
		mobstersEventType[C_COMBINE_USER_MONSTER_PIECES_EVENT_VALUE] =
				CombineUserMonsterPiecesRequestProto.newBuilder();
		mobstersEventType[C_SELL_USER_MONSTER_EVENT_VALUE] =
				SellUserMonsterRequestProto.newBuilder();
		mobstersEventType[C_INVITE_FB_FRIENDS_FOR_SLOTS_EVENT_VALUE] =
				InviteFbFriendsForSlotsRequestProto.newBuilder();
		mobstersEventType[C_ACCEPT_AND_REJECT_FB_INVITE_FOR_SLOTS_EVENT_VALUE] =
				AcceptAndRejectFbInviteForSlotsRequestProto.newBuilder();
		mobstersEventType[C_UPDATE_USER_CURRENCY_EVENT_VALUE] =
				UpdateUserCurrencyRequestProto.newBuilder();
		mobstersEventType[C_BEGIN_PVP_BATTLE_EVENT_VALUE] =
				BeginPvpBattleRequestProto.newBuilder();
		mobstersEventType[C_END_PVP_BATTLE_EVENT_VALUE] =
				EndPvpBattleRequestProto.newBuilder();
		mobstersEventType[C_BEGIN_CLAN_RAID_EVENT_VALUE] =
				BeginClanRaidRequestProto.newBuilder();
		mobstersEventType[C_ATTACK_CLAN_RAID_MONSTER_EVENT_VALUE] =
				AttackClanRaidMonsterRequestProto.newBuilder();
		mobstersEventType[C_RECORD_CLAN_RAID_STATS_EVENT_VALUE] =
				RecordClanRaidStatsRequestProto.newBuilder();
		mobstersEventType[C_PROMOTE_DEMOTE_CLAN_MEMBER_EVENT_VALUE] =
				PromoteDemoteClanMemberRequestProto.newBuilder();
		mobstersEventType[C_SET_GAME_CENTER_ID_EVENT_VALUE] =
				SetGameCenterIdRequestProto.newBuilder();
		mobstersEventType[C_SPAWN_OBSTACLE_EVENT_VALUE] =
				SpawnObstacleRequestProto.newBuilder();
		mobstersEventType[C_BEGIN_OBSTACLE_REMOVAL_EVENT_VALUE] =
				BeginObstacleRemovalRequestProto.newBuilder();
		mobstersEventType[C_OBSTACLE_REMOVAL_COMPLETE_EVENT_VALUE] =
				ObstacleRemovalCompleteRequestProto.newBuilder();
		mobstersEventType[C_SPAWN_MINI_JOB_EVENT_VALUE] =
				SpawnMiniJobRequestProto.newBuilder();
		mobstersEventType[C_BEGIN_MINI_JOB_EVENT_VALUE] =
				BeginMiniJobRequestProto.newBuilder();
		mobstersEventType[C_COMPLETE_MINI_JOB_EVENT_VALUE] =
				CompleteMiniJobRequestProto.newBuilder();
		mobstersEventType[C_REDEEM_MINI_JOB_EVENT_VALUE] =
				RedeemMiniJobRequestProto.newBuilder();
		mobstersEventType[C_SET_AVATAR_MONSTER_EVENT_VALUE] =
				SetAvatarMonsterRequestProto.newBuilder();
		mobstersEventType[C_RESTRICT_USER_MONSTER_EVENT_VALUE] =
				RestrictUserMonsterRequestProto.newBuilder();
		mobstersEventType[C_LOGOUT_EVENT_VALUE] =
				LogoutRequestProto.newBuilder();

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