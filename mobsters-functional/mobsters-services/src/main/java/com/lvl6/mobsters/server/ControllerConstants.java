package com.lvl6.mobsters.server;

import com.lvl6.mobsters.info.AnimatedSpriteOffset;
import com.lvl6.properties.Globals;

public class ControllerConstants
{

	// MOBSTERS CONSTANTS

	// includes oil and cash, 1 gem per 1000 resource?
	public static final float GEMS_PER_RESOURCE = 0.001F;

	public static final float GEMS_PER_DOLLAR = 10f;// client doesn't need this

	public static final float MINUTES_PER_GEM = 10f;

	// this multiplies with the cost to heal all monsters on user's battle team
	// BATTLE, DUNGEON, TASK
	public static final float BATTLE__CONTINUE_GEM_COST_MULTIPLIER = 1.2F;

	public static final float BATTLE__RUN_AWAY_BASE_PERCENT = 0.5F;

	public static final float BATTLE__RUN_AWAY_INCREMENT = 0.25F;

	// clan
	public static final int CLAN__MAX_NUM_MEMBERS = 3;

	public static final int CREATE_CLAN__COIN_PRICE_TO_CREATE_CLAN = 1000; // TODO: FIGURE OUT IF STILL
																			// NEEDED

	public static final int CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_NAME = 15;

	public static final int CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_DESCRIPTION = 350;

	public static final int CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_TAG = 3;

	// CLAN EVENT PERSISTENT
	public static final int CLAN_EVENT_PERSISTENT__NUM_DAYS_FOR_RAID_HISTORY = 14;

	public static final int CLAN_EVENT_PERSISTENT__NUM_DAYS_FOR_RAID_STAGE_HISTORY = 7;

	// EVENT PERSISTENT STUFF
	public static final int EVENT_PERSISTENT__END_COOL_DOWN_TIMER_GEM_COST = 5;

	public static final float MONSTER__CASH_PER_HEALTH_POINT = 0.5f;

	public static final float MONSTER__SECONDS_TO_HEAL_PER_HEALTH_POINT = 2f;

	public static final float MONSTER__ELEMENTAL_STRENGTH = 1.2F;

	public static final float MONSTER__ELEMENTAL_WEAKNESS = 0.8F;

	public static final float MONSTER__OIL_PER_MONSTER_LEVEL = 100F;

	// public static final int MONSTER_INVENTORY_SLOTS__INCREMENT_AMOUNT = 5;
	// public static final int MONSTER_INVENTORY_SLOTS__GEM_PRICE_PER_SLOT = 2;
	// public static final int MONSTER_INVENTORY_SLOTS__MIN_INVITES_TO_INCREASE_SLOTS = 3;

	// MFUSOP = monster_for_user_source_of_pieces
	public static final String MFUSOP__BOOSTER_PACK = "M boosterPackId";

	public static final String MFUSOP__END_DUNGEON = "M Task4UserId";

	public static final String MFUSOP__MINI_JOB = "M miniJobId";

	public static final String MFUSOP__QUEST = "M questId";

	public static final String MFUSOP__USER_CREATE = "M user create";

	// MFUDR = monster_for_user_delete_reasons
	public static final String MFUDR__ENHANCING = "D enhancing";

	public static final String MFUDR__QUEST = "D quest";

	public static final String MFUDR__SELL = "D sold";

	// MONSTER FOR USER
	public static final int MONSTER_FOR_USER__MAX_TEAM_SIZE = 3;

	public static final int MONSTER_FOR_USER__INITIAL_MAX_NUM_MONSTER_LIMIT = 10;

	// MINI TUTORIAL CONSTANTS
	public static final int MINI_TUTORIAL__GUARANTEED_MONSTER_DROP_TASK_ID = 4;

	// OBSTACLE CONSTANTS
	public static final int OBSTACLE__MAX_OBSTACLES = 15;

	public static final int OBSTACLE__MINUTES_PER_OBSTACLE = 5;

	// PVP
	// user lvl means nothing, since it doesn't indicate much besides maybe how much you played
	public static final int PVP__REQUIRED_MIN_LEVEL = 30;

	public static final int PVP__MAX_QUEUE_SIZE = 10;

