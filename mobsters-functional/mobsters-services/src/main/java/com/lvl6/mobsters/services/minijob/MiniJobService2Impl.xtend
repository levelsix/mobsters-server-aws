package com.lvl6.mobsters.services.minijob

import com.google.common.base.Function
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import com.lvl6.mobsters.common.utils.Director
import com.lvl6.mobsters.dynamo.MiniJobForUser
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepository
import com.lvl6.mobsters.dynamo.repository.MiniJobForUserRepositoryImpl
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.info.MiniJob
import com.lvl6.mobsters.info.repository.MiniJobRepository
import java.util.Date
import java.util.HashMap
import java.util.List
import java.util.Map
import java.util.Random
import java.util.Set
import java.util.UUID
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.lvl6.mobsters.services.minijob.MiniJobService2Impl.*
import com.lvl6.mobsters.common.utils.CollectionUtils

@Component
public class MiniJobService2Impl implements MiniJobService
{

	private static val Logger LOG = LoggerFactory.getLogger(MiniJobServiceImpl)

	@Autowired
	private var MiniJobForUserRepository miniJobForUserRepository

	@Autowired
	private var DataServiceTxManager txManager
	
	@Autowired
	private var MiniJobRepository miniJobRepo

	//NON CRUD LOGIC******************************************************************
	def List<MiniJob> spawnMiniJobs(int numToSpawn, int structId)
	{
		val List<MiniJob> candidateMiniJobs = miniJobRepo.findByRequiredStructId(structId)

		if (0 == numToSpawn)
		{
			LOG.info("client just reseting the last spawn time")
			return emptyList()
		}

		val Random rand = new Random()
		val MiniJobService2Impl.NumWrap probabilitySum = new MiniJobService2Impl.NumWrap()
		
		LOG.info("probabilitySum=" + probabilitySum)
		LOG.info("miniJobIdToMiniJob=" + candidateMiniJobs)
		
		val Iterable<Pair<Float, MiniJob>> cumDistFunc = 
			candidateMiniJobs.map [
				probabilitySum.sum = probabilitySum.sum + it.chanceToAppear
				return probabilitySum.sum -> it
			]
		val Iterable<MiniJob> spawnedMiniJobs = (0 .. numToSpawn).map [
			val float randFloat = rand.nextFloat() * probabilitySum.sum
			val MiniJob retVal = cumDistFunc.findFirst [
				return randFloat <= it.getKey()
			].getValue()
			
			//we have a winner
			LOG.info("randFloat=" + randFloat)
			LOG.info("selected MiniJob!: " + retVal)
			return retVal
		]

		if (spawnedMiniJobs.size() != numToSpawn)
		{
			// TODO: Should this or throw an Exception or just return as it does now?  Does the margin of
			//        error matter?  Consider a case where the spawned mini job list is empty as opposed to
			//        merely "short a few".
			LOG.error(
				"Could not spawn " + numToSpawn + " mini jobs.   Actual spawn count = " +
					spawnedMiniJobs.size() + ", actual set = " + spawnedMiniJobs)
		}

		return spawnedMiniJobs.toList()
	}

	//READ-ONLY CRUD LOGIC******************************************************
	public override List<MiniJobForUser> getMiniJobForUserId(String userId)
	{
		return miniJobForUserRepository.findByUserId(userId)
	}

