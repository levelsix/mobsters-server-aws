package com.lvl6.mobsters.dynamo.tests.e2e;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch
import com.lvl6.mobsters.dynamo.ClanForUser
import com.lvl6.mobsters.dynamo.QuestForUser
import com.lvl6.mobsters.dynamo.repository.DynamoRepositorySetup
import com.lvl6.mobsters.dynamo.repository.QuestForUserRepository
import com.lvl6.mobsters.dynamo.setup.SetupDynamoDB
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.List
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration("classpath:spring-dynamo.xml")
public class E2ETestQuestForUserRepositoryQueryFilter
{
	private static val Logger LOG = LoggerFactory.getLogger(E2ETestClanForUserRepository);

	@Autowired
	var SetupDynamoDB setup;

	@Autowired
	var DynamoDBMapper mapper;

	@Autowired
	var List<DynamoRepositorySetup> repoSetupList;

	// SUT
	@Autowired
	var QuestForUserRepository qfuRepo;

	val static String userId = "0102ca66-56fc-4f24-e5c3-0d585e8c3b0b";
	val static String otherUserId = "e289ca66-a6f4-c15e-e483-ef5835a3da1c";
	val static int[] redeemedQuestIds = #[
		93, 275, 493, 572, 846
	]
	val static int[] completedQuestIds = #[
		783, 941
	]
	val static int[] pendingQuestIds = #[
		157, 305, 632
	]

	@Before
	def void createTestData()
	{
		LOG.info("BEGIN createTestData()")
		setup.checkDynamoTables()
		val List<FailedBatch> failures = mapper.batchSave(
			#{userId, otherUserId}.map[ String u |
				#{
					redeemedQuestIds.map [ int questId |
						return new QuestForUser() => [
							it.userId = u
							it.questId = questId
							it.redeemed = true
							it.complete = false
						]
					],
					completedQuestIds.map [ int questId |
						return new QuestForUser() => [
							it.userId = u
							it.questId = questId
							it.redeemed = false
							it.complete = true
						]
					],
					pendingQuestIds.map [ int questId |
						return new QuestForUser() => [
							it.userId = u
							it.questId = questId
							it.redeemed = false
							it.complete = false
						]	
					]
				}.flatten()
			].flatten().toList()
		)
		Assert.assertTrue(failures.toString(), failures.isEmpty())
	}

	@After
	def void destroyTestData()
	{
		LOG.info("BEGIN destroyTestData()")
		for (DynamoRepositorySetup repoSetup : repoSetupList)
		{
			repoSetup.dropTable();
		}
	}

	@Test
	def void testLoadCompleted()
	{
		LOG.info("BEGIN testLoadEach()")
		val List<QuestForUser> quests = 
			new ArrayList<QuestForUser>(
				qfuRepo.findByUserId(userId) [
					complete[isTrue].redeemed[isFalse]
				]
			)
		LOG.info("Found %s quests", quests.size());
		LOG.info(quests.toString())
		LOG.info(completedQuestIds.toString)
		Assert.assertEquals("Found right number of completed quests", completedQuestIds.size(), quests.size());
		Assert.assertArrayEquals("Found all completed questIds", completedQuestIds, quests.map[it.questId]);
		Assert.assertEquals("Found only target user's quests", completedQuestIds.size(), quests.filter[it.userId == userId].size());
	}

	@Test
	def void testLoadRedeemed()
	{
		LOG.info("BEGIN testLoadEach()")
		val List<QuestForUser> quests = 
			new ArrayList<QuestForUser>(
				qfuRepo.findByUserId(userId) [
					complete[isFalse].redeemed[isTrue]
				]
			)
		LOG.info("Found %s quests", quests.size());
		LOG.info(quests.toString())
		LOG.info(completedQuestIds.toString)
		Assert.assertEquals("Found right number of redeemed quests", redeemedQuestIds.size(), quests.size());
		Assert.assertArrayEquals("Found all redeemed questIds", redeemedQuestIds, quests.map[it.questId]);
		Assert.assertEquals("Found only target user's quests", redeemedQuestIds.size(), quests.filter[it.userId == userId].size());
	}

	@Test
	def void testLoadPending()
	{
		LOG.info("BEGIN testLoadPending()")
		val List<QuestForUser> quests = 
			new ArrayList<QuestForUser>(
				qfuRepo.findByUserId(userId) [
					complete[isFalse].redeemed[isFalse]
				]
			)
		LOG.info("Found %s quests", quests.size());
		LOG.info(quests.toString())
		LOG.info(completedQuestIds.toString)
		Assert.assertEquals("Found right number of pending quests", pendingQuestIds.size(), quests.size());
		Assert.assertArrayEquals("Found all pending questIds", pendingQuestIds, quests.map[it.questId]);
		Assert.assertEquals("Found only target user's quests", pendingQuestIds.size(), quests.filter[it.userId == userId].size());
	}

    // TODO: Need to add an a mode flag to toggle between "and" and "or" conjunction...
	@Test
	def void testLoadNone()
	{
		LOG.info("BEGIN testLoadEach()")
		val List<QuestForUser> quests = 
			new ArrayList<QuestForUser>(
				qfuRepo.findByUserId(userId) [
					complete[isTrue].redeemed[isTrue]
				]
			)
		LOG.info("Found %s quests", quests.size());
		LOG.info(quests.toString())
		Assert.assertEquals("Found right number of impossible quests", 0, quests.size());
	}

/*
	@Test
	def void testLoadAll()
	{
		LOG.info("BEGIN testLoadAll()")
		val List<ClanForUser> clans = new ArrayList<ClanForUser>(cfuRepo.loadAll(userId))
		LOG.info("Found {} clans", clans.size());
		Assert.assertEquals("Found all clans", clans.size(), savedClanIds.size());
		Assert.assertArrayEquals("Found all clans", savedClanIds, clans.map[it.clanId]);
	}

	@Test
	def void testLoadLoop()
	{
		LOG.info("BEGIN testLoadLoop()")
		val List<ClanForUser> clans = savedClanIds.map [
			return cfuRepo.load(userId, it)
		]
		LOG.info("Found {} clans", clans.size());
		Assert.assertEquals("Found all clans", savedClanIds.size(), clans.size());
		Assert.assertArrayEquals("Found all clans", savedClanIds, clans.map[it.clanId]);
	}
*/

	def void setSetup(SetupDynamoDB setup)
	{
		this.setup = setup;
	}
}
