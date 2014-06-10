package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.common.utils.TimeUtils;
import com.lvl6.mobsters.dynamo.CepfuRaidHistory;
import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.MonsterForUser;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.UserClan;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoRequestProto.ClanInfoGrabType;
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventClanProto.RetrieveClanInfoResponseProto.RetrieveClanInfoStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.RetrieveClanInfoRequestEvent;
import com.lvl6.mobsters.events.response.RetrieveClanInfoResponseEvent;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.MinimumUserProtoForClans;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.FullUserMonsterProto;
import com.lvl6.mobsters.noneventproto.NoneventMonsterProto.UserCurrentMonsterTeamProto;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class RetrieveClanInfoController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(RetrieveClanInfoController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected TimeUtils timeUtils;

	@Autowired
	protected HazelcastPvpUtil hazelcastPvpUtil;

	public RetrieveClanInfoController()
	{
		numAllocatedThreads = 8;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new RetrieveClanInfoRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_RETRIEVE_CLAN_INFO_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final RetrieveClanInfoRequestProto reqProto =
		    ((RetrieveClanInfoRequestEvent) event).getRetrieveClanInfoRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final int clanId = reqProto.getClanUuid();
		final String clanName = reqProto.getClanName();
		final int beforeClanId = reqProto.getBeforeThisClanUuid();
		final ClanInfoGrabType grabType = reqProto.getGrabType();

		final RetrieveClanInfoResponseProto.Builder resBuilder =
		    RetrieveClanInfoResponseProto.newBuilder();
		resBuilder.setSender(senderProto);
		resBuilder.setIsForBrowsingList(reqProto.getIsForBrowsingList());
		resBuilder.setIsForSearch(false);

		if (reqProto.hasClanName()) {
			resBuilder.setClanName(clanName);
		}
		if (reqProto.hasClanId()) {
			resBuilder.setClanId(clanId);
		}

		try {
			// User user =
			// RetrieveUtils.userRetrieveUtils().getUserById(senderProto.getUserUuid());

			final boolean legitCreate = checkLegitCreate(resBuilder, clanName, clanId);

			if (legitCreate) {
				if (reqProto.hasClanName()
				    || reqProto.hasClanId()) {
					if ((grabType == ClanInfoGrabType.ALL)
					    || (grabType == ClanInfoGrabType.CLAN_INFO)) {
						List<Clan> clanList = null;
						if (reqProto.hasClanName()) {
							// Can search for clan name or tag name
							clanList =
							    ClanRetrieveUtils.getClansWithSimilarNameOrTag(clanName,
							        clanName);
							resBuilder.setIsForSearch(true);
						} else if (reqProto.hasClanId()) {
							final Clan clan = ClanRetrieveUtils.getClanWithId(clanId);
							clanList = new ArrayList<Clan>();
							clanList.add(clan);
						}
						setClanProtosWithSize(resBuilder, clanList);

					}
					if ((grabType == ClanInfoGrabType.ALL)
					    || (grabType == ClanInfoGrabType.MEMBERS)) {
						LOG.info("getUserClansRelatedToClan clanId="
						    + clanId);
						final List<UserClan> userClans = RetrieveUtils.userClanRetrieveUtils()
						    .getUserClansRelatedToClan(clanId);
						LOG.info("user clans related to clanId:"
						    + clanId
						    + "\t "
						    + userClans);
						final Set<Integer> userUuids = new HashSet<Integer>();
						// this is because clan with 1k+ users overflows buffer
						// when sending to client and need to
						// include clan owner
						// UPDATE: well, the user clan status now specifies
						// whether a person is a leader, so
						// owner id in clan is not needed
						// Clan c = ClanRetrieveUtils.getClanWithId(clanId);
						// int ownerId = c.getOwnerId();
						for (final UserClan uc : userClans) {
							userUuids.add(uc.getUserUuid());
						}
						// userUuids.add(ownerId);
						final List<Integer> userIdList = new ArrayList<Integer>(userUuids);

						// get the users
						final Map<Integer, User> usersMap = RetrieveUtils.userRetrieveUtils()
						    .getUsersByUuids(userIdList);
						// get the monster battle teams for the users
						final Map<Integer, List<MonsterForUser>> userUuidsToMonsterTeams =
						    RetrieveUtils.monsterForUserRetrieveUtils()
						        .getUserUuidsToMonsterTeamForUserUuids(userIdList);

						final int nDays =
						    ControllerConstants.CLAN_EVENT_PERSISTENT__NUM_DAYS_FOR_RAID_HISTORY;
						// get the clan raid contribution stuff
						final Map<Date, Map<Integer, CepfuRaidHistory>> timesToUserIdToRaidHistory =
						    CepfuRaidHistoryRetrieveUtils.getRaidHistoryForPastNDaysForClan(
						        clanId, nDays, new Date(), timeUtils);
						final Map<Integer, Float> userIdToClanRaidContribution =
						    calculateRaidContribution(timesToUserIdToRaidHistory);

						for (final UserClan uc : userClans) {
							final String userUuid = uc.getUserUuid();
							final User u = usersMap.get(userUuid);

							// user might not have a clan raid entry, so
							float clanRaidContribution = 0F;
							if (userIdToClanRaidContribution.containsKey(userUuid)) {
								clanRaidContribution =
								    userIdToClanRaidContribution.get(userUuid);
							}

							// might be better if just got all user's battle
							// wons from db
							// instead of one by one from hazelcast
							final int battlesWon = getBattlesWonForUser(userUuid);
							final MinimumUserProtoForClans minUser =
							    CreateInfoProtoUtils.createMinimumUserProtoForClans(u,
							        uc.getStatus(), clanRaidContribution, battlesWon);
							resBuilder.addMembers(minUser);

							// create the monster team for this user if possible
							if (userUuidsToMonsterTeams.containsKey(userUuid)) {
								final List<MonsterForUser> monsterTeam =
								    userUuidsToMonsterTeams.get(userUuid);
								final List<FullUserMonsterProto> proto =
								    CreateInfoProtoUtils.createFullUserMonsterProtoList(monsterTeam);

								// create the user monster team proto via the
								// builder
								final UserCurrentMonsterTeamProto.Builder teamForUser =
								    UserCurrentMonsterTeamProto.newBuilder();
								teamForUser.setUserUuid(userUuid);
								teamForUser.addAllCurrentTeam(proto);
								resBuilder.addMonsterTeams(teamForUser.build());
							}

						}

					}
				} else {
					List<Clan> clanList = null;
					if (beforeClanId <= 0) {
						clanList =
						    ClanRetrieveUtils.getMostRecentClans(ControllerConstants.RETRIEVE_CLANS__NUM_CLANS_CAP);
					} else {
						clanList =
						    ClanRetrieveUtils.getMostRecentClansBeforeClanId(
						        ControllerConstants.RETRIEVE_CLANS__NUM_CLANS_CAP, beforeClanId);
						resBuilder.setBeforeThisClanId(reqProto.getBeforeThisClanUuid());
					}

					LOG.info("clanList="
					    + clanList);
					setClanProtosWithSize(resBuilder, clanList);
				}
			}

			final RetrieveClanInfoResponseEvent resEvent =
			    new RetrieveClanInfoResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setRetrieveClanInfoResponseProto(resBuilder.build());
			// write to client
			LOG.info("Writing event: "
			    + resEvent);
			try {
				eventWriter.writeEvent(resEvent);
			} catch (final Throwable e) {
				LOG.error("fatal exception in RetrieveClanInfoController.processRequestEvent",
				    e);
			}
		} catch (final Exception e) {
			LOG.error("exception in RetrieveClanInfo processEvent", e);
		}
	}

	private boolean checkLegitCreate( final Builder resBuilder, final String clanName,
	    final int clanId )
	{
		if (((clanName == null) || (clanName.length() != 0))
		    && (clanId != 0)) {
			resBuilder.setStatus(RetrieveClanInfoStatus.OTHER_FAIL);
			LOG.error("clan name and clan id set");
			return false;
		}
		resBuilder.setStatus(RetrieveClanInfoStatus.SUCCESS);
		return true;
	}

	private void setClanProtosWithSize( final Builder resBuilder, final List<Clan> clanList )
	{

		if ((null == clanList)
		    || clanList.isEmpty()) {
			return;
		}
		final List<Integer> clanUuids = ClanRetrieveUtils.getClanUuidsFromClans(clanList);

		final List<String> statuses = new ArrayList<String>();
		statuses.add(UserClanStatus.LEADER.name());
		statuses.add(UserClanStatus.JUNIOR_LEADER.name());
		statuses.add(UserClanStatus.CAPTAIN.name());
		statuses.add(UserClanStatus.MEMBER.name());

		final Map<Integer, Integer> clanUuidsToSizes = RetrieveUtils.userClanRetrieveUtils()
		    .getClanSizeForClanUuidsAndStatuses(clanUuids, statuses);

		for (final Clan c : clanList) {
			final int clanId = c.getId();
			final int size = clanUuidsToSizes.get(clanId);
			resBuilder.addClanInfo(CreateInfoProtoUtils.createFullClanProtoWithClanSize(c, size));
		}
	}

	// clan raid contribution is calculated through summing all the clanCrDmg
	// summing all of a user's crDmg, and taking dividing
	// sumUserCrDmg by sumClanCrDmg
	private Map<Integer, Float> calculateRaidContribution(
	    final Map<Date, Map<Integer, CepfuRaidHistory>> timesToUserIdToRaidHistory )
	{
		LOG.info("calculating clan raid contribution.");

		// return value
		final Map<Integer, Float> userIdToContribution = new HashMap<Integer, Float>();

		final Map<Integer, Integer> userIdToSumCrDmg = new HashMap<Integer, Integer>();
		int sumClanCrDmg = 0;

		// each date represents a raid
		for (final Date aDate : timesToUserIdToRaidHistory.keySet()) {
			final Map<Integer, CepfuRaidHistory> userIdToRaidHistory =
			    timesToUserIdToRaidHistory.get(aDate);

			sumClanCrDmg += getClanAndCrDmgs(userIdToRaidHistory, userIdToSumCrDmg);
		}

		for (final Integer userUuid : userIdToSumCrDmg.keySet()) {
			final int userCrDmg = userIdToSumCrDmg.get(userUuid);
			final float contribution = ((float) userCrDmg / (float) sumClanCrDmg);

			userIdToContribution.put(userUuid, contribution);
		}

		LOG.info("total clan cr dmg="
		    + sumClanCrDmg
		    + "\t userIdToContribution="
		    + userIdToContribution);

		return userIdToContribution;
	}

	// returns the cr dmg and computes running sum of user's cr dmgs
	private int getClanAndCrDmgs( final Map<Integer, CepfuRaidHistory> userIdToRaidHistory,
	    final Map<Integer, Integer> userIdToSumCrDmg )
	{

		int clanCrDmg = 0;// return value

		// now for each user in this raid, sum up all their damages
		for (final Integer userUuid : userIdToRaidHistory.keySet()) {
			final CepfuRaidHistory raidHistory = userIdToRaidHistory.get(userUuid);

			// all of the clanCrDmg values for "userIdToRaidHistory" are the
			// same
			clanCrDmg = raidHistory.getClanCrDmg();

			final int userCrDmg = raidHistory.getCrDmgDone();

			// sum up the damage for this raid with the current running sum for
			// this user
			// user might not exist in current running sum
			int userCrDmgSoFar = 0;
			if (userIdToSumCrDmg.containsKey(userUuid)) {
				userCrDmgSoFar = userIdToSumCrDmg.get(userUuid);
			}

			final int totalUserCrDmg = userCrDmg
			    + userCrDmgSoFar;

			userIdToSumCrDmg.put(userUuid, totalUserCrDmg);
		}

		return clanCrDmg;
	}

	private int getBattlesWonForUser( final String userUuid )
	{
		final PvpUser pu = getHazelcastPvpUtil().getPvpUser(userUuid);

		int battlesWon = 0;
		if (null != pu) {
			battlesWon = pu.getBattlesWon();
		}

		return battlesWon;
	}

	public TimeUtils getTimeUtils()
	{
		return timeUtils;
	}

	public void setTimeUtils( final TimeUtils timeUtils )
	{
		this.timeUtils = timeUtils;
	}

	public HazelcastPvpUtil getHazelcastPvpUtil()
	{
		return hazelcastPvpUtil;
	}

	public void setHazelcastPvpUtil( final HazelcastPvpUtil hazelcastPvpUtil )
	{
		this.hazelcastPvpUtil = hazelcastPvpUtil;
	}
}