	public static final int PVP__FAKE_USER_LVL_DIVISOR = 50;

	public static final long PVP__MAX_BATTLE_DURATION_MILLIS = 3600000L; // one hour

	// USED TO CREATE AN ELO RANGE FROM WHICH TO SELECT AN OPPONENT
	public static final int PVP__ELO_RANGE_SUBTRAHEND = 100;

	public static final int PVP__ELO_RANGE_ADDEND = 100;

	// NOT USING ANYMORE--------------------------------------------------------------------
	// all these pairing chances need to sum to one
	public static final float PVP__ELO_CATEGORY_ONE_PAIRING_CHANCE = 0.05F;

	public static final float PVP__ELO_CATEGORY_TWO_PAIRING_CHANCE = 0.15F;

	public static final float PVP__ELO_CATEGORY_THREE_PAIRING_CHANCE = 0.20F;

	public static final float PVP__ELO_CATEGORY_FOUR_PAIRING_CHANCE = 0.20F;

	public static final float PVP__ELO_CATEGORY_FIVE_PAIRING_CHANCE = 0.25F;

	public static final float PVP__ELO_CATEGORY_SIX_PAIRING_CHANCE = 0.15F;

	public static final int PVP__ELO_DISTANCE_ONE = 100;

	public static final int PVP__ELO_DISTANCE_TWO = 200;

	public static final int PVP__ELO_DISTANCE_THREE = 300;

	public static final int PVP__NUM_ENEMIES_LIMIT = 100;

	// -------------------------------------------------------------------------------------
	public static final float PVP__PERCENT_CASH_LOST = 0.25F;

	public static final float PVP__PERCENT_OIL_LOST = 0.25F;

	public static final int PVP__SHIELD_DURATION_DAYS = 3;

	public static final int PVP__LOST_BATTLE_SHIELD_DURATION_HOURS = 12;

	public static final int PVP__INITIAL_LEAGUE_ID = 1;

	public static final int PVP__INITIAL_ELO = 0;

	// PVP BATTLE HISTORY
	public static final int PVP_HISTORY__NUM_RECENT_BATTLES = 10;

	// chats
	public static final int RETRIEVE_PLAYER_WALL_POSTS__NUM_POSTS_CAP = 150;

	// STARTUP
	public static final AnimatedSpriteOffset[] STARTUP__ANIMATED_SPRITE_OFFSETS = {

	};

	// STRUCTURE FOR USER STUFF
	public static final int STRUCTURE_FOR_USER__TOWN_HALL_ID = 120;

	public static final int STRUCTURE_FOR_USER__CASH_STORAGE_ID = 20;

	public static final int STRUCTURE_FOR_USER__OIL_STORAGE_ID = 60;

	public static final float STRUCTURE_FOR_USER__TOWN_HALL_X_COORD = 10F;

	public static final float STRUCTURE_FOR_USER__TOWN_HALL_Y_COORD = 10F;

	public static final float STRUCTURE_FOR_USER__CASH_STORAGE_X_COORD = 13F;

	public static final float STRUCTURE_FOR_USER__CASH_STORAGE_Y_COORD = 10F;

	public static final float STRUCTURE_FOR_USER__OIL_STORAGE_X_COORD = 10F;

	public static final float STRUCTURE_FOR_USER__OIL_STORAGE_Y_COORD = 13F;

	// TASK MAP
	public static final String TASK_MAP__SECTION_IMAGE_PREFIX = "mapsection";

	public static final int TASK_MAP__NUMBER_OF_SECTIONS = 5;

	public static final float TASK_MAP__SECTION_HEIGHT = 328;

	public static final float TASK_MAP__TOTAL_WIDTH = 328;

	public static final float TASK_MAP__TOTAL_HEIGHT = 1406;

	// TUTORIAL CONSTANTS
	// MONSTER IDS 1 AND 3
	public static final int TUTORIAL__STARTING_MONSTER_ID = 2011;

	public static final int TUTORIAL__GUIDE_MONSTER_ID = 1000;

	public static final int TUTORIAL__ENEMY_MONSTER_ID_ONE = 2010;

	public static final int TUTORIAL__ENEMY_MONSTER_ID_TWO = 1003;

	public static final int TUTORIAL__ENEMY_BOSS_MONSTER_ID = 1002;

