package com.lvl6.mobsters.dynamo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.lvl6.mobsters.dynamo.PvpLeagueForUser;
import com.lvl6.mobsters.dynamo.repository.PvpLeagueForUserRepository;
import com.lvl6.mobsters.dynamo.setup.SetupDynamoDB;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-dynamo.xml")
public class TestPvpLeagueForUsers {

	
	
	private static final Logger log = LoggerFactory.getLogger(TestQuestForUsers.class);
	
	
	@Autowired
	public SetupDynamoDB setup;
	
	@Autowired
	public AmazonDynamoDBClient dynamoClient;
	
	
	@Autowired
	public PvpLeagueForUserRepository repo;
	
	
	
	public static String userId = UUID.randomUUID().toString();
	public static List<String> pvpLeagueIds = Arrays.asList(UUID.randomUUID().toString(),UUID.randomUUID().toString(),UUID.randomUUID().toString(),UUID.randomUUID().toString(),UUID.randomUUID().toString(),UUID.randomUUID().toString(),UUID.randomUUID().toString());
	public static List<PvpLeagueForUser> leagues = new ArrayList<>();
	
	
	//@BeforeClass
	public void createTestData() {
		int index = pvpLeagueIds.size();
		for(String league : pvpLeagueIds) {
			DateTime tim = new DateTime();
			tim.minusWeeks(index);
			index--;
			PvpLeagueForUser pvp = new PvpLeagueForUser(userId, league, index, index, tim.getMillis(), tim.getMillis(), index, index, index, index, tim.getMillis());
			repo.save(pvp);
			leagues.add(pvp);
			log.info("Saving: {}", pvp);
		}
	}
	
	
	public void destroyTestData() {
		repo.getMapper().batchDelete(leagues);
	}
	
	
	
	
	
	@Test
	public void test() {
		createTestData();
		List<PvpLeagueForUser> res = repo.getLeaguesByEloAndShieldEndTimeLessThanAndBattleEndTimeLessThan(pvpLeagueIds.size()-4, pvpLeagueIds.size()-1, System.currentTimeMillis(), System.currentTimeMillis(), 10);
		log.info("Found {} leagues", res.size());
		for(PvpLeagueForUser le : res) {
			log.info("Found: {}", le);
		}
		Assert.assertTrue("Found all relevant pvpLeaguesForUser", res.size() == 4);
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


	public PvpLeagueForUserRepository getRepo() {
		return repo;
	}


	public void setRepo(PvpLeagueForUserRepository repo) {
		this.repo = repo;
	}


}
