package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Tuple;

import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.RetrieveTournamentRankingsRequestEvent;
import com.lvl6.mobsters.events.response.RetrieveTournamentRankingsResponseEvent;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.leaderboards.LeaderBoardUtilImpl;
import com.lvl6.properties.ControllerConstants;
import com.lvl6.mobsters.eventproto.EventTournamentProto.RetrieveTournamentRankingsRequestProto;
import com.lvl6.mobsters.eventproto.EventTournamentProto.RetrieveTournamentRankingsResponseProto;
import com.lvl6.mobsters.eventproto.EventTournamentProto.RetrieveTournamentRankingsResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventTournamentProto.RetrieveTournamentRankingsResponseProto.RetrieveTournamentStatus;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.server.Locker;
import com.lvl6.utils.CreateInfoProtoUtils;
import com.lvl6.utils.RetrieveUtils;

@Component
@DependsOn("gameServer")
public class RetrieveTournamentRankingsController extends EventController {

	private static Logger LOG = LoggerFactory.getLogger(RetrieveTournamentRankingsController.class);
  }.getClass().getEnclosingClass());

  @Autowired
 protected DataServiceTxManager svcTxManager;

 @Autowired
  public LeaderBoardUtilImpl leader;

  public RetrieveTournamentRankingsController() {
    numAllocatedThreads = 5;
  }

  @Override
  public RequestEvent createRequestEvent() {
    return new RetrieveTournamentRankingsRequestEvent();
  }

  @Override
  public EventProtocolRequest getEventType() {
    return EventProtocolRequest.C_RETRIEVE_TOURNAMENT_RANKINGS_EVENT;
  }

  @Override
  protected void processRequestEvent( final RequestEvent event, final EventsToDispatch eventWriter ) throws Exception {
    final RetrieveTournamentRankingsRequestProto reqProto = ((RetrieveTournamentRankingsRequestEvent) event)
        .getRetrieveTournamentRankingsRequestProto();

    final MinimumUserProto senderProto = reqProto.getSender();

    final int eventId = reqProto.getEventUuid();
    final int afterThisRank = reqProto.getAfterThisRank();

    final RetrieveTournamentRankingsResponseProto.Builder resBuilder = RetrieveTournamentRankingsResponseProto
        .newBuilder();
    resBuilder.setSender(senderProto);
    resBuilder.setEventId(eventId);
    resBuilder.setAfterThisRank(afterThisRank);

    svcTxManager.beginTransaction();
    try {
      final User user = RetrieveUtils.userRetrieveUtils().getUserById(senderProto.getUserUuid());
      final String userUuid = user.getId();
      final boolean legitRetrieval = checkLegitRetrieval(resBuilder, user,	eventId);
      Map<Integer, UserRankScore> lurs = null;
      if (legitRetrieval) {
        final int rank = (int) leader.getRankForEventAndUser(eventId, userUuid);
        final double score = leader.getScoreForEventAndUser(eventId, userUuid);

        resBuilder.setRetriever(CreateInfoProtoUtils.createMinimumUserProtoWithLevelForTournament(
            user,  rank, score));

        //TODO: FIX THIS IMPLEMENTATION
        lurs = getUsersAfterThisRank(eventId, afterThisRank);

        if (lurs != null) {
          final List<User> resultUsers = new ArrayList<User>(RetrieveUtils.userRetrieveUtils().getUsersByUuids(new ArrayList<Integer>(lurs.keySet())).values());
          LOG.debug("Populating leaderboard results for event: "+eventId+" after this rank: "+afterThisRank+" found results: "+resultUsers.size());
          for (final User u : resultUsers) {
            final UserRankScore urs = lurs.get(u.getId());
            resBuilder.addResultPlayers(CreateInfoProtoUtils.createMinimumUserProtoWithLevelForTournament(u, urs.rank, urs.score));
            //null PvpLeagueFromUser means will pull from hazelcast instead
            resBuilder.addFullUsers(CreateInfoProtoUtils.createFullUserProtoFromUser(u, null));
          }
        }
      }

      final RetrieveTournamentRankingsResponseProto resProto = resBuilder.build();
      final RetrieveTournamentRankingsResponseEvent resEvent = new RetrieveTournamentRankingsResponseEvent(senderProto.getUserUuid());
      resEvent.setTag(event.getTag());
      resEvent.setRetrieveTournamentRankingsResponseProto(resProto);

      // write to client
      LOG.info("Writing event: " + resEvent);
      try {
          eventWriter.writeEvent(resEvent);
      } catch (final Throwable e) {
          LOG.error("fatal exception in RetrieveTournamentRankingsController.processRequestEvent", e);
      }
    } catch (final Exception e) {
      LOG.error(
          "exception in RetrieveTournamentController processEvent",
          e);
    } finally {
      svcTxManager.commit();
    }

  }

  private Map<Integer, UserRankScore> getUsersAfterThisRank(final int eventId,	final int afterThisRank) {
    Set<Tuple> usrs = new HashSet<Tuple>();

    usrs = leader.getEventTopN(eventId, afterThisRank, afterThisRank+ControllerConstants.TOURNAMENT_EVENT__MAX_PLAYERS_SENT_AT_ONCE);

    final Map<Integer, UserRankScore> lurs = new LinkedHashMap<Integer, UserRankScore>();
    final Iterator<Tuple> it = usrs.iterator();
    int counter = 1;
    while(it.hasNext()) {
      final Tuple t = it.next();
      final Integer userUuid = Integer.valueOf(t.getElement());
      final UserRankScore urs = new UserRankScore(userUuid, t.getScore(), counter+afterThisRank);
      lurs.put(userUuid, urs);
      LOG.debug(urs.toString());
      counter++;
    }
    return lurs;
  }

  private boolean checkLegitRetrieval(final Builder resBuilder, final User user,
      final int eventId) {
    if ((user == null) || (0 >= eventId)) {
      resBuilder.setStatus(RetrieveTournamentStatus.OTHER_FAIL);
      LOG.error("user is " + user + ", event id="
          + eventId);
      return false;
    }
    resBuilder.setStatus(RetrieveTournamentStatus.SUCCESS);
    return true;
  }

  public class UserRankScore{
    public UserRankScore(final Integer userUuid, final Double score, final Integer rank) {
      super();
      this.userUuid = userUuid;
      this.score = score;
      this.rank = rank;
    }
    Integer userUuid;
    Double score;
    Integer rank;
    @Override
    public String toString() {
      return "UserRankScore [userUuid=" + userUuid + ", rank=" + rank + ", score=" + score + "]";
    }

  }

}
