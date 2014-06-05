package com.lvl6.mobsters.dynamo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepository;
import com.lvl6.mobsters.dynamo.setup.SetupDynamoDB;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-dynamo.xml")
public class TestQuestForUsers {

	
	
	private static final Logger log = LoggerFactory.getLogger(TestQuestForUsers.class);
	
	
	@Autowired
	public SetupDynamoDB setup;
	
	@Autowired
	public AmazonDynamoDBClient dynamoClient;
	
	
	@Autowired
	public QuestForUserRepository qfuRepo;
	
	
	
	public static String userId = UUID.randomUUID().toString();
	public static List<Integer> questIds = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
	public static List<String> questForUserIds = new ArrayList<>();
	
	
	
	//@BeforeClass
	public void createTestData() {
		for(Integer quest : questIds) {
			QuestForUser qu = new QuestForUser(userId, quest, false, true);
			log.info("Saving: {}", qu);
			qfuRepo.save(qu);
			QuestForUser qul = qfuRepo.getMapper().load(QuestForUser.class, qu.getUserId(), qu.getQuestId());
			//questForUserIds.add(qu.getId());
			log.info("Loaded: {}", qul);
		}
	}
	
	
	public void destroyTestData() {
		List<QuestForUser> quests = qfuRepo.getQuestsForUser(userId, true, questIds);
		qfuRepo.getMapper().batchDelete(quests);
	}
	
	
	
	
	
	@Test
	public void test() {
		createTestData();
		Collection<QuestForUser> quests = qfuRepo.getQuestsForUser(userId, true, questIds);
		log.info("Found {} quests", quests.size());
		Assert.assertTrue("Found all quests", quests.size() == questIds.size());
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


	public QuestForUserRepository getQfuRepo() {
		return qfuRepo;
	}


	public void setQfuRepo(QuestForUserRepository qfuRepo) {
		this.qfuRepo = qfuRepo;
	}


}