	public static final int TUTORIAL__MARK_Z_MONSTER_ID = 2005;

	// everything at index i goes together
	public static final int[] TUTORIAL__EXISTING_BUILDING_IDS = { 40, 80, 120, 140, 170, 180 };

	public static final float[] TUTORIAL__EXISTING_BUILDING_X_POS = { 17F, 10F, 11F, 15F, 12F,
		5F };

	public static final float[] TUTORIAL__EXISTING_BUILDING_Y_POS = { 11F, 15F, 9F, 15F, -5F,
		11F };

	public static final Integer[] TUTORIAL__STRUCTURE_IDS_TO_BUILD = { 1, 20, 60 };

	public static final int TUTORIAL__CITY_ONE_ID = 1;

	public static final int TUTORIAL__CITY_ONE_ASSET_NUM_FOR_FIRST_DUNGEON = 5;

	public static final int TUTORIAL__CITY_ONE_ASSET_NUM_FOR_SECOND_DUNGEON = 6;

	public static final int TUTORIAL__INIT_CASH = 750;

	public static final int TUTORIAL__INIT_OIL = 750;

	public static final int TUTORIAL__INIT_GEMS = 50;

	public static final int TUTORIAL__INIT_RANK = 100;

	public static final int[] TUTORIAL__INIT_OBSTACLE_ID = { 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4,
		4, 4, 5, 5, 5, 5, 5 };

	public static final int[] TUTORIAL__INIT_OBSTACLE_X = { 4, 2, 1, 17, 21, 12, 4, 5, 20, 15,
		22, 2, 10, 4, 9, 21, 22, 18 };

	public static final int[] TUTORIAL__INIT_OBSTACLE_Y = { 8, 2, 20, 3, 14, 20, 4, 19, 5, 22,
		21, 13, 2, 15, 21, 2, 9, 20 };

	// USER CURRENCY HISTORY REASON FOR CHANGE VALUES
	public static final String UCHRFC__ACHIEVEMENT_REDEEM = "achievement redeemed";

	public static final String UCHRFC__CREATE_CLAN = "created clan";

	public static final String UCHRFC__CURRENCY_EXCHANGE = "currency exchange";

	public static final String UCHRFC__EARN_FREE_DIAMONDS_FB_CONNECT = "connecting to facebook";

	public static final String UCHRFC__END_PERSISTENT_EVENT_COOLDOWN =
		"ended persistent event cooldown";

	public static final String UCHRFC__END_TASK = "end task";

	public static final String UCHRFC__ENHANCING = "enhancing user monsters";

	public static final String UCHRFC__EVOLVING = "evolving user monsters";

	public static final String UCHRFC__EXPANSION_WAIT_COMPLETE = "expansion wait complete: ";

	public static final String UCHRFC__HEAL_MONSTER_OR_SPED_UP_HEALING =
		"healing or sped up healing user monsters";

	public static final String UCHRFC__INCREASE_MONSTER_INVENTORY =
		"increased user monster inventory";

	public static final String UCHRFC__PURCHASE_NORM_STRUCT = "purchased norm struct";

	public static final String UCHRFC__PURHCASED_BOOSTER_PACK = "purchased booster pack";

	public static final String UCHRFC__PURCHASE_CITY_EXPANSION = "expanded city";

	public static final String UCHRFC__PVP_BATTLE = "pvp battle";

	public static final String UCHRFC__QUEST_REDEEM = "quest redeemed";

	public static final String UCHRFC__REMOVE_OBSTACLE = "remove obstacle";

	public static final String UCHRFC__REVIVE_IN_DUNGEON = "revive in dungeon";

	public static final String UCHRFC__SOLD_USER_MONSTERS = "sold user monsters";

	public static final String UCHRFC__SPED_UP_COMBINING_MONSTER =
		"sped up combining user monster";

	public static final String UCHRFC__SPED_UP_COMPLETE_MINI_JOB = "sped up complete mini job";

	public static final String UCHRFC__SPED_UP_ENHANCING = "sped up enhancing user monster";

	public static final String UCHRFC__SPED_UP_EVOLUTION = "sped up evolving user monster";

	public static final String UCHRFC__SPED_UP_NORM_STRUCT = "sped up norm stuct";

