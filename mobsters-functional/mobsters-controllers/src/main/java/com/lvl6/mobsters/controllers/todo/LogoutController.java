//package com.lvl6.mobsters.controllers.todo;
//
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//
//import com.lvl6.mobsters.dynamo.User;
//import com.lvl6.mobsters.dynamo.setup.DataServiceTxManager;
//import com.lvl6.mobsters.eventproto.EventUserProto.LogoutRequestProto;
//import com.lvl6.mobsters.events.EventsToDispatch;
//import com.lvl6.mobsters.events.RequestEvent;
//import com.lvl6.mobsters.events.request.LogoutRequestEvent;
//import com.lvl6.mobsters.info.ConnectedPlayer;
//import com.lvl6.mobsters.noneventproto.ConfigEventProtocolProto.EventProtocolRequest;
//import com.lvl6.mobsters.noneventproto.NoneventUserProto.MinimumUserProto;
//import com.lvl6.mobsters.server.EventController;
//
//@Component
//@DependsOn("gameServer")
//public class LogoutController extends EventController {
//
//	private static Logger LOG = LoggerFactory.getLogger(LogoutController.class);
//	}.getClass().getEnclosingClass());
//
//	public LogoutController() {
//		numAllocatedThreads = 4;
//	}
//
//	@Resource(name = "playersByPlayerId")
//	protected Map<Integer, ConnectedPlayer> playersByPlayerId;
//
//	public Map<Integer, ConnectedPlayer> getPlayersByPlayerId() {
//		return playersByPlayerId;
//	}
//
//	public void setPlayersByPlayerId(final Map<Integer, ConnectedPlayer> playersByPlayerId) {
//		this.playersByPlayerId = playersByPlayerId;
//	}
//
//	@Autowired
//	protected DataServiceTxManager svcTxManager;
//
//	@Autowired
//  protected HazelcastPvpUtil hazelcastPvpUtil;
//  
//	@Autowired
//	protected PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil;
//	
//	@Override
//	public RequestEvent createRequestEvent() {
//		return new LogoutRequestEvent();
//	}
//
//	@Override
//	public EventProtocolRequest getEventType() {
//		return EventProtocolRequest.C_LOGOUT_EVENT;
//	}
//
//	@Override
//	protected void processRequestEvent( final RequestEvent event, final EventsToDispatch eventWriter ) throws Exception {
//		final LogoutRequestProto reqProto = ((LogoutRequestEvent) event)
//				.getLogoutRequestProto();
//
//		final MinimumUserProto sender = reqProto.getSender();
//		final String userUuid = sender.getUserUuid();
//		final Timestamp lastLogout = new Timestamp(new Date().getTime());
//
//		if (userUuid > 0) {
//			svcTxManager.beginTransaction();
//			try {
//				final User user = RetrieveUtils.userRetrieveUtils().getUserById(userUuid);
//				if (null != user) {
//					//TODO: figure out if still deducting elo from user after logging out
//					//FOR NOW DON'T DO THE FOLLOWING
//					//if user has unfinished battle, reward defender and penalize attacker
////					List<Integer> eloChangeList = new ArrayList<Integer>();
////					pvpBattleStuff(user, userUuid, eloChangeList, lastLogout);
////					
////					int eloChange = 0;
////					if (!eloChangeList.isEmpty()) {
////						eloChange = eloChangeList.get(0);
////					}
//					if (!user.updateLastLogout(lastLogout)) {
//						LOG.error("problem with updating user's last logout time for user "	+ userUuid);
//					}
//			    if (!InsertUtils.get().insertLastLoginLastLogoutToUserSessions(user.getId(), null, lastLogout)) {
//			      LOG.error("problem with inserting last logout time for user " + user + ", logout=" + lastLogout);
//			    }
//			    final String udid = user.getUdid();
//			    final boolean isLogin = false;
//			    final boolean isNewUser = false;
//			    InsertUtils.get().insertIntoLoginHistory(udid, userUuid, lastLogout,
//			        isLogin, isNewUser);
//			    
//			    
//			    //put this user back into pool of people who can be attacked,
//			    //don't really need to, since will most likely still be there. eh might as well
////			    int elo = user.getElo();
////			    String userIdStr = String.valueOf(userUuid);
////			    Date shieldEndTime = user.getShieldEndTime();
////			    Date inBattleEndTime = user.getInBattleShieldEndTime();
////			    PvpUser userOpu = new PvpUser(userIdStr, elo, shieldEndTime, inBattleEndTime);
////			    getHazelcastPvpUtil().replacePvpUser(userOpu, userUuid);
////			    
//				}
//				LOG.info("Player logged out: "+userUuid);
//				playersByPlayerId.remove(userUuid);
//			} catch (final Exception e) {
//				LOG.error("exception in updating user logout", e);
//			} finally {
//				svcTxManager.commit();
//			}
//		} else {
//			LOG.error("cannot update last logout because playerid of sender:"+sender.getName()+" is <= 0, it's "	+ userUuid);
//		}
//		// TODO: clear cache
//	}
//	
//	/*
//	private void pvpBattleStuff(User user, String userUuid, List<Integer> eloChange,
//			Timestamp now) {
//		PvpBattleForUser battle = PvpBattleForUserRetrieveUtils
//  			.getPvpBattleForUserForAttacker(userUuid);
//		
//		if (null == battle) {
//			return;
//		}
//
//		PvpLeagueForUser plfu = getPvpLeagueForUserRetrieveUtil()
//				.getUserPvpLeagueForId(userUuid);
//		//capping max elo attacker loses
//		int eloAttackerLoses = battle.getAttackerLoseEloChange();
//		if (plfu.getElo() + eloAttackerLoses < 0) {
//			eloAttackerLoses = -1 * plfu.getElo();
//		}
//		int defenderId = battle.getDefenderId();
//		int eloDefenderWins = battle.getDefenderWinEloChange();
//		
//		//user has unfinished battle, reward defender and penalize attacker
//  	//nested try catch's in order to prevent exception bubbling up, all because of
//  	//some stinkin' elo XP
//  	try {
//  		//eloChange will be filled up if defender is real
//  		penalizeUserForLeavingGameWhileInPvp(userUuid, defenderId, eloAttackerLoses,
//  				eloDefenderWins, now, battle, eloChange);
//  	} catch (Exception e2) {
//  		LOG.error("could not successfully penalize, reward attacker, defender respectively." +
//  				" battle=" + battle, e2);
//  	}
//	} */
//	
//	/*
//	private void penalizeUserForLeavingGameWhileInPvp(String userUuid, int defenderId,
//			int eloAttackerLoses, int eloDefenderWins, Timestamp now, PvpBattleForUser battle,
//			List<Integer> eloChange) {
//		//NOTE: this lock ordering might result in a temp deadlock
//		//doesn't reeeally matter if can't penalize defender...
//		User defender = null;
//		PvpUser defenderOpu = null;
//		
//
//		//only lock real users
////		if (0 != defenderId) {
////			getLocker().lockPlayer(defenderId, this.getClass().getSimpleName());
////		}
//		try {
//			if (0 != defenderId) {
//				defender = RetrieveUtils.userRetrieveUtils().getUserById(defenderId);
//				defenderOpu = getHazelcastPvpUtil().getPvpUser(defenderId);
//			}
//			
//			//update attacker
//			eloChange.add(eloAttackerLoses);
//			
//			//TODO: figure out if still doing any of this
//			//update defender if real, might need to cap defenderElo, defender can now be
//			//attacked
////			if (null != defender) {
////				defender.updateEloInBattleEndTime(eloDefenderWins, now);
////				int defenderElo = defender.getElo();                    
////				defenderOpu = new PvpUser();
////				defenderOpu.setElo(defenderElo);                        
////				Date nowDate = new Date(now.getTime());                 
////				defenderOpu.setInBattleEndTime(nowDate);                
////				getHazelcastPvpUtil().replacePvpUser(defenderOpu, defenderId);
////			}
////			if (null != defenderOpu) { //update if exists
////				int defenderElo = defender.getElo();
////				defenderOpu.setElo(defenderElo);
////				Date nowDate = new Date(now.getTime());
////				defenderOpu.setInBattleEndTime(nowDate);
////				getHazelcastPvpUtil().updateOfflinePvpUser(defenderOpu);
////			}
//			
//			//delete that this battle occurred
//			DeleteUtils.get().deletePvpBattleForUser(userUuid);
//			LOG.info("successfully penalized, rewarded attacker, defender respectively. battle= " +
//					battle);
//			
//		} catch (Exception e){
//			LOG.error("tried to penalize, reward attacker, defender respectively. battle=" +
//					battle, e);
//		} finally {
//			if (0 != defenderId) {
//				getLocker().unlockPlayer(defenderId, this.getClass().getSimpleName());
//			}
//		}
//	}*/
//
//	public HazelcastPvpUtil getHazelcastPvpUtil() {
//		return hazelcastPvpUtil;
//	}
//
//	public void setHazelcastPvpUtil(final HazelcastPvpUtil hazelcastPvpUtil) {
//		this.hazelcastPvpUtil = hazelcastPvpUtil;
//	}
//
//	public PvpLeagueForUserRetrieveUtil getPvpLeagueForUserRetrieveUtil() {
//		return pvpLeagueForUserRetrieveUtil;
//	}
//
//	public void setPvpLeagueForUserRetrieveUtil(
//			final PvpLeagueForUserRetrieveUtil pvpLeagueForUserRetrieveUtil) {
//		this.pvpLeagueForUserRetrieveUtil = pvpLeagueForUserRetrieveUtil;
//	}
//	
//}