	//TRANSACTIONAL CRUD LOGIC**************************************************
	public override void modifyMiniJobsForUser(String userId,
		Director<MiniJobService.ModifyUserMiniJobsSpecBuilder> director)
	{

		// Collect a work definition from the caller
		val MiniJobService2Impl.ModifyUserMiniJobsSpecBuilderImpl specBuilder = new MiniJobService2Impl.ModifyUserMiniJobsSpecBuilderImpl()
		director.apply(specBuilder)

		// get whatever we need from the database
		val Multimap<String, MiniJobService.UserMiniJobFunc> modSpecMultimap = 
			specBuilder.getModSpecMultimap()
		val Set<String> miniJobIds = modSpecMultimap.keySet()

		var boolean success = false
		txManager.beginTransaction()
		try
		{
			val List<MiniJobForUser> existingUserMiniJobs = miniJobForUserRepository.
				findByUserIdAndId(userId, miniJobIds)
			if (existingUserMiniJobs.nullOrEmpty)
			{

				val String message = "User has none of the required mini jobs.  userId=" +
					userId + ", miniJobIds=" + miniJobIds.toString()
				LOG.error(message)

				// TODO: Select the Lvl6 Exception that expresses "none found"
				throw new IllegalStateException(message)
			}
			else if (existingUserMiniJobs.size() != miniJobIds.size())
			{

				val String message = "User lacks some of the required mini jobs.  userId=" +
					userId + ", miniJobIds=" + miniJobIds.toString()
				LOG.error(message)

				// TODO: Select the Lvl6 Exception that expresses "some missing"
				throw new IllegalStateException(message)
			}

			// Mutate the objects.
			// No null test by design.  The contract of repository classes is to return a smaller list when objects do not exist, not to
			// return null place-holders.  Use unit tests to assert invariants hold true rather than redundant runtime null checks.
			existingUserMiniJobs.forEach [ MiniJobForUser nextMiniJob |
				modSpecMultimap.get(
					nextMiniJob.getMiniJobForUserId()
				).forEach [
					it.apply(nextMiniJob)
				]
			]

			// Write back to the database, then commit the transaction by toggling success to true.
			miniJobForUserRepository.saveEach(existingUserMiniJobs)
			success = true
		}
		finally
		{
			if (success)
			{
				txManager.commit()
			}
			else
			{
				txManager.rollback()
			}
		}
	}

	// A class that wraps a mutable float variable for use in forEach[] loops that may only access 'val's from their outer context, but need to increment 
	// the same book-keeping counter each iteration.
	static class NumWrap
	{
		public var float sum = 0
	}

	// Strongly typed functional interface for SpecBuilder lambdas that encapsulate evaluation blocks deferred until post-fetch runtime.
	interface UserMiniJobFunc extends Function<MiniJobForUser, Void>
	{
	}

	// motivation for two separate Builders is because service will only be modifying
	// existing objects or creating new ones
	static class ModifyUserMiniJobsSpecBuilderImpl implements MiniJobService.ModifyUserMiniJobsSpecBuilder
	{
		// modification specification map
		val Multimap<String, MiniJobService.UserMiniJobFunc> modSpecMap

		new()
		{
			this.modSpecMap = ArrayListMultimap.create()
		}

		/**
         * This is this concrete builder's build step.  It therefore does NOT belong in the builder interface!
         */
		def Multimap<String, MiniJobService.UserMiniJobFunc> getModSpecMultimap()
		{

			// TODO: There are better ways to prevent the value returned by a call to this method from potentially
			//       being changed further, but extracting an immutable copy is the easiest to implement.
			return ImmutableMultimap.copyOf(modSpecMap)
		}

		public override MiniJobService.ModifyUserMiniJobsSpecBuilder startJob(
			String userMiniJobId, Set<String> userMonsterIds, Date timeStarted)
		{
			modSpecMap.put(
				userMiniJobId,
				[ MiniJobForUser mj |
					if (mj.userMonsterIds.nullOrEmpty && (mj.timeStarted == null))
					{
						mj.setTimeStarted(timeStarted)
						mj.setUserMonsterIds(userMonsterIds)
					}
					else
					{
						LOG.error(
							"UserMiniJob already started (not reseting). MiniJob=" + mj +
								", timeStarted=" + timeStarted + ", userMonsterIds=" +
								userMonsterIds.toString())
					}
				]
			)

			return this
		}

		public override MiniJobService.ModifyUserMiniJobsSpecBuilder completeJob(
			String userMiniJobId, Date timeCompleted)
		{
			modSpecMap.put(
				userMiniJobId,
				[ MiniJobForUser mj |
					if (mj.timeCompleted == null)
					{
						mj.timeCompleted = timeCompleted
					}
					else
					{
						LOG.error(
							"UserMiniJob already completed (not completing again). MiniJob=" + mj +
								", timeCompleted=" + timeCompleted)
					}
				]
			)

			return this
		}
	}

