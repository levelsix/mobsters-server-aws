//package com.lvl6.mobsters.controllers.todo
//
//import com.lvl6.mobsters.dynamo.StructureForUser
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
//import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityResponseProto
//import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityResponseProto.Builder
//import com.lvl6.mobsters.eventproto.EventCityProto.LoadPlayerCityResponseProto.LoadPlayerCityStatus
//import com.lvl6.mobsters.events.EventsToDispatch
//import com.lvl6.mobsters.events.RequestEvent
//import com.lvl6.mobsters.events.request.LoadPlayerCityRequestEvent
//import com.lvl6.mobsters.events.response.LoadPlayerCityResponseEvent
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
//import com.lvl6.mobsters.server.EventController
//import java.util.ArrayList
//import java.util.List
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.DependsOn
//import org.springframework.stereotype.Component
//
//@Component
//@DependsOn('gameServer')
//class LoadPlayerCityController extends EventController
//{
//	static var LOG = LoggerFactory::getLogger(typeof(LoadPlayerCityController))
//	@Autowired
//	protected var DataServiceTxManager svcTxManager
//	@Autowired
//	protected var ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil
//
//	new()
//	{
//		numAllocatedThreads = 10
//	}
//
//	override createRequestEvent()
//	{
//		new LoadPlayerCityRequestEvent()
//	}
//
//	override getEventType()
//	{
//		EventProtocolRequest.C_LOAD_PLAYER_CITY_EVENT
//	}
//
//	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
//	throws Exception {
//		val reqProto = ((event as LoadPlayerCityRequestEvent)).loadPlayerCityRequestProto
//		val senderProto = reqProto.sender
//		val cityOwnerId = reqProto.cityOwnerUuid
//		val resBuilder = LoadPlayerCityResponseProto::newBuilder
//		resBuilder.sender = senderProto
//		resBuilder.status = LoadPlayerCityStatus.SUCCESS
//		svcTxManager.beginTransaction
//		try
//		{
//			val owner = RetrieveUtils::userRetrieveUtils.getUserById(cityOwnerId)
//			val userStructs = RetrieveUtils::userStructRetrieveUtils.
//				getUserStructsForUser(cityOwnerId)
//			setResponseUserStructs(resBuilder, userStructs)
//			setObstacleStuff(resBuilder, cityOwnerId)
//			val userCityExpansionDataList = ExpansionPurchaseForUserRetrieveUtils::
//				getUserCityExpansionDatasForUserUuid(senderProto.userUuid)
//			val userCityExpansionDataProtoList = new ArrayList<UserCityExpansionDataProto>()
//			if (userCityExpansionDataList !== null)
//			{
//				for (uced : userCityExpansionDataList)
//				{
//					userCityExpansionDataProtoList.add(
//						CreateInfoProtoUtils::
//							createUserCityExpansionDataProtoFromUserCityExpansionData(uced))
//				}
//				resBuilder.addAllUserCityExpansionDataProtoList(userCityExpansionDataProtoList)
//			}
//			if (owner === null)
//			{
//				LOG.error('owner is null for ownerId = ' + cityOwnerId)
//			}
//			else
//			{
//				resBuilder.cityOwner = CreateInfoProtoUtils::
//					createMinimumUserProtoFromUser(owner)
//				val resEvent = new LoadPlayerCityResponseEvent(senderProto.userUuid)
//				resEvent.tag = event.tag
//				resEvent.loadPlayerCityResponseProto = resBuilder.build
//				LOG.info('Writing event: ' + resEvent)
//				try
//				{
//					eventWriter.writeEvent(resEvent)
//				}
//				catch (Throwable e)
//				{
//					LOG.error('fatal exception in LoadPlayerCityController.processRequestEvent',
//						e)
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			LOG.error('exception in LoadPlayerCity processEvent', e)
//		}
//		finally
//		{
//			svcTxManager.commit
//		}
//	}
//
//	private def setResponseUserStructs(Builder resBuilder, List<StructureForUser> userStructs)
//	{
//		if (userStructs !== null)
//		{
//			for (userStruct : userStructs)
//			{
//				resBuilder.addOwnerNormStructs(
//					CreateInfoProtoUtils::createFullUserStructureProtoFromUserstruct(userStruct))
//			}
//		}
//		else
//		{
//			resBuilder.status = LoadPlayerCityStatus.FAIL_OTHER
//			LOG.error('user structs found for user is null')
//		}
//	}
//
//	private def setObstacleStuff(Builder resBuilder, String userUuid)
//	{
//		val ofuList = obstacleForUserRetrieveUtil.getUserObstacleForUser(userUuid)
//		if (null === ofuList)
//		{
//			return;
//		}
//		for (ofu : ofuList)
//		{
//			val uop = CreateInfoProtoUtils::createUserObstacleProto(ofu)
//			resBuilder.addObstacles(uop)
//		}
//	}
//
//	def getObstacleForUserRetrieveUtil()
//	{
//		obstacleForUserRetrieveUtil
//	}
//
//	def setObstacleForUserRetrieveUtil(ObstacleForUserRetrieveUtil obstacleForUserRetrieveUtil)
//	{
//		this.obstacleForUserRetrieveUtil = obstacleForUserRetrieveUtil
//	}
//}
