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

import com.lvl6.mobsters.info.Clan;
import com.lvl6.mobsters.info.User;
import com.lvl6.mobsters.info.repository.UserRepository;
import com.lvl6.mobsters.services.user.UserService;
import com.lvl6.mobsters.tests.fixture.InitWorldService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-db.xml", "classpath:spring-redis.xml", "classpath:spring-dynamo.xml", "classpath:spring-services.xml", "classpath:spring-testdata.xml"})
@TransactionConfiguration(transactionManager="lvl6Txm", defaultRollback=true)
public class E2ETestUpdateUserService { 
	// Subject under test
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepo;
    
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
	
	@Test
	public void testDeleteEffect() {
		/*
		User userOne = userService.findByIdWithClan(protoUserOne.getId());
		assertNotNull("Before delete, userOne is not null", userOne);

		// Orthogonal to the user service, a delete occurs.
		userRepo.delete(userOne);

		// The service should no longer find the user given the same ID.
		userOne = userService.findById(protoUserOne.getId());
		assertNull("After delete, userOne must be null", userOne);
		*/
	}
		
	
	@Test
	public void testFindWithClan() {
		/*
		// Retrieve a user with a clan and a user without a clan.
		final User userOne = userService.findByIdWithClan(protoUserOne.getId());
		final User userTwo = userService.findByIdWithClan(protoUserTwo.getId());
		
		// Verify the user without a clan is not dependent on a clan to be retrieved
		// and does not artificially acquire a clan.
		assertNotNull("UserOne cannot be null", userOne);
		assertNull("UserOne.clan is not set and must be null", userOne.getClan());

		// Verify the user with a clan is in-memory and there are no lazily loading
		// proxies still present in the subgraph of interest.
		assertNotNull("UserTwo cannot be null", userTwo);
		assertNotNull("UserTwo.clan cannot be null", userTwo.getClan());
		assertEquals("UserTwo.clan must have an identity equal to aClan", protoAClan.getId(), userTwo.getClan().getId());
		assertEquals("UserTwo.clan.description must still be its static default", protoAClan.getDescription(), userTwo.getClan().getDescription());
		assertNull("UserTwo.clan.clanIcon is not set and must be null", userTwo.getClan().getClanIcon());
		*/
	}
}
