package com.lvl6.mobsters.dynamo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.lvl6.mobsters.dynamo.UserCredential;
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
	
	
	
	//@BeforeClass

	public void createTestData()
	{
		for (final String user : TestUserCredentials.userIds) {
			final UserCredential us = new UserCredential(
				user,
				user,
				user);
			TestUserCredentials.log.info(
				"Saving: {}",
				us);
			userRepo.save(us);
			final UserCredential qul = userRepo.getMapper().load(
				UserCredential.class,
				user);
			TestUserCredentials.log.info(
				"Loaded: {}",
				qul);
		}
	}
	
	
	public void destroyTestData() {
		for(String userId : userIds) {
			List<UserCredential> users = userRepo.getUserCredentialByUdid(userId);
			userRepo.getMapper().batchDelete(users);
		}
	}
	
	
	
	
	
	@Test
	public void test() {
		createTestData();
		List<UserCredential> users = userRepo.getUserCredentialByFacebook(userIds.get(0));
		List<UserCredential> userz = userRepo.getUserCredentialByUdid(userIds.get(0));
		Assert.assertTrue("Found one user", users.size() == userz.size());
		Assert.assertTrue("Found same", users.get(0).equals(userz.get(0)));
		destroyTestData();
	}
	
	
	
	
	

	public SetupDynamoDB getSetup() {
		return setup;
	}

	public void setSetup(SetupDynamoDB setup) {
		this.setup = setup;
	}

	public AmazonDynamoDBClient getDynamoClient() {
		return dynamoClient;
	}

	public void setDynamoClient(AmazonDynamoDBClient dynamoClient) {
		this.dynamoClient = dynamoClient;
	}


	public UserCredentialRepository getUserRepo() {
		return userRepo;
	}


	public void setUserRepo(UserCredentialRepository userRepo) {
		this.userRepo = userRepo;
	}



}
