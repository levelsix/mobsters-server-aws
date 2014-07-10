package com.lvl6.mobsters.controllers.todo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadCityRequestProto;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadCityResponseProto;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadCityResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventCityProto.LoadCityResponseProto.LoadCityStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.LoadCityRequestEvent;
import com.lvl6.mobsters.events.response.LoadCityResponseEvent;
import com.lvl6.mobsters.info.City;
import com.lvl6.mobsters.info.CityElement;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class LoadCityController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(LoadCityController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	public LoadCityController()
	{
		numAllocatedThreads = 3;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new LoadCityRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_LOAD_CITY_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final LoadCityRequestProto reqProto =
		    ((LoadCityRequestEvent) event).getLoadCityRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final int cityId = reqProto.getCityUuid();
		final City city = CityRetrieveUtils.getCityForCityId(cityId);

		final LoadCityResponseProto.Builder resBuilder = LoadCityResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setCityId(cityId);

		resBuilder.setStatus(LoadCityStatus.SUCCESS);

		// svcTxManager.beginTransaction();
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(userUuid);

			final boolean legitCityLoad = checkLegitCityLoad(resBuilder, user, city);// ,
			// currentCityRankForUser);

			if (legitCityLoad) {
				final List<CityElement> neutralCityElements =
				    CityElementsRetrieveUtils.getCityElementsForCity(cityId);
				if (neutralCityElements != null) {
					for (final CityElement nce : neutralCityElements) {
						resBuilder.addCityElements(CreateInfoProtoUtils.createCityElementProtoFromCityElement(nce));
					}
				}

				// List<Monster> bosses =
				// MonsterRetrieveUtils.getBossesForCityId(cityId);
				// if (bosses != null && bosses.size() > 0) {
				// List<Integer> bossUuids = new ArrayList<Integer>();
				// for (Monster b : bosses) {
				// bossUuids.add(b.getId());
				// }
				// setResponseUserBossInfos(resBuilder, bossUuids,
				// user.getId());
				// }

			}

			final LoadCityResponseEvent resEvent =
			    new LoadCityResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setLoadCityResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in LoadCityController.processRequestEvent", e);
			}

		} catch (final Exception e) {
			LOG.error("exception in LoadCity processEvent", e);
		} finally {
			// svcTxManager.commit();
		}
	}

	// for each of this city's bosses send the corresponding user_bosses
	// private void setResponseUserBossInfos(Builder resBuilder, List<Integer>
	// bossUuids, String userUuid) {
	// boolean livingBossesOnly = false;
	// List<UserBoss> userBosses = UserBossRetrieveUtils
	// .getUserBossesForUserUuid(userUuid, livingBossesOnly);
	// for (UserBoss b : userBosses) {
	// if (bossUuids.contains(b.getBossId())) {
	// resBuilder.addUserBosses(CreateInfoProtoUtils.createFullUserBossProtoFromUserBoss(b));
	// }
	// }
	// }

	private boolean checkLegitCityLoad( final Builder resBuilder, final User user,
	    final City city )
	{// , int currentCityRankForUser) {
		if ((city == null)
		    || (user == null)) {
			resBuilder.setStatus(LoadCityStatus.OTHER_FAIL);
			LOG.error("city or user is null. city="
			    + city
			    + ", user="
			    + user);
			return false;
		}

		resBuilder.setStatus(LoadCityStatus.SUCCESS);
		return true;
	}

}
