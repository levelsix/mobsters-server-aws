package com.lvl6.mobsters.tests.e2e;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.mobsters.controllers.UpdateMonsterHealthController;
import com.lvl6.mobsters.info.Clan;
import com.lvl6.mobsters.info.User;
import com.lvl6.mobsters.info.repository.ClanRepository;
import com.lvl6.mobsters.info.repository.MonsterForUserRepository;
import com.lvl6.mobsters.info.repository.UserRepository;
import com.lvl6.mobsters.services.user.UserService;
import com.lvl6.mobsters.tests.fixture.InitWorldService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-db.xml", "classpath:spring-redis.xml", "classpath:spring-dynamo.xml", "classpath:spring-services.xml", "classpath:spring-controllers.xml", "classpath:spring-testdata.xml"})
@TransactionConfiguration(transactionManager="lvl6Txm", defaultRollback=true)
public class E2ETestUpdateMonsterHealthController { // extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    UpdateMonsterHealthController sutController;
 
    @Autowired
    ClanRepository clanRepo;
    
    @Autowired
    UserRepository userRepo;
    
    @Autowired
    MonsterForUserRepository userMonsterRepo;
    
    @Autowired
    UserService userService;

    @Autowired
    InitWorldService initDataService;

    User protoUserOne;
    User protoUserTwo;
    Clan protoAClan;
    
	@Before
	public void setUpBeforeTest() throws Exception {
		initDataService.buildUp();

		protoUserOne = initDataService.getItem("UserOne");
		protoUserTwo = initDataService.getItem("UserTwo");
		protoAClan = initDataService.getItem("AClan");
	}

	@After
	public void tearDownAfterTest() throws Exception {
		initDataService.tearDown();
	} 

	public void setUp() throws Exception {
	}

	/*
	private void buildClanDependency() {
		Clan aClan = new Clan(null, A_CLAN_NAME, TimeUtils.createNow(), A_CLAN_DESCRIPTION, A_CLAN_TAG, A_CLAN_REQUEST_REQUIRED, null);
		aClan = clanRepo.saveAndFlush(aClan);
		A_CLAN_ID = aClan.getId();
	}

	private void buildUsersAndMonsters() {
		User userOne = new User(null, USER_ONE_CASH, USER_ONE_XP, USER_ONE_GEMS, USER_ONE_IS_ADMIN, USER_ONE_LEVEL, USER_ONE_NAME, USER_ONE_OIL, null);
		userOne = userRepo.save(userOne);
		
		final Clan aClanTwo = clanRepo.getOne(A_CLAN_ID);
		User userTwo = new User(null, USER_TWO_CASH, USER_TWO_XP, USER_TWO_GEMS, USER_TWO_IS_ADMIN, USER_TWO_LEVEL, USER_TWO_NAME, USER_TWO_OIL, aClanTwo);
		userTwo = userRepo.save(userTwo);
		
		USER_ONE_ID = userOne.getId();
		USER_TWO_ID = userTwo.getId();
		System.out.println("<" + USER_ONE_ID + ">, <" + USER_TWO_ID + ">");
	}
	*/
	
	@Test
	public void testDeleteEffectA() {
		User userOne = userService.findByIdWithClan(protoUserOne.getId());
		assertNotNull("Before delete, userOne is not null", userOne);
		userRepo.delete(userOne);
		userOne = userService.findByIdWithClan(protoUserOne.getId());
		assertNull("After delete, userOne must be null", userOne);
	}
		
	
	@Test
	@Transactional
	public void testDeleteEffectB() {
		User userOne = userService.findByIdWithClan(protoUserOne.getId());
		assertNotNull("Before delete, userOne is not null", userOne);
		userRepo.delete(userOne);
		userOne = userService.findByIdWithClan(protoUserOne.getId());
		assertNull("After delete, userOne must be null", userOne);
	}
		
	
	@Test
	public void testNothing01() {
		final User userOne = userService.findByIdWithClan(protoUserOne.getId());
		final User userTwo = userService.findByIdWithClan(protoUserTwo.getId());
		
		assertNotNull("UserOne cannot be null", userOne);
		assertNull("UserOne.clan is not set and must be null", userOne.getClan());

		assertNotNull("UserTwo cannot be null", userTwo);
		assertNotNull("UserTwo.clan cannot be null", userTwo.getClan());
		assertEquals("UserTwo.clan must have an identity equal to aClan", protoAClan.getId(), userTwo.getClan().getId());
		assertEquals("UserTwo.clan.description must still be its static default", protoAClan.getDescription(), userTwo.getClan().getDescription());
		assertNull("UserTwo.clan.clanIcon is not set and must be null", userTwo.getClan().getClanIcon());
	}
}
