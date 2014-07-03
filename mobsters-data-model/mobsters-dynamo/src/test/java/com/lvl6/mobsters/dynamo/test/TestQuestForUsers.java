package com.lvl6.mobsters.dynamo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.lvl6.mobsters.dynamo.QuestForUser;
import com.lvl6.mobsters.dynamo.repository.BaseDynamoItemRepositoryImpl;
import com.lvl6.mobsters.dynamo.repository.DynamoRepositorySetup;
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepository;
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepositoryImpl;
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
	
	@Autowired
	public List<DynamoRepositorySetup> repoSetupList;
	
	
	public static String userId = UUID.randomUUID().toString();
	public static List<Integer> questIds = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
	public static List<String> questForUserIds = new ArrayList<>();
	
	
	
	@Before
	public void createTestData() {
		for(Integer quest : questIds) {
			QuestForUser qu = new QuestForUser(userId, quest, false, true);
			log.info("Saving: {}", qu);
			qfuRepo.save(qu);
			//questForUserIds.add(qu.getId());
		}
	}
	
	@After
	public void destroyTestData() {
		for (DynamoRepositorySetup nextRepoSetup : repoSetupList) {
			nextRepoSetup.emptyTable();
		}
	}
	
	@Test
	public void testLoadEach() {
		Collection<QuestForUser> quests = qfuRepo.findByUserIdAndIsCompleteAndQuestIdIn(userId, true, questIds);
		log.info("Found {} quests", quests.size());
		Assert.assertTrue("Found all quests", quests.size() == questIds.size());
	}
	
	@Test
	public void testLoadLoop() {
		ArrayList<QuestForUser> quests = new ArrayList(questIds.size());
		for(Integer quest : questIds) {
			QuestForUser qul = qfuRepo.load(userId, quest);
			quests.add(qul);
			log.info("Loaded: {}", qul);			
		}
		log.info("Found {} quests", quests.size());
		Assert.assertEquals("Found all quests", questIds.size(), quests.size());
	}

	public void setSetup(SetupDynamoDB setup) {
		this.setup = setup;
	}

	public void setDynamoClient(AmazonDynamoDBClient dynamoClient) {
		this.dynamoClient = dynamoClient;
	}

	public void setQfuRepo(QuestForUserRepositoryImpl qfuRepo) {
		this.qfuRepo = qfuRepo;
	}
}
