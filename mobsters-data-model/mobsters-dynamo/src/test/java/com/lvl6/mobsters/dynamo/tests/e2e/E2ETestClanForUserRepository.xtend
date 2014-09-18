package com.lvl6.mobsters.dynamo.tests.e2e;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch
import com.lvl6.mobsters.dynamo.ClanForUser
import com.lvl6.mobsters.dynamo.repository.ClanForUserRepository
import com.lvl6.mobsters.dynamo.repository.DynamoRepositorySetup
import com.lvl6.mobsters.dynamo.setup.SetupDynamoDB
import java.util.ArrayList
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
@ContextConfiguration("classpath:spring-configuration.xml", "classpath:spring-dynamo.xml")
public class E2ETestClanForUserRepository
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
	var ClanForUserRepository cfuRepo;

	val static String userId = "0105ca66-59fc-4f24-8a83-63585e8c34ed";
	val static String[] savedClanIds = #[
		"07e2de0b-0c3a-4d6c-ab67-c098cf656d15",
		"48988898-e9ce-40f8-9f3e-20fdaedee14d",
		"858f8754-30a7-4c27-b010-5b56095b965f",
		"ff5a5449-ca20-480f-8729-ee97b5c0d9ab"
	]
	val static String[] unsavedClanIds = #[
		"09cc0113-3d8f-4264-a857-bd5cb78caead",
		"22c288b7-7456-437d-8431-b2e8cbccbcd1",
		"b8ba18fe-0221-43d6-b9ae-3030d6d1ff3f",
		"ae8a2497-b02d-48f7-b6a1-14c1a04a7453"
	]
	val static Date requestTime = new Date()

	@Before
	def void createTestData()
	{
		LOG.info("BEGIN createTestData()")
		setup.checkDynamoTables()
		val List<FailedBatch> failures = mapper.batchSave(
			savedClanIds.map [ String clanId |
				return new ClanForUser() => [
					it.userId = userId
					it.clanId = clanId
					it.status = clanId
					it.requestTime = requestTime
				]
			]
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
	def void testLoadEach()
	{
		LOG.info("BEGIN testLoadEach()")
		val List<ClanForUser> clans = new ArrayList<ClanForUser>(cfuRepo.findByUserId(userId))
		LOG.info("Found {} clans", clans.size());
		LOG.info(clans.map[it.clanId].toString)
		LOG.info(savedClanIds.toString)
		Assert.assertEquals("Found all clans", clans.size(), savedClanIds.size());
		Assert.assertArrayEquals("Found all clans", savedClanIds, clans.map[it.clanId]);
	}

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

	def void setSetup(SetupDynamoDB setup)
	{
		this.setup = setup;
	}
}