	public static final String UCHRFC__SPED_UP_REMOVE_OBSTACLE = "sped up remove obstacle";

	public static final String UCHRFC__UPGRADE_NORM_STRUCT = "upgrading norm struct";

	// USER
	public static final int USER__LEVEL_TO_DISPLAY_RATE_US_POPUP = 8;

	public static final int USER__MAX_LEVEL = 100; // add level up equipment for fake players if
													// increasing

	// old aoc constants
	public static final int NOT_SET = -1;

	// --------------------------------------------------------------------------------------------------------------------------

	// FORMULA CONSTANTS (ALSO) SENT TO CLIENT
	public static final double BATTLE_WEIGHT_GIVEN_TO_ATTACK_STAT = 1;

	public static final double BATTLE_WEIGHT_GIVEN_TO_ATTACK_EQUIP_SUM = 1;

	public static final double BATTLE_WEIGHT_GIVEN_TO_DEFENSE_STAT = 1;

	public static final double BATTLE_WEIGHT_GIVEN_TO_DEFENSE_EQUIP_SUM = 1;

	public static final double BATTLE_WEIGHT_GIVEN_TO_LEVEL = 1;

	public static final float BATTLE_LOCATION_BAR_MAX = 83.33f;

	public static final float BATTLE_PERFECT_PERCENT_THRESHOLD = 3.0f;

	public static final float BATTLE_GREAT_PERCENT_THRESHOLD = 17.0f;

	public static final float BATTLE_GOOD_PERCENT_THRESHOLD = 38.0f;

	public static final float BATTLE_PERFECT_MULTIPLIER = 2.0f;

	public static final float BATTLE_GREAT_MULTIPLIER = 1.5f;

	public static final float BATTLE_GOOD_MULTIPLIER = 1.0f;

	public static final float BATTLE_IMBALANCE_PERCENT = .67f;

	public static final float BATTLE_PERFECT_LIKELIHOOD = .25f;

	public static final float BATTLE_GREAT_LIKELIHOOD = .55f;

	public static final float BATTLE_GOOD_LIKELIHOOD = .15f;

	public static final float BATTLE_MISS_LIKELIHOOD = .05f;

	public static final double BATTLE__HIT_ATTACKER_PERCENT_OF_HEALTH = 0.2;

	public static final double BATTLE__HIT_DEFENDER_PERCENT_OF_HEALTH = 0.25;

	public static final double BATTLE__PERCENT_OF_WEAPON = 1.0 / 9.0;

	public static final double BATTLE__PERCENT_OF_ARMOR = 1.0 / 9.0;

	public static final double BATTLE__PERCENT_OF_AMULET = 1.0 / 9.0;

	public static final double BATTLE__PERCENT_OF_PLAYER_STATS = 3.0 / 9.0;

	public static final double BATTLE__ATTACK_EXPO_MULTIPLIER = 0.8;

	public static final double BATTLE__PERCENT_OF_EQUIPMENT = 3.0 / 9.0;

	public static final double BATTLE__INDIVIDUAL_EQUIP_ATTACK_CAP = 5.0;

	public static final double BATTLE__FAKE_PLAYER_COIN_GAIN_MULTIPLIER = 3;

	public static final double BATTLE__CHANCE_OF_ZERO_GAIN_FOR_SILVER = .2;

	public static final int BATTLE__ELO_DISTANCE_ONE = 100;

	public static final int BATTLE__ELO_DISTANCE_TWO = 200;

	public static final int BATTLE__ELO_DISTANCE_THREE = 300;

	public static final int BATTLE__ELO_USER_LIMIT_ONE = 68;

	public static final int BATTLE__ELO_USER_LIMIT_TWO = 13;

	public static final int BATTLE__ELO_USER_LIMIT_THREE = 3;

	public static final int BATTLE__LAST_VIEWED_TIME_MILLIS_ADDEND = 600000; // 10 MINUTES

	// --------------------------------------------------------------------------------------------------------------------------

	// TUTORIAL CONSTANTS
	public static final double CHARACTERS_ATTACK_DEFENSE_VARIABILITY = 0.67;

	public static final int TUTORIAL__INIT_COINS = 50;

