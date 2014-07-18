package com.lvl6.mobsters.controllers.todo

import com.lvl6.mobsters.dynamo.User
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager
import com.lvl6.mobsters.events.EventsToDispatch
import com.lvl6.mobsters.events.RequestEvent
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest
import com.lvl6.mobsters.server.EventController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component

@Component
@DependsOn('gameServer')
class LoadCityController extends EventController
{
	static var LOG = LoggerFactory::getLogger(typeof(LoadCityController))
	@Autowired
	protected var DataServiceTxManager svcTxManager

	new()
	{
		numAllocatedThreads = 3
	}

	override createRequestEvent()
	{
		new LoadCityRequestEvent()
	}

	override getEventType()
	{
		EventProtocolRequest.C_LOAD_CITY_EVENT
	}

	protected override processRequestEvent(RequestEvent event, EventsToDispatch eventWriter)
	throws Exception {
		val reqProto = ((event as LoadCityRequestEvent)).loadCityRequestProto
		val senderProto = reqProto.sender
		val userUuid = senderProto.userUuid
		val cityId = reqProto.cityUuid
		val city = CityRetrieveUtils::getCityForCityId(cityId)
		val resBuilder = LoadCityResponseProto::newBuilder
		resBuilder.sender = senderProto
		resBuilder.cityId = cityId
		resBuilder.status = LoadCityStatus.SUCCESS
		try
		{
			val user = RetrieveUtils::userRetrieveUtils.getUserById(userUuid)
			val legitCityLoad = checkLegitCityLoad(resBuilder, user, city)
			if (legitCityLoad)
			{
				val neutralCityElements = CityElementsRetrieveUtils::
					getCityElementsForCity(cityId)
				if (neutralCityElements !== null)
				{
					for (nce : neutralCityElements)
					{
						resBuilder.addCityElements(
							CreateInfoProtoUtils::createCityElementProtoFromCityElement(nce))
					}
				}
			}
			val resEvent = new LoadCityResponseEvent(senderProto.userUuid)
			resEvent.tag = event.tag
			resEvent.loadCityResponseProto = resBuilder.build
			LOG.info('Writing event: ' + resEvent)
			try
			{
				eventWriter.writeEvent(resEvent)
			}
			catch (Throwable e)
			{
				LOG.error('fatal exception in LoadCityController.processRequestEvent', e)
			}
		}
		catch (Exception e)
		{
			LOG.error('exception in LoadCity processEvent', e)
		}
		finally
		{
		}
	}

	private def checkLegitCityLoad(Builder resBuilder, User user, City city)
	{
		if ((city === null) || (user === null))
		{
			resBuilder.status = LoadCityStatus.OTHER_FAIL
			LOG.error('city or user is null. city=' + city + ', user=' + user)
			return false;
		}
		resBuilder.status = LoadCityStatus.SUCCESS
		true
	}
}
