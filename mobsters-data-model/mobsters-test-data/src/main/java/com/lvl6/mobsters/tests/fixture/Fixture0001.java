package com.lvl6.mobsters.tests.fixture;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.mobsters.info.Monster;
import com.lvl6.mobsters.info.repository.MonsterRepository;

@Component
public class Fixture0001 implements PartialDataSet {

	private static final String A_CLAN_NAME = "LastNameFirst";
	private static final String A_CLAN_DESCRIPTION = "This describes it";
	private static final String A_CLAN_TAG = "You're it";
	private static final boolean A_CLAN_REQUEST_REQUIRED = false;


	private static final int USER_ONE_CASH = 175;
	private static final int USER_ONE_XP = 948;
	private static final int USER_ONE_GEMS = 43;
	private static final byte USER_ONE_IS_ADMIN = Byte.parseByte("0");
	private static final int USER_ONE_LEVEL = 2;
	private static final String USER_ONE_NAME = "Sam";
	private static final int USER_ONE_OIL = 42;

	private static final int USER_TWO_CASH = 904;
	private static final int USER_TWO_XP = 8192;
	private static final int USER_TWO_GEMS = 88;
	private static final byte USER_TWO_IS_ADMIN = Byte.parseByte("0");
	private static final int USER_TWO_LEVEL = 6;
	private static final String USER_TWO_NAME = "Pete";
	private static final int USER_TWO_OIL = 512;
	

	private static final String MONSTER_A_NAME = "Joe Monster of the Gang";
	private static final String MONSTER_A_GROUP = "TheGang";
	private static final String MONSTER_A_SHORT_NAME = "Joe";
	private static final String MONSTER_A_ANIMATE_TYPE = "Stiff";
	private static final String MONSTER_A_QUALITY = "Rare";
	private static final int MONSTER_A_EVO_LEVEL = 4;
	private static final String MONSTER_A_DISPLAY_NAME = "Joe Monster";
	private static final String MONSTER_A_ELEMENT = "fire";
	private static final String MONSTER_A_IMG_PREFIX = "img_";
	private static final int MONSTER_A_PUZZLE_PIECE_COUNT = 3;
	private static final int MONSTER_A_MINUTES_TO_COMBINE = 3;
	private static final int MONSTER_A_MAX_LEVEL = 3;
	private static final int MONSTER_A_EVO_LEVEL_ID = 2;
	private static final int MONSTER_A_EVO_CATALYST_ID = 4;
	private static final int MONSTER_A_MINUTES_TO_EVOLVE = 4;
	private static final int MONSTER_A_NUM_CATALYSTS_REQD = 5;
	private static final String MONSTER_A_CARROT_RECRUITED = "You know you wanna join the army";
	private static final String MONSTER_A_CARROT_DEFEATED = "You know you wanna get beaten";
	private static final String MONSTER_A_CARROT_EVOLVED = "You know you wanna evolve";
	private static final String MONSTER_A_DESC = "Monster A looks like monster A";
	private static final int MONSTER_A_EVO_COST = 10;
	private static final int MONSTER_A_VERT_PIX_OFFSET = 3;
	private static final String MONSTER_A_ATK_SOUND_FILE = "chirp.wav";
	private static final int MONSTER_A_ATK_SOUND_ANIM_FRAME = 7;
	private static final int MONSTER_A_ATK_SOUND_REPEATED = 4;
	private static final int MONSTER_A_ATK_SOUND_REPEATED_FRAMES_END = 29;


	private static final int MONSTER_ONE_FOR_USER_ONE_CURRENT_XP = 400;
	private static final int MONSTER_ONE_FOR_USER_ONE_CURRENT_HEALTH = 32;
	private static final byte MONSTER_ONE_FOR_USER_ONE_CURRENT_LEVEL = 2;
	private static final byte MONSTER_ONE_FOR_USER_ONE_NUM_PIECES = 5;
	private static final byte MONSTER_ONE_FOR_USER_ONE_IS_COMPLETE = 1;
	private static final String MONSTER_ONE_FOR_USER_ONE_SOURCE_OF_PIECES = "taskRef=1; taskRef=2; taskRef=3; taskRef=4; taskRef=5";
	private static final byte MONSTER_ONE_FOR_USER_ONE_TEAM_SLOT = 1;

