package com.lvl6.mobsters.controllers.todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.mobsters.dynamo.Clan;
import com.lvl6.mobsters.dynamo.User;
import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsRequestProto;
import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsResponseProto;
import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsResponseProto.Builder;
import com.lvl6.mobsters.eventproto.EventClanProto.ChangeClanSettingsResponseProto.ChangeClanSettingsStatus;
import com.lvl6.mobsters.events.EventsToDispatch;
import com.lvl6.mobsters.events.RequestEvent;
import com.lvl6.mobsters.events.request.ChangeClanSettingsRequestEvent;
import com.lvl6.mobsters.events.response.ChangeClanSettingsResponseEvent;
import com.lvl6.mobsters.info.ClanIcon;
import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
import com.lvl6.mobsters.noneventproto.NoneventClanProto.UserClanStatus;
import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
import com.lvl6.mobsters.server.EventController;

@Component
@DependsOn("gameServer")
public class ChangeClanSettingsController extends EventController
{

	private static Logger LOG = LoggerFactory.getLogger(ChangeClanSettingsController.class);

	@Autowired
	protected DataServiceTxManager svcTxManager;

	@Autowired
	protected ClanIconRetrieveUtils clanIconRetrieveUtils;

	public ChangeClanSettingsController()
	{
		numAllocatedThreads = 4;
	}

	@Override
	public RequestEvent createRequestEvent()
	{
		return new ChangeClanSettingsRequestEvent();
	}

	@Override
	public EventProtocolRequest getEventType()
	{
		return EventProtocolRequest.C_CHANGE_CLAN_SETTINGS_EVENT;
	}