	public static final int TUTORIAL__DIAMOND_COST_TO_INSTABUILD_FIRST_STRUCT = 2; // Because it does not
																					// warn the user

	public static final String TUTORIAL__FAKE_QUEST_GOOD_NAME = "Preserve the Peace";

	public static final String TUTORIAL__FAKE_QUEST_BAD_NAME = "Witness Protection";

	public static final String TUTORIAL__FAKE_QUEST_GOOD_ACCEPT_DIALOGUE = "10~good~";

	public static final String TUTORIAL__FAKE_QUEST_BAD_ACCEPT_DIALOGUE = "10~bad~";

	public static final String TUTORIAL__FAKE_QUEST_GOOD_DESCRIPTION =
		"Soldier, we are in dire times and we need your help.";

	public static final String TUTORIAL__FAKE_QUEST_BAD_DESCRIPTION =
		"Soldier, we are in dire times and we need your help.";

	public static final String TUTORIAL__FAKE_QUEST_GOOD_DONE_RESPONSE =
		"Simply amazing! Your battle prowess makes our village seem safer already. ";

	public static final String TUTORIAL__FAKE_QUEST_BAD_DONE_RESPONSE =
		"Excellent work soldier. Good to know I have a competent ally watching my back.";

	public static final int TUTORIAL__FIRST_TASK_ID = 1;

	// in development select any task, doesn't matter for now
	public static final int TUTORIAL__FAKE_QUEST_TASK_ID = Globals.IS_SANDBOX() ? 1 : 168;

	public static final int TUTORIAL__FAKE_QUEST_ASSET_NUM_WITHIN_CITY = 0;

	public static final int TUTORIAL__FAKE_QUEST_COINS_GAINED = 8;

	public static final int TUTORIAL__FAKE_QUEST_EXP_GAINED = 4;

	public static final int TUTORIAL__FAKE_QUEST_AMULET_LOOT_EQUIP_ID = 5;

	public static final int TUTORIAL__FIRST_BATTLE_COIN_GAIN = 5;

	public static final int TUTORIAL__FIRST_BATTLE_EXP_GAIN = 1;

	public static final int TUTORIAL__FIRST_STRUCT_TO_BUILD = 1;

	public static final int TUTORIAL__FIRST_NEUTRAL_CITY_ID = 1;

	public static final int TUTORIAL__COST_TO_SPEED_UP_FORGE = 2;

	// STARTUP
	public static final int STARTUP__MAX_NUM_OF_STARTUP_NOTIFICATION_TYPE_TO_SEND = 20;

	public static final int STARTUP__HOURS_OF_BATTLE_NOTIFICATIONS_TO_SEND = 24 * 2;

	public static final int STARTUP__APPROX_NUM_ALLIES_TO_SEND = 20;

	public static final int STARTUP__DAILY_BONUS_MAX_CONSECUTIVE_DAYS = 5;

	public static final int STARTUP__LEADERBOARD_MIN_LEVEL = 1;

	public static final int STARTUP__ENHANCING_MIN_LEVEL_TO_UNLOCK = 20;

	// if development then use user with id = 1
	public static final int STARTUP__ADMIN_CHAT_USER_ID = Globals.IS_SANDBOX() ? 1 : 98394;// Globals.IS_SANDBOX()
																							// ? 98437 :
																							// 131287;

	public static final int STARTUP__MAX_PRIVATE_CHAT_POSTS_SENT = 150;

	public static final int STARTUP__MAX_PRIVATE_CHAT_POSTS_RECEIVED = 150;

	// TASK ACTION
	// public static final int TASK_ACTION__MAX_CITY_RANK = 5;

	// PURCHASE NORM STRUCTURE
	public static final int PURCHASE_NORM_STRUCTURE__MAX_NUM_OF_CERTAIN_STRUCTURE = 3;

	// SPEEDUP NORM STRUCTURE CONSTANT
	public static final double BUILD_LATE_SPEEDUP_CONSTANT = 0.5;

	public static final double UPGRADE_LATE_SPEEDUP_CONSTANT = 0.5;

	// UPGRADE NORM STRUCTURE
	public static final int UPGRADE_NORM_STRUCTURE__MAX_STRUCT_LEVEL = 5;