	private static final int MONSTER_TWO_FOR_USER_ONE_CURRENT_XP = 500;
	private static final int MONSTER_TWO_FOR_USER_ONE_CURRENT_HEALTH = 82;
	private static final byte MONSTER_TWO_FOR_USER_ONE_CURRENT_LEVEL = 3;
	private static final byte MONSTER_TWO_FOR_USER_ONE_NUM_PIECES = 3;
	private static final byte MONSTER_TWO_FOR_USER_ONE_IS_COMPLETE = 1;
	private static final String MONSTER_TWO_FOR_USER_ONE_SOURCE_OF_PIECES = "taskRef=1; taskRef=2; taskRef=3; ";
	private static final byte MONSTER_TWO_FOR_USER_ONE_TEAM_SLOT = 0;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ClanRepository clanRepo;
	
	@Autowired
	MonsterRepository monsterRepo;
	
	@Autowired
	MonsterForUserRepository monsterForUserRepo;

	private Clan aClan;
	private User userOne;
	private User userTwo;
	private Monster monsterA;
	private Monster monsterB;
	private Monster monsterC;
	private MonsterForUser monsterOneForUserOne;
	private MonsterForUser monsterTwoForUserOne;
	
	@Override
	@Transactional
	public void repopulate() {
		// TODO: TimeUtils is not in a common enough place to be utilized here.
		aClan = new Clan(null, A_CLAN_NAME, new Date(), A_CLAN_DESCRIPTION, A_CLAN_TAG, A_CLAN_REQUEST_REQUIRED, null);
		aClan = clanRepo.saveAndFlush(aClan);

		userOne = new User(null, USER_ONE_CASH, USER_ONE_XP, USER_ONE_GEMS, USER_ONE_IS_ADMIN, USER_ONE_LEVEL, USER_ONE_NAME, USER_ONE_OIL, null);
		userOne = userRepo.saveAndFlush(userOne);
		
		final Clan aClanTwo = clanRepo.getOne(aClan.getId());
		userTwo = new User(null, USER_TWO_CASH, USER_TWO_XP, USER_TWO_GEMS, USER_TWO_IS_ADMIN, USER_TWO_LEVEL, USER_TWO_NAME, USER_TWO_OIL, aClanTwo);
		userTwo = userRepo.saveAndFlush(userTwo);
		
		monsterA = new Monster(1, MONSTER_A_NAME, MONSTER_A_GROUP, MONSTER_A_QUALITY, MONSTER_A_EVO_LEVEL, MONSTER_A_DISPLAY_NAME, MONSTER_A_ELEMENT, MONSTER_A_IMG_PREFIX, MONSTER_A_PUZZLE_PIECE_COUNT, MONSTER_A_MINUTES_TO_COMBINE, MONSTER_A_MAX_LEVEL, MONSTER_A_EVO_LEVEL_ID, MONSTER_A_EVO_CATALYST_ID, MONSTER_A_MINUTES_TO_EVOLVE, MONSTER_A_NUM_CATALYSTS_REQD, MONSTER_A_CARROT_RECRUITED, MONSTER_A_CARROT_DEFEATED, MONSTER_A_CARROT_EVOLVED, MONSTER_A_DESC, MONSTER_A_EVO_COST, MONSTER_A_ANIMATE_TYPE, MONSTER_A_VERT_PIX_OFFSET, MONSTER_A_ATK_SOUND_FILE, MONSTER_A_ATK_SOUND_ANIM_FRAME, MONSTER_A_ATK_SOUND_REPEATED, MONSTER_A_ATK_SOUND_REPEATED_FRAMES_END, MONSTER_A_SHORT_NAME);
		monsterB = new Monster(2, MONSTER_A_NAME + " II", MONSTER_A_GROUP, MONSTER_A_QUALITY, MONSTER_A_EVO_LEVEL, MONSTER_A_DISPLAY_NAME, MONSTER_A_ELEMENT, MONSTER_A_IMG_PREFIX, MONSTER_A_PUZZLE_PIECE_COUNT, MONSTER_A_MINUTES_TO_COMBINE, MONSTER_A_MAX_LEVEL, MONSTER_A_EVO_LEVEL_ID, MONSTER_A_EVO_CATALYST_ID, MONSTER_A_MINUTES_TO_EVOLVE, MONSTER_A_NUM_CATALYSTS_REQD, MONSTER_A_CARROT_RECRUITED, MONSTER_A_CARROT_DEFEATED, MONSTER_A_CARROT_EVOLVED, MONSTER_A_DESC, MONSTER_A_EVO_COST, MONSTER_A_ANIMATE_TYPE, MONSTER_A_VERT_PIX_OFFSET, MONSTER_A_ATK_SOUND_FILE, MONSTER_A_ATK_SOUND_ANIM_FRAME, MONSTER_A_ATK_SOUND_REPEATED, MONSTER_A_ATK_SOUND_REPEATED_FRAMES_END, MONSTER_A_SHORT_NAME);
		monsterC = new Monster(3, MONSTER_A_NAME + " III", MONSTER_A_GROUP, MONSTER_A_QUALITY, MONSTER_A_EVO_LEVEL, MONSTER_A_DISPLAY_NAME, MONSTER_A_ELEMENT, MONSTER_A_IMG_PREFIX, MONSTER_A_PUZZLE_PIECE_COUNT, MONSTER_A_MINUTES_TO_COMBINE, MONSTER_A_MAX_LEVEL, MONSTER_A_EVO_LEVEL_ID, MONSTER_A_EVO_CATALYST_ID, MONSTER_A_MINUTES_TO_EVOLVE, MONSTER_A_NUM_CATALYSTS_REQD, MONSTER_A_CARROT_RECRUITED, MONSTER_A_CARROT_DEFEATED, MONSTER_A_CARROT_EVOLVED, MONSTER_A_DESC, MONSTER_A_EVO_COST, MONSTER_A_ANIMATE_TYPE, MONSTER_A_VERT_PIX_OFFSET, MONSTER_A_ATK_SOUND_FILE, MONSTER_A_ATK_SOUND_ANIM_FRAME, MONSTER_A_ATK_SOUND_REPEATED, MONSTER_A_ATK_SOUND_REPEATED_FRAMES_END, MONSTER_A_SHORT_NAME);
		
		monsterA = monsterRepo.saveAndFlush(monsterA);
		monsterB = monsterRepo.saveAndFlush(monsterB);
		monsterC = monsterRepo.saveAndFlush(monsterC);
		
		monsterOneForUserOne = new MonsterForUser(null, new Timestamp(System.currentTimeMillis()-360000), MONSTER_ONE_FOR_USER_ONE_CURRENT_XP, MONSTER_ONE_FOR_USER_ONE_CURRENT_HEALTH, MONSTER_ONE_FOR_USER_ONE_CURRENT_LEVEL, MONSTER_ONE_FOR_USER_ONE_IS_COMPLETE, monsterA, MONSTER_ONE_FOR_USER_ONE_NUM_PIECES, MONSTER_ONE_FOR_USER_ONE_SOURCE_OF_PIECES, MONSTER_ONE_FOR_USER_ONE_TEAM_SLOT, userOne);
		monsterTwoForUserOne = new MonsterForUser(null, new Timestamp(System.currentTimeMillis()-600000), MONSTER_TWO_FOR_USER_ONE_CURRENT_XP, MONSTER_TWO_FOR_USER_ONE_CURRENT_HEALTH, MONSTER_TWO_FOR_USER_ONE_CURRENT_LEVEL, MONSTER_TWO_FOR_USER_ONE_IS_COMPLETE, monsterC, MONSTER_TWO_FOR_USER_ONE_NUM_PIECES, MONSTER_TWO_FOR_USER_ONE_SOURCE_OF_PIECES, MONSTER_TWO_FOR_USER_ONE_TEAM_SLOT, userOne);
		
		monsterOneForUserOne = monsterForUserRepo.saveAndFlush(monsterOneForUserOne);
		monsterTwoForUserOne = monsterForUserRepo.saveAndFlush(monsterTwoForUserOne);
	}
	
