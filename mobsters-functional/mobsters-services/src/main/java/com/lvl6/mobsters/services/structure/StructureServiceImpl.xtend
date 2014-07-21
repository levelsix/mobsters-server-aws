//package com.lvl6.mobsters.services.structure
//
//import com.lvl6.mobsters.common.utils.Director
//import com.lvl6.mobsters.dynamo.ObstacleForUser
//import com.lvl6.mobsters.dynamo.StructureForUser
//import com.lvl6.mobsters.dynamo.repository.ObstacleForUserRepository
//import com.lvl6.mobsters.dynamo.repository.StructureForUserRepository
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.info.CoordinatePair
//import com.lvl6.mobsters.info.repository.StructureRepository
//import com.lvl6.mobsters.services.common.Lvl6MobstersException
//import com.lvl6.mobsters.services.common.Lvl6MobstersStatusCode
//import com.lvl6.mobsters.services.common.TimeUtils
//import java.util.ArrayList
//import java.util.Date
//import java.util.List
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Component
//import com.lvl6.mobsters.services.common.Lvl6MobstersConditions
//
//@Component
//class StructureServiceImpl implements StructureService
//{
//	static val LOG = LoggerFactory::getLogger(StructureServiceImpl)
//
//	@Autowired
//	var ObstacleForUserRepository obstacleForUserRepository
//
//	@Autowired
//	var StructureForUserRepository structureForUserRepository
//
//	@Autowired
//	var DataServiceTxManager txManager
//	
//	@Autowired
//	var StructureRepository structureRepository
//
//	/** 
// 	 */
//	override createObstaclesForUser(StructureService.CreateObstaclesReplyBuilder replyBuilder,
//		String userId, Director<StructureService.CreateObstacleCollectionBuilder> director)
//	{
//		val builder = new StructureServiceImpl.CreateObstacleCollectionBuilderImpl(userId)
//		director.apply(builder)
//		
//		var success = false
//		txManager.requireTransaction
//		try
//		{
//			obstacleForUserRepository.saveEach(builder.build)
//			success = true
//		}
//		finally
//		{
//			if (success)
//			{
//				txManager.commit
//				replyBuilder.resultOk
//			}
//			else
//			{
//				txManager.rollback
//			}
//		}
//	}
//
//	static class CreateObstacleCollectionBuilderImpl implements StructureService.CreateObstacleCollectionBuilder
//	{
//		val String userId
//		val ArrayList<ObstacleForUser> retVal
//
//		new(String userId)
//		{
//			this.userId = userId
//			this.retVal = new ArrayList<ObstacleForUser>()
//		}
//
//		override addObstacle(int obstacleId, float xCoord, float yCoord, StructureService.OrientationKind orientation)
//		{
//			retVal.add(
//				new ObstacleForUser() => [
//					it.obstacleId = obstacleId
//					it.xcoord = xCoord
//					it.ycoord = yCoord
//					it.orientation = orientation.toString
//				]
//			)
//			
//			return this
//		}
//
//		def ArrayList<ObstacleForUser> build()
//		{
//			return retVal
//		}
//	}
//
//	/** 
// 	 */
//	override createStructuresForUser(
//		StructureService.CreateStructuresReplyBuilder replyBuilder, String userId,
//		Director<StructureService.CreateStructureCollectionBuilder> director)
//	{
//		val builder = new StructureServiceImpl.CreateStructureCollectionBuilderImpl(userId)
//		director.apply(builder)
//	
//		val success = false
//		txManager.requireTransaction
//		try
//		{
//			structureForUserRepository.saveEach(builder.build)
//		}
//		finally
//		{
//			if (success)
//			{
//				txManager.commit
//				replyBuilder.resultOk
//			}
//			else
//			{
//				txManager.rollback
//			}
//		}
//	}
//
//	static class CreateStructureCollectionBuilderImpl implements StructureService.CreateStructureCollectionBuilder
//	{
//		val builders = new ArrayList<StructureServiceImpl.CreateStructureOptionsBuilderImpl>()
//		val String userId
//
//		new(String userId)
//		{
//			this.userId = userId
//		}
//
//		override addStructure(int structureId, float xCoord, float yCoord)
//		{
//			builders.add(
//				new StructureServiceImpl.CreateStructureOptionsBuilderImpl(
//					userId, structureId, xCoord, yCoord))
//			
//			return this
//		}
//
//		override addStructure(int structureId, float xCoord, float yCoord,
//			Director<StructureService.CreateStructureOptionsBuilder> director)
//		{
//			val retVal = 
//				new StructureServiceImpl.CreateStructureOptionsBuilderImpl(userId, structureId, xCoord, yCoord)
//			director.apply(retVal)
//			builders.add(retVal)
//			
//			return this
//		}
//
//		def List<StructureForUser> build()
//		{
//			return builders.map[return it.build]
//		}
//	}
//
//	static class CreateStructureOptionsBuilderImpl implements StructureService.CreateStructureOptionsBuilder
//	{
//		var StructureForUser retVal
//		var StructureService.ConstructionStatusKind constructionStatus
//
//		new(String userUuid, int structureId, float xCoord, float yCoord)
//		{
//			retVal = new StructureForUser()
//			retVal.userId = userUuid
//			retVal.structId = structureId
//			retVal.purchaseTime = TimeUtils.createNow
//			retVal.setXCoord(xCoord)
//			retVal.setYCoord(yCoord)
//			
//			// TODO: What is the correct default for these attributes???
//			retVal.fbInviteStructLvl = 0
//			retVal.orientation = "POSITION_1"
//			
//			this.constructionStatus = StructureService.ConstructionStatusKind::INCOMPLETE
//		}
//
//		override StructureService.CreateStructureOptionsBuilder purchaseTime(Date purchaseTime)
//		{
//			retVal.purchaseTime = purchaseTime
//			return this
//		}
//
//		override constructionStatus(
//			StructureService.ConstructionStatusKind constructionStatus)
//		{
//			this.constructionStatus = constructionStatus
//			return this
//		}
//
//		override fbInviteStructLvl(int fbInviteStructLvl)
//		{
//			retVal.fbInviteStructLvl = fbInviteStructLvl
//			return this
//		}
//
//		def StructureForUser build()
//		{
//			switch constructionStatus {
//				case StructureService.ConstructionStatusKind::INCOMPLETE : {
//					retVal.complete = false
//					retVal.lastRetrieved = null
//				}
//				case StructureService.ConstructionStatusKind::COMPLETE_AS_EMPTY : {
//					retVal.complete = true
//					retVal.lastRetrieved = retVal.purchaseTime
//				}
//				case StructureService.ConstructionStatusKind::COMPLETE_AS_FULL : {
//					retVal.complete = true
//					retVal.lastRetrieved = 
//						TimeUtils.createDateAddDays(retVal.purchaseTime, -7)
//				}
//			}
//			
//			return retVal
//		}
//	}
//
//	/** 
// 	 */
//	override beginUpgradingUserStruct(StructureForUser sfu, Date upgradeTime)
//	{
//		val currentStruct = structureRepository.findOne(sfu.structId)
//		val nextLevelStructId = currentStruct.successorStruct.id
//		sfu.structId = nextLevelStructId
//		sfu.purchaseTime = upgradeTime
//		sfu.complete = false
//		structureForUserRepository.save(sfu)
//	}
//
//	def String toString(StructureService.OrientationKind orientation)
//	{
//		var String retVal
//		switch orientation {
//			case StructureService.OrientationKind::POSITION_ONE : 
//			{
//				retVal = "POSITION_1"
//			}
//			case StructureService.OrientationKind::POSITION_TWO :
//			{
//				retVal = "POSITION_2"
//			}
//		}
//		
//		return retVal
//	}
//
//	override moveUserStructure(String userId, String userStructId, CoordinatePair cp) {
//		var success = false
//		val isRootTx = txManager.requireTransaction()
//		try {
//			structureForUserRepository.save(
//				structureForUserRepository.load(userId, userStructId) => [ sfu |
//					Lvl6MobstersConditions.lvl6Precondition(
//						sfu !== null,
//						Lvl6MobstersStatusCode.FAIL_OTHER,
//						"No StructureForUser for userUuid=%s, userStructId=%s",
//						userId, userStructId
//					)
//			
//					sfu.setXCoord(cp.x)
//					sfu.setYCoord(cp.y)
//				]
//			)
//			
//			success = true
//		} finally {
//			if (success) {
//				if (isRootTx) {
//					txManager.commit()
//				}
//			} else {
//				txManager.rollback()
//			}
//		}
//	}
//	
//	override finishConstructingCompletedUserStructures( 
//		String userId, List<String> userStructIdList, Date now) 
//	{
//		// TODO: Transactionify
//		structureForUserRepository.saveEach(
//			structureForUserRepository.loadEach(userId, userStructIdList) => [sfuList |
//				checkIfUserCanFinishConstruction(userStructIdList, now, sfuList);
//				
//				//for each structure, select the ones that can indeed be constructed
//				List<StructureForUser> canBeConstructedStructureForUsers =
//				selectUserStructsThatCanFinishConstruction(now, sfuList);
//			]
//		);
//			canBeConstructedStructureForUsers
//		
//		
//	}
//
//	private void checkIfUserCanFinishConstruction(
//			List<String> userStructIdList, Date now,
//			List<StructureForUser> sfuList)
//	{
//		if ( CollectionUtils.lacksSubstance(sfuList) ) {
//			LOG.error("no StructureForUsers for ids: " + userStructIdList);
//			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
//		}
//		
//		if ( userStructIdList.size() != sfuList.size() ) {
//			LOG.error( "some structs missing. userStructId=" + userStructIdList +
//					", StructureForUsers=" + sfuList
//			);
//			throw new Lvl6MobstersException(Lvl6MobstersStatusCode.FAIL_OTHER);
//		}
//		
//	}
//
//	private def List<StructureForUser> selectUserStructsThatCanFinishConstruction(Date now,
//			List<StructureForUser> sfuList)
//	{
//		List<StructureForUser> canBeConstructedStructureForUsers =
//				new ArrayList<StructureForUser>();
//		Map<Integer, Structure> idToStructureMap = getStructIdsToStructures(sfuList);
//		for ( StructureForUser sfu : sfuList )
//		{
//			int structId = sfu.getStructId();
//			if (!idToStructureMap.containsKey(structId)) {
//				LOG.warn("no struct in db exists with id " + structId +
//						", structureForUser=" + sfu
//				);
//				continue;
//			}
//			Structure struct = idToStructureMap.get(structId);
//			
//			Date purchaseDate = sfu.getPurchaseTime();
//			
//			if (null == purchaseDate) {
//				LOG.warn("user struct has never been bought or purchased according to db. " + sfu);
//				continue;
//			}
//			
//			long buildTimeMillis = 60000*struct.getMinutesToBuild();
//			long timeBuildFinished = purchaseDate.getTime() + buildTimeMillis;
//			if (timeBuildFinished > buildTimeMillis) {
//				LOG.warn("the building is not done yet. userstruct=" + ", client time is " +
//          		now + ", purchase time was " + purchaseDate + ", buildTimeMillis=" + buildTimeMillis);
//				continue;
//			}
//			
//			sfu.setLastRetrieved(new Date(timeBuildFinished));
//			canBeConstructedStructureForUsers.add(sfu);
//		}
//		
//		return canBeConstructedStructureForUsers;
//	}
//	
//	private def Map<Integer, Structure> getStructIdsToStructures(List<StructureForUser> sfuList) {
//		Set<Integer> structureIds = new HashSet<Integer>();
//		
//		for ( StructureForUser sfu : sfuList ) {
//			structureIds.add(sfu.getStructId());
//		}
//		
//		List<Structure> structureList = structureRepository.findAll(structureIds);
//		
//		Map<Integer, Structure> structureMap = new HashMap<Integer, Structure>();
//		for (Structure s : structureList) {
//			structureMap.put(s.getId(), s);
//		}
//		return structureMap;
//	}
//    
//	override speedUpConstructingUserStructures( StructureForUser sfu, Date upgradeTime )
//	{
//		sfu.setLastRetrieved(upgradeTime);
//		sfu.setComplete(true);
//		structureForUserRepository.save(sfu);
//	}
//
//	def void setObstacleForUserRepository(ObstacleForUserRepository obstacleForUserRepository)
//	{
//		this.obstacleForUserRepository = obstacleForUserRepository
//	}
//
//	def void setStructureForUserRepository(StructureForUserRepository structureForUserRepository)
//	{
//		this.structureForUserRepository = structureForUserRepository
//	}
//
//	def void setStructureRepository(StructureRepository structureRepository)
//	{
//		this.structureRepository = structureRepository
//	}
//}

