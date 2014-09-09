package com.lvl6.mobsters.application.protobuf;

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

import java.io.FileInputStream;

import com.google.protobuf.Descriptors.Descriptor;
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

public class FromFile02
{
	public static final int REPEAT_COUNT = 150000;
	public static final int WARMUP_COUNT = 5000;

	private static final GeneratedMessage.Builder<?> bldrRegistry[] = new GeneratedMessage.Builder[102];
	private static final Descriptor[] descriptorRegistry = new Descriptor[102];
	private static final Message[] instanceRegistry = new Message[102];

	public static void main(String[] args) throws Exception
	{
		bldrRegistry[C_STARTUP_EVENT_VALUE] =
			StartupRequestProto.newBuilder();
		bldrRegistry[C_IN_APP_PURCHASE_EVENT_VALUE] =
			InAppPurchaseRequestProto.newBuilder();
		bldrRegistry[C_PURCHASE_NORM_STRUCTURE_EVENT_VALUE] =
			PurchaseNormStructureRequestProto.newBuilder();
		bldrRegistry[C_MOVE_OR_ROTATE_NORM_STRUCTURE_EVENT_VALUE] =
			MoveOrRotateNormStructureRequestProto.newBuilder();
		bldrRegistry[C_SET_FACEBOOK_ID_EVENT_VALUE] =
			SetFacebookIdRequestProto.newBuilder();
		bldrRegistry[C_UPGRADE_NORM_STRUCTURE_EVENT_VALUE] =
			UpgradeNormStructureRequestProto.newBuilder();
		bldrRegistry[C_RETRIEVE_CURRENCY_FROM_NORM_STRUCTURE_EVENT_VALUE] =
			RetrieveCurrencyFromNormStructureRequestProto.newBuilder();
		bldrRegistry[C_FINISH_NORM_STRUCT_WAITTIME_WITH_DIAMONDS_EVENT_VALUE] =
			FinishNormStructWaittimeWithDiamondsRequestProto.newBuilder();
		bldrRegistry[C_NORM_STRUCT_WAIT_COMPLETE_EVENT_VALUE] =
			NormStructWaitCompleteRequestProto.newBuilder();
		bldrRegistry[C_LOAD_PLAYER_CITY_EVENT_VALUE] =
			LoadPlayerCityRequestProto.newBuilder();
		bldrRegistry[C_EXCHANGE_GEMS_FOR_RESOURCES_EVENT_VALUE] =
			ExchangeGemsForResourcesRequestProto.newBuilder();
		bldrRegistry[C_QUEST_ACCEPT_EVENT_VALUE] =
			QuestAcceptRequestProto.newBuilder();
		bldrRegistry[C_QUEST_PROGRESS_EVENT_VALUE] =
			QuestProgressRequestProto.newBuilder();
		bldrRegistry[C_QUEST_REDEEM_EVENT_VALUE] =
			QuestRedeemRequestProto.newBuilder();
		bldrRegistry[C_LEVEL_UP_EVENT_VALUE] =
			LevelUpRequestProto.newBuilder();
		bldrRegistry[C_ENABLE_APNS_EVENT_VALUE] =
			EnableAPNSRequestProto.newBuilder();
		bldrRegistry[C_USER_CREATE_EVENT_VALUE] =
			UserCreateRequestProto.newBuilder();
		bldrRegistry[C_SEND_GROUP_CHAT_EVENT_VALUE] =
			SendGroupChatRequestProto.newBuilder();
		bldrRegistry[C_CREATE_CLAN_EVENT_VALUE] =
			CreateClanRequestProto.newBuilder();
		bldrRegistry[C_LEAVE_CLAN_EVENT_VALUE] =
			LeaveClanRequestProto.newBuilder();
		bldrRegistry[C_REQUEST_JOIN_CLAN_EVENT_VALUE] =
			RequestJoinClanRequestProto.newBuilder();
		bldrRegistry[C_RETRACT_REQUEST_JOIN_CLAN_EVENT_VALUE] =
			RetractRequestJoinClanRequestProto.newBuilder();
		bldrRegistry[C_APPROVE_OR_REJECT_REQUEST_TO_JOIN_CLAN_EVENT_VALUE] =
			ApproveOrRejectRequestToJoinClanRequestProto.newBuilder();
		bldrRegistry[C_TRANSFER_CLAN_OWNERSHIP_VALUE] = 
			TransferClanOwnershipRequestProto.newBuilder();
		bldrRegistry[C_RETRIEVE_CLAN_INFO_EVENT_VALUE] =
			RetrieveClanInfoRequestProto.newBuilder();
		bldrRegistry[C_CHANGE_CLAN_SETTINGS_EVENT_VALUE] =
			ChangeClanSettingsRequestProto.newBuilder();
		bldrRegistry[C_BOOT_PLAYER_FROM_CLAN_EVENT_VALUE] =
			BootPlayerFromClanRequestProto.newBuilder();
		bldrRegistry[C_SUBMIT_MONSTER_ENHANCEMENT_EVENT_VALUE] =
			SubmitMonsterEnhancementRequestProto.newBuilder();
		bldrRegistry[C_EVOLVE_MONSTER_EVENT_VALUE] =
			EvolveMonsterRequestProto.newBuilder();
		bldrRegistry[C_PURCHASE_BOOSTER_PACK_EVENT_VALUE] =
			PurchaseBoosterPackRequestProto.newBuilder();
		bldrRegistry[C_EVOLUTION_FINISHED_EVENT_VALUE] =
			EvolutionFinishedRequestProto.newBuilder();
		bldrRegistry[C_ACHIEVEMENT_PROGRESS_EVENT_VALUE] =
			AchievementProgressRequestProto.newBuilder();
		bldrRegistry[C_PRIVATE_CHAT_POST_EVENT_VALUE] =
			PrivateChatPostRequestProto.newBuilder();
		bldrRegistry[C_BEGIN_DUNGEON_EVENT_VALUE] =
			BeginDungeonRequestProto.newBuilder();
		bldrRegistry[C_END_DUNGEON_EVENT_VALUE] =
			EndDungeonRequestProto.newBuilder();
		bldrRegistry[C_REVIVE_IN_DUNGEON_EVENT_VALUE] =
			ReviveInDungeonRequestProto.newBuilder();
		bldrRegistry[C_QUEUE_UP_EVENT_VALUE] =
			QueueUpRequestProto.newBuilder();
		bldrRegistry[C_UPDATE_MONSTER_HEALTH_EVENT_VALUE] =
			UpdateMonsterHealthRequestProto.newBuilder();
		bldrRegistry[C_HEAL_MONSTER_EVENT_VALUE] =
			HealMonsterRequestProto.newBuilder();
		bldrRegistry[C_ACHIEVEMENT_REDEEM_EVENT_VALUE] =
			AchievementRedeemRequestProto.newBuilder();
		bldrRegistry[C_ADD_MONSTER_TO_BATTLE_TEAM_EVENT_VALUE] =
			AddMonsterToBattleTeamRequestProto.newBuilder();
		bldrRegistry[C_REMOVE_MONSTER_FROM_BATTLE_TEAM_EVENT_VALUE] =
			RemoveMonsterFromBattleTeamRequestProto.newBuilder();
		bldrRegistry[C_INCREASE_MONSTER_INVENTORY_SLOT_EVENT_VALUE] =
			IncreaseMonsterInventorySlotRequestProto.newBuilder();
		bldrRegistry[C_ENHANCEMENT_WAIT_TIME_COMPLETE_EVENT_VALUE] =
			EnhancementWaitTimeCompleteRequestProto.newBuilder();
		bldrRegistry[C_COMBINE_USER_MONSTER_PIECES_EVENT_VALUE] =
			CombineUserMonsterPiecesRequestProto.newBuilder();
		bldrRegistry[C_SELL_USER_MONSTER_EVENT_VALUE] =
			SellUserMonsterRequestProto.newBuilder();
		bldrRegistry[C_INVITE_FB_FRIENDS_FOR_SLOTS_EVENT_VALUE] =
			InviteFbFriendsForSlotsRequestProto.newBuilder();
		bldrRegistry[C_ACCEPT_AND_REJECT_FB_INVITE_FOR_SLOTS_EVENT_VALUE] =
			AcceptAndRejectFbInviteForSlotsRequestProto.newBuilder();
		bldrRegistry[C_UPDATE_USER_CURRENCY_EVENT_VALUE] =
			UpdateUserCurrencyRequestProto.newBuilder();
		bldrRegistry[C_BEGIN_PVP_BATTLE_EVENT_VALUE] =
			BeginPvpBattleRequestProto.newBuilder();
		bldrRegistry[C_END_PVP_BATTLE_EVENT_VALUE] =
			EndPvpBattleRequestProto.newBuilder();
		bldrRegistry[C_BEGIN_CLAN_RAID_EVENT_VALUE] =
			BeginClanRaidRequestProto.newBuilder();
		bldrRegistry[C_ATTACK_CLAN_RAID_MONSTER_EVENT_VALUE] =
			AttackClanRaidMonsterRequestProto.newBuilder();
		bldrRegistry[C_RECORD_CLAN_RAID_STATS_EVENT_VALUE] =
			RecordClanRaidStatsRequestProto.newBuilder();
		bldrRegistry[C_PROMOTE_DEMOTE_CLAN_MEMBER_EVENT_VALUE] =
			PromoteDemoteClanMemberRequestProto.newBuilder();
		bldrRegistry[C_SET_GAME_CENTER_ID_EVENT_VALUE] =
			SetGameCenterIdRequestProto.newBuilder();
		bldrRegistry[C_SPAWN_OBSTACLE_EVENT_VALUE] =
			SpawnObstacleRequestProto.newBuilder();
		bldrRegistry[C_BEGIN_OBSTACLE_REMOVAL_EVENT_VALUE] =
			BeginObstacleRemovalRequestProto.newBuilder();
		bldrRegistry[C_OBSTACLE_REMOVAL_COMPLETE_EVENT_VALUE] =
			ObstacleRemovalCompleteRequestProto.newBuilder();
		bldrRegistry[C_SPAWN_MINI_JOB_EVENT_VALUE] =
			SpawnMiniJobRequestProto.newBuilder();
		bldrRegistry[C_BEGIN_MINI_JOB_EVENT_VALUE] =
			BeginMiniJobRequestProto.newBuilder();
		bldrRegistry[C_COMPLETE_MINI_JOB_EVENT_VALUE] =
			CompleteMiniJobRequestProto.newBuilder();
		bldrRegistry[C_REDEEM_MINI_JOB_EVENT_VALUE] =
			RedeemMiniJobRequestProto.newBuilder();
		bldrRegistry[C_SET_AVATAR_MONSTER_EVENT_VALUE] =
			SetAvatarMonsterRequestProto.newBuilder();
		bldrRegistry[C_RESTRICT_USER_MONSTER_EVENT_VALUE] =
			RestrictUserMonsterRequestProto.newBuilder();
		bldrRegistry[C_LOGOUT_EVENT_VALUE] =
			LogoutRequestProto.newBuilder();

		for( int ii=0; ii<101; ii++) {
			GeneratedMessage.Builder<?> nextBldr = bldrRegistry[ii];
			if (nextBldr != null) {
				descriptorRegistry[ii] = nextBldr.getDescriptorForType();
				instanceRegistry[ii] = nextBldr.getDefaultInstanceForType();
			}
		}

		byte[] buf = new byte[73];
		FileInputStream input = new FileInputStream("output.dat");
		input.read(buf, 0, buf.length);
		input.close();
		
		doWarmup(buf);
		doMeasurement(buf);
	}
	