	@Override
	public Set<JpaRepository<?, ?>> getUsedRepositories() {
		HashSet<JpaRepository<?, ?>> retVal = new HashSet<JpaRepository<?, ?>>(8);
		retVal.add(userRepo);
		retVal.add(clanRepo);
		retVal.add(monsterRepo);
		retVal.add(monsterForUserRepo);
		
		return retVal;
	}
	
	@Override
	public List<ObjectMeta<?>> getObjects() {
		ArrayList<ObjectMeta<?>> retVal = new ArrayList<ObjectMeta<?>>(3);
		retVal.add(
			new ObjectMeta<Clan>("AClan", aClan)
		);
		retVal.add(
			new ObjectMeta<User>("UserOne", userOne)
		);
		retVal.add(
			new ObjectMeta<User>("UserTwo", userTwo)
		);
		retVal.add(
			new ObjectMeta<Monster>("MonsterA", monsterA)
		);
		retVal.add(
			new ObjectMeta<Monster>("MonsterB", monsterB)
		);
		retVal.add(
			new ObjectMeta<Monster>("MonsterC", monsterC)
		);
		retVal.add(
			new ObjectMeta<MonsterForUser>("MonsterOneUserOne", monsterOneForUserOne)
		);
		retVal.add(
			new ObjectMeta<MonsterForUser>("MonsterTwoUserOne", monsterTwoForUserOne)
		);
		
		return retVal;
	}
}
