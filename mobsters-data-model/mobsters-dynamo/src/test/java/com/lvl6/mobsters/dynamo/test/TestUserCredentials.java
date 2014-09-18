package com.lvl6.mobsters.dynamo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.lvl6.mobsters.dynamo.UserCredential;
import com.lvl6.mobsters.dynamo.repository.BaseDynamoItemRepositoryImpl;
import com.lvl6.mobsters.dynamo.repository.UserCredentialRepository;
import com.lvl6.mobsters.dynamo.setup.SetupDynamoDB;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-dynamo.xml")
public class TestUserCredentials
{

	private static final Logger log = LoggerFactory.getLogger(TestUserCredentials.class);

	@Autowired
	public SetupDynamoDB setup;

	@Autowired
	public AmazonDynamoDBClient dynamoClient;

	@Autowired
	public UserCredentialRepository userRepo;

	public static List<String> userIds = Arrays.asList(
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString(),
		UUID.randomUUID().toString());

	public static List<String> questForUserIds = new ArrayList<>();

	@Before
	public void createTestData()
	{
		for (final String user : TestUserCredentials.userIds) {
			final UserCredential us = new UserCredential(user, user, user);
			TestUserCredentials.log.info("Saving: {}", us);
			userRepo.save(us);
			final UserCredential qul = userRepo.load(user);
			TestUserCredentials.log.info("Loaded: {}", qul);
		}
	}

	@After
	@SuppressWarnings("unchecked")
	public void destroyTestData()
	{

		((BaseDynamoItemRepositoryImpl<UserCredential>)userRepo).emptyTable();
		/*
		 * for (final String userId : TestUserCredentials.userIds) { final List<UserCredential> users =
		 * userRepo.getUserCredentialByUdid(userId); userRepo.delete(users); }
		 */
	}

	@Test
	public void test()
	{
		final List<UserCredential> users =
			userRepo.findByFacebookId(TestUserCredentials.userIds.get(0));
		final List<UserCredential> userz =
			userRepo.findByUdid(TestUserCredentials.userIds.get(0));
		Assert.assertEquals("Found one user by facebook", users.size(), 1);
		Assert.assertEquals("Found one user by udid", userz.size(), 1);
		Assert.assertEquals("Found same", users.get(0), userz.get(0));
	}

	public void setSetup( final SetupDynamoDB setup )
	{
		this.setup = setup;
	}

	public void setDynamoClient( final AmazonDynamoDBClient dynamoClient )
	{
		this.dynamoClient = dynamoClient;
	}

	public void setUserRepo( final UserCredentialRepository userRepo )
	{
		this.userRepo = userRepo;
	}

}