	/**************************************************************************/
	public override void spawnMiniJobsForUser(String userId, Date clientStartTime, int numToSpawn, int structId) {
		val List<MiniJob> spawnedMiniJobs = spawnMiniJobs(numToSpawn, structId);
        if (!CollectionUtils.lacksSubstance(spawnedMiniJobs)) {
        	createMiniJobsForUser(userId) [ MiniJobService.CreateUserMiniJobsSpecBuilder builder |
	            spawnedMiniJobs.forEach[ MiniJob mj |
	                //TODO: Figure out more efficient way to get key (or eliminate the need to have one yet)
	                val String userMiniJobId = UUID.randomUUID().toString()
	                builder
	                	.setMiniJobId(userMiniJobId, mj.getId())
	                	.setBaseDmgReceived(userMiniJobId, mj.getDmgDealt())
	                	.setDurationMinutes(userMiniJobId, mj.getDurationMinutes())
	                	.setTimeStarted(userMiniJobId, clientStartTime)
	            ]        		
        	]
        }
	}
	
	public override void createMiniJobsForUser(String userId,
		Director<MiniJobService.CreateUserMiniJobsSpecBuilder> director)
	{
		// Collect a work definition from the caller
		val MiniJobService2Impl.CreateUserMiniJobsSpecBuilderImpl specBuilder = new MiniJobService2Impl.CreateUserMiniJobsSpecBuilderImpl(userId)
		director.apply(specBuilder)

		var boolean success = false
		txManager.beginTransaction()
		try
		{
			// get whatever we have been asked to save to the database
			miniJobForUserRepository.saveEach(specBuilder.build())
			success = true
		}
		finally
		{
			if (success)
			{
				txManager.commit()
			}
			else
			{
				txManager.rollback()
			}
		}
	}

	// motivation for two separate Builders is because service will only be modifying
	// existing objects or creating new ones
	static class CreateUserMiniJobsSpecBuilderImpl implements MiniJobService.CreateUserMiniJobsSpecBuilder
	{

		// the end state: objects to be saved to db
		private val String userId
		private val Map<String, MiniJobForUser> userMiniJobIdToMjfu

		new(String userId)
		{
			this.userId = userId
			this.userMiniJobIdToMjfu = new HashMap<String, MiniJobForUser>()
		}

		private def MiniJobForUser getTarget(String userMiniJobId)
		{
			var MiniJobForUser afu = userMiniJobIdToMjfu.get(userMiniJobId)
			if (afu == null)
			{
				afu = new MiniJobForUser()
				afu.userId = userId
				afu.miniJobForUserId = userMiniJobId
				userMiniJobIdToMjfu.put(userMiniJobId, afu)
			}
			
			return afu
		}

		public override MiniJobService.CreateUserMiniJobsSpecBuilder setMiniJobId(
			String userMiniJobId, int miniJobId)
		{
			getTarget(userMiniJobId).setMiniJobId(miniJobId)

			return this
		}

		public override MiniJobService.CreateUserMiniJobsSpecBuilder setBaseDmgReceived(
			String userMiniJobId, int baseDmgReceived)
		{
			getTarget(userMiniJobId).setMiniJobId(baseDmgReceived)

			return this
		}

		public override MiniJobService.CreateUserMiniJobsSpecBuilder setDurationMinutes(
			String userMiniJobId, int durationMinutes)
		{
			getTarget(userMiniJobId).setMiniJobId(durationMinutes)

			return this
		}

		public override MiniJobService.CreateUserMiniJobsSpecBuilder setUserMonsterIds(
			String userMiniJobId, Set<String> userMonsterIds)
		{
			getTarget(userMiniJobId).setUserMonsterIds(userMonsterIds)

			return this
		}

		public override MiniJobService.CreateUserMiniJobsSpecBuilder setTimeStarted(
			String userMiniJobId, Date timeStarted)
		{
			getTarget(userMiniJobId).setTimeStarted(timeStarted)

			return this
		}

		public override MiniJobService.CreateUserMiniJobsSpecBuilder setTimeCompleted(
			String userMiniJobId, Date timeStarted)
		{
			getTarget(userMiniJobId).setTimeStarted(timeStarted)

			return this
		}

		/**
        * This is this concrete builder's build step.  It therefore does NOT belong in the builder interface!
        */
		def ImmutableList<MiniJobForUser> build()
		{

			// TODO: There are better ways to prevent the value returned by a call to this method from potentially
			//       being changed further, but extracting an immutable copy is the easiest to implement.
			return ImmutableList.copyOf(userMiniJobIdToMjfu.values())
		}
	}

	//for the dependency injection
	def void setMiniJobForUserRepository(MiniJobForUserRepositoryImpl miniJobForUserRepository)
	{
		this.miniJobForUserRepository = miniJobForUserRepository
	}
}