	// SELL NORM STRUCTURE
	public static final double SELL_NORM_STRUCTURE__PERCENT_RETURNED_TO_USER = .2;

	// EARN FREE DIAMONDS
	public static final int EARN_FREE_DIAMONDS__FB_CONNECT_REWARD = 10;

	// USER CREATE
	public static final int USER_CREATE__START_LEVEL = 1;

	public static final int USER_CREATE__MIN_NAME_LENGTH = 1;

	public static final int USER_CREATE__MAX_NAME_LENGTH = 15;

	public static final int USER_CREATE__MIN_COIN_REWARD_FOR_REFERRER = 100;

	public static final int USER_CREATE__COIN_REWARD_FOR_BEING_REFERRED = 50;

	public static final double USER_CREATE__PERCENTAGE_OF_COIN_WEALTH_GIVEN_TO_REFERRER = .2;

	public static final int USER_CREATE__ID_OF_POSTER_OF_FIRST_WALL = Globals.IS_SANDBOX()
		? 1
		: 98394;

	public static final String USER_CREATE__FIRST_WALL_POST_TEXT = "Hi! My name's "
		+ (Globals.KABAM_ENABLED() ? "Stevie" : "Andrew")
		+ ", one of the creators of this game. Feel free to message me if you need any help.";

	public static final int USER_CREATE__INITIAL_GLOBAL_CHATS = 10;

	// SEND GROUP CHAT
	public static final int SEND_GROUP_CHAT__MAX_LENGTH_OF_CHAT_STRING = 200;

	// LEADERBOARD EVENT
	public static final int TOURNAMENT_EVENT__WINS_WEIGHT = 2;

	public static final int TOURNAMENT_EVENT__LOSSES_WEIGHT = -1;

	public static final int TOURNAMENT_EVENT__FLEES_WEIGHT = -3;

	public static final int TOURNAMENT_EVENT__NUM_HOURS_TO_SHOW_AFTER_EVENT_END = 24;

	public static final String UCHRFC__USER_CREATED = "user created";

	public static final String UCHRFC__LEADERBOARD = "leaderboard event";

	public static final String UCHRFC__CLAN_TOWER_WAR_ENDED = "clan tower war ended";

	public static final String UCHRFC__SHORT_MARKET_PLACE_LICENSE =
		"purchased short market place license";

	public static final String UCHRFC__LONG_MARKET_PLACE_LICENSE =
		"purchased long market place license";

	public static final String UCHRFC__GROUP_CHAT = "purchased group chat"; // is controller for this
																			// even used?

	public static final String UCHRFC__BOSS_ACTION = "boss action";

	public static final String UCHRFC__REFILL_STAT = "refilled stat: ";

	public static final String UCHRFC__SELL_NORM_STRUCT = "sell norm struct";

	public static final String UCHRFC__REDEEM_MARKETPLACE_EARNINGS =
		"redeemed marketplace earnings";

	public static final String UCHRFC__PICK_LOCKBOX = "picked lockbox";

	public static final String UCHRFC__RETRACT_MARKETPLACE_POST = "retract marketplace post";

	public static final String UCHRFC__PLAY_THREE_CARD_MONTE = "played three card monte";

	// public static final String UCHRFC__SOLD_ITEM_ON_MARKETPLACE = "sold item on marketplace"; //user's
	// currency change is 0
	public static final String UCHRFC__PURCHASED_FROM_MARKETPLACE =
		"purchased from marketplace";

	public static final String UCHRFC__SUBMIT_EQUIPS_TO_BLACKSMITH =
		"submit equips to blacksmith";

	public static final String UCHRFC__FINISH_FORGE_ATTEMPT_WAIT_TIME =
		"finish forge attempt wait time";

	public static final String UCHRFC__IN_APP_PURCHASE = "inapp purchase: ";

	public static final String UCHRFC__ARMORY_TRANSACTION = "armory transaction";

	public static final String UCHRFC__UPGRADE_CLAN_TIER_LEVEL = "upgraded clan tier level";

	public static final String UCHRFC__EARN_FREE_DIAMONDS_KIIP = "kiip";

	public static final String UCHRFC__EARN_FREE_DIAMONDS_ADCOLONY = "adcolony";