	public final static Message.Builder getFromBuilder(int builderId) {
		return bldrRegistry[builderId].clone();
	}

	public final static Message.Builder getFromInstance(int builderId) {
		return instanceRegistry[builderId].newBuilderForType();
	}
	
	public static void doWarmup(byte[] buf) throws InvalidProtocolBufferException {
		doBuilderTest(buf, WARMUP_COUNT);
		doInstanceTest(buf, WARMUP_COUNT);
	}
	
	public static void doMeasurement(byte[] buf) throws InvalidProtocolBufferException {
		doBuilderTest(buf, REPEAT_COUNT);
		doInstanceTest(buf, REPEAT_COUNT);
	}

	public static Message doBuilderTest(
		final byte[] buf, final int repeatCount
	) throws InvalidProtocolBufferException
	{
		Message protoTest = null;

		for (int ii = 0; ii < repeatCount; ii++) {
			Message.Builder bldr = 
				getFromBuilder(C_BEGIN_DUNGEON_EVENT_VALUE);
			protoTest = bldr.mergeFrom(buf).build();
		}

		return protoTest;
	}
	
	public static Message doInstanceTest(
		final byte[] buf, final int repeatCount
	) throws InvalidProtocolBufferException
	{
		Message protoTest = null;

		for (int ii = 0; ii < repeatCount; ii++) {
			Message.Builder bldr = 
				getFromInstance(C_BEGIN_DUNGEON_EVENT_VALUE);
			protoTest = bldr.mergeFrom(buf).build();
		}

		return protoTest;
	}
}