	@Override
	protected void processRequestEvent( final RequestEvent event,
	    final EventsToDispatch eventWriter ) throws Exception
	{
		final ChangeClanSettingsRequestProto reqProto =
		    ((ChangeClanSettingsRequestEvent) event).getChangeClanSettingsRequestProto();

		final MinimumUserProto senderProto = reqProto.getSender();
		final String userUuid = senderProto.getUserUuid();
		final boolean isChangeDescription = reqProto.getIsChangeDescription();
		final String description = reqProto.getDescriptionNow();
		final boolean isChangeJoinType = reqProto.getIsChangeJoinType();
		final boolean requestToJoinRequired = reqProto.getRequestToJoinRequired();
		final boolean isChangeIcon = reqProto.getIsChangeIcon();
		final int iconId = reqProto.getIconUuid();

		final ChangeClanSettingsResponseProto.Builder resBuilder =
		    ChangeClanSettingsResponseProto.newBuilder();
		resBuilder.setStatus(ChangeClanSettingsStatus.FAIL_OTHER);
		resBuilder.setSender(senderProto);

		int clanId = 0;

		if (senderProto.hasClan()
		    && (null != senderProto.getClan())) {
			clanId = senderProto.getClan()
			    .getClanId();
		}
		boolean lockedClan = false;
		if (0 != clanId) {
			lockedClan = getLocker().lockClan(clanId);
		}
		try {
			final User user = RetrieveUtils.userRetrieveUtils()
			    .getUserById(senderProto.getUserUuid());
			final Clan clan = ClanRetrieveUtils.getClanWithId(user.getClanId());

			final boolean legitChange =
			    checkLegitChange(resBuilder, lockedClan, userUuid, user, clanId, clan);

			if (legitChange) {
				// clan will be modified
				writeChangesToDB(resBuilder, clanId, clan, isChangeDescription, description,
				    isChangeJoinType, requestToJoinRequired, isChangeIcon, iconId);
				setResponseBuilderStuff(resBuilder, clanId, clan);
			}

			final ChangeClanSettingsResponseEvent resEvent =
			    new ChangeClanSettingsResponseEvent(senderProto.getUserUuid());
			resEvent.setTag(event.getTag());
			resEvent.setChangeClanSettingsResponseProto(resBuilder.build());

			// if not successful only write to user
			if (!ChangeClanSettingsStatus.SUCCESS.equals(resBuilder.getStatus())) {
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in ChangeClanSettingsController.processRequestEvent",
					    e);
				}

			} else {
				// only write to clan if successful
				server.writeClanEvent(resEvent, clan.getId());
			}

		} catch (final Exception e) {
			LOG.error("exception in ChangeClanSettings processEvent", e);
			try {
				resBuilder.setStatus(ChangeClanSettingsStatus.FAIL_OTHER);
				final ChangeClanSettingsResponseEvent resEvent =
				    new ChangeClanSettingsResponseEvent(userUuid);
				resEvent.setTag(event.getTag());
				resEvent.setChangeClanSettingsResponseProto(resBuilder.build());
				// write to client
				LOG.info("Writing event: "
				    + resEvent);
				try {
					eventWriter.writeEvent(resEvent);
				} catch (final Throwable e) {
					LOG.error(
					    "fatal exception in ChangeClanSettingsController.processRequestEvent",
					    e);
				}
			} catch (final Exception e2) {
				LOG.error("exception2 in ChangeClanSettings processEvent", e);
			}
		} finally {
			if ((0 != clanId)
			    && lockedClan) {
				getLocker().unlockClan(clanId);
			}
		}
	}

	private boolean checkLegitChange( final Builder resBuilder, final boolean lockedClan,
	    final String userUuid, final User user, final int clanId, final Clan clan )
	{

		if (!lockedClan) {
			LOG.error("couldn't obtain clan lock");
			return false;
		}
		if ((user == null)
		    || (clan == null)) {
			resBuilder.setStatus(ChangeClanSettingsStatus.FAIL_OTHER);
			LOG.error("userUuid is "
			    + userUuid
			    + ", user is "
			    + user
			    + "\t clanId is "
			    + clanId
			    + ", clan is "
			    + clan);
			return false;
		}
		if (user.getClanId() <= 0) {
			resBuilder.setStatus(ChangeClanSettingsStatus.FAIL_NOT_IN_CLAN);
			LOG.error("user not in clan");
			return false;
		}

		final List<String> statuses = new ArrayList<String>();
		statuses.add(UserClanStatus.LEADER.name());
		statuses.add(UserClanStatus.JUNIOR_LEADER.name());
		final List<Integer> userUuids = RetrieveUtils.userClanRetrieveUtils()
		    .getUserUuidsWithStatuses(clanId, statuses);

		final Set<Integer> uniqUserUuids = new HashSet<Integer>();
		if ((null != userUuids)
		    && !userUuids.isEmpty()) {
			uniqUserUuids.addAll(userUuids);
		}

		if (!uniqUserUuids.contains(userUuid)) {
			resBuilder.setStatus(ChangeClanSettingsStatus.FAIL_NOT_AUTHORIZED);
			LOG.error("clan member can't change clan description member="
			    + user);
			return false;
		}
		resBuilder.setStatus(ChangeClanSettingsStatus.SUCCESS);
		return true;
	}

	private void writeChangesToDB( final Builder resBuilder, final int clanId, final Clan clan,
	    final boolean isChangeDescription, final String description,
	    final boolean isChangeJoinType, final boolean requestToJoinRequired,
	    final boolean isChangeIcon, final int iconId )
	{

		if (isChangeDescription) {
			if (description.length() > ControllerConstants.CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_DESCRIPTION) {
				resBuilder.setStatus(ChangeClanSettingsStatus.FAIL_OTHER);
				LOG.warn("description is "
				    + description
				    + ", and length of that is "
				    + description.length()
				    + ", max size is "
				    + ControllerConstants.CREATE_CLAN__MAX_CHAR_LENGTH_FOR_CLAN_DESCRIPTION);
			} else {
				clan.setDescription(description);
			}
		}

		if (isChangeJoinType) {
			clan.setRequestToJoinRequired(requestToJoinRequired);
		}

		if (isChangeIcon) {
			final ClanIcon ci = ClanIconRetrieveUtils.getClanIconForId(iconId);
			if (null == ci) {
				resBuilder.setStatus(ChangeClanSettingsStatus.FAIL_OTHER);
				LOG.warn("no clan icon with id="
				    + iconId);
			} else {
				clan.setClanIconId(iconId);
			}
		}

		final int numUpdated =
		    UpdateUtils.get()
		        .updateClan(clanId, isChangeDescription, description, isChangeJoinType,
		            requestToJoinRequired, isChangeIcon, iconId);

		LOG.info("numUpdated (should be 1)="
		    + numUpdated);
	}

	private void setResponseBuilderStuff( final Builder resBuilder, final int clanId,
	    final Clan clan )
	{
		final List<Integer> clanIdList = Collections.singletonList(clanId);

		final List<String> statuses = new ArrayList<String>();
		statuses.add(UserClanStatus.LEADER.name());
		statuses.add(UserClanStatus.JUNIOR_LEADER.name());
		statuses.add(UserClanStatus.CAPTAIN.name());
		statuses.add(UserClanStatus.MEMBER.name());
		final Map<Integer, Integer> clanIdToSize = RetrieveUtils.userClanRetrieveUtils()
		    .getClanSizeForClanUuidsAndStatuses(clanIdList, statuses);

		resBuilder.setMinClan(CreateInfoProtoUtils.createMinimumClanProtoFromClan(clan));

		final int size = clanIdToSize.get(clanId);
		resBuilder.setFullClan(CreateInfoProtoUtils.createFullClanProtoWithClanSize(clan, size));
	}

	public ClanIconRetrieveUtils getClanIconRetrieveUtils()
	{
		return clanIconRetrieveUtils;
	}

	public void setClanIconRetrieveUtils( final ClanIconRetrieveUtils clanIconRetrieveUtils )
	{
		this.clanIconRetrieveUtils = clanIconRetrieveUtils;
	}

}