	public static final String UCHRFC__CHARACTER_MOD_TYPE = "character type, class";

	public static final String UCHRFC__CHARACTER_MOD_NAME = "character name";

	public static final String UCHRFC__CHARACTER_MOD_RESET = "character reset";

	public static final String UCHRFC__CHARACTER_MOD_SKILL_POINTS = "character skill points";

	public static final String UCHRFC__GOLDMINE = "goldmine reset";

	public static final String UCHRFC__COLLECT_GOLDMINE = "collect from goldmine";

	public static final String UCHRFC__PURCHASED_ADDITIONAL_FORGE_SLOTS =
		"purchased additional forge slots";

	// silver only reasons
	public static final String UCHRFC__RETRIEVE_CURRENCY_FROM_NORM_STRUCT =
		"retrieve currency from normal structures";

	public static final String UCHRFC__TASK_ACTION = "performed task with id ";

	public static final String UCHRFC__STARTUP_DAILY_BONUS = "startup daily bonus";

	public static final String UCHRFC__USER_CREATE_REFERRED_A_USER = "referred a user";

	public static final String UCHRFC__BATTLE_WON = "won battle";

	public static final String UCHRFC__BATTLE_LOST = "lost battle";


	// BOOSTER PACKS
	// amount of booster packs user can buy at one time
	public static final int BOOSTER_PACK__PURCHASE_OPTION_ONE_NUM_BOOSTER_ITEMS = 1;

	public static final int BOOSTER_PACK__PURCHASE_OPTION_TWO_NUM_BOOSTER_ITEMS = 10;

	public static final String BOOSTER_PACK__INFO_IMAGE_NAME = "howchestswork.png";

	public static final int BOOSTER_PACK__NUM_TIMES_TO_BUY_STARTER_PACK = 4;

	public static final int BOOSTER_PACK__NUM_DAYS_TO_BUY_STARTER_PACK = 3;

//	public static final com.lvl6.mobsters.info.AnimatedSpriteOffset[] STARTUP__ANIMATED_SPRITE_OFFSETS =
//		{ new AnimatedSpriteOffset("TutorialGuide", new CoordinatePair(0, -5)),
//			new AnimatedSpriteOffset("TutorialGuideBad", new CoordinatePair(0, -7)),
//			new AnimatedSpriteOffset("AllianceArcher", new CoordinatePair(0, -5)),
//			new AnimatedSpriteOffset("AllianceWarrior", new CoordinatePair(0, -7)),
//			new AnimatedSpriteOffset("AllianceMage", new CoordinatePair(0, -6)),
//			new AnimatedSpriteOffset("LegionArcher", new CoordinatePair(0, -7)),
//			new AnimatedSpriteOffset("LegionWarrior", new CoordinatePair(0, -11)),
//			new AnimatedSpriteOffset("LegionMage", new CoordinatePair(0, -8)),
//			new AnimatedSpriteOffset("Bandit", new CoordinatePair(0, -15)),
//			new AnimatedSpriteOffset("FarmerMitch", new CoordinatePair(0, -8)),
//			new AnimatedSpriteOffset("Carpenter", new CoordinatePair(0, -6)),
//			new AnimatedSpriteOffset("Bandit", new CoordinatePair(0, -6)), };

	public static final String[] STARTUP__NOTICES_TO_PLAYERS = {
	// "FREE limited edition gold equip for joining today!"
	// "Forging Contest! 50 GOLD reward! Details at forum.lvl6.com"
	// "We have just added 40+ equips, a new city, and increased the level cap!"
	"Happy birthday AoC! Buildings will make silver twice as fast all week long!" };

	public static final int STARTUP__QUEST_ID_FOR_FIRST_LOSS_TUTORIAL = 326;

	public static final int[] STARTUP__QUEST_IDS_FOR_GUARANTEED_WIN = { 325 };

	public static final String STARTUP__FAQ_FILE_NAME = "FAQ.3.txt";

	public static final String NIB_NAME__LOCK_BOX = "LockBox.4";

	public static final String NIB_NAME__TRAVELING_MAP = "TravelingMap.4";

	public static final String NIB_NAME__EXPANSION = "Expansion.2";

	public static final String NIB_NAME__GOLD_SHOPPE = "GoldShoppe.4";

}
